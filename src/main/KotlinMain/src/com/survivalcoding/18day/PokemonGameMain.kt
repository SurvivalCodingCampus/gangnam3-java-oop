package com.survivalcoding.`18day`

import com.survivalcoding.`18day`.battle.DefaultTypeEffectivenessStrategy
import com.survivalcoding.`18day`.battle.PokemonBattleService
import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource
import com.survivalcoding.`18day`.Data_source.PokemonDataSource
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.adapter.PokeApiPokemonAdapter
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory.DataSourceKind
import com.survivalcoding.`18day`.pattern.proxy.CachingDataSourceProxy
import com.survivalcoding.`18day`.repostory.PokemonRepositoryImpl

fun main() {
    println("========== 포켓몬 게임 시작 ==========")
    println()

    // [GoF Factory] 팩토리로 저장소 생성
    val useRemote = false
    val repository: com.survivalcoding.`18day`.repostory.IPokemonRepository = if (useRemote) {
        // [GoF Proxy] 원격 데이터 소스에 캐싱 프록시 적용 (팩토리 내부에서 처리)
        PokemonRepositoryFactory.createRepository(DataSourceKind.REMOTE)
    } else {
        val fakeDataSource: PokemonDataSource = FakePokemonDataSource()
        PokemonRepositoryFactory.createRepository(DataSourceKind.FAKE)
    }

    val pokemonNames = listOf("pikachu", "charizard", "bulbasaur", "squirtle", "jigglypuff", "meowth", "psyduck", "snorlax")

    println("📋 사용 가능한 포켓몬 목록:")
    for (name in pokemonNames) {
        val result = repository.getPokemon(name)
        when (result) {
            is Result.Success -> {
                val p = result.value
                println("   ✅ ${p.summary()}")
                println("      타입: ${p.type}, HP: ${p.hp}, 공격: ${p.attack}, 방어: ${p.defense}")
                println("      이미지: ${p.imageUrl ?: "없음"}")
                println("      사운드: ${p.getAvailableSoundUrl() ?: "없음 (기본 효과음)"}")
                println("      전투 가능: ${if (p.isAlive()) "가능" else "불가능"}")
            }
            is Result.Error -> {
                println("   ❌ $name: ${result.error}")
            }
            else -> {
                println("   ❓ $name: 알 수 없는 오류 발생")
            }
        }
        println()
    }

    // [GoF Adapter] PokeApiPokemonAdapter 사용 예시
    println("========== [GoF Adapter 패턴 데모] ==========")
    println("PokeApiPokemonAdapter 준비 완료 (DTO → Pokemon 변환)")
    println()

    // 배틀 서비스 생성 (SRP: 배틀 로직 전담)
    val battleService = PokemonBattleService(DefaultTypeEffectivenessStrategy())

    // 배틀 시연: pikachu vs charizard
    println("========== 배틀 시연: pikachu vs charizard ==========")
    println()

    val pikachuResult = repository.getPokemon("pikachu")
    val charizardResult = repository.getPokemon("charizard")

    when {
        pikachuResult is Result.Success && charizardResult is Result.Success -> {
            var myPokemon = pikachuResult.value
            var opponentPokemon = charizardResult.value
            var turn = 1

            println("⚔ 배틀 시작! ${myPokemon.name.replaceFirstChar { it.uppercase() }} vs ${opponentPokemon.name.replaceFirstChar { it.uppercase() }}")
            println()

            while (myPokemon.isAlive() && opponentPokemon.isAlive()) {
                println("--- 턴 $turn ---")

                if (turn % 2 == 1) {
                    // 내 턴
                    val damage = battleService.calculateDamage(myPokemon, opponentPokemon)
                    val effectiveness = battleService.getTypeEffectiveness(myPokemon.type, opponentPokemon.type)
                    opponentPokemon = battleService.applyDamage(opponentPokemon, damage)
                    println("🔵 ${myPokemon.name.replaceFirstChar { it.uppercase() }}의 공격! ${opponentPokemon.name.replaceFirstChar { it.uppercase() }}에게 ${damage} 데미지! (상성 배율: $effectiveness)")
                    println("   ${battleService.createAttackMessage(myPokemon, opponentPokemon, damage, effectiveness)}")
                    println("   ${opponentPokemon.name.replaceFirstChar { it.uppercase() }} HP: ${opponentPokemon.hp}")
                } else {
                    // 상대 턴
                    val damage = battleService.calculateDamage(opponentPokemon, myPokemon)
                    val effectiveness = battleService.getTypeEffectiveness(opponentPokemon.type, myPokemon.type)
                    myPokemon = battleService.applyDamage(myPokemon, damage)
                    println("🔴 ${opponentPokemon.name.replaceFirstChar { it.uppercase() }}의 공격! ${myPokemon.name.replaceFirstChar { it.uppercase() }}에게 ${damage} 데미지! (상성 배율: $effectiveness)")
                    println("   ${battleService.createAttackMessage(opponentPokemon, myPokemon, damage, effectiveness)}")
                    println("   ${myPokemon.name.replaceFirstChar { it.uppercase() }} HP: ${myPokemon.hp}")
                }
                turn++
                println()
            }

            val winner = if (myPokemon.isAlive()) myPokemon else opponentPokemon
            println("🏆 ${winner.name.replaceFirstChar { it.uppercase() }}의 승리!")
        }
        else -> {
            println("❌ 배틀 시연에 필요한 포켓몬을 불러올 수 없습니다.")
        }
    }

    println()
    println("========== 포켓몬 게임 종료 ==========")
}
