package com.survivalcoding.day18_http_exam.data_source

import com.survivalcoding.day18_http.HttpClientFactory
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class RemotePokemonDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PokemonDataSource {

    private val baseUrl = "https://pokeapi.co/api/v2"

    override suspend fun getPokemon(name: String): Response<String> {
        val response = httpClient.get("$baseUrl/pokemon/$name")
        return Response(
            statusCode = response.status.value,
            body = response.bodyAsText()
        )
    }
}