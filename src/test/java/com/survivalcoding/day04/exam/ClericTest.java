package com.survivalcoding.day04.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {
    @Test
    @DisplayName("heal을 하면 hp를 10 회복해야 한다")
    void heal() {
        // Given
        final Cleric CLERIC = new Cleric();
        CLERIC.mp = 6;

        final int BEFORE_MP = CLERIC.mp;

        // When
        CLERIC.selfAid();

        // Then
        assertEquals(CLERIC.MAX_HP, CLERIC.hp);
        assertEquals(BEFORE_MP - CLERIC.COST_FOR_SELF_AID, CLERIC.mp);
    }

    @Test
    @DisplayName("MP가 부족하면 회복을 못 한다")
    void heal2() {
        // Given
        final Cleric CLERIC = new Cleric();
        CLERIC.mp = 4;

        final int BEFORE_HP = CLERIC.hp;
        final int BEFORE_MP = CLERIC.mp;

        // When
        CLERIC.selfAid();

        // Then
        assertEquals(BEFORE_HP, CLERIC.hp);
        assertEquals(BEFORE_MP, CLERIC.mp);
    }

    @Test
    @DisplayName("회복 마나가 최댓값보다 작을 때")
    void restoreMp() {
        // Given
        final Cleric CLERIC = new Cleric();
        CLERIC.mp = 1;

        final int BEFORE_MP = CLERIC.mp;
        final int DURATION_SECOND = 9;

        final int MIN_MP_RESTORE_AMOUNT = BEFORE_MP + DURATION_SECOND;
        final int MAX_MP_RESTORE_AMOUNT = MIN_MP_RESTORE_AMOUNT + CLERIC.MAX_CORRECTION_VALUE;

        // When
        final int MP_RESTORE_AMOUNT = CLERIC.pray(DURATION_SECOND);

        boolean isInRange = MIN_MP_RESTORE_AMOUNT <= CLERIC.mp && CLERIC.mp <= MAX_MP_RESTORE_AMOUNT;

        // Then
        assertTrue(isInRange);
        assertEquals(BEFORE_MP + MP_RESTORE_AMOUNT, CLERIC.mp);
    }

    @Test
    @DisplayName("회복 마나가 maxMp를 넘길 때")
    void restoreMp11() {
        // Given
        final Cleric CLERIC = new Cleric();
        CLERIC.mp = 8;

        final int BEFORE_MP = CLERIC.mp;
        final int DURATION_SECOND = 3;

        // When
        final int MP_RESTORE_AMOUNT = CLERIC.pray(DURATION_SECOND);

        // Then
        assertEquals(CLERIC.MAX_MP, BEFORE_MP + MP_RESTORE_AMOUNT);
        assertEquals(CLERIC.MAX_MP, CLERIC.mp);
    }

    @Test
    @DisplayName("잘못된 기도 시간")
    void restoreMp2() {
        // Given
        final Cleric CLERIC = new Cleric();
        CLERIC.mp = 0;

        final int DURATION_SECOND = -1;
        final int INVALID_DURATION_ERR_CODE = -1;
        final int BEFORE_MP = CLERIC.mp;

        // When
        final int MP_RESTORE_AMOUNT = CLERIC.pray(DURATION_SECOND);

        // Then
        assertEquals(INVALID_DURATION_ERR_CODE, MP_RESTORE_AMOUNT);
        assertEquals(BEFORE_MP, CLERIC.mp);
    }

    @Test
    @DisplayName("최대 마나면 회복 불가")
    void restoreMp3() {
        // Given
        final Cleric CLERIC = new Cleric();
        CLERIC.mp = 10;

        final int durationSecond = 5;
        final int MAX_MP_CODE = 0;
        final int BEFORE_MP = CLERIC.mp;

        // When
        final int MP_RESTORE_AMOUNT = CLERIC.pray(durationSecond);


        // Then
        assertEquals(MAX_MP_CODE, MP_RESTORE_AMOUNT);
        assertEquals(BEFORE_MP, CLERIC.mp);
    }
}