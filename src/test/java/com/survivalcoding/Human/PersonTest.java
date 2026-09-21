package com.survivalcoding.Human;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    
    @Test
    @DisplayName("Person: 정상적인 이름과 태어난 해로 객체 생성 (동등 분할)")
    void createPersonValid() {
        int birthYear = 1990;
        Person person = new Person("홍길동", birthYear);
        
        assertEquals("홍길동", person.getName());
        assertEquals(birthYear, person.getBirthYear());
    }
    
    @Test
    @DisplayName("Person: 이름이 null일 때 예외 발생")
    void createPersonNameNull() {
        assertThrows(IllegalArgumentException.class, () -> new Person(null, 1990));
    }
    
    @Test
    @DisplayName("Person: 이름 길이가 최소 길이(1) 이하일 때 예외 발생 (경계값 분석)")
    void createPersonNameLengthInvalid() {
        // 기존 Validator.validateMinLength 로직상 length <= minLength 일 때 예외가 발생하므로,
        // MIN_NAME_LENGTH가 1일 때 1글자 이름은 예외가 발생해야 합니다.
        assertThrows(IllegalArgumentException.class, () -> new Person("가", 1990)); // 길이 1
        assertThrows(IllegalArgumentException.class, () -> new Person("", 1990));  // 길이 0
    }
    
    @Test
    @DisplayName("Person: 이름 길이가 최소 길이(1) 초과일 때 정상 생성 (경계값 분석)")
    void createPersonNameLengthValid() {
        // 2글자 이상부터 정상 생성
        assertDoesNotThrow(() -> new Person("가나", 1990));
    }
    
    @Test
    @DisplayName("Person: 태어난 해가 올해일 때 정상 생성 및 나이 0살 확인 (경계값 분석)")
    void createPersonBirthYearCurrentYear() {
        // 테스트 실행 시점의 올해 연도를 동적으로 가져옴
        int currentYear = LocalDate.now().getYear();
        Person person = new Person("신생아", currentYear);
        
        assertEquals(currentYear, person.getBirthYear());
        assertEquals(0, person.getAge());
    }
    
    @Test
    @DisplayName("Person: 태어난 해가 미래(올해 초과)일 때 예외 발생 (경계값 분석)")
    void createPersonBirthYearFuture() {
        int futureYear = LocalDate.now().getYear() + 1; // 내년
        
        assertThrows(IllegalArgumentException.class, () -> new Person("미래인", futureYear));
    }
    
    @Test
    @DisplayName("Person: getAge() 나이 계산 로직 검증")
    void calculateAgeValid() {
        int birthYear = 2000;
        Person person = new Person("김밀레", birthYear);
        
        // 현재 연도 - 2000년 의 값이 실제 getAge()와 일치하는지 확인
        int expectedAge = LocalDate.now().getYear() - birthYear;
        assertEquals(expectedAge, person.getAge());
    }
}