package com.survivalcoding.`18day`.test

import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result

/**
 * FakePokemonDataSource 테스트
 *
 * 테스트 시나리오:
 * 1. 존재하는 포켓몬 조회 → Result.Success 반환
 * 2. 존재하지 않는 포켓몬 조회 → Result.Error(CLIENT_ERROR) 반환
 * 3. 대소문자 무관 조회 (PIKACHU, Pikachu, pikachu 모두 동일)
 * 4. 8마리 포켓몬 모두 조회 가능
 * 5. 조회된 포켓몬 데이터 검증 (id, name, type, hp, attack, defense, imageUrl, soundUrl)
 */
class FakePokemonDataSourceTest {

    // ============================================================
    // 테스트 실행 메서드 (main)
    // ============================================================

    fun main() {
        println("========== [FakePokemonDataSourceTest 시작] ==========")
        println()

        var passCount = 0
        var failCount = 0

        val results = mutableListOf<Pair<String, Boolean>>()

        val dataSource = FakePokemonDataSource()

        results += testExistingPokemon(dataSource)
        results += testNonexistentPokemon(dataSource)
        results += testCaseInsensitive(dataSource)
        results += testAll8Pokemon(dataSource)
        results += testPokemonDataAccuracy(dataSource)

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println()
        println("========== [FakePokemonDataSourceTest 종료] ==========")
    }

    // ============================================================
    // 개별 테스트 케이스
    // ============================================================

    /**
     * 시나리오 1: 존재하는 포켓몬 조회 → Result.Success
     */
    private fun testExistingPokemon(dataSource: FakePokemonDataSource): Pair<String, Boolean> {
        val result = dataSource.getPokemon("pikachu")
        val passed = result is Result.Success && result.value.name == "pikachu"

        println("[테스트 1] 존재하는 포켓몬 조회: ${if (passed) "통과" else "실패"} " +
            "(결과: $result)")
        return "존재하는 포켓몬 조회" to passed
    }

    /**
     * 시나리오 2: 존재하지 않는 포켓몬 조회 → Result.Error(CLIENT_ERROR)
     */
    private fun testNonexistentPokemon(dataSource: FakePokemonDataSource): Pair<String, Boolean> {
        val result = dataSource.getPokemon("dittooo")
        val passed = result is Result.Error && result.error == NetworkError.CLIENT_ERROR

        println("[테스트 2] 존재하지 않는 포켓몬 조회: ${if (passed) "통과" else "실패"} " +
            "(결과: $result)")
        return "존재하지 않는 포켓몬 조회" to passed
    }

    /**
     * 시나리오 3: 대소문자 무관 조회
     */
    private fun testCaseInsensitive(dataSource: FakePokemonDataSource): Pair<String, Boolean> {
        val r1 = dataSource.getPokemon("PIKACHU")
        val r2 = dataSource.getPokemon("Pikachu")
        val r3 = dataSource.getPokemon("pikachu")

        val passed = r1 is Result.Success && r2 is Result.Success && r3 is Result.Success &&
            r1.value.name == r2.value.name && r2.value.name == r3.value.name

        println("[테스트 3] 대소문자 무관 조회: ${if (passed) "통과" else "실패"} " +
            "(PIKACHU: $r1, Pikachu: $r2, pikachu: $r3)")
        return "대소문자 무관 조회" to passed
    }

    /**
     * 시나리오 4: 8마리 포켓몬 모두 조회 가능
     */
    private fun testAll8Pokemon(dataSource: FakePokemonDataSource): Pair<String, Boolean> {
        val pokemonNames = listOf("pikachu", "charizard", "bulbasaur", "squirtle",
            "jigglypuff", "meowth", "psyduck", "snorlax")

        var allFound = true
        for (name in pokemonNames) {
            val result = dataSource.getPokemon(name)
            if (result !is Result.Success) {
                allFound = false
                println("  ❌ $name 조회 실패: $result")
            }
        }

        println("[테스트 4] 8마리 포켓몬 모두 조회: ${if (allFound) "통과" else "실패"}")
        return "8마리 포켓몬 조회" to allFound
    }

    /**
     * 시나리오 5: 조회된 포켓몬 데이터 정확성 검증
     */
    private fun testPokemonDataAccuracy(dataSource: FakePokemonDataSource): Pair<String, Boolean> {
        val result = dataSource.getPokemon("pikachu")
        if (result !is Result.Success) {
            println("[테스트 5] 포켓몬 데이터 정확성: 실패 (조회 불가: $result)")
            return "포켓몬 데이터 정확성" to false
        }

        val p = result.value
        val passed = p.id == 25 &&
            p.name == "pikachu" &&
            p.type == "electric" &&
            p.height == 4 &&
            p.weight == 60 &&
            p.hp == 35 &&
            p.attack == 55 &&
            p.defense == 40 &&
            p.imageUrl?.contains("25.png") == true &&
            p.soundUrl?.contains("pikachu") == true

        println("[테스트 5] 포켓몬 데이터 정확성: ${if (passed) "통과" else "실패"} " +
            "(id=${p.id}, name=${p.name}, type=${p.type}, hp=${p.hp}, " +
            "attack=${p.attack}, defense=${p.defense}, imageUrl=${p.imageUrl})")
        return "포켓몬 데이터 정확성" to passed
    }
}

// ============================================================
// 테스트 실행 진입점
// ============================================================

fun main() {
    FakePokemonDataSourceTest().main()
}
