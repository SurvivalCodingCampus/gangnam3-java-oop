package com.survivalcoding.day03_polymorphism;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.survivalcoding.day01_class_instance.Slime;

@DisplayName("Hero 클래스 테스트")
public class HeroTest {

    @Test
    @DisplayName("Hero가 일반 공격하면 Slime의 hp가 10 감소해야 한다")
    void attack_shouldReduceSlimeHp() {
        // given
        Hero hero = new Hero("히어로", 100);
        Slime slime = new Slime("슬라임", 50);

        // when
        hero.attack(slime);

        // then
        assertEquals(40, slime.getHp());
    }
}
