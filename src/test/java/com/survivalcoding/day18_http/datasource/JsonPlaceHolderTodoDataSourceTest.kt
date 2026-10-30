package com.survivalcoding.day18_http.datasource

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JsonPlaceHolderTodoDataSourceTest {
    val mockEngine = MockEngine { request ->
        when (request.url.toString()) {
            "https://test.com/image1.png" -> {
                respond(
                    content = ByteReadChannel(byteArrayOf(1, 2, 3)),
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", "image/png")
                )
            }

            "https://test.com/error.png" -> {
                respond(
                    content = ByteReadChannel("Error"),
                    status = HttpStatusCode.NotFound
                )
            }

            else -> {
                respond(
                    content = ByteReadChannel(byteArrayOf()),
                    status = HttpStatusCode.NotFound
                )
            }
        }
    }

    @Test
    fun test() = runBlocking {
        val client = HttpClient(mockEngine)
        val response = client.get("https://test.com/image1.png")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}