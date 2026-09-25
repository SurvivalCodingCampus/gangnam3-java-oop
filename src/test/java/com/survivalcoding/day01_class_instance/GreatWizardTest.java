package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GreatWizard 클래스 테스트")
class GreatWizardTest {

    GreatWizard greatWizard;

    @BeforeEach
    void setUp() {
        greatWizard = new GreatWizard();
    }

    @Test
    @DisplayName("생성자 호출 시 mp는 기본값 150으로 초기화되어야 한다")
    void constructor_shouldInitializeMp_to150() {
        // given
        int expected = 150;

        // then
        assertEquals(expected, greatWizard.getMp());
    }

    @Test
    @DisplayName("heal을 하면 25만큼 hp를 회복하고 mp를 5 소모해야 한다")
    void heal_shouldRecoverHp_by25_andConsumeMp_by5() {
        // given
        Hero hero = new Hero("히어로");
        int expectedHeroHp = 125;
        int expectedMp = 145;

        // when
        greatWizard.heal(hero);

        // then
        assertEquals(expectedHeroHp, hero.getHp());
        assertEquals(expectedMp, greatWizard.getMp());
    }

    @Test
    @DisplayName("mp가 5 미만이면 heal을 해도 hp가 회복되지 않아야 한다")
    void heal_whenMpIsInsufficient_shouldNotChangeHp() {
        // given
        greatWizard.setMp(3);
        Hero hero = new Hero("히어로");
        int expectedHeroHp = 100;
        int expectedMp = 3;

        // when
        greatWizard.heal(hero);

        // then
        assertEquals(expectedHeroHp, hero.getHp());
        assertEquals(expectedMp, greatWizard.getMp());
    }

    @Test
    @DisplayName("superHeal을 하면 hero의 hp가 DEFAULT_HP로 고정되고 mp를 50 소모해야 한다")
    void superHeal_shouldResetHeroHp_toDefaultHp() {
        // given
        Hero hero = new Hero("히어로");
        hero.sit(50);
        int expectedHeroHp = Hero.DEFAULT_HP;
        int expectedMp = 100;

        // when
        greatWizard.superHeal(hero);

        // then
        assertEquals(expectedHeroHp, hero.getHp());
        assertEquals(expectedMp, greatWizard.getMp());
    }

    @Test
    @DisplayName("mp가 50 미만이면 superHeal을 해도 hp가 변하지 않아야 한다")
    void superHeal_whenMpIsInsufficient_shouldNotChangeHp() {
        // given
        greatWizard.setMp(10);
        Hero hero = new Hero("히어로");
        hero.takeDamage(30);
        int expectedHeroHp = 70;
        int expectedMp = 10;

        // when
        greatWizard.superHeal(hero);

        // then
        assertEquals(expectedHeroHp, hero.getHp());
        assertEquals(expectedMp, greatWizard.getMp());
    }
}
