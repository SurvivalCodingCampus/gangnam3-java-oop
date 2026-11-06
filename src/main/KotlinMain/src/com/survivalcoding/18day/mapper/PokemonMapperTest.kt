package com.survivalcoding.`18day`.mapper

import com.survivalcoding.`18day`.dto.PokeApiPokemonDto
import com.survivalcoding.`18day`.model.Pokemon

/**
 * PokemonMapper 테스트 클래스
 *
 * 테스트 시나리오:
 * 1. 정상적인 DTO → 정확한 Pokemon 모델 변환
 * 2. name이 빈 문자열("") → "알 수 없는 포켓몬" 기본값
 * 3. name이 공백만 있는 경우 → "알 수 없는 포켓몬" 기본값
 * 4. type이 빈 값 → "unknown" 기본값
 * 5. 음수 값 (id, height, weight, hp, attack, defense, baseExperience) → 0 처리
 * 6. id가 0 이하 → 0 처리
 * 7. imageUrl이 null/빈 값 → null 처리
 * 8. 모든 필드가 잘못된 값 → 안전한 기본값 Pokemon 반환 (게임 중단 없음)
 * 9. 예외 상황에서도 게임 중단 없음 (방어적 프로그래밍 검증)
 */
class PokemonMapperTest {

    // ============================================================
    // 테스트 실행 메서드 (main)
    // ============================================================

    fun main() {
        println("========== [PokemonMapper 테스트 시작] ==========")
        println()

        var passCount = 0
        var failCount = 0

        val results = mutableListOf<Pair<String, Boolean>>()

        results += testNormalDto()
        results += testNameIsEmptyString()
        results += testNameIsBlankOnly()
        results += testTypeIsEmpty()
        results += testNegativeValues()
        results += testIdIsZeroOrNegative()
        results += testImageUrlIsNullOrEmpty()
        results += testAllInvalidValues()
        results += testGameNeverCrashes()

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
        println("========== [PokemonMapper 테스트 종료] ==========")
    }

    // ============================================================
    // 개별 테스트 케이스 (각각 Pair<테스트명, 통과여부> 반환)
    // ============================================================

