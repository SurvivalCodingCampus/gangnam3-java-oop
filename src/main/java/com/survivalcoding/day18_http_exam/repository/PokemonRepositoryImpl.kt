package com.survivalcoding.day18_http_exam.repository

import com.survivalcoding.day18_http_exam.core.NetworkError
import com.survivalcoding.day18_http_exam.core.Result
import com.survivalcoding.day18_http_exam.data_source.PokemonDataSource
import com.survivalcoding.day18_http_exam.mapper.toModel
import com.survivalcoding.day18_http_exam.model.Pokemon
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketException

// 먼저 짠다 (비즈니스 핵심)
// 로직만 볼거야. 기술 관심 없어(역직렬화? 관심 없어)
// 걸러내기 (filter)
class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource
) : PokemonRepository {

    override suspend fun getPokemon(name: String): Result<Pokemon, NetworkError> {
        try {
            val response = dataSource.getPokemon(name)
            val dto = response.body ?: return Result.Error(NetworkError.SERVER_ERROR)
            return Result.Success(dto.toModel())
        } catch (ex: SocketException) {
            return Result.Error(NetworkError.NO_INTERNET_CONNECTION)
        } catch (e: TimeoutCancellationException) {
            return Result.Error(NetworkError.TIMEOUT_ERROR)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }
    }
}