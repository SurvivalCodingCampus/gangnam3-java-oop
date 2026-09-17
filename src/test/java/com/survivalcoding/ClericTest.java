package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {

    @Test
    @DisplayName("selfAid는 mp를 5 소모하고 hp를 최대값으로 회복합니다.")
    void selfAidTest() {
        //given
        Cleric cleric = new Cleric();

        //when
        cleric.selfAid();

        //then
        assertEquals(50, cleric.hp);
    }

    @RepeatedTest(50)
    @DisplayName("0~8초 동안 pray하면 mp는 0 이상, 2*초 이하(최대 10)입니다.")
    void prayZeroToEightSeconds() {
        for (int sec = 0; sec <= 8; sec++) {
            //given
            Cleric cleric = new Cleric();
            cleric.mp = 0;
            int expectedMax = Math.min(sec * 2, cleric.max_mp);

            //when
            int result = cleric.pray(sec);

            //then
            assertTrue(cleric.mp >= 0 && cleric.mp <= expectedMax);
            assertEquals(cleric.mp, result);
        }
    }

    @RepeatedTest(100)
    @DisplayName("pray는 mp를 최대값(10)을 넘기지 않습니다.")
    void prayDoesNotExceedMaxMp() {
        //given
        Cleric cleric = new Cleric();
        cleric.mp = 0;

        //when
        cleric.pray(100);

        //then
        assertTrue(cleric.mp <= 10);
    }
}
