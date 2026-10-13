package com.survivalcoding.day12kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class WizardTest {
    @Test
    fun `magic consumes ten mp and damages target`() {
        val wizard = Wizard("마법사", mp = 20, hp = 40, magicDamage = 12)
        val target = Monster("표적", hp = 20, attackDamage = 0)

        val result = wizard.castMagic(target)

        assertEquals(10, wizard.mp)
        assertEquals(12, result.damage)
        assertEquals(8, target.hp)
        assertEquals(50, MagicCaster.BASE_MP)
    }

    @Test
    fun `magic fails without changing mp when below cost`() {
        val wizard = Wizard("마법사", mp = 9, hp = 40)
        val target = Monster("표적", hp = 20, attackDamage = 0)

        assertThrows(IllegalStateException::class.java) {
            wizard.castMagic(target)
        }
        assertEquals(9, wizard.mp)
        assertEquals(20, target.hp)
    }
}
