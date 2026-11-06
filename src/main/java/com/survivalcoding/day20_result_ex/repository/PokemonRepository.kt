package com.survivalcoding.day20_result_ex.repository

import com.survivalcoding.day20_result_ex.core.NetworkError
import com.survivalcoding.day20_result_ex.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemon(name: String): Result<Pokemon, NetworkError>
}