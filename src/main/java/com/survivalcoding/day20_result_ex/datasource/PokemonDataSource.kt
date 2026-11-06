package com.survivalcoding.day20_result_ex.datasource

import com.survivalcoding.day20_result_ex.core.Response
import com.survivalcoding.day20_result_ex.dto.PokemonDto

class PokemonDataSource {
    suspend fun getPokemon(name: String): Response<PokemonDto>
}