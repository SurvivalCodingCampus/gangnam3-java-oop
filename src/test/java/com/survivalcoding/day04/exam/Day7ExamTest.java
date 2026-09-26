package com.survivalcoding.day04.exam;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Day7ExamTest {

    @Nested
    class SlimeTest {

        @ParameterizedTest
        @NullAndEmptySource
        void 널_공란_불가(String name) {
            String errorMsg = "이름에 널, 공란 불가";

            IllegalArgumentException error = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Slime.Builder()
                            .suffix(name)
                            .build()
            );

            assertEquals(errorMsg, error.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"A", "B"})
        void 이름이_1자_이상이면_정상_초기화(String name) {
            Slime slime = new Slime.Builder()
                    .suffix(name)
                    .build();

            assertEquals(name, slime.getSuffix());
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 2 })
        void _1이상_데미지가_들어오면_체력차감(int damage) {
            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();

            int beforeHp = slime.getHp();

            slime.takeDamage(damage);

            assertThat(slime.getHp()).isEqualTo(beforeHp - damage);
        }

        @ParameterizedTest
        @ValueSource(ints = { Slime.MAX_HP, 10000 })
        void 체력이_0이하면_죽음(int deadDamage) {
            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();

            slime.takeDamage(deadDamage);

            assertThat(slime.isDead()).isEqualTo(true);
        }

        @ParameterizedTest
        @NullSource
        void 어택시_히어로에_널_불가(Hero hero) {
            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();

            String errorMsg = "히어로 널";

            IllegalArgumentException error = assertThrows(
                    IllegalArgumentException.class,
                    () -> slime.attack(hero)
            );
            assertEquals(errorMsg, error.getMessage());
        }

        @Test
        void 어택데미지는_1이상이면_정상() {
            Hero hero = new Hero.Builder()
                    .build();
            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();

            int heroBeforeHp = hero.getHp();

            slime.attack(hero);

            assertEquals(Math.max(0, heroBeforeHp - slime.getPower()), hero.getHp());
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, 0 })
        void 받는공격데미지가_0미만이면_예외(int damage) {
            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();
            String errorMsg = "1보다 작은값은 불가";
            IllegalArgumentException error = assertThrows(
                    IllegalArgumentException.class,
                    () -> slime.takeDamage(damage)
            );
            assertEquals(errorMsg, error.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 5, 1000 })
        void 받는공격데미지가_1이상이면_정상(int damage) {
            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();
            int beforeHp = slime.getHp();

            slime.takeDamage(damage);

            assertEquals(Math.max(0, beforeHp - damage), slime.getHp());
        }
    }

    @Nested
    class PoisonSlimeTest {

        @ParameterizedTest
        @ValueSource(ints = { 0, 1 })
        void 독카운트는_0이상이면_정상(int count) {
            PoisonSlime poisonSlime = new PoisonSlime.Builder()
                    .suffix("A")
                    .poisonCount(count)
                    .build();

            assertThat(poisonSlime.getPoisonCount()).isEqualTo(count);
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, -5 })
        void 독카운트는_0미만이면_예외(int invalidCount) {
            assertThatThrownBy(() -> new PoisonSlime.Builder()
                    .suffix("A")
                    .poisonCount(invalidCount)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("0보다 작습니다");
        }

        @Test
        void 독카운트가_남으면_독공격하고_카운트를_1감소한다() {
            Hero hero = new Hero.Builder()
                    .build();
            PoisonSlime poisonSlime = new PoisonSlime.Builder()
                    .suffix("A")
                    .build();

            int beforeHp = hero.getHp();
            int totalDamage = (beforeHp - poisonSlime.getPower()) / poisonSlime.getPoisonCount() + poisonSlime.getPower();
            int expectedCount = poisonSlime.getPoisonCount() - 1;

            poisonSlime.attack(hero);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(hero.getHp()).isEqualTo(beforeHp - totalDamage);
                softly.assertThat(poisonSlime.getPoisonCount()).isEqualTo(expectedCount);
            });
        }

        @Test
        void 독카운트가_0이하이면_기본공격만() {
            PoisonSlime poisonSlime = new PoisonSlime.Builder()
                    .suffix("A")
                    .poisonCount(0) // 독 카운트 소진 상태로 설정
                    .build();
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(100)
                    .build();

            poisonSlime.attack(hero);

            assertThat(hero.getHp()).isEqualTo(90);
        }

        @Test
        void 플레이어체력이_적더라도_최소1의_독데미지는_들어간다() {
            PoisonSlime poisonSlime = new PoisonSlime.Builder()
                    .suffix("A")
                    .build();

            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(14)
                    .build();

            poisonSlime.attack(hero);
        }
    }

    @Nested
    class HeroTest {

        @ParameterizedTest
        @ValueSource(ints = { 1, 3, 1000 })
        void _1이상_데미지가_들어오면_HP차감(int damage) {
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .build();

            int beforeHp = hero.getHp();

            hero.takeDamage(damage);

            assertThat(hero.getHp()).isEqualTo(Math.max(0, beforeHp - damage));
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, -1 })
        void _0이하_데미지가_들어오면_예외(int damage) {
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .build();

            assertThatThrownBy(() -> hero.takeDamage(damage))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("1보다 작은값은 불가");
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 10, 100 })
        void _1이상_힐이_들어오면_회복한다(int amount) {
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(50)
                    .build();

            int beforeHp = hero.getHp();

            hero.takeHeal(amount);

            assertThat(hero.getHp()).isEqualTo(Math.min(Hero.MAX_HP, beforeHp + amount));
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, -1, -50 })
        void _0미만_힐이_들어오면_예외(int amount) {
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(50)
                    .build();

            assertThatThrownBy(() -> hero.takeHeal(amount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("힐은 0보다 커야 함");
        }
    }

    @Nested
    class SuperHeroTeest {

        @Test
        void 비행상태아니면_기본공격만() {
            SuperHero superHero = new SuperHero.Builder()
                    .name("영웅")
                    .hp(Hero.MAX_HP)
                    .isFlying(false)
                    .build();
            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();
            int beforeSlimeHp = slime.getHp();

            superHero.attack(slime);

            assertThat(slime.getHp()).isEqualTo(beforeSlimeHp - superHero.getPower());
        }

        @Test
        void 비행상태면_기본공격과_추가공격() {
            SuperHero superHero = new SuperHero.Builder()
                    .name("영웅")
                    .hp(Hero.MAX_HP)
                    .isFlying(true)
                    .build();

            Slime slime = new Slime.Builder()
                    .suffix("A")
                    .build();
            int beforeSlimeHp = slime.getHp();

            superHero.attack(slime);

            assertThat(slime.getHp()).isEqualTo(Math.max(0, beforeSlimeHp - (superHero.getPower() + SuperHero.BONUS_DAMAGE)));
        }
    }

    @Nested
    class WizardTest {

        @Test
        void 힐하면_히어로체력을_20힐하고_위자드Mp를_10감소한다() {
            Wizard wizard = new Wizard.Builder()
                    .build();
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(10)
                    .build();

            int beforeMp = wizard.getMp();
            int beforeHroHp = hero.getHp();

            wizard.heal(hero);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(wizard.getMp()).isEqualTo(beforeMp - Wizard.COST_FOR_HEAL);
                softly.assertThat(hero.getHp()).isEqualTo(beforeHroHp + Wizard.HEAL_HP_AMOUNT);
            });
        }

        @Test
        void mp없으면_힐불가() {
            Wizard wizard = new Wizard.Builder()
                    .mp(1)
                    .build();
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(10)
                    .build();

            int beforeMp = wizard.getMp();
            int beforeHroHp = hero.getHp();

            wizard.heal(hero);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(wizard.getMp()).isEqualTo(beforeMp);
                softly.assertThat(hero.getHp()).isEqualTo(beforeHroHp);
            });
        }
    }

    @Nested
    class GreatWizardTest {

        @Test
        void 힐하면_히어로체력을_25힐하고_위자드Mp를_5감소한다() {
            GreatWizard greatWizard = new GreatWizard.Builder()
                    .build();
            Hero hero = new Hero.Builder()
                    .hp(10)
                    .build();

            int beforeMp = greatWizard.getMp();
            int beforeHroHp = hero.getHp();

            greatWizard.heal(hero);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(greatWizard.getMp()).isEqualTo(beforeMp - GreatWizard.COST_FOR_HEAL);
                softly.assertThat(hero.getHp()).isEqualTo(beforeHroHp + GreatWizard.HEAL_HP_AMOUNT);
            });
        }

        @Test
        void mp없으면_힐불가() {
            GreatWizard greatWizard = new GreatWizard.Builder()
                    .mp(1)
                    .build();
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(10)
                    .build();

            int beforeMp = greatWizard.getMp();
            int beforeHroHp = hero.getHp();

            greatWizard.heal(hero);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(greatWizard.getMp()).isEqualTo(beforeMp);
                softly.assertThat(hero.getHp()).isEqualTo(beforeHroHp);
            });
        }

        @Test
        void 슈퍼힐하면_히어로체력을_풀피로_힐하고_위자드Mp를_50감소한다() {
            GreatWizard greatWizard = new GreatWizard.Builder()
                    .build();
            Hero hero = new Hero.Builder()
                    .name("영웅")
                    .hp(10)
                    .build();

            int beforeMp = greatWizard.getMp();

            greatWizard.superHeal(hero);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(greatWizard.getMp()).isEqualTo(beforeMp - GreatWizard.COST_FOR_SUPER_HEAL);
                softly.assertThat(hero.getHp()).isEqualTo(Hero.MAX_HP);
            });
        }

        @Test
        void mp없으면_슈퍼힐불가() {
            GreatWizard greatWizard = new GreatWizard.Builder()
                    .mp(1)
                    .build();
            Hero hero = new Hero.Builder()
                    .hp(10)
                    .build();

            int beforeMp = greatWizard.getMp();
            int beforeHroHp = hero.getHp();

            greatWizard.heal(hero, GreatWizard.COST_FOR_SUPER_HEAL, Hero.MAX_HP, GreatWizard.SUPER_HEAL_SKILL_NAME);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(greatWizard.getMp()).isEqualTo(beforeMp);
                softly.assertThat(hero.getHp()).isEqualTo(beforeHroHp);
            });
        }
    }
}