package com.survivalcoding.day05_kotlin_basic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SuperHero 클래스 테스트")
class SuperHeroTest {

    @Test
    fun `생성자에 넘긴 nickname과 hp가 그대로 설정된다`() {
        // when
        val superHero = SuperHero("이순신", "충무공", 70)

        // then
        assertEquals("충무공", superHero.nickname)
        assertEquals(70, superHero.hp)
    }

    @Test
    fun `생성자에 hp가 MAX_HP를 초과하면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            SuperHero("이순신", "충무공", Hero.MAX_HP + 1)
        }
    }
}