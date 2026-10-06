package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Cleric} 클래스의 테스트.
 * <p>
 * 검증 대상: 생성자 오버로딩 3종(전체 지정 / HP 까지 / 이름만)이
 * 각자 올바른 초기값을 채우는지, 그리고 {@code selfAid} 와 {@code pray} 의
 * MP 소모·회복 및 최대치 제한 동작.
 * <p>
 * 각 테스트는 Given(준비) → When(실행) → Then(검증) 순서로 주석을 달아
 * 어떤 상황에서 무엇을 확인하는지 드러낸다.
 */
class ClericTest {

    @Test
    @DisplayName("최대 HP는 50, 최대 MP는 10 이다")
    void maxValueTest() {
        assertEquals(50, Cleric.MAX_HP);
        assertEquals(10, Cleric.MAX_MP);
    }

    @Test
    @DisplayName("이름, HP, MP를 모두 지정하는 생성자로 모든 값이 저장된다")
    void clericWithNameHpMpTest() {

        // Given
        // When
        Cleric cleric = new Cleric("홍길동", 30, 4);

        // Then
        assertEquals("홍길동", cleric.name);
        assertEquals(30, cleric.hp);
        assertEquals(4, cleric.mp);
    }

    @Test
    @DisplayName("이름과 HP만 지정하면 MP는 최대치로 초기화된다")
    void clericWithNameHpTest() {

        // Given
        // When
        Cleric cleric = new Cleric("홍길동", 30);

        // Then
        assertEquals("홍길동", cleric.name);
        assertEquals(30, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("이름만 지정하면 HP와 MP는 각각 최대치로 초기화된다")
    void clericWithNameTest() {

        // Given
        // When
        Cleric cleric = new Cleric("홍길동");

        // Then
        assertEquals("홍길동", cleric.name);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("selfAid 는 MP를 5 소모하고 HP를 최대치로 회복한다")
    void selfAidTest() {

        // Given
        Cleric cleric = new Cleric("테스트", 20, 20);

        // When
        cleric.selfAid();

        // Then
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(15, cleric.mp);
    }

    @Test
    @DisplayName("pray 는 3초 기도하면 3 이상 5 이하의 MP를 회복한다")
    void prayTest() {

        // Given
        Cleric cleric = new Cleric("테스트", Cleric.MAX_HP, 0);

        // When
        int gained = cleric.pray(3);

        // Then
        assertTrue(gained >= 3 && gained <= 5);
        assertEquals(gained, cleric.mp);
    }

    @Test
    @DisplayName("pray 로 회복한 MP는 최대치를 넘지 않는다")
    void prayDoesNotExceedMaxMpTest() {

        // Given
        Cleric cleric = new Cleric("테스트", Cleric.MAX_HP, 0);

        // When
        int gained = cleric.pray(10);

        // Then
        assertEquals(Cleric.MAX_MP, cleric.mp);
        assertEquals(Cleric.MAX_MP, gained);
    }
}
