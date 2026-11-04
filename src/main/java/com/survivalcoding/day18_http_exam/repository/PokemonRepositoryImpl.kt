package com.survivalcoding.day18_http_exam.repository

import com.survivalcoding.day18_http_exam.data_source.PokemonDataSource
import com.survivalcoding.day18_http_exam.model.Pokemon
import kotlinx.serialization.json.Json

// 먼저 짠다 (비즈니스 핵심)
class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource
) : PokemonRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getPokemon(name: String): Pokemon? {
        val response = dataSource.getPokemon(name)

        val jsonString: String = response.body ?: return null

        return json.decodeFromString<Pokemon>(jsonString)
    }
}