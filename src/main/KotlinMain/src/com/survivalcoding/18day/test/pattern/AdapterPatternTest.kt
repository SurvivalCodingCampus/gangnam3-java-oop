package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.dto.PokeApiPokemonDto
import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.adapter.PokeApiPokemonAdapter

/**
 * GoF Adapter Pattern 테스트
 *
 * PokeApiPokemonDto → Pokemon 도메인 모델 변환 어댑터의 동작을 검증합니다.
 */
class AdapterPatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [Adapter Pattern Test 시작] ==========")
        println()

        testAdapterBasic()
        testAdapterNullSafety()
        testAdapterCompanion()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [Adapter Pattern Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    private fun testAdapterBasic() {
        val dto = PokeApiPokemonDto(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            baseExperience = 112,
            stats = listOf(
                PokeApiPokemonDto.StatDto(baseStat = 35, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "hp")),
                PokeApiPokemonDto.StatDto(baseStat = 55, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "attack")),
                PokeApiPokemonDto.StatDto(baseStat = 40, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "defense")),
                PokeApiPokemonDto.StatDto(baseStat = 90, stat = PokeApiPokemonDto.StatDto.StatNameDto(name = "speed"))
            ),
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "electric"))
            ),
            sprites = PokeApiPokemonDto.SpritesDto(frontDefault = "https://example.com/pikachu.png"),
            cries = PokeApiPokemonDto.CriesDto(latest = "https://example.com/pikachu_cry.wav"),
            abilities = listOf(
                PokeApiPokemonDto.AbilityDto(
                    ability = PokeApiPokemonDto.AbilityDto.AbilityNameDto(name = "static"),
                    isHidden = false,
                    slot = 1
                )
            )
        )

        val adapter = PokeApiPokemonAdapter()
        val pokemon = adapter.adapt(dto)

        record("Adapter - 기본 변환",
            pokemon.id == 25 &&
                pokemon.name == "pikachu" &&
                pokemon.type == "electric" &&
                pokemon.hp == 35 &&
                pokemon.attack == 55 &&
                pokemon.defense == 40 &&
                pokemon.speed == 90 &&
                pokemon.imageUrl == "https://example.com/pikachu.png" &&
                pokemon.soundUrl == "https://example.com/pikachu_cry.wav" &&
                pokemon.abilities.contains("static"))
    }

    private fun testAdapterNullSafety() {
        // 빈/null DTO로 변환 - 안전 기본값 적용
        val dto = PokeApiPokemonDto()
        val pokemon = PokeApiPokemonAdapter().adapt(dto)

        record("Adapter - 빈 DTO 안전 변환",
            pokemon.name == "알 수 없는 포켓몬" &&
                pokemon.type == "unknown" &&
                pokemon.id == 0 &&
                pokemon.hp >= 1 &&
                pokemon.imageUrl == null)
    }

    private fun testAdapterCompanion() {
        val dto = PokeApiPokemonDto(
            id = 6,
            name = "charizard",
            types = listOf(
                PokeApiPokemonDto.TypeDto(type = PokeApiPokemonDto.TypeDto.TypeNameDto(name = "fire"))
            )
        )

        val pokemon = PokeApiPokemonAdapter().adapt(dto)
        record("Adapter - 동반 객체 정적 메서드",
            pokemon.id == 6 && pokemon.name == "charizard" && pokemon.type == "fire")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AdapterPatternTest().runAll()
        }
    }
}
