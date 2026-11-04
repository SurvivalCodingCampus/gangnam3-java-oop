package com.survivalcoding.day18.repository.impl

import com.survivalcoding.day18.StatusCode
import com.survivalcoding.day18.datasource.PokemonDataSource
import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day18.repository.inter.PokemonRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource,
) : PokemonRepository {
    override suspend fun getPokemonByName(name: String): Pokemon? {
        val response = dataSource.getPokemonByName(name)

        return if (response.statusCode == StatusCode.Ok) {
            Json.decodeFromString<Pokemon>(response.body)
        } else {
            null
        }
    }
}
