package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.dto.PokeApiPokemonDto
import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.adapter.PokeApiPokemonAdapter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.io.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

// 원격 포켓몬 데이터 소스: Ktor HTTP 클라이언트를 사용하여 PokeAPI에서 실제 데이터를 가져옴
//
// 고도화된 기능:
// - HTTP 연결 타임아웃 및 소켓 타임아웃 설정
// - 재시도 로직 (최대 3회, 지수 백오프)
// - 인메모리 캐시 (동일 포켓몬 반복 조회 시 API 호출 감소)
// - 배치 조회 (여러 포켓몬 한 번에 조회)
// - 상세한 에러 분류 (연결 실패, 타임아웃, 클라이언트 에러, 서버 에러)
class RemotePokemonDataSource private constructor(
    private val httpClient: HttpClient,
    private val cache: MutableMap<String, Pokemon>
) : PokemonDataSource {

    private val maxRetries = 3
    private val baseRetryDelayMs = 500L

    constructor(httpClient: HttpClient) : this(httpClient, ConcurrentHashMap())

    /**
     * 포켓몬 이름(또는 ID)으로 단건 조회합니다.
     * 캐시에 있으면 캐시 값을 반환하고, 없으면 PokeAPI에서 조회 후 캐시에 저장합니다.
     */
    override fun getPokemon(name: String): Result<Pokemon, NetworkError> {
        return runBlocking {
            val lookupKey = name.trim().lowercase()

            // 1단계: 캐시 확인
            cache.get(lookupKey)?.let {
                return@runBlocking Result.Success(it)
            }

            // 2단계: PokeAPI에서 조회 (재시도 로직 포함)
            var lastError: NetworkError? = null
            for (attempt in 1..maxRetries) {
                try {
                    val response = httpClient.get("https://pokeapi.co/api/v2/pokemon/$lookupKey") {
                        header("Accept", "application/json")
                        header("User-Agent", "PokemonGameApp/1.0 (https://github.com/survivalcoding/pokemon-game)")
                    }

                    when {
                        response.status.value in 200..299 -> {
                            val json = response.bodyAsText()
                            val pokeApiDto = PokeApiPokemonDto.fromJson(json)
                            // [GoF Adapter] PokeApiPokemonAdapter로 DTO→도메인 변환
                            val pokemon = PokeApiPokemonAdapter.adapt(pokeApiDto)

                            // 조회 성공 시 캐시에 저장
                            cache[lookupKey] = pokemon
                            return@runBlocking Result.Success(pokemon)
                        }
                        response.status.value in 400..499 -> {
                            return@runBlocking Result.Error(NetworkError.CLIENT_ERROR)
                        }
                        else -> {
                            lastError = NetworkError.SERVER_ERROR
                        }
                    }
                } catch (e: java.net.SocketException) {
                    lastError = NetworkError.NO_CONNECTION
                } catch (e: java.net.UnknownHostException) {
                    lastError = NetworkError.NO_CONNECTION
                } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                    lastError = NetworkError.TIMEOUT
                } catch (e: CancellationException) {
                    lastError = NetworkError.TIMEOUT
                } catch (e: java.io.IOException) {
                    lastError = NetworkError.NO_CONNECTION
                } catch (e: Exception) {
                    lastError = NetworkError.UNKNOWN
                }

                // 재시도 전 지수 백오프 대기 (최대 시도 전에는 대기하지 않음)
                if (attempt < maxRetries) {
                    val delayMs = baseRetryDelayMs * (1 shl (attempt - 1))
                    try {
                        delay(delayMs)
                    } catch (_: CancellationException) {
                        lastError = NetworkError.TIMEOUT
                        break
                    }
                }
            }

            return@runBlocking Result.Error(lastError ?: NetworkError.UNKNOWN)
        }
    }

    /**
     * 여러 포켓몬을 배치로 조회합니다.
     * 각 포켓몬에 대해 개별적으로 조회하며, 실패한 항목은 결과에서 제외합니다.
     */
    fun getPokemonBatch(names: List<String>): Map<String, Result<Pokemon, NetworkError>> {
        return names.associate { name ->
            name to getPokemon(name)
        }
    }

    /**
     * 캐시에 저장된 포켓몬 이름 목록을 반환합니다.
     */
    fun getCachedPokemonNames(): List<String> {
        return cache.keys.toList()
    }

    /**
     * 캐시를 모두 비웁니다.
     */
    fun clearCache() {
        cache.clear()
    }

    /**
     * 캐시 항목 수를 반환합니다.
     */
    fun cacheSize(): Int {
        return cache.size
    }

    fun close() {
        cache.clear()
        httpClient.close()
    }

    companion object {
        /**
         * 타임아웃과 연결 설정이 적용된 RemotePokemonDataSource 인스턴스를 생성합니다.
         */
        fun create(
            connectTimeoutMs: Int = 10000,
            socketTimeoutMs: Int = 15000,
            maxConnectionsPerHost: Int = 10
        ): RemotePokemonDataSource {
            val client = HttpClient(CIO) {
                engine {
                    connectTimeout = connectTimeoutMs
                    socketTimeout = socketTimeoutMs
                    maxConnectionsPerHost = maxConnectionsPerHost
                }
                defaultRequest {
                    url {
                        protocol = HttpUrlProtocol.HTTPS
                        host = "pokeapi.co"
                    }
                    header("Accept", "application/json")
                    header("User-Agent", "PokemonGameApp/1.0")
                }
                install(HttpTimeout) {
                    connectTimeoutMillis = connectTimeoutMs
                    socketTimeoutMillis = socketTimeoutMs
                }
            }
            return RemotePokemonDataSource(client)
        }

        fun create(): RemotePokemonDataSource = create()
    }
}
