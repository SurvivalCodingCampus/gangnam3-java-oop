package com.survivalcoding.day18.datasource

import com.survivalcoding.day18.MyResponse
import com.survivalcoding.day18.model.Pokemon

interface PokemonDataSource {
    suspend fun getPokemonByName(name: String): MyResponse<Pokemon?>
}
