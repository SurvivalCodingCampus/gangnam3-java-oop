package com.survivalcoding.day18.datasource

import com.survivalcoding.day18.HttpClientFactory
import com.survivalcoding.day18.MyResponse
import com.survivalcoding.day18.StatusCode
import com.survivalcoding.day19.dto.PokemonDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.toMap

class RemotePokemonDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create(),
    private val baseUrl: String = "https://pokeapi.co/api/v2/pokemon",
) : PokemonDataSource {
    override suspend fun getPokemonByName(name: String): MyResponse<PokemonDto> {
        val response = httpClient.get("$baseUrl/$name")
        val statusCode = StatusCode.from(response.status.value)

        return MyResponse(
            statusCode = statusCode,
            headers = response.headers.toMap(),
            body = if (statusCode.isSuccess()) response.body<PokemonDto>() else null
        )
    }
}
