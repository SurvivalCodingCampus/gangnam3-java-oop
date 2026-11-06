package com.survivalcoding.day18.datasource

import com.survivalcoding.day19.dto.PokemonDto
import com.survivalcoding.day20.core.NetworkError
import com.survivalcoding.day20.core.Result

interface PokemonDataSource {
    suspend fun getPokemonByName(name: String): Result<PokemonDto, NetworkError>
}
