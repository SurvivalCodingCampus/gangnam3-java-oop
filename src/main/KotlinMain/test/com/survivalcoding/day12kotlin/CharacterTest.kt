package com.survivalcoding.day12kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CharacterTest {
    @Test
    fun `name must not be blank`() {
        assertThrows(IllegalArgumentException::class.java) {
            Hero("  ", hp = 10)
        }
    }

    @Test
    fun `damage floors hp at zero`() {
        val hero = Hero("용사", hp = 10)

        val inflictedDamage = hero.takeDamage(100)

        assertEquals(10, inflictedDamage)
        assertEquals(0, hero.hp)
        assertFalse(hero.isAlive)
    }

    @Test
    fun `damage cannot be negative`() {
        val hero = Hero("용사", hp = 10)

        assertThrows(IllegalArgumentException::class.java) {
            hero.takeDamage(-1)
        }
    }

    @Test
    fun `character reference dispatches to concrete attack`() {
        val attacker: Character = Hero("용사", hp = 10, attackDamage = 4)
        val target = Monster("표적", hp = 10, attackDamage = 0)

        val result = attacker.attack(target)

        assertEquals(4, result.damage)
        assertEquals(6, target.hp)
    }
}
