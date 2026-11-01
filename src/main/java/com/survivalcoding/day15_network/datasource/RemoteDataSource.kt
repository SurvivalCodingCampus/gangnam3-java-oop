package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.HttpClientFactory
import com.survivalcoding.day15_network.model.Post
import com.survivalcoding.day15_network.model.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class RemoteDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PostDataSource {

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    override suspend fun getPosts(): Response<List<Post>?> {
        val response = httpClient.get("$BASE_URL/posts")

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body() else null
        )
    }

    override suspend fun getPost(id: Int): Response<Post?> {
        val response = httpClient.get("$BASE_URL/posts/$id")

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body() else null
        )
    }

    override suspend fun createPost(post: Post): Response<Post?> {
        val response = httpClient.post("$BASE_URL/posts") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body() else null,
        )
    }

    override suspend fun updatePost(id: Int, post: Post): Response<Post?> {
        val response = httpClient.put("$BASE_URL/posts/$id") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body() else null,
        )
    }

    override suspend fun deletePost(id: Int): Response<Unit> {
        val response = httpClient.delete("$BASE_URL/posts/$id")

        return Response(
            statusCode = response.status.value,
            body = Unit,
        )
    }
}