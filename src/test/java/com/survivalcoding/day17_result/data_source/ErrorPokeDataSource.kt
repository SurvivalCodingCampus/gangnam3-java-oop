package com.survivalcoding.day17_result.data_source

import com.survivalcoding.day17_result.dto.PokeDto
import com.survivalcoding.day17_result.model.Response

class ErrorPokeDataSource(
    private val exception: Exception
) : PokeDataSource {

    override suspend fun getPoke(name: String): Response<PokeDto?> {
        throw exception
    }
}
