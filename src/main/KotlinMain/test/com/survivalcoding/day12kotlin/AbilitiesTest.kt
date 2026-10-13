package com.survivalcoding.day12kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AbilitiesTest {
    @Test
    fun `different implementations attack polymorphically`() {
        val target = Monster("표적", hp = 30, attackDamage = 0)
        val attackers: List<Attackable> = listOf(
            Hero("용사", hp = 20, attackDamage = 4),
            Zombie("좀비", damage = 3),
            SpiderMan("스파이더맨", damage = 2),
        )

        val results = attackers.map { it.attack(target) }

        assertEquals(listOf(4, 3, 2), results.map(AttackResult::damage))
        assertEquals(21, target.hp)
        assertEquals(3, Attackable.MAX_ATTACK_PER_TURN)
    }

    @Test
    fun `super flyable also satisfies flyable and toggles flight`() {
        val spiderMan = SpiderMan("스파이더맨")
        val flyable: Flyable = spiderMan

        assertTrue(flyable.fly().contains("날아올랐습니다"))
        assertTrue(spiderMan.isFlying)
        assertTrue(spiderMan.land().contains("착륙했습니다"))
        assertFalse(spiderMan.isFlying)
    }
}
