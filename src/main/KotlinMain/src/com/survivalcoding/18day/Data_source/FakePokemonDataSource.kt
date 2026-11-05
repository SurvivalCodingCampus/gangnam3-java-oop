package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.dto.PokemonDto
import kotlinx.coroutines.delay

// 가짜 포켓몬 데이터 소스: 테스트 및 데모용으로 고정된 데이터를 반환함
class FakePokemonDataSource : PokemonDataSource {

    private val pokemonMap = mapOf(
        "pikachu" to PokemonDto(id = 25, name = "pikachu", height = 4, weight = 60),
        "charizard" to PokemonDto(id = 6, name = "charizard", height = 17, weight = 905),
        "bulbasaur" to PokemonDto(id = 1, name = "bulbasaur", height = 7, weight = 69),
        "squirtle" to PokemonDto(id = 7, name = "squirtle", height = 5, weight = 90),
    )

    override suspend fun getPokemon(name: String): Result<PokemonDto, NetworkError> {
        delay(500) // 네트워크 지연을 시뮬레이션
        return when (val dto = pokemonMap[name.lowercase()]) {
            null -> Result.Error(NetworkError.CLIENT_ERROR)
            else -> Result.Success(dto)
        }
    }
}
