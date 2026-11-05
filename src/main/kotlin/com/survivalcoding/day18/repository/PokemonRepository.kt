package com.survivalcoding.day18.repository.inter

import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day20.core.NetworkError
import com.survivalcoding.day20.core.Result

interface PokemonRepository {
    suspend fun getPokemonByName(name: String): Result<Pokemon, NetworkError>
}
