package com.survivalcoding.day18.datasource

import com.survivalcoding.day18.MyResponse
import com.survivalcoding.day19.dto.PokemonDto

interface PokemonDataSource {
    suspend fun getPokemonByName(name: String): MyResponse<PokemonDto>
}
