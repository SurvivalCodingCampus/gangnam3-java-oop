package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Slime 클래스 테스트")
public class SlimeTest {

    Slime slime;

    @BeforeEach
    void setUp() {
        slime = new Slime();
    }

    @Test
    @DisplayName("슬라임이 피해를 입으면 남은 hp만큼 체력이 감소한다")
    void takeDamage_reduceHp() {
        // given
        slime.hp = 50;

        // when
        slime.takeDamage(10);

        // then
        assertEquals(40, slime.hp);
    }

    @Test
    @DisplayName("남은 hp보다 큰 피해를 받으면 hp가 음수가 되지 않고 0이 되어야 한다")
    void takeDamage_hpShouldNotBeNegative() {
    // given
    slime.hp = 5;

    // when
    slime.takeDamage(10);

    // then
    assertEquals(0, slime.hp);
    }
}
