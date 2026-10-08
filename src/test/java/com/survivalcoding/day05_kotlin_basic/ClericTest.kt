package com.survivalcoding.day05_kotlin_basic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Cleric 클래스 테스트")
class ClericTest {

    @Test
    fun `이름만 지정하면 hp, mp가 최대값으로 생성된다`() {
        // given
        val name = "세실"

        // when
        val cleric = Cleric(name)

        // then
        assertEquals(name, cleric.name)
        assertEquals(Cleric.MAX_HP, cleric.hp)
        assertEquals(Cleric.MAX_MP, cleric.mp)
    }

    @Test
    fun `hp, mp가 경계값(0, 최대값)이면 정상 생성된다`() {
        // when
        val min = Cleric("세실", hp = 0, mp = 0)
        val max = Cleric("세실", hp = Cleric.MAX_HP, mp = Cleric.MAX_MP)

        // then
        assertEquals(0, min.hp)
        assertEquals(0, min.mp)
        assertEquals(Cleric.MAX_HP, max.hp)
        assertEquals(Cleric.MAX_MP, max.mp)
    }

    @Test
    fun `hp가 음수이면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Cleric("세실", hp = -1)
        }
    }

    @Test
    fun `hp가 MAX_HP를 초과하면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Cleric("세실", hp = Cleric.MAX_HP + 1)
        }
    }

    @Test
    fun `mp가 음수이면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Cleric("세실", mp = -1)
        }
    }

    @Test
    fun `mp가 MAX_MP를 초과하면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Cleric("세실", mp = Cleric.MAX_MP + 1)
        }
    }

    @Test
    fun `selfAid를 하면 mp가 MP_COST만큼 감소하고 hp가 MAX_HP가 된다`() {
        // given
        val cleric = Cleric("세실", hp = 10, mp = 10)

        // when
        cleric.selfAid()

        // then
        assertEquals(10 - Cleric.MP_COST, cleric.mp)
        assertEquals(Cleric.MAX_HP, cleric.hp)
    }

    @Test
    fun `mp가 정확히 MP_COST이면 selfAid가 실행되어 mp가 0이 된다`() {
        // given
        val cleric = Cleric("세실", hp = 10, mp = Cleric.MP_COST)

        // when
        cleric.selfAid()

        // then
        assertEquals(0, cleric.mp)
        assertEquals(Cleric.MAX_HP, cleric.hp)
    }

    @Test
    fun `mp가 MP_COST보다 적으면 selfAid가 실행되지 않고 hp, mp가 유지된다`() {
        // given
        val beforeHp = 10
        val beforeMp = Cleric.MP_COST - 1
        val cleric = Cleric("세실", hp = beforeHp, mp = beforeMp)

        // when
        cleric.selfAid()

        // then
        assertEquals(beforeHp, cleric.hp)
        assertEquals(beforeMp, cleric.mp)
    }

    @Test
    fun `기도 시간이 음수이면 예외가 발생한다`() {
        // given
        val cleric = Cleric("세실", mp = 0)

        // when & then
        assertThrows(IllegalArgumentException::class.java) {
            cleric.pray(-1)
        }
    }

    @Test
    fun `mp가 이미 최대이면 pray는 0을 반환하고 mp는 그대로다`() {
        // given
        val cleric = Cleric("세실", mp = Cleric.MAX_MP)

        // when
        val recovered = cleric.pray(3)

        // then
        assertEquals(0, recovered)
        assertEquals(Cleric.MAX_MP, cleric.mp)
    }

    @Test
    fun `회복량이 남은 mp를 넘으면 MAX_MP까지만 회복한다`() {
        // given
        val cleric = Cleric("세실", mp = Cleric.MAX_MP - 1)

        // when
        val recovered = cleric.pray(5)

        // then
        assertEquals(1, recovered)
        assertEquals(Cleric.MAX_MP, cleric.mp)
    }

    @Test
    fun `상한에 걸리지 않으면 seconds ~ seconds + 2 만큼 회복한다`() {
        // given
        val seconds = 3
        val cleric = Cleric("세실", mp = 0)

        // when
        val recovered = cleric.pray(seconds)

        // then
        assertTrue(recovered in seconds..seconds + 2)
    }

    @Test
    fun `pray의 반환값은 실제로 증가한 mp와 같다`() {
        // given
        val beforeMp = 2
        val cleric = Cleric("세실", mp = beforeMp)

        // when
        val recovered = cleric.pray(3)

        // then
        assertEquals(beforeMp + recovered, cleric.mp)
    }
}
