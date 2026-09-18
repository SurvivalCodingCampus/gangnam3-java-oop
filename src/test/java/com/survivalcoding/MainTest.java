package com.survivalcoding;

import com.Cleric;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    // 1. 이름만 지정했을 때
    // HP와 MP가 최대치로 설정되는지 테스트
    @Test
    void testClericWithNameOnly() {
        Cleric cleric = new Cleric("아서스");

        assertEquals("아서스", cleric.name);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    // 2. 이름과 HP를 지정했을 때
    // MP가 MAX_MP로 설정되는지 테스트
    @Test
    void testClericWithNameAndHp() {
        Cleric cleric = new Cleric("세라핌", 30);

        assertEquals("세라핌", cleric.name);
        assertEquals(30, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    // 3. 이름, HP, MP를 모두 지정했을 때
    @Test
    void testClericWithNameHpMp() {
        Cleric cleric = new Cleric("우서", 40, 5);

        assertEquals("우서", cleric.name);
        assertEquals(40, cleric.hp);
        assertEquals(5, cleric.mp);
    }

    // 4. selfAid() 테스트
    // HP는 MAX_HP가 되고 MP는 5 감소해야 함
    @Test
    void testSelfAid() {
        Cleric cleric = new Cleric("아서스", 10, 10);

        cleric.selfAid();

        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(5, cleric.mp);
    }

    // 5. pray() 테스트
    // 기도 후 MP가 MAX_MP를 초과하지 않는지 테스트
    @Test
    void testPray() {
        Cleric cleric = new Cleric("아서스", 30, 5);

        int beforeMp = cleric.mp;
        int recovery = cleric.pray(3);

        // 회복량은 3~5
        assertTrue(recovery >= 3 && recovery <= 5);

        // MP는 기도 전보다 증가
        assertTrue(cleric.mp > beforeMp);

        // MP는 최대 10을 넘지 않음
        assertTrue(cleric.mp <= Cleric.MAX_MP);
    }

    // 6. MP가 MAX_MP를 초과하지 않는지 테스트
    @Test
    void testPrayDoesNotExceedMaxMp() {
        Cleric cleric = new Cleric("아서스", 30, 9);

        cleric.pray(10);

        assertEquals(Cleric.MAX_MP, cleric.mp);
    }
}
