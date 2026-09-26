package com.survivalcoding.day04.exam;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Cleric 테스트")
public class ClericTest {

    @Nested
    @DisplayName("selfAid 테스트")
    class SelfAidTest {

        @Test
        @DisplayName("MP가 충분하면 MP를 소비하고 HP를 최대로 회복한다")
        void selfAidWithEnoughMp() {
            // given
            final Cleric cleric = new Cleric.Builder()
                    .name("엄")
                    .hp(30)
                    .mp(Cleric.COST_FOR_SELF_AID)
                    .build();

            final int beforeMp = cleric.getMp();

            // when
            cleric.selfAid();

            // then
            assertAll(
                    () -> assertEquals(Cleric.MAX_HP, cleric.getHp()),
                    () -> assertEquals(beforeMp - Cleric.COST_FOR_SELF_AID, cleric.getMp())
            );
        }

        @Test
        @DisplayName("MP가 부족하면 HP와 MP가 변하지 않는다")
        void selfAidWithNotEnoughMp() {
            // given
            final Cleric cleric = new Cleric.Builder()
                    .name("엄")
                    .hp(30)
                    .mp(Cleric.COST_FOR_SELF_AID - 1)
                    .build();

            final int beforeHp = cleric.getHp();
            final int beforeMp = cleric.getMp();

            // when
            cleric.selfAid();

            // then
            assertAll(
                    () -> assertEquals(beforeHp, cleric.getHp()),
                    () -> assertEquals(beforeMp, cleric.getMp())
            );
        }
    }

    @Nested
    @DisplayName("pray 테스트")
    class PrayTest {

        @Test
        @DisplayName("MP가 최대값보다 작으면 기도 시간 + 보정치만큼 회복한다")
        void prayShouldRestoreMp() {
            // given
            final Cleric cleric = new Cleric.Builder()
                    .name("엄")
                    .hp(10)
                    .mp(0)
                    .build();

            final int beforeMp = cleric.getMp();
            final int durationSecond = 3;

            final int minRestoreAmount = durationSecond;
            final int maxRestoreAmount = durationSecond + Cleric.MAX_CORRECTION_VALUE;

            // when
            final int restoreAmount = cleric.pray(durationSecond);

            // then
            assertAll(
                    () -> assertTrue(
                            minRestoreAmount <= restoreAmount
                                    && restoreAmount <= maxRestoreAmount
                    ),
                    () -> assertEquals(beforeMp + restoreAmount, cleric.getMp())
            );
        }

        @Test
        @DisplayName("회복량이 MAX_MP를 초과하면 MAX_MP까지만 회복한다")
        void prayShouldNotExceedMaxMp() {
            // given
            final Cleric cleric = new Cleric.Builder()
                    .name("엄")
                    .hp(10)
                    .mp(Cleric.MAX_MP - 1)
                    .build();

            final int beforeMp = cleric.getMp();
            final int durationSecond = 3;

            // when
            final int restoreAmount = cleric.pray(durationSecond);

            // then
            assertAll(
                    () -> assertEquals(
                            Cleric.MAX_MP,
                            beforeMp + restoreAmount
                    ),
                    () -> assertEquals(
                            Cleric.MAX_MP,
                            cleric.getMp()
                    )
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {-10, -1, 0})
        @DisplayName("기도 시간이 0 이하이면 예외가 발생한다")
        void invalidDurationShouldThrowException(final int durationSecond) {
            // given
            final Cleric cleric = new Cleric.Builder()
                    .name("엄")
                    .hp(10)
                    .mp(0)
                    .build();

            // then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> cleric.pray(durationSecond)
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10})
        @DisplayName("이미 최대 MP이면 기도 시간과 관계없이 0을 반환한다")
        void maxMpShouldReturnZero(final int durationSecond) {
            // given
            final Cleric cleric = new Cleric.Builder()
                    .name("엄")
                    .hp(10)
                    .mp(Cleric.MAX_MP)
                    .build();

            // when
            final int restoreAmount = cleric.pray(durationSecond);

            // then
            assertAll(
                    () -> assertEquals(0, restoreAmount),
                    () -> assertEquals(Cleric.MAX_MP, cleric.getMp())
            );
        }
    }

    @Nested
    @DisplayName("생성자(빌더) 테스트")
    class ConstructorTest {

        @Test
        @DisplayName("이름만 입력하면 HP와 MP는 최대값으로 초기화된다")
        void createWithName() {
            // given
            final String name = "홍길동";

            // when
            final Cleric cleric = new Cleric.Builder()
                    .name(name)
                    .build();

            // then
            assertAll(
                    () -> assertEquals(name, cleric.getName()),
                    () -> assertEquals(Cleric.MAX_HP, cleric.getHp()),
                    () -> assertEquals(Cleric.MAX_MP, cleric.getMp())
            );
        }

