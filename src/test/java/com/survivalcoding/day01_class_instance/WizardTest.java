package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Wizard 클래스 테스트")
class WizardTest {

    Wizard wizard;

    @BeforeEach
    void setUp() {
        Wand wand = new Wand("나무지팡이", 10.0);
        wizard = new Wizard("위저드", 50, 10, wand);
    }

    @Test
    @DisplayName("heal을 하면 `power * point`만큼 hp를 회복해야 한다")
    void heal_shouldBeHp_powerTimesTen() {
        // given
        Hero hero = new Hero("히어로", 10);
        double power = wizard.getWand().getPower();
        int point = 10;
        int expected = (int) (power * point) + hero.getHp();

        // when
        wizard.heal(hero);

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("설정할 이름은 null이 아니어야 한다")
    void setName_shouldNotBeNull() {
        // given
        String name = null;
        String errorMessage = "name(이름)은 null이 아니어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wizard.setName(name));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("설정할 이름은 최소 3글자 이상을 입력해야 한다")
    void setName_shouldBeLetters_atLeastThree() {
        // given
        String name = "위저";
        String errorMessage = "name(이름)은 3글자 이상이어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wizard.setName(name));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("hp는 0 이상이어야 한다")
    void setHp_shouldBeHp_moreThanZero() {
        // given
        int hp = -1;
        String errorMessage = "hp(체력)는 0 이상이어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wizard.setHp(hp));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("hp가 0이면 정상적으로 실행되어야 한다")
    void setHp_shouldBeExecutedSuccessfully() {
        // given
        int hp = 0;

        // when
        wizard.setHp(hp);

        // then
        assertEquals(hp, wizard.getHp());
    }

    @Test
    @DisplayName("mp는 0 이상이어야 한다")
    void setMp_shouldBeMp_moreThanZero() {
        // given
        int mp = -1;
        String errorMessage = "mp(마력)는 0 이상이어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wizard.setMp(mp));

        // then
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    @DisplayName("mp가 0이면 정상적으로 실행되어야 한다")
    void setMp_shouldBeExecutedSuccessfully() {
        // given
        int mp = 0;

        // when
        wizard.setMp(mp);

        // then
        assertEquals(mp, wizard.getMp());
    }

    @Test
    @DisplayName("설정할 지팡이는 null이 아니어야 한다")
    void setWand_shouldNotBeNull() {
        // given
        Wand wand = null;
        String errorMessage = "wand(지팡이)는 null이 아니어야 합니다";

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> wizard.setWand(wand));

        // then
        assertEquals(errorMessage, error.getMessage());
    }
}
