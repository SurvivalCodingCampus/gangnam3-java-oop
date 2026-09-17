package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Cleric 클래스 테스트")
public class ClericTest {

    Cleric cleric;

    @BeforeEach
    void setUp() {
        cleric = new Cleric();
    }

    @Test
    @DisplayName("selfAid를 하면 mp가 5만큼 감소한다")
    void selfAid_reduceMp() {
        // given
        cleric.mp = 10;

        // when
        cleric.selfAid();

        // then
        assertEquals(5, cleric.mp);
    }

    @Test
    @DisplayName("selfAid를 하면 현재 hp가 최대 체력(50)이 된다")
    void selfAid_hpShouldBeMax() {
        // given
        cleric.hp = 0;

        // when
        cleric.selfAid();

        // then
        assertEquals(cleric.MAX_HP, cleric.hp);
    }

    @Test
    @DisplayName("회복한 mp는 초에 랜덤하게 0 ~ 2를 보정한 양이다")
    void pray_mpShouldBeAssignedRandomly() {
        // given
        cleric.mp = 5;
        int sec = 3;
        List<Integer> expected = List.of(8, 9, 10);

        // when
        cleric.pray(sec);

        // then
        assertTrue(expected.contains(cleric.mp));
    }

    @Test
    @DisplayName("회복 후 현재 mp는 최대 마나(10)를 초과하지 않아야 한다")
    void pray_mpShouldBeMax() {
        // given
        cleric.mp = 5;
        int sec = 4;

        // when
        cleric.pray(sec);

        // then
        assertTrue(cleric.mp <= cleric.MAX_MP);
    }
}
