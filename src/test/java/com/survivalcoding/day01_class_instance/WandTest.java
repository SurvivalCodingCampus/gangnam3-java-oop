package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Wand 클래스 테스트")
public class WandTest {

    Wand wand;

    @BeforeEach
    void setUp() {
        wand = new Wand("지팡이", 10.0);
    }

    @Test
    @DisplayName("설정할 이름은 null이 아니어야 한다")
    void setName_shouldNotBeNull() {
        // given
        String name = null;
        String errorMessage = "name(이름)은 null이 아니어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wand.setName(name));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("설정할 이름은 최소 3글자 이상을 입력해야 한다")
    void setName_shouldBeLetters_atLeastThree() {
        // given
        String name = "나무";
        String errorMessage = "name(이름)은 3글자 이상이어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wand.setName(name));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("power는 0.5 이상이어야 한다")
    void setPower_shouldBePower_moreThanPointFive() {
        // given
        double power = 0.4;
        String errorMessage = "wand(지팡이)의 power(지력)는 0.5 이상이어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wand.setPower(power));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("power는 100을 초과하지 말아야 한다")
    void setPower_shouldBePower_lessThanHundred() {
        // given
        double power = 101;
        String errorMessage = "wand(지팡이)의 power(지력)는 100을 넘길 수 없습니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wand.setPower(power));

        // then
        assertEquals(errorMessage, error.getMessage());
    }
}
