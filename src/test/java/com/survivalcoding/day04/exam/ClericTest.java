package com.survivalcoding.day04.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {
    @Test
    @DisplayName("heal을 하면 hp를 10 회복해야 한다")
    void heal() {
        // given
        final Cleric cleric = new Cleric();
        cleric.mp = 6;

        final int beforeMp = cleric.mp;

        // when
        cleric.selfAid();

        // then
        assertEquals(cleric.MAX_HP, cleric.hp);
        assertEquals(beforeMp - cleric.COST_FOR_SELF_AID, cleric.mp);
    }

    @Test
    @DisplayName("MP가 부족하면 회복을 못 한다")
    void heal2() {
        // given
        final Cleric cleric = new Cleric();
        cleric.mp = 4;

        final int beforeHp = cleric.hp;
        final int beforeMp = cleric.mp;

        // when
        cleric.selfAid();

        // then
        assertEquals(beforeHp, cleric.hp);
        assertEquals(beforeMp, cleric.mp);
    }
}