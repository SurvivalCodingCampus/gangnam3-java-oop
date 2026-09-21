package com.survivalcoding.day04_calss_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WizardWandTest {
    
    // ==========================================
    // Wand Test Scenarios
    // ==========================================
    
    @Test
    @DisplayName("Wand: 이름 3글자 이상 정상 설정 (동등 분할)")
    void nameValid() {
        Wand wand = new Wand();
        wand.setName("불의지팡이");
        assertEquals("불의지팡이", wand.getName());
    }
    
    @Test
    @DisplayName("Wand: 이름 3글자 경계값 정상 설정 (경계값 분석)")
    void nameBoundaryValid() {
        Wand wand = new Wand();
        wand.setName("나무지"); // 3글자 (MIN_NAME_LENGTH = 2 이므로 length() > 2 만족)
        assertEquals("나무지", wand.getName());
    }
    
    @Test
    @DisplayName("Wand: 이름 2글자 이하 예외 발생 (경계값 분석)")
    void nameBoundaryInvalid() {
        Wand wand = new Wand();
        assertThrows(IllegalArgumentException.class, () -> wand.setName("지팡")); // 2글자
        assertThrows(IllegalArgumentException.class, () -> wand.setName("봉")); // 1글자
    }
    
    @Test
    @DisplayName("Wand: 이름 null 예외 발생")
    void nameNullInvalid() {
        Wand wand = new Wand();
        assertThrows(IllegalArgumentException.class, () -> wand.setName(null));
    }
    
    @Test
    @DisplayName("Wand: 마력(Power) 정상 범위 설정 (동등 분할)")
    void powerValid() {
        Wand wand = new Wand();
        wand.setPower(50.0); // 0.5 ~ 100.0 사이의 임의의 값
        assertEquals(50.0, wand.getPower());
    }
    
    @Test
    @DisplayName("Wand: 마력(Power) 하한 경계값 정상 설정 (경계값 분석)")
    void powerLowerBoundaryValid() {
        Wand wand = new Wand();
        wand.setPower(Wand.MIN_POWER); // 하한값
        assertEquals(Wand.MIN_POWER, wand.getPower());
    }
    
    @Test
    @DisplayName("Wand: 마력(Power) 상한 경계값 정상 설정 (경계값 분석)")
    void powerUpperBoundaryValid() {
        Wand wand = new Wand();
        wand.setPower(Wand.MAX_POWER); // 상한값
        assertEquals(Wand.MAX_POWER, wand.getPower());
    }
    
    @Test
    @DisplayName("Wand: 마력(Power) 하한 미만 예외 발생 (경계값 분석)")
    void powerLowerBoundaryInvalid() {
        Wand wand = new Wand();
        assertThrows(IllegalArgumentException.class, () -> wand.setPower(0.49));
    }
    
    @Test
    @DisplayName("Wand: 마력(Power) 상한 초과 예외 발생 (경계값 분석)")
    void powerUpperBoundaryInvalid() {
        Wand wand = new Wand();
        assertThrows(IllegalArgumentException.class, () -> wand.setPower(100.01));
    }
}

/* 테스트 시나리오 요약

- name
    - 동등 분할 : 3글자 이상의 유효한 이름 ("불의지팡이") 설정 성공 확인
    - 경계값 분석 : 정확히 3글자 ("나무지") 설정 성공, 2글자 ("지팡") 이하 설정 실패 확인
    - 예외 케이스 : null 서정 시 실패 확인
- mp
    - 동등 분할 : 허용 범위 (0.5 ~ 100.0) 내의 중간값 (50.0) 설정 성공 확인
    - 경계값 분석 : 최소값 (0.5), 최대값 (100.0) 설정 성공 확인, 최소값 미만 (0.49), 최대값 초과 (100.01) 설정 실패 확인
 */