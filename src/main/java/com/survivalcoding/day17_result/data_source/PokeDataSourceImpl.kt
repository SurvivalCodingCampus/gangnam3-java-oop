package com.survivalcoding.day17_result.data_source

import com.survivalcoding.day17_result.core.HttpClientFactory
import com.survivalcoding.day17_result.dto.PokeDto
import com.survivalcoding.day17_result.model.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PokeDataSourceImpl(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PokeDataSource {

    override suspend fun getPoke(name: String): Response<PokeDto?> {
        val response = httpClient.get("https://pokeapi.co/api/v2/pokemon") {
            url { appendPathSegments(name, encodeSlash = true) }
        }
        return Response(
            statusCode = response.status.value,
            body = if (response.status.isSuccess()) response.body<PokeDto>() else null
        )
    }
}