    /**
     * 시나리오 1: 정상적인 DTO → 정확한 Pokemon 모델 변환
     */
    private fun testNormalDto(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            baseExperience = 112,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = 35, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = 55, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = 40, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "electric"))
            ),
            sprites = PokeApiPokemonDto.SpritesDto(frontDefault = "https://example.com/pikachu.png")
        )

        val result = PokemonMapper.toModel(dto)

        val passed = result.id == 25 &&
            result.name == "pikachu" &&
            result.type == "electric" &&
            result.height == 4 &&
            result.weight == 60 &&
            result.baseExperience == 112 &&
            result.hp == 35 &&
            result.attack == 55 &&
            result.defense == 40 &&
            result.imageUrl == "https://example.com/pikachu.png" &&
            result.isAlive()

        println("[테스트 1] 정상적인 DTO 변환: ${if (passed) "통과" else "실패"}")
        if (!passed) {
            println("  예상: id=25, name=pikachu, type=electric, height=4, weight=60, hp=35, imageUrl=https://example.com/pikachu.png")
            println("  실제: id=${result.id}, name=${result.name}, type=${result.type}, height=${result.height}, weight=${result.weight}, hp=${result.hp}, imageUrl=${result.imageUrl}")
        }
        return "정상적인 DTO 변환" to passed
    }

    /**
     * 시나리오 2: name이 빈 문자열("") → "알 수 없는 포켓몬" 기본값
     */
    private fun testNameIsEmptyString(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = 1,
            name = "",
            height = 7,
            weight = 69,
            baseExperience = 64,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = 45, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = 49, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = 49, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "grass"))
            )
        )

        val result = PokemonMapper.toModel(dto)

        val passed = result.name == "알 수 없는 포켓몬"
        println("[테스트 2] name이 빈 문자열인 경우: ${if (passed) "통과" else "실패"}")
        if (!passed) {
            println("  예상: name='알 수 없는 포켓몬'")
            println("  실제: name='${result.name}'")
        }
        return "name이 빈 문자열" to passed
    }

    /**
     * 시나리오 3: name이 공백만 있는 경우 → "알 수 없는 포켓몬" 기본값
     */
    private fun testNameIsBlankOnly(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = 1,
            name = "   ",
            height = 7,
            weight = 69,
            baseExperience = 64,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = 45, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = 49, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = 49, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "grass"))
            )
        )

        val result = PokemonMapper.toModel(dto)

        val passed = result.name == "알 수 없는 포켓몬"
        println("[테스트 3] name이 공백만 있는 경우: ${if (passed) "통과" else "실패"}")
        if (!passed) {
            println("  예상: name='알 수 없는 포켓몬'")
            println("  실제: name='${result.name}'")
        }
        return "name이 공백만 있음" to passed
    }

    /**
     * 시나리오 4: type이 빈 값 → "unknown" 기본값
     */
    private fun testTypeIsEmpty(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = 45, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = 49, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = 49, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = emptyList()
        )

        val result = PokemonMapper.toModel(dto)

        val passed = result.type == "unknown"
        println("[테스트 4] type이 빈 문자열인 경우: ${if (passed) "통과" else "실패"}")
        if (!passed) {
            println("  예상: type='unknown'")
            println("  실제: type='${result.type}'")
        }
        return "type이 빈 값" to passed
    }

    /**
     * 시나리오 5: 음수 값 → 0 처리
     */
    private fun testNegativeValues(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = 1,
            name = "test",
            height = -10,
            weight = -5,
            baseExperience = -100,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = -10, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = -5, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = -3, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "fire"))
            )
        )

        val result = PokemonMapper.toModel(dto)

        val passed = result.height == 0 &&
            result.weight == 0 &&
            result.baseExperience == 0 &&
            result.hp == 0 &&
            result.attack == 0 &&
            result.defense == 0

        println("[테스트 5] 모든 수치 필드가 음수인 경우: ${if (passed) "통과" else "실패"}")
        if (!passed) {
            println("  예상: height=0, weight=0, baseExperience=0, hp=0, attack=0, defense=0")
            println("  실제: height=${result.height}, weight=${result.weight}, baseExperience=${result.baseExperience}, hp=${result.hp}, attack=${result.attack}, defense=${result.defense}")
        }
        return "음수 값 처리" to passed
    }

    /**
     * 시나리오 6: id가 0 이하 → 0 처리
     */
    private fun testIdIsZeroOrNegative(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = -5,
            name = "test",
            height = 10,
            weight = 50,
            baseExperience = 50,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = 50, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = 50, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = 50, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "fire"))
            )
        )

        val result = PokemonMapper.toModel(dto)

        val passed = result.id == 0
        println("[테스트 6] id가 음수인 경우: ${if (passed) "통과" else "실패"}")
        if (!passed) {
            println("  예상: id=0")
            println("  실제: id=${result.id}")
        }
        return "id가 0 이하" to passed
    }

    /**
     * 시나리오 7: imageUrl이 null/빈 값 → null 처리
     */
    private fun testImageUrlIsNullOrEmpty(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = 1,
            name = "test",
            height = 10,
            weight = 50,
            baseExperience = 50,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = 50, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = 50, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = 50, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "fire"))
            ),
            sprites = PokeApiPokemonDto.SpritesDto(frontDefault = "")
        )

        val result = PokemonMapper.toModel(dto)

        val passed = result.imageUrl == null
        println("[테스트 7] imageUrl이 빈 문자열인 경우: ${if (passed) "통과" else "실패"}")
        if (!passed) {
            println("  예상: imageUrl=null")
            println("  실제: imageUrl=${result.imageUrl}")
        }
        return "imageUrl이 빈 값" to passed
    }

    /**
     * 시나리오 8: 모든 필드가 잘못된 값 → 안전한 기본값 반환 (게임 중단 없음)
     */
    private fun testAllInvalidValues(): Pair<String, Boolean> {
        val dto = PokeApiPokemonDto(
            id = -999,
            name = "   ",
            height = -999,
            weight = -999,
            baseExperience = -999,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = -999, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = -999, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = -999, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "   "))
            ),
            sprites = PokeApiPokemonDto.SpritesDto(frontDefault = "   ")
        )

        var exceptionOccurred = false
        try {
            val result = PokemonMapper.toModel(dto)
            exceptionOccurred = false

            val passed = result.id == 0 &&
                result.name == "알 수 없는 포켓몬" &&
                result.type == "unknown" &&
                result.height == 0 &&
                result.weight == 0 &&
                result.baseExperience == 0 &&
                result.hp == 0 &&
                result.attack == 0 &&
                result.defense == 0 &&
                result.imageUrl == null &&
                !result.isAlive()

            println("[테스트 8] 모든 필드가 잘못된 값인 경우: ${if (passed) "통과" else "실패"}")
            if (!passed) {
                println("  예상: 모든 필드가 기본값, 게임 중단 없음")
                println("  실제: id=${result.id}, name='${result.name}', type='${result.type}', hp=${result.hp}")
            }
            return "모든 필드가 잘못된 값" to (passed && !exceptionOccurred)
        } catch (e: Exception) {
            exceptionOccurred = true
            println("[테스트 8] 모든 필드가 잘못된 값인 경우: 실패 (예외 발생: ${e.message})")
            return "모든 필드가 잘못된 값" to false
        }
    }

    /**
     * 시나리오 9: 예외 상황에서도 게임 중단 없음 (방어적 프로그래밍 검증)
     */
    private fun testGameNeverCrashes(): Pair<String, Boolean> {
        // 다양한 비정상 입력으로 매퍼 호출 - 예외가 발생하면 게임 중단
        val testCases = listOf(
            PokeApiPokemonDto(
                id = Int.MIN_VALUE,
                name = "",
                height = Int.MIN_VALUE,
                weight = Int.MIN_VALUE,
                baseExperience = Int.MIN_VALUE,
                stats = listOf(
                    PokeApiPokemonDto.StatDto(baseStat = Int.MIN_VALUE, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                    PokeApiPokemonDto.StatDto(baseStat = Int.MIN_VALUE, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                    PokeApiPokemonDto.StatDto(baseStat = Int.MIN_VALUE, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
                ),
                types = listOf(
                    PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = ""))
                )
            ),
            PokeApiPokemonDto(
                id = Int.MAX_VALUE,
                name = "a".repeat(10000),
                height = Int.MAX_VALUE,
                weight = Int.MAX_VALUE,
                baseExperience = Int.MAX_VALUE,
                stats = listOf(
                    PokeApiPokemonDto.StatDto(baseStat = Int.MAX_VALUE, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                    PokeApiPokemonDto.StatDto(baseStat = Int.MAX_VALUE, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                    PokeApiPokemonDto.StatDto(baseStat = Int.MAX_VALUE, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense"))
                ),
                types = listOf(
                    PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "x".repeat(10000)))
                )
            ),
            PokeApiPokemonDto(
                id = 0,
                name = "",
                height = 0,
                weight = 0,
                baseExperience = 0,
                stats = emptyList(),
                types = emptyList()
            )
        )

        var allPassed = true
        for ((index, dto) in testCases.withIndex()) {
            try {
                val result = PokemonMapper.toModel(dto)
                // 예외 없이 Pokemon 객체가 반환되면 성공
                if (result.name.isBlank() || result.type.isBlank()) {
                    allPassed = false
                    println("[테스트 9-$(index + 1)] 예외 상황 처리: 실패 (결과가 비정상)")
                }
            } catch (e: Exception) {
                allPassed = false
                println("[테스트 9-$(index + 1)] 예외 상황 처리: 실패 (예외 발생: ${e.message}) - 게임 중단!")
            }
        }

        if (allPassed) {
            println("[테스트 9] 모든 예외 상황 처리: 통과 (게임 중단 없음)")
        }
        return "게임 중단 없음 (방어적 처리)" to allPassed
    }
}

// ============================================================
// 테스트 실행 진입점
// ============================================================

fun main() {
    PokemonMapperTest().main()
}
