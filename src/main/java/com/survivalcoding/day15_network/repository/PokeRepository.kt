package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.model.Pokemon

interface PokeRepository {

    suspend fun getPokemon(name: String): Pokemon?
}