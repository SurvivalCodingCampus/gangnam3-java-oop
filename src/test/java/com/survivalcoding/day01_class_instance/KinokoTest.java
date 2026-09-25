package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Kinoko 클래스 테스트")
class KinokoTest {

    private static final String DIE_MESSAGE = "버섯는 죽었다";

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    Kinoko kinoko;

    @BeforeEach
    void setUp() {
        kinoko = new Kinoko("버섯", 50);
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
    @DisplayName("데미지를 입으면 hp가 감소해야 한다")
    void takeDamage_shouldReduceHp() {
        // given
        int damage = 10;
        int expected = 40;

        // when
        kinoko.takeDamage(damage);

        // then
        assertEquals(expected, kinoko.getHp());
    }

    @Test
    @DisplayName("남은 hp보다 큰 피해를 받으면 hp가 음수가 되지 않고 0이 되어야 한다")
    void takeDamage_hpShouldNotBeNegative() {
        // given
        int damage = 100;
        int expected = 0;

        // when
        kinoko.takeDamage(damage);

        // then
        assertEquals(expected, kinoko.getHp());
    }

    @Test
    @DisplayName("setHp에 음수를 입력하면 0으로 보정되어야 한다")
    void setHp_shouldClampToZero_whenNegativeValueGiven() {
        // given
        int hp = -5;
        int expected = 0;

        // when
        kinoko.setHp(hp);

        // then
        assertEquals(expected, kinoko.getHp());
    }

    @Test
    @DisplayName("생성자에 음수 hp를 입력하면 0으로 보정되어야 한다")
    void constructor_shouldClampHpToZero_whenNegativeValueGiven() {
        // given
        int hp = -5;
        int expected = 0;

        // when
        Kinoko negative = new Kinoko("버섯", hp);

        // then
        assertEquals(expected, negative.getHp());
    }

    @Test
    @DisplayName("피해로 hp가 양수에서 0이 되면 사망 메시지가 한 번 출력되어야 한다")
    void takeDamage_shouldPrintDieMessageOnce_whenHpBecomesZero() {
        // given
        int damage = 100;
        int expectedHp = 0;
        int expectedCount = 1;

        // when
        kinoko.takeDamage(damage);

        // then
        assertEquals(expectedHp, kinoko.getHp());
        assertEquals(expectedCount, countDieMessage());
    }

    @Test
    @DisplayName("hp가 이미 0일 때 추가 피해를 받으면 사망 메시지가 다시 출력되지 않아야 한다")
    void takeDamage_shouldNotPrintDieMessageAgain_whenHpIsAlreadyZero() {
        // given
        kinoko.takeDamage(100);
        int damage = 10;
        int expectedHp = 0;
        int expectedCount = 1;

        // when
        kinoko.takeDamage(damage);

        // then
        assertEquals(expectedHp, kinoko.getHp());
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
        kinoko.setHp(hp);

        // then
        assertEquals(expectedHp, kinoko.getHp());
        assertEquals(expectedCount, countDieMessage());
    }
}
