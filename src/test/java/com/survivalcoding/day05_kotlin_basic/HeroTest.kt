package com.survivalcoding.day05_kotlin_basic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Hero 클래스 테스트")
class HeroTest {

    @Test
    fun `hp를 지정하지 않으면 MAX_HP로 생성된다`() {
        // when
        val hero = Hero("홍길동", "Mr.홍")

        // then
        assertEquals("Mr.홍", hero.nickname)
        assertEquals(Hero.MAX_HP, hero.hp)
    }

    @Test
    fun `생성자에 hp가 음수이면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Hero("홍길동", "Mr.홍", hp = -1)
        }
    }

    @Test
    fun `생성자에 hp가 MAX_HP를 초과하면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Hero("홍길동", "Mr.홍", hp = Hero.MAX_HP + 1)
        }
    }

    @Test
    fun `hp에 경계값(0, MAX_HP)을 대입할 수 있다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍")

        // when & then
        hero.hp = 0
        assertEquals(0, hero.hp)

        hero.hp = Hero.MAX_HP
        assertEquals(Hero.MAX_HP, hero.hp)
    }

    @Test
    fun `hp에 음수를 대입하면 예외가 발생한다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍")

        // when & then
        assertThrows(IllegalArgumentException::class.java) {
            hero.hp = -1
        }
    }

    @Test
    fun `hp에 MAX_HP 초과 값을 대입하면 예외가 발생한다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍")

        // when & then
        assertThrows(IllegalArgumentException::class.java) {
            hero.hp = Hero.MAX_HP + 1
        }
    }

    @Test
    fun `damage를 인자 없이 호출하면 hp가 10 감소한다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍", hp = 50)

        // when
        hero.damage()

        // then
        assertEquals(40, hero.hp)
    }

    @Test
    fun `damage에 지정한 만큼 hp가 감소한다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍", hp = 50)

        // when
        hero.damage(amount = 30)

        // then
        assertEquals(20, hero.hp)
    }

    @Test
    fun `hp보다 큰 피해를 받으면 hp는 0이 된다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍", hp = 5)

        // when
        hero.damage(amount = 30)

        // then
        assertEquals(0, hero.hp)
    }

    @Test
    fun `damage에 0이나 음수를 넣으면 hp가 변하지 않는다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍", hp = 50)

        // when
        hero.damage(amount = 0)
        hero.damage(amount = -10)

        // then
        assertEquals(50, hero.hp)
    }

    @Test
    fun `hp가 1 이상이면 isAlive는 true다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍", hp = 1)

        // then
        assertTrue(hero.isAlive)
    }

    @Test
    fun `피해를 받아 hp가 0이 되면 isAlive는 false다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍", hp = 10)

        // when
        hero.damage(amount = 10)

        // then
        assertFalse(hero.isAlive)
    }

    @Test
    fun `sleep을 하면 hp가 MAX_HP로 회복된다`() {
        // given
        val hero = Hero("홍길동", "Mr.홍", hp = 10)

        // when
        hero.sleep()

        // then
        assertEquals(Hero.MAX_HP, hero.hp)
    }
}
