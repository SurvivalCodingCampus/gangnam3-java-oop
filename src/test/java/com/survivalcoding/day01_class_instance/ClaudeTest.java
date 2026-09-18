package com.survivalcoding.day01_class_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClaudeTest {
    // ===== 생성자 =====
    @Test
    @DisplayName("이름, HP, MP를 지정하면 그 값으로 생성된다")
    void constructor_nameHpMp() {
        Cleric cleric = new Cleric("홍길동", 40, 5);

        assertEquals("홍길동", cleric.name);
        assertEquals(40, cleric.hp);
        assertEquals(5, cleric.mp);
    }

    @Test
    @DisplayName("이름과 HP만 지정하면 MP는 최대치(10)가 된다")
    void constructor_nameHp() {
        Cleric cleric = new Cleric("홍길동", 35);

        assertEquals("홍길동", cleric.name);
        assertEquals(35, cleric.hp);
        assertEquals(10, cleric.mp);
    }

    @Test
    @DisplayName("이름만 지정하면 HP와 MP가 최대치(50, 10)가 된다")
    void constructor_nameOnly() {
        Cleric cleric = new Cleric("홍길동");

        assertEquals("홍길동", cleric.name);
        assertEquals(50, cleric.hp);
        assertEquals(10, cleric.mp);
    }

    // ===== pray =====
    @RepeatedTest(20)
    @DisplayName("pray를 3초 하면 3~5가 회복된다")
    void pray_threeSeconds_recoversBetween3And5() {
        Cleric cleric = new Cleric("홍길동", 50, 0);

        int recoveredMp = cleric.pray(3);

        assertTrue(recoveredMp >= 3 && recoveredMp <= 5,
                "회복량은 3~5 사이여야 합니다. 실제 회복량: " + recoveredMp);
        assertEquals(recoveredMp, cleric.mp);
    }

    @Test
    @DisplayName("pray를 해도 MP는 최대치(10)를 넘지 않는다")
    void pray_doesNotExceedMaxMp() {
        Cleric cleric = new Cleric("홍길동", 50, 9);

        int recoveredMp = cleric.pray(3);

        assertEquals(1, recoveredMp);
        assertEquals(10, cleric.mp);
    }

    // ===== selfAid =====
    @Test
    @DisplayName("MP가 5일 때 selfAid를 하면 HP가 최대치가 되고 MP는 0이 된다")
    void selfAid_whenMpIs5_recoversHpToMax() {
        Cleric cleric = new Cleric("홍길동", 10, 5);

        cleric.selfAid();

        assertEquals(50, cleric.hp);
        assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("MP가 5보다 적으면 selfAid를 해도 아무것도 바뀌지 않는다")
    void selfAid_whenMpIsLessThan5_doesNothing() {
        Cleric cleric = new Cleric("홍길동", 10, 4);

        cleric.selfAid();

        assertEquals(10, cleric.hp);
        assertEquals(4, cleric.mp);
    }
}