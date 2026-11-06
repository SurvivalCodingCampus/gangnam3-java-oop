package com.survivalcoding.day18.repository.impl

import com.survivalcoding.day18.datasource.PokemonDataSource
import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day18.repository.inter.PokemonRepository
import com.survivalcoding.day19.mapper.toModel
import com.survivalcoding.day20.core.NetworkError
import com.survivalcoding.day20.core.Result

class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource,
) : PokemonRepository {
    override suspend fun getPokemonByName(name: String): Result<Pokemon, NetworkError> =
        when (val result = dataSource.getPokemonByName(name)) {
            is Result.Success -> Result.Success(result.value.toModel())
            is Result.Error -> result
        }
}
