package com.survivalcoding.day17_result.data_source

import com.survivalcoding.day17_result.dto.PokeDto
import com.survivalcoding.day17_result.model.Response

class FakePokeDataSource(
    private val statusCode: Int,
    private val body: PokeDto?,
) : PokeDataSource {

    override suspend fun getPoke(name: String): Response<PokeDto?> {
        return Response(
            statusCode = statusCode,
            body = body
        )
    }
}