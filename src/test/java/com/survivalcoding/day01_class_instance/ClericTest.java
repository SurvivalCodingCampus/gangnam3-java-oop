package com.survivalcoding.day01_class_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {
    @Test
    @DisplayName("이름, HP, MP를 전달하면 전달한 값으로 초기화한다")
    void constructor_withNameHpMp() {
        // Given & When
        Cleric cleric = new Cleric("아서스", 40, 5);

        // Then
        assertEquals("아서스", cleric.getName());
        assertEquals(40, cleric.getHp());
        assertEquals(5, cleric.getMp());
    }

    @Test
    @DisplayName("이름과 HP만 전달하면 MP는 최대 MP가 된다")
    void constructor_withNameAndHp() {
        // Given & When
        Cleric cleric = new Cleric("아서스", 35);

        // Then
        assertEquals("아서스", cleric.getName());
        assertEquals(35, cleric.getHp());
        assertEquals(10, cleric.getMp());
    }

    @Test
    @DisplayName("이름만 전달하면 HP와 MP는 최대값이 된다")
    void constructor_withNameOnly() {
        // Given & When
        Cleric cleric = new Cleric("아서스");

        // Then
        assertEquals(50, cleric.getHp());
        assertEquals(10, cleric.getMp());
    }

    @Test
    @DisplayName("음수 MP를 설정하면 예외가 발생한다")
    void setMp_withNegativeValue() {
        // Given
        Cleric cleric = new Cleric("아서스");

        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> cleric.setMp(-1)
        );
    }
}