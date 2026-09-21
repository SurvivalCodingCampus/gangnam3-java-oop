package com.survivalcoding.day04_calss_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WizardTest {
    @Test
    @DisplayName("Wizard: 이름 3글자 이상 정상 설정 (동등 분할)")
    void nameValid() {
        Wizard wizard = new Wizard();
        wizard.setName("간달프");
        assertEquals("간달프", wizard.getName());
    }
    
    @Test
    @DisplayName("Wizard: 이름 3글자 경계값 정상 설정 (경계값 분석)")
    void nameBoundaryValid() {
        Wizard wizard = new Wizard();
        wizard.setName("박마법"); // 3글자
        assertEquals("박마법", wizard.getName());
    }
    
    @Test
    @DisplayName("Wizard: 이름 2글자 이하 예외 발생 (경계값 분석)")
    void nameBoundaryInvalid() {
        Wizard wizard = new Wizard();
        assertThrows(IllegalArgumentException.class, () -> wizard.setName("마법")); // 2글자
    }
    
    @Test
    @DisplayName("Wizard: 이름 null 예외 발생")
    void nameNullInvalid() {
        Wizard wizard = new Wizard();
        assertThrows(IllegalArgumentException.class, () -> wizard.setName(null));
    }
    
    @Test
    @DisplayName("Wizard: MP 정상 범위 설정 (동등 분할)")
    void mpValid() {
        Wizard wizard = new Wizard();
        int tempValue = Wizard.MAX_MP / 2;
        wizard.setMp(tempValue); // 0 이상, MAX_MP 이하의 임의의 값
        assertEquals(tempValue, wizard.getMp());
    }
    
    @Test
    @DisplayName("Wizard: MP 하한 경계값 정상 설정 (경계값 분석)")
    void mpLowerBoundaryValid() {
        Wizard wizard = new Wizard();
        wizard.setMp(Wizard.MIN_MP); // 하한값
        assertEquals(Wizard.MIN_MP, wizard.getMp());
    }
    
    @Test
    @DisplayName("Wizard: MP 하한 미만(음수) 예외 발생 (경계값 분석)")
    void mpLowerBoundaryInvalid() {
        Wizard wizard = new Wizard();
        assertThrows(IllegalArgumentException.class, () -> wizard.setMp(-1));
    }
    
    @Test
    @DisplayName("Wizard: HP 정상 설정 (동등 분할)")
    void hpValid() {
        Wizard wizard = new Wizard();
        int tempValue = Wizard.MAX_HP / 2;
        wizard.setHp(tempValue); // 0 이상, MAX_HP 이하
        assertEquals(tempValue, wizard.getHp());
    }
    
    @Test
    @DisplayName("Wizard: HP 음수 설정 시 0으로 보정 (경계값 분석)")
    void hpNegativeCorrectedToZero() {
        Wizard wizard = new Wizard();
        wizard.setHp(-1); // 음수 설정
        assertEquals(0, wizard.getHp()); // 0으로 보정되었는지 확인
    }
    
    @Test
    @DisplayName("Wizard: HP 최대치 초과 시 예외 발생 (경계값 분석)")
    void hpUpperBoundaryInvalid() {
        Wizard wizard = new Wizard();
        assertThrows(IllegalArgumentException.class, () -> wizard.setHp(Wizard.MAX_HP + 1)); // MAX_HP(50) 초과
    }
    
    @Test
    @DisplayName("Wizard: Wand 정상 설정 (동등 분할)")
    void wandValid() {
        Wizard wizard = new Wizard();
        double tempValue = 10.0;
        Wand wand = new Wand("빛의지팡이", tempValue);
        wizard.setWand(wand);
        assertEquals(wand, wizard.getWand());
    }
    
    @Test
    @DisplayName("Wizard: Wand null 설정 시 예외 발생")
    void wandNullInvalid() {
        Wizard wizard = new Wizard();
        assertThrows(IllegalArgumentException.class, () -> wizard.setWand(null));
    }
}

/* 테스트 시나리오 요약
- name
    - 동등 분할 : 3글자 이상의 유효한 이름 ("불의지팡이") 설정 성공 확인
    - 경계값 분석 : 정확히 3글자 ("나무지") 설정 성공, 2글자 ("지팡") 이하 설정 실패 확인
    - 예외 케이스 : null 서정 시 실패 확인
- mp
    - 동등 분할 : 허용 범위 내의 중간값 설정 성공 확인
    - 경계값 분석 : 최소값 설정 성공 확인, 최소값 미만 설정 실패 확인
- hp
    - 동등 분할 : 허용 범위 내의 중간값 설정 성공 확인
    - 경계값 분석 : 음수 설정 시 0으로 보정되는지 확인, 최대값 초과 설정 실패 확인
- wand
    - 유효한 Wand 객체 설정 성공 확인
    - 예외 케이스 : null 설정 시 실패 확인
 */