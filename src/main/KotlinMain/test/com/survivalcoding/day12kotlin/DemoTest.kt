package com.survivalcoding.day12kotlin

import com.survivalcoding.generic_enum_string.createDemoOutput
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DemoTest {
    @Test
    fun `demo reports battle polymorphism inheritance and constants`() {
        val output = createDemoOutput().joinToString("\n")

        assertTrue(output.contains("파티 승리! 총 경험치: 300"))
        assertTrue(output.contains("최종 상태: 용사 HP 7, 마법사 HP 60, MP 0"))
        assertTrue(output.contains("Attackable 다형성"))
        assertTrue(output.contains("인터페이스 상속"))
        assertTrue(output.contains("최대 공격 3회, 기본 MP 50"))
    }
}
