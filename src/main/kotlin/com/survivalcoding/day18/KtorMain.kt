package com.survivalcoding.day18

import com.survivalcoding.day18.model.Posts
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

fun main(): Unit = runBlocking {
    val httpClient = HttpClientFactory.create()
    val response = httpClient.get("https://jsonplaceholder.typicode.com/posts")

    Json.decodeFromString<List<Posts>>(response.bodyAsText()).forEach(::println)
}