package com.survivalcoding.game.entity;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("StatusEffect: 지속시간과 DoT")
class StatusEffectTest {
    private GameState state;
    private Hero hero;

    @BeforeEach
    void setUp() {
        state = new GameState();
        hero = new Hero("hero", 100, 100);
    }

    @Test
    void poisonDealsDamageOverTime() {
        hero.addStatusEffect(new StatusEffect(StatusEffect.Type.POISON, 1.0, 5, 0.5));

        int before = hero.getHp();
        hero.updateStatusEffects(0.6, state);

        assertTrue(hero.getHp() < before);
    }

    @Test
    void effectExpires() {
        hero.addStatusEffect(new StatusEffect(StatusEffect.Type.STUN, 0.1, 0, 0.5));

        hero.updateStatusEffects(0.2, state);

        assertFalse(hero.isStunned());
    }

    @Test
    void stunIsVisibleThroughIsStunned() {
        hero.addStatusEffect(new StatusEffect(StatusEffect.Type.STUN, 1.0, 0, 0.5));
        assertTrue(hero.isStunned());
    }
}
