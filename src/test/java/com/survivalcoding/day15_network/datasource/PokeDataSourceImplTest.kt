package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.HttpClientFactory
import com.survivalcoding.day15_network.model.Pokemon
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class PokeDataSourceImplTest {

    // 각 테스트의 given 단계에서 MockEngine이 어떻게 응답할지 정한다
    private var handler: MockRequestHandler = { respondJson(content = "{}") }

    private lateinit var mockEngine: MockEngine
    private lateinit var dataSource: PokeDataSourceImpl

    private fun MockRequestHandleScope.respondJson(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ) = respond(content, status, headersOf(HttpHeaders.ContentType, "application/json"))

    @BeforeEach
    fun setUp() {
        mockEngine = MockEngine { request -> handler(request) }
        dataSource = PokeDataSourceImpl(HttpClientFactory.create(mockEngine))
    }

    // 1) 성공 케이스
    @Test
    fun `getPokemon은 이름으로 포켓몬을 가져온다`() = runTest {
        // given: 서버가 피카츄 정보를 반환한다
        handler = {
            respondJson("""{"id":25,"name":"pikachu","base_experience":112,"height":4,"is_default":true,"order":35,"weight":60}""")
        }

        // when: pikachu로 조회한다
        val response = dataSource.getPokemon(name = "pikachu")

        // then: GET /pokemon/pikachu로 요청하고, snake_case 필드까지 Pokemon으로 변환된다
        val request = mockEngine.requestHistory.last()
        assertEquals(HttpMethod.Get, request.method)
        assertEquals("https://pokeapi.co/api/v2/pokemon/pikachu", request.url.toString())
        assertEquals(200, response.statusCode)
        assertEquals(
            Pokemon(id = 25, name = "pikachu", baseExperience = 112, height = 4, isDefault = true, order = 35, weight = 60),
            response.body,
        )
    }

    // 2) 네트워크 에러 케이스
    @Test
    fun `getPokemon은 네트워크 에러가 나면 예외를 던진다`() = runTest {
        // given: 서버에 연결할 수 없다
        handler = { throw IOException("서버에 연결할 수 없습니다") }

        // when & then: 조회 요청 시 IOException이 발생한다
        assertThrows<IOException> {
            dataSource.getPokemon(name = "pikachu")
        }
    }

    // 3) 존재하지 않는 이름 요청 케이스
    @Test
    fun `getPokemon은 존재하지 않는 이름이면 null을 반환한다`() = runTest {
        // given: 서버가 404를 반환한다
        handler = { respond("Not Found", HttpStatusCode.NotFound) }

        // when: 없는 이름으로 조회한다
        val response = dataSource.getPokemon(name = "unknown")

        // then: 상태 코드는 404이고, body는 null이다
        assertEquals(404, response.statusCode)
        assertNull(response.body)
    }
}
