package com.survivalcoding.day18_http_exam.data_source

interface PokemonDataSource {
    suspend fun getPokemon(name: String): Response<String>
}