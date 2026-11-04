package com.survivalcoding.day18_http_exam.repository

import com.survivalcoding.day18_http_exam.data_source.FakePokemonDataSource
import com.survivalcoding.day18_http_exam.data_source.PokemonDataSource
import com.survivalcoding.day18_http_exam.data_source.RemotePokemonDataSource
import com.survivalcoding.day18_http_exam.data_source.mockEngine
import com.survivalcoding.day18_http_exam.model.Pokemon
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class PokemonRepositoryImplTest {
    @Test
    fun `Pokemon 데이터가 잘 들어와야 한다`(): Unit = runBlocking {
//        val dataSource: PokemonDataSource = RemotePokemonDataSource(HttpClient(mockEngine))
        val dataSource: PokemonDataSource = FakePokemonDataSource()

        val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

        val pokemon: Pokemon? = repository.getPokemon("ditto")

        assertNotNull(pokemon)
        assertEquals("ditto", pokemon.name)
    }
}