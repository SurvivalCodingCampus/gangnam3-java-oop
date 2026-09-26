package com.survivalcoding.day04.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Day6ExamTest {

    @Nested
    class WandTest {
        @Test
        @DisplayName("지팡이 이름은 null일 수 없다")
        void nameShouldNotBeNull() {
            // given & when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Wand.Builder().name(null).build()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"가나", "가"})
        @DisplayName("지팡이 이름이 3문자 미만이면 예외가 발생한다")
        void nameShouldBeAtLeastThreeCharacters(final String name) {
            // given & when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Wand.Builder().name(name).build()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"가나다", "가나다라"})
        @DisplayName("지팡이 이름이 3문자 이상이면 설정된다")
        void validNameShouldBeSet(final String name) {
            // given & when
            final Wand wand = new Wand.Builder().name(name).build();

            // then
            assertEquals(name, wand.getName());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.4, 100.1})
        @DisplayName("지팡이 마력이 0.5 미만 또는 100.0 초과이면 예외가 발생한다")
        void invalidPowerShouldThrowException(final double power) {
            // given & when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Wand.Builder().power(power).build()
            );
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.5, 0.51, 99.9, 100.0})
        @DisplayName("지팡이 마력이 0.5 이상 100.0 이하이면 설정된다")
        void validPowerShouldBeSet(final double power) {
            // given & when
            final Wand wand = new Wand.Builder().power(power).build();

            // then
            assertEquals(power, wand.getPower());
        }
    }

    @Nested
    class WizardTest {
        @ParameterizedTest
        @NullSource
        @DisplayName("마법사의 이름은 null일 수 없다")
        void nameShouldNotBeNull(final String name) {
            // given & when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Wizard.Builder().name(name).build()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = { "홍길", "홍" })
        @DisplayName("마법사의 이름이 3문자 미만이면 예외가 발생한다")
        void nameShouldBeAtLeastThreeCharacters(final String name) {
            // given & when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Wizard.Builder().name(name).build()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = { "홍길동", "홍길동님" })
        @DisplayName("마법사의 이름이 3문자 이상이면 설정할 수 있다")
        void validNameShouldBeSet(final String name) {
            // given & when
            final Wizard wizard = new Wizard.Builder().name(name).build();

            // then
            assertEquals(name, wizard.getName());
        }

        @Test
        @DisplayName("마법사의 지팡이는 null일 수 없다")
        void wandShouldNotBeNull() {
            // given & when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Wizard.Builder().wand(null).build()
            );
        }

        @Test
        @DisplayName("마법사에게 유효한 지팡이를 설정할 수 있다")
        void validWandShouldBeSet() {
            // given
            final Wand wand = new Wand.Builder()
                    .name("불꽃봉")
                    .power(50.0)
                    .build();

            // when
            final Wizard wizard = new Wizard.Builder()
                    .wand(wand)
                    .build();

            // then
            assertSame(wand, wizard.getWand());
        }

        @ParameterizedTest
        @ValueSource(ints = { -1 })
        @DisplayName("MP가 0보다 작으면 예외가 발생한다")
        void negativeMpShouldThrowException(final int mp) {
            // given & when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Wizard.Builder().mp(mp).build()
            );
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 1 })
        @DisplayName("MP가 0 이상이면 설정할 수 있다")
        void validMpShouldBeSet(final int mp) {
            // given & when
            final Wizard wizard = new Wizard.Builder().mp(mp).build();

            // then
            assertEquals(mp, wizard.getMp());
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, -100 })
        @DisplayName("HP가 음수이면 0으로 설정된다")
        void negativeHpShouldBeSetToZero(final int hp) {
            // given & when
            final Wizard wizard = new Wizard.Builder().hp(hp).build();

            // then
            assertEquals(0, wizard.getHp());
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 1 })
        @DisplayName("HP가 0 이상이면 입력한 값으로 설정된다")
        void validHpShouldBeSet(final int hp) {
            // given & when
            final Wizard wizard = new Wizard.Builder().hp(hp).build();

            // then
            assertEquals(hp, wizard.getHp());
        }
    }

    @Nested
    class PersonTest {
        @ParameterizedTest
        @MethodSource("provideBirthYears")
        @DisplayName("나이는 올해 연도에서 태어난 연도를 뺀 값이다")
        void ageShouldBeCurrentYearMinusBirthYear(
                final int birthYear,
                final int expectedAge
        ) {
            // given & when
            final Person person = new Person("홍길동", birthYear);

            final int age = person.getAge();

            // then
            assertEquals(expectedAge, age);
        }

        static List provideBirthYears() {
            final int thisYear = LocalDate.now().getYear();

            return List.of(
                    Arguments.of(thisYear, 0),
                    Arguments.of(thisYear - 1, 1),
                    Arguments.of(thisYear - 20, 20)
            );
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("이름이 null이면 예외가 발생한다")
        void nameShouldNotBeNull(final String name) {
            // given
            final int birthYear = LocalDate.now().getYear();

            // when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Person(name, birthYear)
            );
        }

        @ParameterizedTest
        @ValueSource(strings = { " ", "  ", "\t" })
        @DisplayName("이름이 공란이면 예외가 발생한다")
        void nameShouldNotBeBlank(final String name) {
            // given
            final int birthYear = LocalDate.now().getYear();

            // when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Person(name, birthYear)
            );
        }

        @Test
        @DisplayName("태어난 해가 올해보다 미래이면 예외가 발생한다")
        void birthYearShouldNotBeFuture() {
            // given
            final String name = "홍길동";
            final int thisYear = LocalDate.now().getYear();
            final int birthYear = thisYear + 1; // 경계 바로 위

            // when & then
            assertThrows(IllegalArgumentException.class, () ->
                    new Person(name, birthYear)
            );
        }
    }
}