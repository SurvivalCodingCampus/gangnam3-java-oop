package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.datasource.FakePokeDataSource
import com.survivalcoding.day15_network.model.Pokemon
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class PokeRepositoryImplTest {

    private lateinit var dataSource: FakePokeDataSource
    private lateinit var pokeRepository: PokeRepository

    @BeforeEach
    fun setUp() {
        dataSource = FakePokeDataSource()
        dataSource.pokemons = listOf(
            pokemonOf(id = 1, name = "bulbasaur"),
            pokemonOf(id = 25, name = "pikachu"),
        )

        pokeRepository = PokeRepositoryImpl(dataSource)
    }

    @Test
    fun `getPokemon은 성공하면 포켓몬을 반환한다`() = runTest {
        // when
        val pokemon = pokeRepository.getPokemon(name = "pikachu")

        // then
        assertEquals(25, pokemon?.id)
    }

    @Test
    fun `getPokemon은 실패 응답이면 null을 반환한다`() = runTest {
        // given
        dataSource.statusCode = 404

        // when
        val pokemon = pokeRepository.getPokemon(name = "unknown")

        // then
        assertNull(pokemon)
    }

    @Test
    fun `getPokemon은 네트워크 에러가 나면 예외를 던지지 않고 null을 반환한다`() = runTest {
        // given
        dataSource.exception = IOException("서버에 연결할 수 없습니다")

        // when
        val pokemon = pokeRepository.getPokemon(name = "pikachu")

        // then
        assertNull(pokemon)
    }

    @Test
    fun `코루틴 취소 예외는 처리하지 않고 다시 던진다`() = runTest {
        // given
        dataSource.exception = CancellationException("취소됨")

        // when & then
        assertThrows<CancellationException> {
            pokeRepository.getPokemon(name = "pikachu")
        }
    }

    private fun pokemonOf(
        id: Int = 1,
        name: String = "name",
    ) = Pokemon(
        id = id,
        name = name,
        baseExperience = 0,
        height = 0,
        isDefault = true,
        order = 0,
        weight = 0,
    )
}
