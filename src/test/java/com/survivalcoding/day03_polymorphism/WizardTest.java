package com.survivalcoding.day03_polymorphism;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.survivalcoding.day01_class_instance.Slime;

@DisplayName("Wizard 클래스 테스트")
public class WizardTest {

    @Test
    @DisplayName("Wizard가 일반 공격하면 슬라임의 hp가 5 감소해야 한다")
    void attack_shouldReduceSlimeHp() {
        // given
        Wizard wizard = new Wizard("마법사", 50, new Wand("지팡이", 10));
        Slime slime = new Slime("슬라임", 50);

        // when
        wizard.attack(slime);

        // then
        assertEquals(45, slime.getHp());
    }
}
