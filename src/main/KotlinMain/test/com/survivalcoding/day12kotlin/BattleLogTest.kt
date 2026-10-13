package com.survivalcoding.day12kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BattleLogTest {
    @Test
    fun `formats round started`() {
        assertEquals(
            "=== 3 라운드 ===",
            BattleLog.format(BattleEvent.RoundStarted(round = 3)),
        )
    }

    @Test
    fun `formats every attack result field`() {
        val event = BattleEvent.AttackPerformed(
            AttackResult(
                attackerName = "용사",
                targetName = "고블린",
                damage = 7,
                remainingHp = 23,
                attackName = "검 공격",
            ),
        )

        assertEquals(
            "용사: 검 공격 -> 고블린에게 7 피해 (HP 23)",
            BattleLog.format(event),
        )
    }

    @Test
    fun `formats opponent defeated`() {
        assertEquals(
            "고대 골렘을(를) 처치했습니다. 경험치 300 획득!",
            BattleLog.format(BattleEvent.OpponentDefeated("고대 골렘", rewardExp = 300)),
        )
    }

    @Test
    fun `formats party victory`() {
        assertEquals(
            "파티 승리! 총 경험치: 300",
            BattleLog.format(BattleEvent.PartyVictory(totalExp = 300)),
        )
    }

    @Test
    fun `formats party defeat`() {
        assertEquals(
            "파티가 패배했습니다.",
            BattleLog.format(BattleEvent.PartyDefeat),
        )
    }
}
