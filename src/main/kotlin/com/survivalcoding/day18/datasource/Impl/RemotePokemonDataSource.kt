package com.survivalcoding.day18.datasource

import com.survivalcoding.day18.HttpClientFactory
import com.survivalcoding.day18.StatusCode
import com.survivalcoding.day19.dto.PokemonDto
import com.survivalcoding.day20.core.NetworkError
import com.survivalcoding.day20.core.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import java.net.SocketException

class RemotePokemonDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create(),
    private val baseUrl: String = "https://pokeapi.co/api/v2/pokemon",
) : PokemonDataSource {
    override suspend fun getPokemonByName(name: String): Result<PokemonDto, NetworkError> {
        return try {
            val response = httpClient.get("$baseUrl/$name")
            val statusCode = StatusCode.from(response.status.value)

            when {
                statusCode.isSuccess() -> Result.Success(response.body<PokemonDto>())
                statusCode.isServerError() -> Result.Error(NetworkError.SERVER_ERR)
                statusCode.isClientError() -> Result.Error(NetworkError.CLIENT_ERR)
                else -> Result.Error(NetworkError.UNKNOWN_ERR)
            }
        } catch (_: SocketException) {
            Result.Error(NetworkError.NO_INTERNET_ERR)
        } catch (_: HttpRequestTimeoutException) {
            Result.Error(NetworkError.TIMEOUT_ERR)
        } catch (_: Exception) {
            Result.Error(NetworkError.UNKNOWN_ERR)
        }
    }
}
