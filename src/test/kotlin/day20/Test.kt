@file:Suppress("NonAsciiCharacters")

package day20

import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day18.datasource.RemotePokemonDataSource
import com.survivalcoding.day18.repository.impl.PokemonRepositoryImpl
import com.survivalcoding.day20.core.NetworkError
import com.survivalcoding.day20.core.Result
import day18.mock.FakeHttpClientFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.SocketException

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

    private val eeveeJson = """
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

    private fun repositoryWith(handler: MockRequestHandler) = PokemonRepositoryImpl(
        RemotePokemonDataSource(FakeHttpClientFactory.create(MockEngine(handler)))
    )

    @Test
    fun `존재하는 포켓몬을 검색하면 Success로 Model을 반환한다`() = runTest {
        val repository = repositoryWith {
            respond(
                content = eeveeJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        val result = repository.getPokemonByName("eevee")

        assertThat(result).isEqualTo(Result.Success(eevee))
    }

    @Test
    fun `존재하지 않는 포켓몬 검색 시 CLIENT_ERR이 반환된다`() = runTest {
        // Given: 404를 반환하는 MockEngine
        val repository = repositoryWith {
            respondError(HttpStatusCode.NotFound)
        }

        // When: 오타가 난 포켓몬 이름 검색
        val result = repository.getPokemonByName("dittooo")

        // Then: 실패 타입과 구체적인 에러 검증
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(NetworkError.CLIENT_ERR)
    }

    @Test
    fun `서버가 5xx를 반환하면 SERVER_ERR이 반환된다`() = runTest {
        val repository = repositoryWith {
            respondError(HttpStatusCode.InternalServerError)
        }

        val result = repository.getPokemonByName("eevee")

        assertThat(result).isEqualTo(Result.Error(NetworkError.SERVER_ERR))
    }

    @Test
    fun `네트워크가 끊기면 NO_INTERNET_ERR이 반환된다`() = runTest {
        val repository = repositoryWith {
            throw SocketException("network unreachable")
        }

        val result = repository.getPokemonByName("eevee")

        assertThat(result).isEqualTo(Result.Error(NetworkError.NO_INTERNET_ERR))
    }

    @Test
    fun `그 외 예외가 발생하면 UNKNOWN_ERR이 반환된다`() = runTest {
        val repository = repositoryWith {
            throw IllegalStateException("unexpected")
        }

        val result = repository.getPokemonByName("eevee")

        assertThat(result).isEqualTo(Result.Error(NetworkError.UNKNOWN_ERR))
    }
}
