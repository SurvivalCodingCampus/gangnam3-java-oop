package com.survivalcoding.day18_http_exam.data_source

import com.survivalcoding.day18_http_exam.dto.PokemonDto
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RemotePokemonDataSourceTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `포켓몬 데이터가 잘 들어와야 한다`() = runBlocking {
        val dataSource: PokemonDataSource = RemotePokemonDataSource(HttpClient(mockEngine))

        val response = dataSource.getPokemon("ditto")
        assertEquals(json.decodeFromString<PokemonDto>(jsonString).name, response.body?.name)
    }
}