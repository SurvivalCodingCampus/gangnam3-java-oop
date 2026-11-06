package com.survivalcoding.`18day`

import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.repostory.PokemonRepository
import com.survivalcoding.`18day`.repostory.PokemonRepositoryImpl
import kotlinx.coroutines.runBlocking

// 포켓몬 게임 진입점: 가짜 데이터 소스를 사용한 포켓몬 검색 시연
fun main() = runBlocking {
    val dataSource = FakePokemonDataSource()
    val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

    println("=== 포켓몬 게임 시작 ===\n")

    val pokemonNames = listOf("pikachu", "charizard", "unknown", "bulbasaur", "squirtle")

    for (name in pokemonNames) {
        println("포켓몬 검색: $name")
        val result = repository.getPokemon(name)
        when (result) {
            is Result.Success -> {
                val p = result.value
                println("  -> 발견! 이름: ${p.name}, 도감 번호: ${p.id}, 키: ${p.height}, 몸무게: ${p.weight}")
            }
            is Result.Error -> {
                println("  -> 실패: ${result.error} (포켓몬을 찾을 수 없습니다)")
            }
            is Result.Loading -> {
                println("  -> 로딩 중...")
            }
            is Result.Failure -> {
                println("  -> 오류 발생: ${result.error}")
            }
        }
        println()
    }

    println("=== 포켓몬 게임 종료 ===")
}
