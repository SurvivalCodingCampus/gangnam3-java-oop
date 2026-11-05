package com.survivalcoding.day17_result.repository

import com.survivalcoding.day17_result.core.NetworkError
import com.survivalcoding.day17_result.core.Result
import com.survivalcoding.day17_result.data_source.PokeDataSource
import com.survivalcoding.day17_result.mapper.toModel
import com.survivalcoding.day17_result.model.Poke
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketException

class PokeRepositoryImpl(
    private val dataSource: PokeDataSource
) : PokeRepository {

    override suspend fun getPoke(name: String): Result<Poke, NetworkError> {
        return try {
            val response = dataSource.getPoke(name)

            if (!response.isSuccess) {
                return Result.Error(NetworkError.CLIENT_ERROR)
            }

            val data = response.body ?: return Result.Error(NetworkError.SERVER_ERROR)

            Result.Success(data.toModel())
        } catch (e: SocketException) {
            Result.Error(NetworkError.NO_INTERNET)
        } catch (e: TimeoutCancellationException) {
            Result.Error(NetworkError.TIMEOUT)
        } catch (e: Exception) {
            Result.Error(NetworkError.UNKNOWN)
        }
    }
}