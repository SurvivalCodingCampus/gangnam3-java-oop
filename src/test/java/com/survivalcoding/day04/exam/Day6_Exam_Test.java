package com.survivalcoding.day04.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Day6_Exam_Test {
    @Nested
    class WandTest {
        @Test
        @DisplayName("지팡이 이름은 null일 수 없다")
        void nameShouldNotBeNull() {
            // given
            final Wand wand = new Wand();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> wand.setName(null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"가나", "가"})
        @DisplayName("지팡이 이름이 3문자 미만이면 예외가 발생한다")
        void nameShouldBeAtLeastThreeCharacters(final String name) {
            // given
            final Wand wand = new Wand();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> wand.setName(name));
        }

        @ParameterizedTest
        @ValueSource(strings = {"가나다", "가나다라"})
        @DisplayName("지팡이 이름이 3문자 이상이면 설정된다")
        void validNameShouldBeSet(final String name) {
            // given
            final Wand wand = new Wand();

            // when
            wand.setName(name);

            // then
            assertEquals(name, wand.getName());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.4, 100.1})
        @DisplayName("지팡이 마력이 0.5 미만 또는 100.0 초과이면 예외가 발생한다")
        void invalidPowerShouldThrowException(final double power) {
            // given
            final Wand wand = new Wand();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> wand.setPower(power));
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.5, 0.51, 99.9, 100})
        @DisplayName("지팡이 마력이 0.5 이상 100.0 이하이면 설정된다")
        void validPowerShouldBeSet(final double power) {
            // given
            final Wand wand = new Wand();

            // when
            wand.setPower(power);

            // then
            assertEquals(power, wand.getPower());
        }
    }

    @Nested
    class WizardTest {
        @Test
        @DisplayName("마법사의 이름은 null일 수 없다")
        void nameShouldNotBeNull() {
            // given
            final Wizard wizard = new Wizard();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> wizard.setName(null));
        }

        @Test
        @DisplayName("마법사의 이름이 3문자 미만이면 예외가 발생한다")
        void nameShouldBeAtLeastThreeCharacters() {
            // given
            final Wizard wizard = new Wizard();
            final String name = "홍길"; // 경계 바로 아래: 2문자

            // when & then
            assertThrows(IllegalArgumentException.class, () -> wizard.setName(name));
        }

        @ParameterizedTest
        @ValueSource(strings = { "홍길동", "홍길동님" })
        @DisplayName("마법사의 이름이 3문자 이상이면 설정할 수 있다")
        void validNameShouldBeSet(final String name) {
            // given
            final Wizard wizard = new Wizard();

            // when
            wizard.setName(name);

            // then
            assertEquals(name, wizard.getName());
        }

        @Test
        @DisplayName("마법사의 지팡이는 null일 수 없다")
        void wandShouldNotBeNull() {
            // given
            final Wizard wizard = new Wizard();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> wizard.setWand(null));
        }

        @Test
        @DisplayName("마법사에게 유효한 지팡이를 설정할 수 있다")
        void validWandShouldBeSet() {
            // given
            final Wizard wizard = new Wizard();
            final Wand wand = new Wand();

            wand.setName("불꽃봉");
            wand.setPower(50.0);

            // when
            wizard.setWand(wand);

            // then
            assertSame(wand, wizard.getWand());
        }

        @Test
        @DisplayName("MP가 0보다 작으면 예외가 발생한다")
        void negativeMpShouldThrowException() {
            // given
            final Wizard wizard = new Wizard();
            final int mp = -1; // 경계 바로 아래

            // when & then
            assertThrows(IllegalArgumentException.class, () -> wizard.setMp(mp));
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 1 })
        @DisplayName("MP가 0 이상이면 설정할 수 있다")
        void validMpShouldBeSet(final int mp) {
            // given
            final Wizard wizard = new Wizard();

            // when
            wizard.setMp(mp);

            // then
            assertEquals(mp, wizard.getMp());
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, -100 })
        @DisplayName("HP가 음수이면 0으로 설정된다")
        void negativeHpShouldBeSetToZero(final int hp) {
            // given
            final Wizard wizard = new Wizard();

            // when
            wizard.setHp(hp);

            // then
            assertEquals(0, wizard.getHp());
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 1 })
        @DisplayName("HP가 0 이상이면 입력한 값으로 설정된다")
        void validHpShouldBeSet(final int hp) {
            // given
            final Wizard wizard = new Wizard();

            // when
            wizard.setHp(hp);

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
            // given
            final Person person = new Person("홍길동", birthYear);

            // when
            final int age = person.getAge();

            // then
            assertEquals(expectedAge, age);
        }

        static List<Arguments> provideBirthYears() {
            final int thisYear = LocalDate.now().getYear();

            return List.of(
                    Arguments.of(thisYear, 0),       // 경계값
                    Arguments.of(thisYear - 1, 1),   // 경계 바로 옆
                    Arguments.of(thisYear - 20, 20)  // 일반값
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("이름이 null이면 예외가 발생한다")
        void nameShouldNotBeNull(final String name) {
            // given
            final int birthYear = LocalDate.now().getYear();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> new Person(name, birthYear));
        }

        @ParameterizedTest
        @ValueSource(strings = { " ", "  ", "\t" })
        @DisplayName("이름이 공란이면 예외가 발생한다")
        void nameShouldNotBeBlank(final String name) {
            // given
            final int birthYear = LocalDate.now().getYear();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> new Person(name, birthYear));
        }

        @Test
        @DisplayName("태어난 해가 올해보다 미래이면 예외가 발생한다")
        void birthYearShouldNotBeFuture() {
            // given
            final String name = "홍길동";
            final int thisYear = LocalDate.now().getYear();
            final int birthYear = thisYear + 1; // 경계 바로 위

            // when & then
            assertThrows(IllegalArgumentException.class, () -> new Person(name, birthYear));
        }
    }
}