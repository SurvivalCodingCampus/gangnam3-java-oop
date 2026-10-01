package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link Hero} · {@link Wand} · {@link Wizard} 필드 검증의 테스트.
 * <p>
 * 세 클래스는 모두 "private 필드 + getter/setter + setter 에서 값 검증" 구조를 공유한다.
 * 이 테스트는 그 검증 규칙이 각 클래스에서 제대로 지켜지는지 확인한다.
 * <ul>
 *     <li>{@code Hero#setName} : null 불가, 3글자 이상</li>
 *     <li>{@code Hero#setHp} : 음수이면 0 으로 보정</li>
 *     <li>{@code Wand#setName} : null 불가, 3글자 이상</li>
 *     <li>{@code Wand#setPower} : 0.5 이상 100.0 이하</li>
 *     <li>{@code Wizard#setMp} : 음수이면 예외</li>
 *     <li>{@code Wizard#setWand} : null 이면 예외</li>
 * </ul>
 * <p>
 * "조용히 고치는 방식"(HP 를 0 으로 보정)과 "에러로 알리는 방식"(MP 를 음수로 두지 않음)을
 * 구분해서 사용하는 이유를 여기서 비교해 볼 수 있다.
 */
public class MainTest {

    // ==============================
    // Hero 테스트
    // ==============================
    @Test
    void heroNameTest() {
        Hero hero = new Hero();

        hero.setName("준석이");

        assertEquals("준석이", hero.getName());
    }

    @Test
    void heroHpTest() {
        Hero hero = new Hero();

        hero.setHp(100);

        assertEquals(100, hero.getHp());
    }

    @Test
    void heroNegativeHpTest() {
        Hero hero = new Hero();

        hero.setHp(-50);

        // HP가 음수가 되면 0으로 변경되어야 한다.
        assertEquals(0, hero.getHp());
    }

    // ==============================
    // Wand 테스트
    // ==============================
    @Test
    void wandNameTest() {
        Wand wand = new Wand();

        wand.setName("마법지팡이");

        assertEquals("마법지팡이", wand.getName());
    }

    @Test
    void wandPowerTest() {
        Wand wand = new Wand();

        wand.setPower(50.0);

        assertEquals(50.0, wand.getPower());
    }

    @Test
    void wandNameNullTest() {
        Wand wand = new Wand();

        assertThrows(
                IllegalArgumentException.class,
                () -> wand.setName(null)
        );
    }

    @Test
    void wandNameTooShortTest() {
        Wand wand = new Wand();

        assertThrows(
                IllegalArgumentException.class,
                () -> wand.setName("AB")
        );
    }

    @Test
    void wandPowerTooLowTest() {
        Wand wand = new Wand();

        assertThrows(
                IllegalArgumentException.class,
                () -> wand.setPower(0.4)
        );
    }

    @Test
    void wandPowerTooHighTest() {
        Wand wand = new Wand();

        assertThrows(
                IllegalArgumentException.class,
                () -> wand.setPower(100.1)
        );
    }


    // ==============================
    // Wizard 테스트
    // ==============================

    @Test
    void wizardNameTest() {
        Wizard wizard = new Wizard();

        wizard.setName("마법사");

        assertEquals("마법사", wizard.getName());
    }

    @Test
    void wizardHpTest() {
        Wizard wizard = new Wizard();

        wizard.setHp(100);

        assertEquals(100, wizard.getHp());
    }

    @Test
    void wizardNegativeHpTest() {
        Wizard wizard = new Wizard();

        wizard.setHp(-10);

        // HP가 음수가 되면 0이 되어야 한다.
        assertEquals(0, wizard.getHp());
    }

    @Test
    void wizardMpTest() {
        Wizard wizard = new Wizard();

        wizard.setMp(50);

        assertEquals(50, wizard.getMp());
    }

    @Test
    void wizardNegativeMpTest() {
        Wizard wizard = new Wizard();

        assertThrows(
                IllegalArgumentException.class,
                () -> wizard.setMp(-1)
        );
    }

    @Test
    void wizardWandTest() {
        Wizard wizard = new Wizard();
        Wand wand = new Wand();

        wand.setName("마법지팡이");
        wand.setPower(50.0);

        wizard.setWand(wand);

        assertEquals(wand, wizard.getWand());
    }

    @Test
    void wizardWandNullTest() {
        Wizard wizard = new Wizard();

        assertThrows(
                IllegalArgumentException.class,
                () -> wizard.setWand(null)
        );
    }
}