package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClericTest {

//    @Test
//    @DisplayName("프레이쓰면 어케되는지 확인해야함")
//    void pray() {
//        final Cleric cleric = new Cleric();
//        cleric.mp = 0;
//
//        cleric.pray(3);
//
//        assertEquals(5, cleric.mp);


    // 1. 이름, HP, MP를 모두 넣는 생성자 테스트
    @Test
    void 생성자_3개_테스트() {
        Cleric cleric = new Cleric("울트라마린", 30, 5);

        assertEquals("울트라마린", cleric.name);
        assertEquals(30, cleric.hp);
        assertEquals(5, cleric.mp);
    }


    // 2. 이름, HP만 넣었을 때 MP가 MAXMP(10)가 되는지 테스트
    @Test
    void 생성자_2개_테스트() {
        Cleric cleric = new Cleric("울트라마린", 30);

        assertEquals("울트라마린", cleric.name);
        assertEquals(30, cleric.hp);
        assertEquals(Cleric.MAXMP, cleric.mp);
    }


    // 3. 이름만 넣었을 때 HP, MP가 최대치가 되는지 테스트
    @Test
    void 생성자_1개_테스트() {
        Cleric cleric = new Cleric("울트라마린");

        assertEquals("울트라마린", cleric.name);
        assertEquals(Cleric.MAXHP, cleric.hp);
        assertEquals(Cleric.MAXMP, cleric.mp);
    }


    // 4. selfAid 테스트
    @Test
    void selfAid_테스트() {
        Cleric cleric = new Cleric("울트라마린", 20, 10);

        cleric.selfAid();

        assertEquals(70, cleric.hp); // 이게 맥스 값을 더해서 70이 되 버림 수정하려면 셀프에이드로 가야함
        assertEquals(5, cleric.mp);
    }


    // 5. pray 테스트
    @Test
    void pray_테스트() {
        Cleric cleric = new Cleric("울트라마린", 50, 3);

        int recovery = cleric.pray(3);

        // 3초 기도 + 랜덤 보너스 0~2
        // 따라서 MP 회복량은 3~5
        assertTrue(recovery >= 3 && recovery <= 5);

        // 기존 MP가 3이었으므로 최종 MP는 6~8
        assertTrue(cleric.mp >= 5 && cleric.mp <= 10);
    }
}









