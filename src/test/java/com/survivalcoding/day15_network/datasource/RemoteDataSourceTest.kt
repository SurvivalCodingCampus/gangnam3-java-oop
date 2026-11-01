package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.HttpClientFactory
import com.survivalcoding.day15_network.model.Post
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class RemoteDataSourceTest {

    // 각 테스트의 given 단계에서 MockEngine이 어떻게 응답할지 정한다
    private var handler: MockRequestHandler = { respondJson(content = "{}") }

    private lateinit var mockEngine: MockEngine
    private lateinit var dataSource: RemoteDataSource

    private fun MockRequestHandleScope.respondJson(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ) = respond(content, status, headersOf(HttpHeaders.ContentType, "application/json"))

    @BeforeEach
    fun setUp() {
        mockEngine = MockEngine { request -> handler(request) }
        dataSource = RemoteDataSource(HttpClientFactory.create(mockEngine))
    }

    // 1) 성공 케이스
    @Test
    fun `getPosts는 게시글 목록을 가져온다`() = runTest {
        // given: 서버가 게시글 2개를 반환한다
        handler = {
            respondJson("""[{"userId":1,"id":1,"title":"제목1","body":"내용1"},{"userId":1,"id":2,"title":"제목2","body":"내용2"}]""")
        }

        // when: 게시글 목록을 조회한다
        val response = dataSource.getPosts()

        // then: GET /posts로 요청하고, 2개의 게시글을 받는다
        val request = mockEngine.requestHistory.last()
        assertEquals(HttpMethod.Get, request.method)
        assertEquals("https://jsonplaceholder.typicode.com/posts", request.url.toString())
        assertEquals(200, response.statusCode)
        assertEquals(2, response.body?.size)
    }

    @Test
    fun `getPost는 게시글 하나를 가져온다`() = runTest {
        // given: 서버가 id 1인 게시글을 반환한다
        handler = { respondJson("""{"userId":1,"id":1,"title":"제목","body":"내용"}""") }

        // when: id 1인 게시글을 조회한다
        val response = dataSource.getPost(id = 1)

        // then: GET /posts/1로 요청하고, 게시글을 받는다
        val request = mockEngine.requestHistory.last()
        assertEquals(HttpMethod.Get, request.method)
        assertEquals("https://jsonplaceholder.typicode.com/posts/1", request.url.toString())
        assertEquals(200, response.statusCode)
        assertEquals(Post(userId = 1, id = 1, title = "제목", body = "내용"), response.body)
    }

    @Test
    fun `createPost는 게시글을 생성한다`() = runTest {
        // given: 서버가 id 101로 생성된 게시글을 반환한다
        handler = {
            respondJson("""{"userId":1,"id":101,"title":"네트워크 학습","body":"어려움"}""", HttpStatusCode.Created)
        }

        // when: 게시글을 생성 요청한다
        val response = dataSource.createPost(Post(userId = 1, title = "네트워크 학습", body = "어려움"))

        // then: POST /posts로 요청하고, 서버가 정한 id를 받는다
        val request = mockEngine.requestHistory.last()
        assertEquals(HttpMethod.Post, request.method)
        assertEquals("https://jsonplaceholder.typicode.com/posts", request.url.toString())
        assertEquals(201, response.statusCode)
        assertEquals(101, response.body?.id)
    }

    @Test
    fun `updatePost는 게시글을 수정한다`() = runTest {
        // given: 서버가 수정된 게시글을 반환한다
        handler = { respondJson("""{"userId":1,"id":1,"title":"Network Study","body":"Difficult"}""") }

        // when: id 1인 게시글을 수정 요청한다
        val response = dataSource.updatePost(
            id = 1,
            post = Post(id = 1, userId = 1, title = "Network Study", body = "Difficult"),
        )

        // then: PUT /posts/1로 요청하고, 수정된 게시글을 받는다
        val request = mockEngine.requestHistory.last()
        assertEquals(HttpMethod.Put, request.method)
        assertEquals("https://jsonplaceholder.typicode.com/posts/1", request.url.toString())
        assertEquals(200, response.statusCode)
        assertEquals("Network Study", response.body?.title)
    }

    @Test
    fun `deletePost는 게시글을 삭제한다`() = runTest {
        // given: 서버가 200을 반환한다
        handler = { respondJson("{}") }

        // when: id 1인 게시글을 삭제 요청한다
        val response = dataSource.deletePost(id = 1)

        // then: DELETE /posts/1로 요청하고, 상태 코드 200을 받는다
        val request = mockEngine.requestHistory.last()
        assertEquals(HttpMethod.Delete, request.method)
        assertEquals("https://jsonplaceholder.typicode.com/posts/1", request.url.toString())
        assertEquals(200, response.statusCode)
    }

    // 2) 네트워크 에러 케이스
    @Test
    fun `getPost는 네트워크 에러가 나면 예외를 던진다`() = runTest {
        // given: 서버에 연결할 수 없다
        handler = { throw IOException("서버에 연결할 수 없습니다") }

        // when & then: 조회 요청 시 IOException이 발생한다
        assertThrows<IOException> {
            dataSource.getPost(id = 1)
        }
    }

    // 3) 존재하지 않는 ID 요청 케이스
    @Test
    fun `getPost는 존재하지 않는 id면 null을 반환한다`() = runTest {
        // given: 서버가 404를 반환한다
        handler = { respondJson("{}", HttpStatusCode.NotFound) }

        // when: 없는 id로 게시글을 조회한다
        val response = dataSource.getPost(id = 9999)

        // then: 상태 코드는 404이고, body는 null이다
        assertEquals(404, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deletePost는 존재하지 않는 id면 404를 반환한다`() = runTest {
        // given: 서버가 404를 반환한다
        handler = { respondJson("{}", HttpStatusCode.NotFound) }

        // when: 없는 id로 게시글을 삭제 요청한다
        val response = dataSource.deletePost(id = 9999)

        // then: 상태 코드 404를 받는다
        assertEquals(404, response.statusCode)
    }
}
