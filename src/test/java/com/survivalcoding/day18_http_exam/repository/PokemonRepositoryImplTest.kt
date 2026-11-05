package com.survivalcoding.day18_http_exam.repository

import com.survivalcoding.day18_http_exam.core.NetworkError
import com.survivalcoding.day18_http_exam.core.Response
import com.survivalcoding.day18_http_exam.core.Result
import com.survivalcoding.day18_http_exam.data_source.FakePokemonDataSource
import com.survivalcoding.day18_http_exam.data_source.PokemonDataSource
import com.survivalcoding.day18_http_exam.dto.PokemonDto
import com.survivalcoding.day18_http_exam.model.Pokemon
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.SocketException

class NetworkErrorDataSource : PokemonDataSource {
    override suspend fun getPokemon(name: String): Response<PokemonDto> {
        throw SocketException("Something went wrong")
    }
}

class PokemonRepositoryImplTest {
    @Test
    fun `Pokemon 데이터가 잘 들어와야 한다`(): Unit = runTest {
//        val dataSource: PokemonDataSource = RemotePokemonDataSource(HttpClient(mockEngine))
        val dataSource: PokemonDataSource = FakePokemonDataSource()

        val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

        val result = repository.getPokemon("ditto")

        assertEquals(true, result is Result.Success)
        val pokemon: Pokemon = (result as Result.Success).data
        assertEquals("ditto", pokemon.name)
    }

    @Test
    fun `네트워크 연결 실패시 NO_INTERNET`(): Unit = runTest {
        val dataSource: PokemonDataSource = NetworkErrorDataSource()
        val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

        val result = repository.getPokemon("no_internet")
        assertEquals(true, result is Result.Error)
        assertEquals(NetworkError.NO_INTERNET_CONNECTION, (result as Result.Error).error)
    }
}