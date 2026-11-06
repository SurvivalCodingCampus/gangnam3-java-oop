package com.survivalcoding.day20_result_ex.repository

import com.survivalcoding.day20_result_ex.core.NetworkError
import com.survivalcoding.day20_result_ex.core.Response
import com.survivalcoding.day20_result_ex.data_source.FakePokemonDataSource
import com.survivalcoding.day20_result_ex.data_source.PokemonDataSource
import com.survivalcoding.day20_result_ex.dto.PokemonDto
import com.survivalcoding.day20_result_ex.model.Pokemon
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
    fun `존재하지_않는_포켓몬_검색_시_CLIENT_ERROR가_반환된다`(): Unit = runTest {
        // Given: 404를 반환하는 FakeDataSource 준비
        val repository = PokemonRepositoryImpl(FakePokemonDataSource())

        // When: 오타가 난 포켓몬 이름 검색
        val result = repository.getPokemon("dittooo")

        // Then: 실패 타입과 구체적인 에러 검증
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.CLIENT_ERROR, (result as Result.Error).error)
    }
}

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