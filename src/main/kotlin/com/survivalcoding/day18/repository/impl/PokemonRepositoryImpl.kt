package com.survivalcoding.day18.repository.impl

import com.survivalcoding.day18.datasource.PokemonDataSource
import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day18.repository.inter.PokemonRepository
import com.survivalcoding.day19.mapper.toModel
import com.survivalcoding.day20.core.NetworkError
import com.survivalcoding.day20.core.Result
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketException

class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource,
) : PokemonRepository {
    override suspend fun getPokemonByName(name: String): Result<Pokemon, NetworkError> {
        try {
            val response = dataSource.getPokemonByName(name)
            val pokemonDto = response.body ?: return Result.Error(NetworkError.SERVER_ERR)
            return Result.Success(pokemonDto.toModel())
        } catch (_: SocketException) {
            return Result.Error(NetworkError.NO_INTERNET_ERR)
        } catch (_: TimeoutCancellationException) {
            return Result.Error(NetworkError.TIMEOUT_ERR)
        } catch (_: Exception) {
            return Result.Error(NetworkError.UNKNOWN_ERR)
        }
    }
}
