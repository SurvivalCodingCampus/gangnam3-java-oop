package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.HttpClientFactory
import com.survivalcoding.day15_network.dto.CreateTodoRequest
import com.survivalcoding.day15_network.dto.PatchTodoRequest
import com.survivalcoding.day15_network.dto.TodoDto
import com.survivalcoding.day15_network.dto.UpdateTodoRequest
import com.survivalcoding.day15_network.model.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class TodoDataSourceImpl(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : TodoDataSource {

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    override suspend fun getTodos(): Response<List<TodoDto>?> {
        val response = httpClient.get("$BASE_URL/todos")

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body<List<TodoDto>>() else null
        )
    }

    override suspend fun getTodo(id: Int): Response<TodoDto?> {
        val response = httpClient.get("$BASE_URL/todos/$id")

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body<TodoDto>() else null
        )
    }

    override suspend fun postTodo(request: CreateTodoRequest): Response<TodoDto?> {
        val response = httpClient.post("$BASE_URL/todos") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body<TodoDto>() else null
        )
    }

    override suspend fun putTodo(id: Int, request: UpdateTodoRequest): Response<TodoDto?> {
        val response = httpClient.put("$BASE_URL/todos/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body<TodoDto>() else null
        )
    }

    override suspend fun patchTodo(id: Int, request: PatchTodoRequest): Response<TodoDto?> {
        val response = httpClient.patch("$BASE_URL/todos/$id") {
            contentType(ContentType.Application.Json)
            // setBody(TodoDto(title = title))
            setBody(request)
        }

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body<TodoDto>() else null
        )
    }

    override suspend fun deleteTodo(id: Int): Response<Unit> {
        val response = httpClient.delete("$BASE_URL/todos/$id")
        println("Delete request for ID $id resulted in status: ${response.status}")

        return Response(
            statusCode = response.status.value,
            body = Unit
        )
    }
}