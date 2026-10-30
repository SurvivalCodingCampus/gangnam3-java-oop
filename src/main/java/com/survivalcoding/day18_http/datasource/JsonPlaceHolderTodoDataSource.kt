package com.survivalcoding.day18_http.datasource

import com.survivalcoding.day18_http.HttpClientFactory
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

// 응답
// header, statusCode(200), body?

class JsonPlaceHolderTodoDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PostDataSource {
    override suspend fun getPosts(): Response {
        val response = httpClient.get("https://jsonplaceholder.typicode.com/posts")
        return Response(
            statusCode = response.status.value,
            body = response.bodyAsText(),
        )
    }

    override suspend fun getPost(id: Int): Response {
        val response = httpClient.get("https://jsonplaceholder.typicode.com/posts/$id")
        return Response(
            statusCode = response.status.value,
            body = response.bodyAsText(),
        )
    }
}

fun main(): Unit = runBlocking {
    val dataSource = JsonPlaceHolderTodoDataSource()
    val posts = dataSource.getPosts()
    println(posts)

    val post = dataSource.getPost(1)
    println(post)
}