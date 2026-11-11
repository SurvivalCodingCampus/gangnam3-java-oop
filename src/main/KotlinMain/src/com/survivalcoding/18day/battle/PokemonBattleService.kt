package com.survivalcoding.`18day`.battle

import com.survivalcoding.`18day`.model.Pokemon

/**
 * 포켓몬 배틀 로직 전담 서비스.
 * 데미지 계산, HP 감소, 승패 판정, 공격 메시지 생성을 담당합니다 (SRP, SDP).
 */
class PokemonBattleService(
    private val typeEffectivenessStrategy: TypeEffectivenessStrategy = DefaultTypeEffectivenessStrategy()
) {

    /**
     * 공격 포켓몬이 방어 포켓몬에게 입힐 데미지를 계산합니다.
     * 기본 데미지 = 공격력 - 상대 방어력 / 2, 타입 상성 배율 적용
     * 최소 데미지는 1입니다.
     */
    fun calculateDamage(attacker: Pokemon, defender: Pokemon): Int {
        val baseDamage = (attacker.attack - defender.defense / 2).coerceAtLeast(1)
        val effectiveness = typeEffectivenessStrategy.getEffectiveness(attacker.type, defender.type)
        return (baseDamage * effectiveness).coerceAtLeast(1.0).toInt()
    }

    /**
     * 타입 상성 배율을 반환합니다.
     */
    fun getTypeEffectiveness(attackType: String, defendType: String): Double {
        return typeEffectivenessStrategy.getEffectiveness(attackType, defendType)
    }

    /**
     * 데미지를 입어 HP를 감소시킨 새 Pokemon 인스턴스를 반환합니다.
     * HP는 0 이하로 내려가지 않습니다 (불변성 보장).
     */
    fun applyDamage(pokemon: Pokemon, damage: Int): Pokemon {
        val actualDamage = damage.coerceAtLeast(0)
        val newHp = (pokemon.hp - actualDamage).coerceAtLeast(0)
        return pokemon.copy(hp = newHp)
    }

    /**
     * 공격 결과 메시지를 생성합니다.
     */
    fun createAttackMessage(attacker: Pokemon, target: Pokemon, damage: Int, effectiveness: Double): String {
        val typeMsg = when {
            effectiveness > 1.0 -> "상대방에게 효과가 탁월하다!"
            effectiveness < 1.0 && effectiveness > 0.0 -> "효과가 별로 없는 것 같다..."
            else -> ""
        }
        return "${attacker.name.replaceFirstChar { it.uppercase() }}의 공격! ${target.name.replaceFirstChar { it.uppercase() }}에게 ${damage} 데미지! $typeMsg"
    }

    /**
     * 승패를 판정합니다.
     * @return 승리한 포켓몬, 둘 다 살아있으면 null
     */
    fun determineWinner(myPokemon: Pokemon, opponentPokemon: Pokemon): Pokemon? {
        if (!myPokemon.isAlive()) return opponentPokemon
        if (!opponentPokemon.isAlive()) return myPokemon
        return null
    }
}
