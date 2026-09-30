package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Slime 클래스 테스트")
public class SlimeTest {

    @Test
    @DisplayName("슬라임이 피해를 입으면 남은 hp만큼 체력이 감소한다")
    void takeDamage_reduceHp() {
        // given
        Slime slime = new Slime("슬라임A", 50);
        int damage = 10;
        int expected = 40;

        // when
        slime.takeDamage(damage);

        // then
        assertEquals(expected, slime.getHp());
    }

    @Test
    @DisplayName("남은 hp보다 큰 피해를 받으면 hp가 음수가 되지 않고 0이 되어야 한다")
    void takeDamage_hpShouldNotBeNegative() {
        // given
        Slime slime = new Slime("슬라임B", 5);
        int damage = 10;
        int expected = 0;

        // when
        slime.takeDamage(damage);

        // then
        assertEquals(expected, slime.getHp());
    }

    @Test
    @DisplayName("attack을 하면 상대방 hero의 hp가 10 감소해야 한다")
    void attack_shouldReduceHeroHp_byTen() {
        // given
        Slime slime = new Slime("슬라임A", 50);
        Hero hero = new Hero("히어로");
        int expected = 90;

        // when
        slime.attack(hero);

        // then
        assertEquals(expected, hero.getHp());
    }
}
