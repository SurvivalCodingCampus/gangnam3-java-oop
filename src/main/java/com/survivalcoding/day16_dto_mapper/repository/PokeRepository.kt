package com.survivalcoding.day16_dto_mapper.repository

import com.survivalcoding.day16_dto_mapper.model.Poke

interface PokeRepository {
    
    suspend fun getPoke(name: String): Poke?
}