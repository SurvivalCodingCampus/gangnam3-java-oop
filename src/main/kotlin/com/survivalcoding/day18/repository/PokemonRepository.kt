package com.survivalcoding.day18.repository.inter

import com.survivalcoding.day18.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonByName(name: String): Pokemon?
}
