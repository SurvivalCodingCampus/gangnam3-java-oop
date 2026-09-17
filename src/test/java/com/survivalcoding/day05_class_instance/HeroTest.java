package com.survivalcoding.day05_class_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroTest {
    @Test
    @DisplayName("sleep 은 hp 를 100으로 만들어야 한다")
    void sleepTest2() {
        // given (준비)
        Hero hero = new Hero();
        hero.hp = 50;

        // when (실행)
        hero.sleep();

        // then (검증)
        assertEquals(200, hero.hp);
    }

    @Test
    @DisplayName("sleep 은 hp 를 100으로 만들어야 한다")
    void sleepTest() {
        // given (준비)
        Hero hero = new Hero();
        hero.hp = 50;

        // when (실행)
        hero.sleep();

        // then (검증)
        assertEquals(200, hero.hp);
    }
}