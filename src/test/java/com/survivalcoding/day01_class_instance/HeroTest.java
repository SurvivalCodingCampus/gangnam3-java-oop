package com.survivalcoding.day01_class_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeroTest {

    @Test
    @DisplayName("sleep 은 hp 를 100으로 만들어야 한다")
    void sleepTest() {
        // given (준비)
        Hero hero = new Hero();
        hero.hp = 50;

        hero = null;

        // when (실행)
        hero.sleep();

        // then (검증)
        assertEquals(100, hero.hp);
    }
}