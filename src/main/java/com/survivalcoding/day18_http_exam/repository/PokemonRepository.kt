package com.survivalcoding.day18_http_exam.repository

import com.survivalcoding.day18_http_exam.core.NetworkError
import com.survivalcoding.day18_http_exam.core.Result
import com.survivalcoding.day18_http_exam.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemon(name: String): Result<Pokemon, NetworkError>
}