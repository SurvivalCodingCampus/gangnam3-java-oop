package com.survivalcoding.day05_kotlin_basic

class Cleric(
    val name: String?,
    hp: Int = MAX_HP,
    mp: Int = MAX_MP
) {
    var hp: Int = hp
        private set

    var mp: Int = mp
        private set

    init {
        require(hp in 0..MAX_HP) {
            "HP는 0부터 $MAX_HP 사이여야 합니다"
        }
        require(mp in 0..MAX_MP) {
            "MP는 0부터 $MAX_MP 사이여야 합니다"
        }
    }

    fun selfAid() {
        if (mp < MP_COST) {
            return
        }
        mp -= MP_COST
        hp = MAX_HP
    }

    fun pray(seconds: Int): Int {
        require(seconds >= 0) {
            "기도 시간은 음수일 수 없습니다"
        }

        val bonus = (0..2).random()
        val recovery = seconds + bonus
        val maxRecovery = MAX_MP - mp
        val actualRecovery = minOf(recovery, maxRecovery)

        mp += actualRecovery

        return actualRecovery
    }

    companion object {
        const val MAX_HP = 50
        const val MAX_MP = 10
        const val MP_COST = 5
    }
}