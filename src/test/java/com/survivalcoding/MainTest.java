package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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