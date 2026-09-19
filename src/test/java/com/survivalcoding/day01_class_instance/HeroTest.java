package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Hero 클래스 테스트")
public class HeroTest {

    Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero("히어로");
    }

    @Test
    @DisplayName("설정할 이름은 null이 아니어야 한다")
    void setName_shouldNotBeNull() {
        // given
        String name = null;
        String errorMessage = "name(이름)은 null이 아니어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> hero.setName(name));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("설정할 이름은 최소 2글자 이상을 입력해야 한다")
    void setName_shouldBeLetters_atLeastTwo() {
        // given
        String name = "히";
        String errorMessage = "name(이름)은 최소 2글자 이상을 입력해 주세요";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> hero.setName(name));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("설정할 이름은 8글자를 넘기지 않아야 한다")
    void setName_shouldBeLetters_lessThanEight() {
        // given
        String name = "피카츄라이츄파이리꼬북이";
        String errorMessage = "name(이름)은 8글자 이상을 넘길 수 없습니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> hero.setName(name));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("생성자 호출 시 이름이 null이면 IllegalArgumentException이 발생한다")
    void constructor_whenNameIsNull_throwIllegalArgumentException() {
        // given
        String name = null;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Hero(name));
    }

    @Test
    @DisplayName("생성자 호출 시 이름이 2글자 미만이면 IllegalArgumentException이 발생한다")
    void constructor_whenNameLengthIsUnderTwo_throwIllegalArgumentException() {
        // given
        String name = "히";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Hero(name));
    }

    @Test
    @DisplayName("생성자 호출 시 이름이 8글자 이상이면 IllegalArgumentException이 발생한다")
    void constructor_whenNameLengthIsAtLeastEight_throwIllegalArgumentException() {
        // given
        String name = "피카츄라이츄파이리꼬북이";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Hero(name));
    }
}
