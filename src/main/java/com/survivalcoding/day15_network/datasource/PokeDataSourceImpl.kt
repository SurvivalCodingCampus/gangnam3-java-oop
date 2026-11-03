package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.HttpClientFactory
import com.survivalcoding.day15_network.model.Pokemon
import com.survivalcoding.day15_network.model.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PokeDataSourceImpl(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PokeDataSource {

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon"
    }

    override suspend fun getPokemon(name: String): Response<Pokemon?> {
        val response = httpClient.get(BASE_URL) {
            url { appendPathSegments(name, encodeSlash = true) }
        }

        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body<Pokemon>() else null
        )
    }
}