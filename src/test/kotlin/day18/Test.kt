@file:Suppress("NonAsciiCharacters", "ClassName")

package day18

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
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
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
        fun `성공 응답의 body를 PokemonDto로 역직렬화해서 Success로 반환한다`() = runTest {
            val dataSource = createMockDataSource()

            val result = dataSource.getPokemonByName("eevee")

            assertThat(result).isInstanceOf(Result.Success::class.java)
            val dto = (result as Result.Success).value
            assertThat(dto.id).isEqualTo(133)
            assertThat(dto.name).isEqualTo("eevee")
            assertThat(dto.baseExperience).isEqualTo(65)
            assertThat(dto.isDefault).isTrue()
        }

        @Test
        fun `존재하지 않는 name이면 CLIENT_ERR Error를 반환한다`() = runTest {
            val dataSource = createMockDataSource()

            val result = dataSource.getPokemonByName("unknown")

            assertThat(result).isEqualTo(Result.Error(NetworkError.CLIENT_ERR))
        }

        @Test
        fun `5xx 응답이면 SERVER_ERR Error를 반환한다`() = runTest {
            val engine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
            val dataSource = RemotePokemonDataSource(FakeHttpClientFactory.create(engine))

            val result = dataSource.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Error(NetworkError.SERVER_ERR))
        }

        @Test
        fun `SocketException이 발생하면 NO_INTERNET_ERR Error를 반환한다`() = runTest {
            val dataSource = remoteDataSourceThrowing(SocketException())

            val result = dataSource.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Error(NetworkError.NO_INTERNET_ERR))
        }

        @Test
        fun `응답이 타임아웃 시간보다 늦으면 TIMEOUT_ERR Error를 반환한다`() = runTest {
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

            val result = dataSource.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Error(NetworkError.TIMEOUT_ERR))
        }

        @Test
        fun `그 외 예외가 발생하면 UNKNOWN_ERR Error를 반환한다`() = runTest {
            val dataSource = remoteDataSourceThrowing(IllegalStateException())

            val result = dataSource.getPokemonByName("eevee")

            assertThat(result).isEqualTo(Result.Error(NetworkError.UNKNOWN_ERR))
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
        fun `datasource가 Error를 반환하면 그대로 전달한다`() = runTest {
            NetworkError.entries.forEach { error ->
                val repository = PokemonRepositoryImpl(fakeDataSource(Result.Error(error)))

                val result = repository.getPokemonByName("eevee")

                assertThat(result).isEqualTo(Result.Error(error))
            }
        }
    }

    private fun fakeDataSource(result: Result<PokemonDto, NetworkError>) = object : PokemonDataSource {
        override suspend fun getPokemonByName(name: String) = result
    }

    private fun remoteDataSourceThrowing(throwable: Throwable) =
        RemotePokemonDataSource(FakeHttpClientFactory.create(MockEngine { throw throwable }))
}
