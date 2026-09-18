package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private static final int CURRENT_YEAR = Year.now().getValue();

    @Nested
    @DisplayName("생성 및 조회")
    class Creation {

        @Test
        @DisplayName("이름이 정상적으로 저장된다")
        void name_is_stored() {
            Person p = new Person(2000, "김철수");
            assertEquals("김철수", p.getName());
        }

        @Test
        @DisplayName("출생연도가 정상적으로 저장된다")
        void birthYear_is_stored() {
            Person p = new Person(2000, "김철수");
            assertEquals(2000, p.getBirthYear());
        }

        @Test
        @DisplayName("서로 다른 객체는 독립적인 값을 가진다")
        void objects_are_independent() {
            Person a = new Person(2000, "김철수");
            Person b = new Person(1990, "이영희");

            assertAll(
                    () -> assertEquals("김철수", a.getName()),
                    () -> assertEquals("이영희", b.getName()),
                    () -> assertEquals(2000, a.getBirthYear()),
                    () -> assertEquals(1990, b.getBirthYear())
            );
        }
    }

    @Nested
    @DisplayName("나이 계산")
    class AgeCalculation {

        @Test
        @DisplayName("현재 연도에서 출생 연도를 뺀 값이다")
        void age_is_year_difference() {
            Person p = new Person(2000, "김철수");
            assertEquals(CURRENT_YEAR - 2000, p.getAge());
        }

        @Test
        @DisplayName("올해 태어난 사람은 0살이다")
        void newborn_is_zero() {
            Person p = new Person(CURRENT_YEAR, "아기");
            assertEquals(0, p.getAge());
        }

        @Test
        @DisplayName("나이는 음수가 될 수 없다")
        void age_is_never_negative() {
            Person p = new Person(CURRENT_YEAR, "아기");
            assertTrue(p.getAge() >= 0);
        }
    }

    @Nested
    @DisplayName("생성자 검증")
    class Validation {

        @Test
        @DisplayName("이름이 null이면 예외가 발생한다")
        void null_name_rejected() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Person(2000, null));
        }

        @Test
        @DisplayName("이름이 공백이면 예외가 발생한다")
        void blank_name_rejected() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Person(2000, "   "));
        }

        @Test
        @DisplayName("미래 출생연도는 예외가 발생한다")
        void future_birthYear_rejected() {
            IllegalArgumentException e = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Person(CURRENT_YEAR + 1, "미래인"));

            assertTrue(e.getMessage().contains("미래"));
        }

        @Test
        @DisplayName("올해 출생은 허용된다 (경계값)")
        void current_year_allowed() {
            assertDoesNotThrow(() -> new Person(CURRENT_YEAR, "아기"));
        }
    }
}