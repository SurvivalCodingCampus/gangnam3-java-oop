package com.survivalcoding.day20_result_ex.datasource

import com.survivalcoding.day20_result_ex.HttpClientFactory
import com.survivalcoding.day20_result_ex.core.Response
import com.survivalcoding.day20_result_ex.dto.PokemonDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

// 데이터 관련 복잡한 거 최대한 다해서 DTO 만 돌려주자
class RemotePokemonDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PokemonDataSource {

    private val baseUrl = "https://pokeapi.co/api/v2"
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getPokemon(name: String): Response<PokemonDto> {
        val response = httpClient.get("$baseUrl/pokemon/$name")
        val jsonString = response.bodyAsText()
        return Response(
            statusCode = response.status.value,
            body = json.decodeFromString<PokemonDto>(jsonString)
        )
    }
}