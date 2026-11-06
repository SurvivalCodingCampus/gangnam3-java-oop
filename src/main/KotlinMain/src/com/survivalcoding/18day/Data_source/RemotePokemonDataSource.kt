package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.dto.PokemonDto
import com.survivalcoding.`18day`.dto.PokemonDto.Companion.fromJson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

// 원격 포켓몬 데이터 소스: Ktor HTTP 클라이언트를 사용하여 실제 API에서 데이터를 가져옴
class RemotePokemonDataSource(
    private val httpClient: HttpClient
) : PokemonDataSource {

    override suspend fun getPokemon(name: String): Result<PokemonDto, NetworkError> {
        return try {
            val response = httpClient.get("https://pokeapi.co/api/v2/pokemon/${name.lowercase()}")
            when {
                response.status.isSuccess() -> {
                    val json = response.bodyAsText()
                    val dto = fromJson(json)
                    Result.Success(dto)
                }
                response.status.value in 400..499 -> Result.Error(NetworkError.CLIENT_ERROR)
                else -> Result.Error(NetworkError.SERVER_ERROR)
            }
        } catch (e: java.net.SocketException) {
            Result.Error(NetworkError.NO_INTERNET)
        } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
            Result.Error(NetworkError.TIMEOUT)
        } catch (e: Exception) {
            Result.Error(NetworkError.UNKNOWN)
        }
    }
}
