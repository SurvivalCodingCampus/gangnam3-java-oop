package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Hero 클래스 테스트")
public class HeroTest {

    private static final String DIE_MESSAGE = "히어로은/는 죽었다";

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero("히어로");
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private int countDieMessage() {
        return output.toString().split(DIE_MESSAGE, -1).length - 1;
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

    @Test
    @DisplayName("데미지를 입으면 hp가 감소해야 한다")
    void takeDamage_shouldReduceHp() {
        // given
        int damage = 30;
        int expected = 70;

        // when
        hero.takeDamage(damage);

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("남은 hp보다 큰 피해를 받으면 hp가 음수가 되지 않고 0이 되어야 한다")
    void takeDamage_hpShouldNotBeNegative() {
        // given
        int damage = 150;
        int expected = 0;

        // when
        hero.takeDamage(damage);

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("setHp에 음수를 입력하면 0으로 보정되어야 한다")
    void setHp_shouldClampToZero_whenNegativeValueGiven() {
        // given
        int hp = -10;
        int expected = 0;

        // when
        hero.setHp(hp);

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("attack을 하면 반격으로 hp가 2 감소해야 한다")
    void attack_shouldReduceHp_byTwo() {
        // given
        Kinoko kinoko = new Kinoko("버섯", 10);
        int expected = 98;

        // when
        hero.attack(kinoko);

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("sit을 하면 입력한 초만큼 hp가 회복되어야 한다")
    void sit_shouldIncreaseHp_bySeconds() {
        // given
        int sec = 10;
        int expected = 110;

        // when
        hero.sit(sec);

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("slip을 하면 hp가 5 감소해야 한다")
    void slip_shouldDecreaseHp_byFive() {
        // given
        int expected = 95;

        // when
        hero.slip();

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("남은 hp보다 큰 slip 피해를 받으면 hp가 음수가 되지 않고 0이 되어야 한다")
    void slip_hpShouldNotBeNegative_whenHpIsLessThanFive() {
        // given
        hero.setHp(3);
        int expected = 0;

        // when
        hero.slip();

        // then
        assertEquals(expected, hero.getHp());
    }

    @Test
    @DisplayName("sleep을 하면 현재 hp와 상관없이 DEFAULT_HP로 회복되어야 한다")
    void sleep_shouldResetHp_toDefaultHp() {
        // given
        hero.setHp(20);

        // when
        hero.sleep();

        // then
        assertEquals(Hero.DEFAULT_HP, hero.getHp());
    }

    @Test
    @DisplayName("피해로 hp가 양수에서 0이 되면 사망 메시지가 한 번 출력되어야 한다")
    void takeDamage_shouldPrintDieMessageOnce_whenHpBecomesZero() {
        // given
        int damage = 150;
        int expectedHp = 0;
        int expectedCount = 1;

        // when
        hero.takeDamage(damage);

        // then
        assertEquals(expectedHp, hero.getHp());
        assertEquals(expectedCount, countDieMessage());
    }

    @Test
    @DisplayName("hp가 이미 0일 때 추가 피해를 받으면 사망 메시지가 다시 출력되지 않아야 한다")
    void takeDamage_shouldNotPrintDieMessageAgain_whenHpIsAlreadyZero() {
        // given
        hero.takeDamage(150);
        int damage = 10;
        int expectedHp = 0;
        int expectedCount = 1;

        // when
        hero.takeDamage(damage);

        // then
        assertEquals(expectedHp, hero.getHp());
        assertEquals(expectedCount, countDieMessage());
    }

    @Test
    @DisplayName("setHp로 hp를 0으로 설정하면 사망 메시지가 출력되지 않아야 한다")
    void setHp_shouldNotPrintDieMessage_whenHpIsSetToZero() {
        // given
        int hp = 0;
        int expectedHp = 0;
        int expectedCount = 0;

        // when
        hero.setHp(hp);

        // then
        assertEquals(expectedHp, hero.getHp());
        assertEquals(expectedCount, countDieMessage());
    }
}
