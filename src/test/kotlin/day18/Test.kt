@file:Suppress("NonAsciiCharacters", "ClassName")

package day18

import com.survivalcoding.day18.StatusCode
import com.survivalcoding.day18.datasource.PokemonDataSource
import com.survivalcoding.day18.datasource.RemotePokemonDataSource
import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day18.repository.inter.PokemonRepository
import day18.mock.FakeHttpClientFactory
import day18.mock.FakePokemonDataSource
import day18.mock.FakePokemonRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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

        return FakePokemonDataSource(HttpClient(engine))
    }

    @Nested
    inner class `RemotePokemonDataSource테스트` {
        private lateinit var dataSource: PokemonDataSource

        @BeforeEach
        fun setUp() {
            dataSource = createMockDataSource()
        }

        @Test
        fun `응답의 상태코드와 헤더를 그대로 담아 반환한다`() = runTest {
            val dataSource = createMockDataSource()

            val response = dataSource.getPokemonByName("eevee")

            assertThat(response.statusCode).isEqualTo(StatusCode.Ok)
            assertThat(response.headers[HttpHeaders.ContentType])
                .containsExactly("application/json")
        }

        @Test
        fun `존재하지 않는 name으로 요청하면 404 상태코드를 반환한다`() = runTest {
            val response = dataSource.getPokemonByName("unknown")

            assertThat(response.statusCode).isEqualTo(StatusCode.NotFound)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `응답이 타임아웃 시간보다 늦으면 HttpRequestTimeoutException이 발생한다`() = runTest {
            // 엔진의 dispatcher를 테스트 스케줄러에 연결해야 타임아웃과 advanceTimeBy가 가상 시간으로 동작함
            val engine = MockEngine.create {
                dispatcher = StandardTestDispatcher(testScheduler)
                addHandler {
                    advanceTimeBy(11.seconds)
                    respond(content = "{}", status = HttpStatusCode.OK)
                }
            }

            val client = FakeHttpClientFactory.create(engine, requestTimeoutMillis = 10_000)
            val dataSource = FakePokemonDataSource(client)

            val exception = runCatching { dataSource.getPokemonByName("eevee") }.exceptionOrNull()

            assertThat(exception).isInstanceOf(HttpRequestTimeoutException::class.java)
        }
    }

    @Nested
    inner class `PokemonRepository테스트` {
        private lateinit var pokemonRepository: PokemonRepository

        @BeforeEach
        fun setUp() {
            pokemonRepository = FakePokemonRepository(createMockDataSource())
        }

        @Test
        fun `getPokemonByName을 통해 name에 해당하는 포켓몬을 가져온다`() = runTest {
            val actualPokemon = pokemonRepository.getPokemonByName("eevee")

            assertThat(actualPokemon).isEqualTo(eevee)
        }

        @Test
        fun `상태코드가 200가 아니면 Not Found 반환한다`() = runTest {
            val repository = FakePokemonRepository(createMockDataSource())

            val actualPokemon = repository.getPokemonByName("unknown")

            assertThat(actualPokemon).isNull()
        }
    }
}
