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
        cleric = new Cleric("클레릭");
    }

    @Test
    @DisplayName("selfAid를 하면 mp가 5만큼 감소한다")
    void selfAid_reduceMp() {
        // given
        cleric.mp = 10;
        int expectedMp = 5;

        // when
        cleric.selfAid();

        // then
        assertEquals(expectedMp, cleric.mp);
    }

    @Test
    @DisplayName("selfAid를 하면 현재 hp가 최대 체력(50)이 된다")
    void selfAid_hpShouldBeMax() {
        // given
        cleric.hp = 0;

        // when
        cleric.selfAid();

        // then
        assertEquals(Cleric.MAX_HP, cleric.hp);
    }

    @Test
    @DisplayName("mp가 0이면 selfAid가 실행되지 않고 hp, mp가 유지된다")
    void selfAid_mpZero_doesNothing() {
        // given
        int beforeHp = 20;
        int beforeMp = 0;
        cleric.hp = beforeHp;
        cleric.mp = beforeMp;

        // when
        cleric.selfAid();

        // then
        assertEquals(beforeHp, cleric.hp);
        assertEquals(beforeMp, cleric.mp);
    }

    @Test
    @DisplayName("mp가 음수이면 selfAid가 실행되지 않고 hp, mp가 유지된다")
    void selfAid_mpNegative_doesNothing() {
        // given
        int beforeHp = 20;
        int beforeMp = -1;
        cleric.hp = beforeHp;
        cleric.mp = beforeMp;

        // when
        cleric.selfAid();

        // then
        assertEquals(beforeHp, cleric.hp);
        assertEquals(beforeMp, cleric.mp);
    }

    @Test
    @DisplayName("mp가 COST_MP보다 1 작으면 selfAid가 실행되지 않고 hp, mp가 유지된다")
    void selfAid_mpBelowCost_doesNothing() {
        // given
        int beforeHp = 20;
        int beforeMp = Cleric.COST_MP - 1;
        cleric.hp = beforeHp;
        cleric.mp = beforeMp;

        // when
        cleric.selfAid();

        // then
        assertEquals(beforeHp, cleric.hp);
        assertEquals(beforeMp, cleric.mp);
    }

    @Test
    @DisplayName("mp가 COST_MP와 같으면 selfAid가 실행되어 mp가 0이 되고 hp가 최대 체력이 된다")
    void selfAid_mpEqualsCost_executes() {
        // given
        cleric.hp = 20;
        cleric.mp = Cleric.COST_MP;
        int expectedMp = 0;

        // when
        cleric.selfAid();

        // then
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(expectedMp, cleric.mp);
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
        assertTrue(cleric.mp <= Cleric.MAX_MP);
    }

    @Test
    @DisplayName("mp가 이미 MAX_MP이면 pray를 해도 MAX_MP를 유지한다")
    void pray_mpAlreadyMax_staysAtMax() {
        // given
        cleric.mp = Cleric.MAX_MP;

        // when
        cleric.pray(0);

        // then
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("mp와 heal의 합이 MAX_MP를 넘지 않으면 heal만큼 그대로 더해진다")
    void pray_amountNotExceedMax_addsHeal() {
        // given
        cleric.mp = 6;
        int sec = 2; // heal: 2~4, amount: 8~10 (MAX_MP를 넘지 않는 구간)
        int beforeMp = cleric.mp;

        // when
        int heal = cleric.pray(sec);

        // then
        assertEquals(beforeMp + heal, cleric.mp);
    }

    @Test
    @DisplayName("mp와 heal의 합이 MAX_MP를 넘으면 mp는 MAX_MP로 고정된다")
    void pray_amountExceedsMax_capsAtMax() {
        // given
        cleric.mp = 9;
        int sec = 2; // heal: 2~4, amount: 11~13 (항상 MAX_MP 초과)

        // when
        cleric.pray(sec);

        // then
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }
}
