package com.survivalcoding.day18_http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val httpClient = HttpClientFactory.create()
    val response: HttpResponse = httpClient.get("https://jsonplaceholder.typicode.com/posts/1")
    println(response.bodyAsText())
}