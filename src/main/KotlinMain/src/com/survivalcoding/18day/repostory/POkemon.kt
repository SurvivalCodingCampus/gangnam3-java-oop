package com.survivalcoding.`18day`.repostory

import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result

// 포켓몬 레포지토리 테스트 클래스: FakePokemonDataSource를 사용한 검증 예시
class PokemonRepositoryTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("=== 포켓몬 레포지토리 테스트 시작 ===\n")
            PokemonRepositoryTest().`존재하지_않는_포켓몬_검색_시_CLIENT_ERROR가_반환된다`()
            println()
            PokemonRepositoryTest().`존재하는_포켓몬_검색_시_Success_가_반환된다`()
            println("\n=== 포켓몬 레포지토리 테스트 종료 ===")
        }
    }

    fun `존재하지_않는_포켓몬_검색_시_CLIENT_ERROR가_반환된다`() {
        // Given: 가짜 데이터 소스와 레포지토리 준비
        val repository = PokemonRepositoryImpl(FakePokemonDataSource())

        // When: 존재하지 않는 포켓몬 이름 검색 ("dittooo")
        val result = repository.getPokemon("dittooo")

        // Then: 실패 타입(Result.Error) 및 구체적인 에러(CLIENT_ERROR) 검증
        if (result is Result.Error) {
            println("테스트 통과: ${result.error}")
        } else {
            println("테스트 실패: 예상된 에러가 아님 - $result")
        }
    }

    fun `존재하는_포켓몬_검색_시_Success_가_반환된다`() {
        // Given: 가짜 데이터 소스와 레포지토리 준비
        val repository = PokemonRepositoryImpl(FakePokemonDataSource())

        // When: 존재하는 포켓몬 이름 검색 ("pikachu")
        val result = repository.getPokemon("pikachu")

        // Then: 성공 타입(Result.Success) 및 포켓몬 데이터 검증
        if (result is Result.Success) {
            println("테스트 통과: ${result.value.name} (ID: ${result.value.id})")
        } else {
            println("테스트 실패: 예상된 성공 결과가 아님 - $result")
        }
    }
}
