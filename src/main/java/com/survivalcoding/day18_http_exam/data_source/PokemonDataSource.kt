package com.survivalcoding.day18_http_exam.data_source

import com.survivalcoding.day18_http_exam.core.Response
import com.survivalcoding.day18_http_exam.dto.PokemonDto

interface PokemonDataSource {
    suspend fun getPokemon(name: String): Response<PokemonDto>
}