        @Test
        @DisplayName("이름과 HP만 입력하면 MP는 최대값으로 초기화된다")
        void createWithNameAndHp() {
            // given
            final String name = "홍길동";
            final int hp = 30;

            // when
            final Cleric cleric = new Cleric.Builder()
                    .name(name)
                    .hp(hp)
                    .build();

            // then
            assertAll(
                    () -> assertEquals(name, cleric.getName()),
                    () -> assertEquals(hp, cleric.getHp()),
                    () -> assertEquals(Cleric.MAX_MP, cleric.getMp())
            );
        }

        @Test
        @DisplayName("이름, HP, MP를 입력하면 해당 값으로 초기화된다")
        void createWithNameHpAndMp() {
            // given
            final String name = "홍길동";
            final int hp = 30;
            final int mp = 5;

            // when
            final Cleric cleric = new Cleric.Builder()
                    .name(name)
                    .hp(hp)
                    .mp(mp)
                    .build();

            // then
            assertAll(
                    () -> assertEquals(name, cleric.getName()),
                    () -> assertEquals(hp, cleric.getHp()),
                    () -> assertEquals(mp, cleric.getMp())
            );
        }
    }

    @Nested
    @DisplayName("이름 검증 테스트")
    class NameValidationTest {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "     "})
        @DisplayName("이름이 null, 빈 문자열 또는 공백이면 예외가 발생한다")
        void invalidNameShouldThrowException(final String name) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Cleric.Builder()
                            .name(name)
                            .build()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"홍길동", "엄", "Cleric"})
        @DisplayName("유효한 이름은 정상적으로 저장된다")
        void validNameShouldBeStored(final String name) {
            // when
            final Cleric cleric = new Cleric.Builder()
                    .name(name)
                    .build();

            // then
            assertEquals(name, cleric.getName());
        }
    }

    @Nested
    @DisplayName("HP 경계값 테스트")
    class HpValidationTest {

        @ParameterizedTest
        @ValueSource(ints = {
                0,
                1,
                Cleric.MAX_HP - 1,
                Cleric.MAX_HP
        })
        @DisplayName("HP가 허용 범위 안이면 정상적으로 생성된다")
        void validHpShouldBeAccepted(final int hp) {
            // when
            final Cleric cleric = new Cleric.Builder()
                    .name("홍길동")
                    .hp(hp)
                    .build();

            // then
            assertEquals(hp, cleric.getHp());
        }

        @ParameterizedTest
        @ValueSource(ints = {
                -1,
                Cleric.MAX_HP + 1
        })
        @DisplayName("HP가 허용 범위를 벗어나면 예외가 발생한다")
        void invalidHpShouldThrowException(final int hp) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Cleric.Builder()
                            .name("홍길동")
                            .hp(hp)
                            .build()
            );
        }
    }

    @Nested
    @DisplayName("MP 경계값 테스트")
    class MpValidationTest {

        @ParameterizedTest
        @ValueSource(ints = {
                0,
                1,
                Cleric.MAX_MP - 1,
                Cleric.MAX_MP
        })
        @DisplayName("MP가 허용 범위 안이면 정상적으로 생성된다")
        void validMpShouldBeAccepted(final int mp) {
            // when
            final Cleric cleric = new Cleric.Builder()
                    .name("홍길동")
                    .hp(30)
                    .mp(mp)
                    .build();

            // then
            assertEquals(mp, cleric.getMp());
        }

        @ParameterizedTest
        @ValueSource(ints = {
                -1,
                Cleric.MAX_MP + 1
        })
        @DisplayName("MP가 허용 범위를 벗어나면 예외가 발생한다")
        void invalidMpShouldThrowException(final int mp) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Cleric.Builder()
                            .name("홍길동")
                            .hp(30)
                            .mp(mp)
                            .build()
            );
        }
    }

    @Nested
    @DisplayName("복합 입력 검증 테스트")
    class MultipleValidationTest {

        @ParameterizedTest
        @CsvSource({
                "'', 30, 5",
                "' ', 30, 5",
                "'홍길동', -1, 5",
                "'홍길동', 51, 5",
                "'홍길동', 30, -1",
                "'홍길동', 30, 11",
                "'', -1, 5",
                "'', 30, 11",
                "'홍길동', -1, 11",
                "'', -1, 11"
        })
        @DisplayName("하나 이상의 입력값이 잘못되면 예외가 발생한다")
        void invalidValuesShouldThrowException(
                final String name,
                final int hp,
                final int mp
        ) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Cleric.Builder()
                            .name(name)
                            .hp(hp)
                            .mp(mp)
                            .build()
            );
        }
    }
}