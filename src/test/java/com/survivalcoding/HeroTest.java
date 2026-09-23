package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HeroTest {

    @Test
    void testHeroCreation() {
        Hero hero = new Hero();

        assertNotNull(hero);
    }
}