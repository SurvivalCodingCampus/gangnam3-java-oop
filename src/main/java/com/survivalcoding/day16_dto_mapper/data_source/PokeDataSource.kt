package com.survivalcoding.day16_dto_mapper.data_source

import com.survivalcoding.day16_dto_mapper.dto.PokeDto

interface PokeDataSource {

    suspend fun getPoke(name: String): PokeDto?
}