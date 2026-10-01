package com.survivalcoding.day05_class_instance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WandTest {

    @Test
    @DisplayName("[Wand] null 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectNullName() {
        // given: 잘못된 이름 null을 준비한다.
        Wand wand = new Wand();
        wand.setName("가나다");
        String invalidName = null;

        // when: wand.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setName(invalidName));

        // then: null 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wand.getName());
    }

    @Test
    @DisplayName("[Wand] 빈 문자열(길이 0) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectEmptyName() {
        // given: 잘못된 이름 ""을 준비한다.
        Wand wand = new Wand();
        wand.setName("가나다");
        String invalidName = "";

        // when: wand.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setName(invalidName));

        // then: 빈 문자열(길이 0) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wand.getName());
    }

    @Test
    @DisplayName("[Wand] 2글자 이름(가나) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectTwoCharacterName() {
        // given: 잘못된 이름 "가나"을 준비한다.
        Wand wand = new Wand();
        wand.setName("가나다");
        String invalidName = "가나";

        // when: wand.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setName(invalidName));

        // then: 2글자 이름(가나) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wand.getName());
    }

    @Test
    @DisplayName("[Wand] 공백만 3개인 이름 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectBlankName() {
        // given: 잘못된 이름 "   "을 준비한다.
        Wand wand = new Wand();
        wand.setName("가나다");
        String invalidName = "   ";

        // when: wand.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setName(invalidName));

        // then: 공백만 3개인 이름 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wand.getName());
    }

    @Test
    @DisplayName("[Wand] 앞뒤 공백을 제거하면 2글자인 이름( 가나 ) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectPaddedTwoCharacterName() {
        // given: 잘못된 이름 " 가나 "을 준비한다.
        Wand wand = new Wand();
        wand.setName("가나다");
        String invalidName = " 가나 ";

        // when: wand.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setName(invalidName));

        // then: 앞뒤 공백을 제거하면 2글자인 이름( 가나 ) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wand.getName());
    }

    @Test
    @DisplayName("[Wand] 3글자 이름(가나다)을 설정하면 그대로 저장한다")
    void acceptThreeCharacterName() {
        // given: 이름 "가나다"을 준비한다.
        Wand wand = new Wand();
        String name = "가나다";

        // when: wand.setName()을 실행한다.
        wand.setName(name);

        // then: 3글자 이름(가나다)을 설정하면 그대로 저장한다
        Assertions.assertEquals(name, wand.getName());
    }

    @Test
    @DisplayName("[Wand] 4글자 이름(가나다라)을 설정하면 그대로 저장한다")
    void acceptFourCharacterName() {
        // given: 이름 "가나다라"을 준비한다.
        Wand wand = new Wand();
        String name = "가나다라";

        // when: wand.setName()을 실행한다.
        wand.setName(name);

        // then: 4글자 이름(가나다라)을 설정하면 그대로 저장한다
        Assertions.assertEquals(name, wand.getName());
    }

    @Test
    @DisplayName("[Wand] 마력에 하한 0.5을 설정하면 그대로 저장한다")
    void acceptMinimumPower() {
        // given: 마력 0.5을 준비한다.
        Wand wand = new Wand();
        double power = 0.5;

        // when: wand.setPower()을 실행한다.
        wand.setPower(power);

        // then: 마력에 하한 0.5을 설정하면 그대로 저장한다
        Assertions.assertEquals(power, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 하한 0.5 바로 위 값(nextUp)을 설정하면 그대로 저장한다")
    void acceptJustAboveMinimumPower() {
        // given: 마력 Math.nextUp(0.5)을 준비한다.
        Wand wand = new Wand();
        double power = Math.nextUp(0.5);

        // when: wand.setPower()을 실행한다.
        wand.setPower(power);

        // then: 마력에 하한 0.5 바로 위 값(nextUp)을 설정하면 그대로 저장한다
        Assertions.assertEquals(power, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 상한 100.0 바로 아래 값(nextDown)을 설정하면 그대로 저장한다")
    void acceptJustBelowMaximumPower() {
        // given: 마력 Math.nextDown(100.0)을 준비한다.
        Wand wand = new Wand();
        double power = Math.nextDown(100.0);

        // when: wand.setPower()을 실행한다.
        wand.setPower(power);

        // then: 마력에 상한 100.0 바로 아래 값(nextDown)을 설정하면 그대로 저장한다
        Assertions.assertEquals(power, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 상한 100.0을 설정하면 그대로 저장한다")
    void acceptMaximumPower() {
        // given: 마력 100.0을 준비한다.
        Wand wand = new Wand();
        double power = 100.0;

        // when: wand.setPower()을 실행한다.
        wand.setPower(power);

        // then: 마력에 상한 100.0을 설정하면 그대로 저장한다
        Assertions.assertEquals(power, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 하한 0.5 바로 아래 값(nextDown)를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다")
    void rejectBelowMinimumPower() {
        // given: 마력 Math.nextDown(0.5)을 준비한다.
        Wand wand = new Wand();
        wand.setPower(5.0);
        double power = Math.nextDown(0.5);

        // when: wand.setPower()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setPower(power));

        // then: 마력에 하한 0.5 바로 아래 값(nextDown)를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals(5.0, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 상한 100.0 바로 위 값(nextUp)를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다")
    void rejectAboveMaximumPower() {
        // given: 마력 Math.nextUp(100.0)을 준비한다.
        Wand wand = new Wand();
        wand.setPower(5.0);
        double power = Math.nextUp(100.0);

        // when: wand.setPower()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setPower(power));

        // then: 마력에 상한 100.0 바로 위 값(nextUp)를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals(5.0, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 NaN를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다")
    void rejectNaNPower() {
        // given: 마력 Double.NaN을 준비한다.
        Wand wand = new Wand();
        wand.setPower(5.0);
        double power = Double.NaN;

        // when: wand.setPower()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setPower(power));

        // then: 마력에 NaN를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals(5.0, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 음의 무한대를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다")
    void rejectNegativeInfinityPower() {
        // given: 마력 Double.NEGATIVE_INFINITY을 준비한다.
        Wand wand = new Wand();
        wand.setPower(5.0);
        double power = Double.NEGATIVE_INFINITY;

        // when: wand.setPower()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setPower(power));

        // then: 마력에 음의 무한대를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals(5.0, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 마력에 양의 무한대를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다")
    void rejectPositiveInfinityPower() {
        // given: 마력 Double.POSITIVE_INFINITY을 준비한다.
        Wand wand = new Wand();
        wand.setPower(5.0);
        double power = Double.POSITIVE_INFINITY;

        // when: wand.setPower()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wand.setPower(power));

        // then: 마력에 양의 무한대를 설정하면 예외가 발생하고 기존 마력 5.0을 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals(5.0, wand.getPower());
    }

    // 게임 사용 흐름, 상태 전이, 객체 관계와 실패 후 재시도 검사

    @Test
    @DisplayName("[Wand] 유효한 이름의 앞뒤 공백과 가운데 공백은 저장 시 유지된다")
    void preserveWhitespaceName() {
        // given: 이름 "  홍 길동  "을 준비한다.
        String name = "  홍 길동  ";
        Wand subject = new Wand();

        // when: subject.setName()을 실행한다.
        subject.setName(name);

        // then: 유효한 이름의 앞뒤 공백과 가운데 공백은 저장 시 유지된다
        Assertions.assertEquals(name, subject.getName());
    }

    @Test
    @DisplayName("[Wand] 영문 이름을 사용할 수 있다")
    void preserveEnglishName() {
        // given: 이름 "Merlin"을 준비한다.
        String name = "Merlin";
        Wand subject = new Wand();

        // when: subject.setName()을 실행한다.
        subject.setName(name);

        // then: 영문 이름을 사용할 수 있다
        Assertions.assertEquals(name, subject.getName());
    }

    @Test
    @DisplayName("[Wand] 강화와 약화로 마력을 여러 번 바꾸면 마지막 값이 적용된다")
    void upgradeAndDowngradePower() {
        // given: wand = new Wand()을 준비한다.
        Wand wand = new Wand();
        wand.setName("수습지팡이");
        wand.setPower(1.0);

        // when: wand.setPower()을 실행한다.
        wand.setPower(5.0);
        Assertions.assertEquals(5.0, wand.getPower()); // 중간 강화 상태
        wand.setPower(2.0);

        // then: 강화와 약화로 마력을 여러 번 바꾸면 마지막 값이 적용된다
        Assertions.assertEquals(2.0, wand.getPower());
        Assertions.assertEquals("수습지팡이", wand.getName());
    }

    @Test
    @DisplayName("[Wand] 지팡이 이름 변경은 마력에 영향을 주지 않는다")
    void renameDoesNotChangePower() {
        // given: wand = new Wand()을 준비한다.
        Wand wand = new Wand();
        wand.setName("수습지팡이");
        wand.setPower(3.0);

        // when: wand.setName()을 실행한다.
        wand.setName("현자지팡이");

        // then: 지팡이 이름 변경은 마력에 영향을 주지 않는다
        Assertions.assertEquals("현자지팡이", wand.getName());
        Assertions.assertEquals(3.0, wand.getPower());
    }

    @Test
    @DisplayName("[Wand] 같은 이름을 가진 두 지팡이 중 하나만 강화한다")
    void differentWandsAreIndependent() {
        // given: first = new Wand(), second = new Wand()을 준비한다.
        Wand first = new Wand();
        first.setName("나무지팡이");
        first.setPower(1.0);
        Wand second = new Wand();
        second.setName("나무지팡이");
        second.setPower(1.0);

        // when: first.setPower()을 실행한다.
        first.setPower(8.0);

        // then: 같은 이름을 가진 두 지팡이 중 하나만 강화한다
        Assertions.assertNotSame(first, second);
        Assertions.assertEquals(8.0, first.getPower());
        Assertions.assertEquals(1.0, second.getPower());
    }

    @Test
    @DisplayName("[Wand] 강화 실패는 이름과 마력을 보존하고 이후 정상 강화는 성공한다")
    void failedUpgradeCanBeRetried() {
        // given: wand = new Wand()을 준비한다.
        Wand wand = new Wand();
        wand.setName("나무지팡이");
        wand.setPower(2.0);

        // when: wand.setPower(), wand.getName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(IllegalArgumentException.class, () -> wand.setPower(101.0));
        Assertions.assertEquals(2.0, wand.getPower()); // 실패 직후 보존 확인
        Assertions.assertEquals("나무지팡이", wand.getName());
        wand.setPower(4.0);

        // then: 강화 실패는 이름과 마력을 보존하고 이후 정상 강화는 성공한다
        Assertions.assertEquals(4.0, wand.getPower());
        Assertions.assertEquals("나무지팡이", wand.getName());
    }

    @Test
    @DisplayName("[Wand] 같은 설정을 두 번 적용해도 값이 누적되지 않는다")
    void sameSettingIsIdempotent() {
        // given: wand = new Wand()을 준비한다.
        Wand wand = new Wand();
        wand.setName("나무지팡이");
        wand.setPower(2.0);

        // when: wand.setName(), wand.setPower()을 실행한다.
        wand.setName("나무지팡이");
        wand.setPower(2.0);

        // then: 같은 설정을 두 번 적용해도 값이 누적되지 않는다
        Assertions.assertEquals("나무지팡이", wand.getName());
        Assertions.assertEquals(2.0, wand.getPower());
    }
}
