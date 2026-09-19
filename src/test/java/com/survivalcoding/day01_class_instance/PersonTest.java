package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Person 클래스 테스트")
public class PersonTest {

    @Test
    @DisplayName("특정 날짜가 주어지면 해당 연도 기준 나이를 계산한다")
    void getAge_withDate_returnAge() {
        // given
        Person person = new Person("김준기", 1996);
        LocalDate date = LocalDate.of(2026, 1, 1);
        int expectedAge = 30;

        // when
        int age = person.getAge(date);

        // then
        assertEquals(expectedAge, age);
    }
}
