package com.survivalcoding.day12kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class AdvancedMonsterTest {
    @Test
    fun `die reduces hp to zero and returns reward message`() {
        val monster = AdvancedMonster(
            name = "고대 골렘",
            hp = 100,
            attackDamage = 15,
            rewardExp = 300,
        )

        val message = monster.die()

        assertEquals(0, monster.hp)
        assertFalse(monster.isAlive)
        assertEquals("고대 골렘을(를) 처치했습니다. 경험치 300 획득!", message)
    }
}
