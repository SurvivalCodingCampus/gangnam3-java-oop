package com.survivalcoding.day16_dto_mapper.data_source

import com.survivalcoding.day16_dto_mapper.HttpClientFactory
import com.survivalcoding.day16_dto_mapper.dto.PokeDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PokeDataSourceImpl(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PokeDataSource {

    override suspend fun getPoke(name: String): PokeDto? {
        val response = httpClient.get("https://pokeapi.co/api/v2/pokemon") {
            url { appendPathSegments(name, encodeSlash = true) }
        }
        return if (response.status.isSuccess()) response.body<PokeDto>() else null
    }
}