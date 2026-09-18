package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClericTest {

    // Cleric 객체가 정상적으로 생성되는지 테스트
    @Test
    void clericCreateTest() {

        Cleric cleric = new Cleric("아서스", 40, 5);

        assertNotNull(cleric);
    }

    // 이름이 정상적으로 저장되는지 테스트
    @Test
    void clericNameTest() {

        Cleric cleric = new Cleric("아서스", 40, 5);

        assertEquals("아서스", cleric.name);
    }

    // HP가 정상적으로 저장되는지 테스트
    @Test
    void clericHpTest() {

        Cleric cleric = new Cleric("아서스", 40, 5);

        assertEquals(40, cleric.hp);
    }

    // MP가 정상적으로 저장되는지 테스트
    @Test
    void clericMpTest() {

        Cleric cleric = new Cleric("아서스", 40, 5);

        assertEquals(5, cleric.mp);
    }

    // selfAid()를 사용하면 HP가 10 증가하는지 테스트
    @Test
    void selfAidTest() {

        Cleric cleric = new Cleric("아서스", 40, 5);

        cleric.selfAid();

        assertEquals(50, cleric.hp);
    }

    // selfAid() 사용 후 MP가 5 감소하는지 테스트
    @Test
    void selfAidMpTest() {

        Cleric cleric = new Cleric("아서스", 40, 5);

        cleric.selfAid();

        assertEquals(0, cleric.mp);
    }

    // pray()를 사용하면 지정한 초만큼 MP가 증가하는지 테스트
    @Test
    void prayTest() {

        Cleric cleric = new Cleric("아서스", 40, 5);

        cleric.pray(3);

        assertEquals(8, cleric.mp);
    }
}