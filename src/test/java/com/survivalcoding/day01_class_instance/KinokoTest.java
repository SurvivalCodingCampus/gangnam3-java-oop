package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Kinoko 클래스 테스트")
class KinokoTest {

    Kinoko kinoko;

    @BeforeEach
    void setUp() {
        kinoko = new Kinoko("버섯", 50);
    }

    @Test
    @DisplayName("데미지를 입으면 hp가 감소해야 한다")
    void takeDamage_shouldReduceHp() {
        // given
        int damage = 10;
        int expected = 40;

        // when
        kinoko.takeDamage(damage);

        // then
        assertEquals(expected, kinoko.getHp());
    }

    @Test
    @DisplayName("남은 hp보다 큰 피해를 받으면 hp가 음수가 되지 않고 0이 되어야 한다")
    void takeDamage_hpShouldNotBeNegative() {
        // given
        int damage = 100;
        int expected = 0;

        // when
        kinoko.takeDamage(damage);

        // then
        assertEquals(expected, kinoko.getHp());
    }

    @Test
    @DisplayName("setHp에 음수를 입력하면 0으로 보정되어야 한다")
    void setHp_shouldClampToZero_whenNegativeValueGiven() {
        // given
        int hp = -5;
        int expected = 0;

        // when
        kinoko.setHp(hp);

        // then
        assertEquals(expected, kinoko.getHp());
    }
}
