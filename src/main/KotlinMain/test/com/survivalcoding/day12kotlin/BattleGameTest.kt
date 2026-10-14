package com.survivalcoding.day12kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BattleGameTest {
    @Test
    fun `deterministic demo battle wins with expected state and events`() {
        val hero = Hero("용사", hp = 80)
        val wizard = Wizard("마법사", mp = 30, hp = 60)
        val opponents = listOf(
            AdvancedMonster("고대 골렘", hp = 100, attackDamage = 15, rewardExp = 300),
            Monster("고블린", hp = 30, attackDamage = 7),
        )

        val events = BattleGame().play(listOf(hero, wizard), opponents)

        assertEquals(7, hero.hp)
        assertEquals(60, wizard.hp)
        assertEquals(0, wizard.mp)
        assertTrue(opponents.none(Character::isAlive))
        assertEquals(2, events.filterIsInstance<BattleEvent.OpponentDefeated>().size)
        val victory = events.last()
        assertInstanceOf(BattleEvent.PartyVictory::class.java, victory)
        assertEquals(300, (victory as BattleEvent.PartyVictory).totalExp)
    }

    @Test
    fun `battle emits defeat when the party falls`() {
        val hero = Hero("용사", hp = 1, attackDamage = 0)
        val monster = Monster("강한 몬스터", hp = 10, attackDamage = 2)

        val events = BattleGame().play(listOf(hero), listOf(monster))

        assertEquals(BattleEvent.PartyDefeat, events.last())
    }

    @Test
    fun `battle rejects an empty party`() {
        val monster = Monster("고블린", hp = 10, attackDamage = 1)

        assertThrows(IllegalArgumentException::class.java) {
            BattleGame().play(emptyList(), listOf(monster))
        }
    }

    @Test
    fun `battle rejects empty opponents`() {
        val hero = Hero("용사", hp = 10)

        assertThrows(IllegalArgumentException::class.java) {
            BattleGame().play(listOf(hero), emptyList())
        }
    }

    @Test
    fun `battle rejects a zero damage stalemate`() {
        val hero = Hero("용사", hp = 10, attackDamage = 0)
        val monster = Monster("몬스터", hp = 10, attackDamage = 0)

        val exception = assertThrows(IllegalStateException::class.java) {
            BattleGame().play(listOf(hero), listOf(monster))
        }

        assertEquals(
            "전투가 교착 상태입니다. 이번 라운드에 HP 변화가 없습니다.",
            exception.message,
        )
    }
}
