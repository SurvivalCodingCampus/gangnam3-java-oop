package com.survivalcoding.day01_class_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PoisonSlime 클래스 테스트")
class PoisonSlimeTest {

    @Test
    @DisplayName("생성자 호출 시 poisonCount는 기본값 5로 초기화되어야 한다")
    void constructor_shouldInitializePoisonCount_toFive() {
        // given & when
        PoisonSlime poisonSlime = new PoisonSlime("독슬라임");
        int expected = 5;

        // then
        assertEquals(expected, poisonSlime.getPoisonCount());
    }

    @Test
    @DisplayName("attack을 하면 기본 데미지와 추가 독 데미지가 함께 적용되어야 한다")
    void attack_shouldApplyBaseDamageAndPoisonDamage() {
        // given
        PoisonSlime poisonSlime = new PoisonSlime("독슬라임");
        Hero hero = new Hero("히어로");
        int expectedHeroHp = 72;
        int expectedPoisonCount = 4;

        // when
        poisonSlime.attack(hero);

        // then
        assertEquals(expectedHeroHp, hero.getHp());
        assertEquals(expectedPoisonCount, poisonSlime.getPoisonCount());
    }

    @Test
    @DisplayName("poisonCount가 소진되면 추가 데미지 없이 기본 데미지만 적용되어야 한다")
    void attack_shouldApplyOnlyBaseDamage_whenPoisonCountIsExhausted() {
        // given
        PoisonSlime poisonSlime = new PoisonSlime("독슬라임");
        poisonSlime.setPoisonCount(0);
        Hero hero = new Hero("히어로");
        int expectedHeroHp = 90;
        int expectedPoisonCount = 0;

        // when
        poisonSlime.attack(hero);

        // then
        assertEquals(expectedHeroHp, hero.getHp());
        assertEquals(expectedPoisonCount, poisonSlime.getPoisonCount());
    }

    @Test
    @DisplayName("poisonCount에 음수를 설정하면 0으로 보정되어야 한다")
    void setPoisonCount_shouldClampToZero_whenNegativeValueGiven() {
        // given
        PoisonSlime poisonSlime = new PoisonSlime("독슬라임");
        int expected = 0;

        // when
        poisonSlime.setPoisonCount(-3);

        // then
        assertEquals(expected, poisonSlime.getPoisonCount());
    }

    @Test
    @DisplayName("기본 공격으로 hero의 hp가 0이 되면 독 공격을 하지 않고 poisonCount도 유지되어야 한다")
    void attack_shouldSkipPoisonAttack_whenHeroIsDefeatedByBaseDamage() {
        // given
        PoisonSlime poisonSlime = new PoisonSlime("독슬라임");
        Hero hero = new Hero("히어로");
        hero.setHp(10);
        int expectedHeroHp = 0;
        int expectedPoisonCount = 5;

        // when
        poisonSlime.attack(hero);

        // then
        assertEquals(expectedHeroHp, hero.getHp());
        assertEquals(expectedPoisonCount, poisonSlime.getPoisonCount());
    }
}
