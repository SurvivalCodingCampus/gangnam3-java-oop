package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WizardTest {

    @Test
    @DisplayName("heal을 하면 hp를 10 회복해야 한다")
    void heal() {
        // given
        final Wizard wizard = new Wizard("마법사", 100);
        final Hero hero = new Hero("히어로", 10);

        // when
        wizard.heal(hero);

        // then
        assertEquals(20, hero.hp);
    }
}