package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.model.Pokemon
import com.survivalcoding.day15_network.model.Response

interface PokeDataSource {

    suspend fun getPokemon(name: String): Response<Pokemon?>
}