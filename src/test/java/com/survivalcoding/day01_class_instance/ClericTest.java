package com.survivalcoding.day01_class_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClericTest {

    @Test
    @DisplayName("생성자 및 방어 검증: 오버로딩 및 비정상 입력값 클램핑 테스트")
    void testAllConstructorsAndValidation() {
        // Given & When
        Cleric defaultCleric = new Cleric();
        Cleric overCleric = new Cleric("오버맨", 999, 999);
        Cleric underCleric = new Cleric("언더맨", -50, -10);

        // Then
        assertAll("생성자 초기값 및 방어 로직 검증",
                () -> assertEquals("김경환", defaultCleric.getName()),
                () -> assertEquals(50, defaultCleric.getHp()),
                () -> assertEquals(10, defaultCleric.getMp()),

                () -> assertEquals(50, overCleric.getHp(), "HP는 MAX_HP(50)를 넘을 수 없습니다."),
                () -> assertEquals(10, overCleric.getMp(), "MP는 MAX_MP(10)를 넘을 수 없습니다."),

                () -> assertEquals(0, underCleric.getHp(), "HP는 0 미만일 수 없습니다."),
                () -> assertEquals(0, underCleric.getMp(), "MP는 0 미만일 수 없습니다.")
        );
    }

    @Test
    @DisplayName("selfAid 성공 케이스: MP가 충분할 때 정상 회복 및 소모 검증")
    void testSelfAidSuccess() {
        // Given
        Cleric cleric = new Cleric("테스터", 10, 8);

        // When
        cleric.selfAid();

        // Then
        assertEquals(50, cleric.getHp(), "셀프 에이드 후 HP는 최대치(50)여야 합니다.");
        assertEquals(3, cleric.getMp(), "MP는 5 소모되어 3이 되어야 합니다.");
    }

    @Test
    @DisplayName("selfAid 엣지케이스: HP가 이미 최대치(50)일 때 MP만 소모되는지 검증")
    void testSelfAidWhenHpIsAlreadyMax() {
        // Given
        Cleric cleric = new Cleric("만땅맨", 50, 6);

        // When
        cleric.selfAid();

        // Then
        assertEquals(50, cleric.getHp());
        assertEquals(1, cleric.getMp(), "HP가 깎이지 않았어도 MP는 5 소모되어야 합니다.");
    }

    @Test
    @DisplayName("selfAid 실패 케이스: MP가 부족할 때(MP < 5) 스킬 실패 검증")
    void testSelfAidFailDueToLackOfMp() {
        // Given
        Cleric cleric = new Cleric("마나거지", 20, 4);

        // When
        cleric.selfAid();

        // Then
        assertEquals(20, cleric.getHp(), "HP가 변하지 않아야 합니다.");
        assertEquals(4, cleric.getMp(), "MP가 소모되지 않고 유지되어야 합니다.");
    }

    @Test
    @DisplayName("pray 정상 케이스: 무작위 회복 범위(sec + 0~2) 검증")
    void testPrayNormal() {
        // Given
        Cleric cleric = new Cleric("기도자", 50, 2);
        int sec = 3;

        // When
        int healed = cleric.pray(sec);

        // Then
        assertTrue(healed >= 3 && healed <= 5, "회복량은 3~5 사이여야 합니다. 실제: " + healed);
        assertEquals(2 + healed, cleric.getMp());
    }

    @Test
    @DisplayName("pray 엣지케이스: 대형 기도 시간(100초) 입력 시 최대 MP(10) 초과 방지 검증")
    void testPrayMaxMpLimit() {
        // Given
        Cleric cleric = new Cleric("욕심쟁이", 50, 8);

        // When
        int healed = cleric.pray(100);

        // Then
        assertEquals(2, healed, "맥스치 10을 채우기 위해 실제 회복량은 정확히 2여야 합니다.");
        assertEquals(10, cleric.getMp(), "최종 MP는 MAX_MP(10)를 넘을 수 없습니다.");
    }

    @Test
    @DisplayName("pray 방어 테스트: 음수 시간(-sec) 입력 시 0 리턴 및 방어 검증")
    void testPrayNegativeSecondsDefense() {
        // Given
        Cleric cleric = new Cleric("시간수호자", 50, 5);

        // When
        int healed = cleric.pray(-5);

        // Then
        assertEquals(0, healed, "음수 시간 입력 시 회복량은 0이어야 합니다.");
        assertEquals(5, cleric.getMp(), "MP가 유지되어야 합니다.");
    }
}