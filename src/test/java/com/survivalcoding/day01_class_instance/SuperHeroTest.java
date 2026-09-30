package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SuperHero 클래스 테스트")
class SuperHeroTest {

    SuperHero superHero;

    @BeforeEach
    void setUp() {
        superHero = new SuperHero("홍길동");
    }

    @Test
    @DisplayName("생성자 호출 시 isFlying은 기본값 false여야 한다")
    void constructor_shouldInitializeIsFlying_toFalse() {
        // then
        assertFalse(superHero.isFlying());
    }

    @Test
    @DisplayName("fly를 호출하면 isFlying이 true가 되어야 한다")
    void fly_shouldSetIsFlying_toTrue() {
        // when
        superHero.fly();

        // then
        assertTrue(superHero.isFlying());
    }

    @Test
    @DisplayName("land를 호출하면 isFlying이 false가 되어야 한다")
    void land_shouldSetIsFlying_toFalse() {
        // given
        superHero.fly();

        // when
        superHero.land();

        // then
        assertFalse(superHero.isFlying());
    }

    @Test
    @DisplayName("isFlying이 true이면 attack 시 적에게 추가 피해를 입혀야 한다")
    void attack_shouldDealExtraDamage_whenFlying() {
        // given
        superHero.fly();
        Kinoko kinoko = new Kinoko("버섯", 30);
        int expectedSuperHeroHp = 98;
        int expectedKinokoHp = 25;

        // when
        superHero.attack(kinoko);

        // then
        assertEquals(expectedSuperHeroHp, superHero.getHp());
        assertEquals(expectedKinokoHp, kinoko.getHp());
    }

    @Test
    @DisplayName("isFlying이 false이면 attack 시 적에게 추가 피해를 입히지 않아야 한다")
    void attack_shouldNotDealExtraDamage_whenNotFlying() {
        // given
        Kinoko kinoko = new Kinoko("버섯", 30);
        int expectedSuperHeroHp = 98;
        int expectedKinokoHp = 30;

        // when
        superHero.attack(kinoko);

        // then
        assertEquals(expectedSuperHeroHp, superHero.getHp());
        assertEquals(expectedKinokoHp, kinoko.getHp());
    }
}
