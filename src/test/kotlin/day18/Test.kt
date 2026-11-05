@file:Suppress("NonAsciiCharacters", "ClassName")

package day18

import com.survivalcoding.day18.MyResponse
import com.survivalcoding.day18.StatusCode
import com.survivalcoding.day18.datasource.PokemonDataSource
import com.survivalcoding.day18.datasource.RemotePokemonDataSource
import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day18.repository.impl.PokemonRepositoryImpl
import com.survivalcoding.day18.repository.inter.PokemonRepository
import com.survivalcoding.day19.dto.PokemonDto
import com.survivalcoding.day20.core.NetworkError
import com.survivalcoding.day20.core.Result
import day18.mock.FakeHttpClientFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.SocketException
import kotlin.time.Duration.Companion.seconds

class Test {
    private val eevee = Pokemon(
        id = 133,
        name = "eevee",
        baseExperience = 65,
        height = 3,
        isDefault = true,
        order = 197,
        weight = 65,
    )

    private fun createMockDataSource(): PokemonDataSource {

        val eeveeJson = """
            {
                "id": 133,
                "name": "eevee",
                "base_experience": 65,
                "height": 3,
                "is_default": true,
                "order": 197,
                "weight": 65
            }
        """.trimIndent()

        val engine = MockEngine { request ->
            when (request.url.toString()) {
                "https://pokeapi.co/api/v2/pokemon/eevee" -> {
                    respond(
                        content = eeveeJson,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        return RemotePokemonDataSource(FakeHttpClientFactory.create(engine))
    }

    @Nested
    inner class `RemotePokemonDataSource테스트` {
        @Test
        fun `응답의 상태코드와 헤더를 그대로 담아 반환한다`() = runTest {
            val dataSource = createMockDataSource()

            val response = dataSource.getPokemonByName("eevee")

            assertThat(response.statusCode).isEqualTo(StatusCode.Ok)
            assertThat(response.headers[HttpHeaders.ContentType])
                .containsExactly("application/json")
        }

        @Test
        fun `응답 body를 PokemonDto로 역직렬화한다`() = runTest {
            val dataSource = createMockDataSource()

            val response = dataSource.getPokemonByName("eevee")

            assertThat(response.body.id).isEqualTo(133)
            assertThat(response.body.name).isEqualTo("eevee")
            assertThat(response.body.baseExperience).isEqualTo(65)
            assertThat(response.body.isDefault).isTrue()
        }

        @Test
        fun `존재하지 않는 name으로 요청하면 body를 역직렬화할 수 없어 예외가 발생한다`() = runTest {
            val dataSource = createMockDataSource()

            val exception = runCatching { dataSource.getPokemonByName("unknown") }.exceptionOrNull()

            assertThat(exception).isNotNull()
        }

        @Test
        fun `응답이 타임아웃 시간보다 늦으면 HttpRequestTimeoutException이 발생한다`() = runTest {
            // 엔진의 dispatcher를 테스트 스케줄러에 연결해야 타임아웃이 가상 시간으로 동작함
            val engine = MockEngine.create {
                dispatcher = StandardTestDispatcher(testScheduler)
                addHandler {
                    delay(11.seconds)
                    respond(content = "{}", status = HttpStatusCode.OK)
                }
            }
            val client = FakeHttpClientFactory.create(engine, requestTimeoutMillis = 10_000)
            val dataSource = RemotePokemonDataSource(client)

            val exception = runCatching { dataSource.getPokemonByName("eevee") }.exceptionOrNull()

            assertThat(exception).isInstanceOf(HttpRequestTimeoutException::class.java)
        }
    }

    @Nested
    inner class `PokemonRepository테스트` {
        @Test
        fun `getPokemonByName을 통해 name에 해당하는 포켓몬을 Success로 가져온다`() = runTest {
            val pokemonRepository: PokemonRepository = PokemonRepositoryImpl(createMockDataSource())

            val result = pokemonRepository.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Success(eevee))
        }

        @Test
        fun `존재하지 않는 name이면 UNKNOWN_ERR Error를 반환한다`() = runTest {
            val repository = PokemonRepositoryImpl(createMockDataSource())

            val result = repository.getPokemonByName("unknown")

            assertThat(result).isEqualTo(Result.Error(NetworkError.UNKNOWN_ERR))
        }

        @Test
        fun `SocketException이 발생하면 NO_INTERNET_ERR Error를 반환한다`() = runTest {
            val repository = PokemonRepositoryImpl(failingDataSource(SocketException()))

            val result = repository.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Error(NetworkError.NO_INTERNET_ERR))
        }

        @Test
        fun `TimeoutCancellationException이 발생하면 TIMEOUT_ERR Error를 반환한다`() = runTest {
            val repository = PokemonRepositoryImpl(failingDataSource(timeoutCancellation()))

            val result = repository.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Error(NetworkError.TIMEOUT_ERR))
        }

        @Test
        fun `그 외 예외가 발생하면 UNKNOWN_ERR Error를 반환한다`() = runTest {
            val repository = PokemonRepositoryImpl(failingDataSource(IllegalStateException()))

            val result = repository.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Error(NetworkError.UNKNOWN_ERR))
        }
    }

    private fun failingDataSource(throwable: Throwable) = object : PokemonDataSource {
        override suspend fun getPokemonByName(name: String): MyResponse<PokemonDto> = throw throwable
    }

    private suspend fun timeoutCancellation(): TimeoutCancellationException =
        try {
            withTimeout(1) { awaitCancellation() }
        } catch (e: TimeoutCancellationException) {
            e
        }
}
