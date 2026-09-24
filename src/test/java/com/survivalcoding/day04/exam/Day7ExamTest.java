package com.survivalcoding.day04.exam;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class Day7ExamTest {

    @Nested
    class SlimeTest {

        @ParameterizedTest
        @NullAndEmptySource
        void 널_공란_불가(String name) {

            String errorMsg = "이름에 널, 공란 불가";

            IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> new Slime(name));

            assertEquals(errorMsg, error.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"슬", "라임"})
        void 이름이_1자_이상이면_정상_초기화(String name) {

            Slime slime = new Slime(name);

            assertEquals(name, slime.getSuffix());
        }

        @ParameterizedTest
        @NullSource
        void 어택시_히어로에_널_불가(Hero hero) {

            Slime slime = new Slime("슬");

            String errorMsg = "히어로 널";

            IllegalArgumentException error
                    = assertThrows(IllegalArgumentException.class, () -> slime.attack(hero, 10));
            assertEquals(errorMsg, error.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, 0 })
        void 어택데미지는_0미만이면_예외(int damage) {

            Hero hero = new Hero();
            Slime slime = new Slime("슬");

            String errorMsg = "데미지는 1이상이어야 함";

            IllegalArgumentException error
                    = assertThrows(IllegalArgumentException.class, () -> slime.attack(hero, damage));
            assertEquals(errorMsg, error.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 3, 1000})
        void 어택데미지는_1이상이면_정상(int damage) {

            Hero hero = new Hero();
            Slime slime = new Slime("슬");

            int heroBeforeHp = hero.getHp();

            slime.attack(hero, damage);

            assertEquals(Math.max(0, heroBeforeHp - damage), hero.getHp());
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, 0 })
        void 받는공격데미지가_0미만이면_예외(int damage) {

            Slime slime = new Slime("슬");
            String errorMsg = "1보다 작은값은 불가";
            IllegalArgumentException error
                    = assertThrows(IllegalArgumentException.class, () -> slime.takeDamage(damage));
            assertEquals(errorMsg, error.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = { 1 })
        void 받는공격데미지가_1이상이면_정상(int damage) {

            Slime slime = new Slime("슬");
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
            PoisonSlime poisonSlime = new PoisonSlime("독슬", count);

            assertThat(poisonSlime.getPoisonCount()).isEqualTo(count);
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, -5 })
        void 독카운트는_0미만이면_예외(int invalidCount) {

            assertThatThrownBy(() -> new PoisonSlime("독슬", invalidCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("0보다 작습니다");
        }

        @Test
        void 독카운트가_남으면_독공격하고_카운트를_1감소한다() {

            Hero hero = new Hero();
            PoisonSlime poisonSlime = new PoisonSlime("독슬");

            int beforeHp = hero.getHp();
            int totalDamage = (beforeHp - Slime.INIT_POWER) / PoisonSlime.POISON_RATE + Slime.INIT_POWER;

            poisonSlime.attack(hero);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(hero.getHp()).isEqualTo(beforeHp - totalDamage);
                softly.assertThat(poisonSlime.getPoisonCount()).isEqualTo(PoisonSlime.MAX_POISON_COUNT - 1);
            });
        }

        @Test
        void 독카운트가_0이하이면_기본공격만() {

            PoisonSlime poisonSlime = new PoisonSlime("A");
            Hero hero = new Hero("영웅", 100);

            poisonSlime.setPoisonCount(0); // 독 카운트 소진 상태로 설정

            poisonSlime.attack(hero);

            assertThat(hero.getHp()).isEqualTo(90);
        }

        @Test
        void 플레이어체력이_적더라도_최소1의_독데미지는_들어간다() {

            PoisonSlime poisonSlime = new PoisonSlime("A");

            Hero hero = new Hero("영웅", 14);

            poisonSlime.attack(hero);
        }
    }
}
