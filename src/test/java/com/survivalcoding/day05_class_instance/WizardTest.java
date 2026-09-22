package com.survivalcoding.day05_class_instance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WizardTest {

    @Test
    @DisplayName("[Wizard] null 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectNullName() {
        // given: 잘못된 이름 null을 준비한다.
        Wizard wizard = new Wizard();
        wizard.setName("가나다");
        String invalidName = null;

        // when: wizard.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.setName(invalidName));

        // then: null 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] 빈 문자열(길이 0) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectEmptyName() {
        // given: 잘못된 이름 ""을 준비한다.
        Wizard wizard = new Wizard();
        wizard.setName("가나다");
        String invalidName = "";

        // when: wizard.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.setName(invalidName));

        // then: 빈 문자열(길이 0) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] 2글자 이름(가나) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectTwoCharacterName() {
        // given: 잘못된 이름 "가나"을 준비한다.
        Wizard wizard = new Wizard();
        wizard.setName("가나다");
        String invalidName = "가나";

        // when: wizard.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.setName(invalidName));

        // then: 2글자 이름(가나) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] 공백만 3개인 이름 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectBlankName() {
        // given: 잘못된 이름 "   "을 준비한다.
        Wizard wizard = new Wizard();
        wizard.setName("가나다");
        String invalidName = "   ";

        // when: wizard.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.setName(invalidName));

        // then: 공백만 3개인 이름 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] 앞뒤 공백을 제거하면 2글자인 이름( 가나 ) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다")
    void rejectPaddedTwoCharacterName() {
        // given: 잘못된 이름 " 가나 "을 준비한다.
        Wizard wizard = new Wizard();
        wizard.setName("가나다");
        String invalidName = " 가나 ";

        // when: wizard.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.setName(invalidName));

        // then: 앞뒤 공백을 제거하면 2글자인 이름( 가나 ) 설정 시 예외가 발생하고 기존 이름 가나다를 유지한다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals("가나다", wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] 3글자 이름(가나다)을 설정하면 그대로 저장한다")
    void acceptThreeCharacterName() {
        // given: 이름 "가나다"을 준비한다.
        Wizard wizard = new Wizard();
        String name = "가나다";

        // when: wizard.setName()을 실행한다.
        wizard.setName(name);

        // then: 3글자 이름(가나다)을 설정하면 그대로 저장한다
        Assertions.assertEquals(name, wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] 4글자 이름(가나다라)을 설정하면 그대로 저장한다")
    void acceptFourCharacterName() {
        // given: 이름 "가나다라"을 준비한다.
        Wizard wizard = new Wizard();
        String name = "가나다라";

        // when: wizard.setName()을 실행한다.
        wizard.setName(name);

        // then: 4글자 이름(가나다라)을 설정하면 그대로 저장한다
        Assertions.assertEquals(name, wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] HP에 -1을 설정하면 0으로 보정한다")
    void clampNegativeHpToZero() {
        // given: HP -1을 준비한다.
        Wizard wizard = new Wizard();
        int hp = -1;

        // when: wizard.setHp()을 실행한다.
        wizard.setHp(hp);

        // then: HP에 -1을 설정하면 0으로 보정한다
        Assertions.assertEquals(0, wizard.getHp());
    }

    @Test
    @DisplayName("[Wizard] HP에 0을 설정하면 0을 저장한다")
    void acceptZeroHp() {
        // given: HP 0을 준비한다.
        Wizard wizard = new Wizard();
        int hp = 0;

        // when: wizard.setHp()을 실행한다.
        wizard.setHp(hp);

        // then: HP에 0을 설정하면 0을 저장한다
        Assertions.assertEquals(0, wizard.getHp());
    }

    @Test
    @DisplayName("[Wizard] HP에 1을 설정하면 1을 저장한다")
    void acceptOneHp() {
        // given: HP 1을 준비한다.
        Wizard wizard = new Wizard();
        int hp = 1;

        // when: wizard.setHp()을 실행한다.
        wizard.setHp(hp);

        // then: HP에 1을 설정하면 1을 저장한다
        Assertions.assertEquals(1, wizard.getHp());
    }

    @Test
    @DisplayName("[Wizard] HP에 Integer.MIN_VALUE을 설정하면 0으로 보정한다")
    void clampMinimumIntegerHpToZero() {
        // given: HP Integer.MIN_VALUE을 준비한다.
        Wizard wizard = new Wizard();
        int hp = Integer.MIN_VALUE;

        // when: wizard.setHp()을 실행한다.
        wizard.setHp(hp);

        // then: HP에 Integer.MIN_VALUE을 설정하면 0으로 보정한다
        Assertions.assertEquals(0, wizard.getHp());
    }

    @Test
    @DisplayName("[Wizard] HP에 Integer.MAX_VALUE을 설정하면 최댓값을 그대로 저장한다")
    void acceptMaximumIntegerHp() {
        // given: HP Integer.MAX_VALUE을 준비한다.
        Wizard wizard = new Wizard();
        int hp = Integer.MAX_VALUE;

        // when: wizard.setHp()을 실행한다.
        wizard.setHp(hp);

        // then: HP에 Integer.MAX_VALUE을 설정하면 최댓값을 그대로 저장한다
        Assertions.assertEquals(Integer.MAX_VALUE, wizard.getHp());
    }

    @Test
    @DisplayName("[Wizard] MP에 0을 설정하면 예외 없이 그대로 저장한다")
    void acceptZeroMp() {
        // given: MP 0을 준비한다.
        Wizard wizard = new Wizard();
        int mp = 0;

        // when: wizard.setMp()을 실행한다.
        wizard.setMp(mp);

        // then: MP에 0을 설정하면 예외 없이 그대로 저장한다
        Assertions.assertEquals(mp, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] MP에 1을 설정하면 예외 없이 그대로 저장한다")
    void acceptOneMp() {
        // given: MP 1을 준비한다.
        Wizard wizard = new Wizard();
        int mp = 1;

        // when: wizard.setMp()을 실행한다.
        wizard.setMp(mp);

        // then: MP에 1을 설정하면 예외 없이 그대로 저장한다
        Assertions.assertEquals(mp, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] MP에 Integer.MAX_VALUE을 설정하면 예외 없이 그대로 저장한다")
    void acceptMaximumIntegerMp() {
        // given: MP Integer.MAX_VALUE을 준비한다.
        Wizard wizard = new Wizard();
        int mp = Integer.MAX_VALUE;

        // when: wizard.setMp()을 실행한다.
        wizard.setMp(mp);

        // then: MP에 Integer.MAX_VALUE을 설정하면 예외 없이 그대로 저장한다
        Assertions.assertEquals(mp, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] 음수 MP 거부 후 기존 MP 유지")
    void rejectNegativeMp() {
        // given: wizard = new Wizard()을 준비한다.
        Wizard wizard = new Wizard();
        wizard.setMp(10);

        // when: wizard.setMp()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.setMp(-1));

        // then: 음수 MP 거부 후 기존 MP 유지
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals(10, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] 지팡이 참조를 장착한다")
    void equipWand() {
        // given: wizard = new Wizard(), wand = new Wand()을 준비한다.
        Wizard wizard = new Wizard();
        Wand wand = new Wand();

        // when: wizard.setWand()을 실행한다.
        wizard.setWand(wand);

        // then: 지팡이 참조를 장착한다
        Assertions.assertSame(wand, wizard.getWand());
    }

    @Test
    @DisplayName("[Wizard] null 지팡이 거부 후 기존 지팡이 유지")
    void rejectNullWand() {
        // given: wizard = new Wizard(), wand = new Wand()을 준비한다.
        Wizard wizard = new Wizard();
        Wand wand = new Wand();
        wizard.setWand(wand);

        // when: wizard.setWand()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.setWand(null));

        // then: null 지팡이 거부 후 기존 지팡이 유지
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertSame(wand, wizard.getWand());
    }

    @Test
    @DisplayName("[Wizard] 마력 0.5로 HP 50인 용사를 회복하면 5 증가해 55가 되고 MP 0은 유지된다")
    void healWithMinimumPower() {
        // given: wizard = new Wizard(), wand = new Wand(), hero = new Hero()을 준비한다.
        Wizard wizard = new Wizard();
        Wand wand = new Wand();
        wand.setPower(0.5);
        wizard.setWand(wand);
        wizard.setMp(0);
        Hero hero = new Hero();
        hero.setHp(50);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 마력 0.5로 HP 50인 용사를 회복하면 5 증가해 55가 되고 MP 0은 유지된다
        Assertions.assertEquals(55, hero.getHp());
        Assertions.assertEquals(0, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] 마력 1.25로 HP 50인 용사를 회복하면 12 증가해 62가 되고 MP 0은 유지된다")
    void healWithFractionalPower() {
        // given: wizard = new Wizard(), wand = new Wand(), hero = new Hero()을 준비한다.
        Wizard wizard = new Wizard();
        Wand wand = new Wand();
        wand.setPower(1.25);
        wizard.setWand(wand);
        wizard.setMp(0);
        Hero hero = new Hero();
        hero.setHp(50);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 마력 1.25로 HP 50인 용사를 회복하면 12 증가해 62가 되고 MP 0은 유지된다
        Assertions.assertEquals(62, hero.getHp());
        Assertions.assertEquals(0, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] 마력 100.0로 HP 50인 용사를 회복하면 1000 증가해 1050가 되고 MP 0은 유지된다")
    void healWithMaximumPower() {
        // given: wizard = new Wizard(), wand = new Wand(), hero = new Hero()을 준비한다.
        Wizard wizard = new Wizard();
        Wand wand = new Wand();
        wand.setPower(100.0);
        wizard.setWand(wand);
        wizard.setMp(0);
        Hero hero = new Hero();
        hero.setHp(50);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 마력 100.0로 HP 50인 용사를 회복하면 1000 증가해 1050가 되고 MP 0은 유지된다
        Assertions.assertEquals(1050, hero.getHp());
        Assertions.assertEquals(0, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] 지팡이 미설정 상태에서 회복하면 명확한 예외를 낸다")
    void rejectMissingWand() {
        // given: wizard = new Wizard(), hero = new Hero()을 준비한다.
        Wizard wizard = new Wizard();
        Hero hero = new Hero();
        hero.setHp(50);

        // when: wizard.heal()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalStateException error = Assertions.assertThrows(IllegalStateException.class,
                () -> wizard.heal(hero));

        // then: 지팡이 미설정 상태에서 회복하면 명확한 예외를 낸다
        Assertions.assertNotNull(error.getMessage());
        Assertions.assertEquals(50, hero.getHp());
    }

    @Test
    @DisplayName("[Wizard] null 회복 대상을 거부한다")
    void rejectNullHero() {
        // given: wizard = new Wizard()을 준비한다.
        Wizard wizard = new Wizard();

        // when: wizard.heal()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> wizard.heal(null));

        // then: null 회복 대상을 거부한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Wizard] 회복 중 int 오버플로가 발생하면 HP를 변경하지 않는다")
    void rejectHpOverflow() {
        // given: wizard = new Wizard(), wand = new Wand(), hero = new Hero()을 준비한다.
        Wizard wizard = new Wizard();
        Wand wand = new Wand();
        wand.setPower(0.5);
        wizard.setWand(wand);
        Hero hero = new Hero();
        hero.setHp(Integer.MAX_VALUE);

        // when: wizard.heal()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(ArithmeticException.class, () -> wizard.heal(hero));

        // then: 회복 중 int 오버플로가 발생하면 HP를 변경하지 않는다
        Assertions.assertEquals(Integer.MAX_VALUE, hero.getHp());
    }

    // 게임 사용 흐름, 상태 전이, 객체 관계와 실패 후 재시도 검사

    @Test
    @DisplayName("[Wizard] 유효한 이름의 앞뒤 공백과 가운데 공백은 저장 시 유지된다")
    void preserveWhitespaceName() {
        // given: 이름 "  홍 길동  "을 준비한다.
        String name = "  홍 길동  ";
        Wizard subject = new Wizard();

        // when: subject.setName()을 실행한다.
        subject.setName(name);

        // then: 유효한 이름의 앞뒤 공백과 가운데 공백은 저장 시 유지된다
        Assertions.assertEquals(name, subject.getName());
    }

    @Test
    @DisplayName("[Wizard] 영문 이름을 사용할 수 있다")
    void preserveEnglishName() {
        // given: 이름 "Merlin"을 준비한다.
        String name = "Merlin";
        Wizard subject = new Wizard();

        // when: subject.setName()을 실행한다.
        subject.setName(name);

        // then: 영문 이름을 사용할 수 있다
        Assertions.assertEquals(name, subject.getName());
    }

    @Test
    @DisplayName("[Wizard] 같은 용사를 세 번 회복하면 회복량이 누적된다")
    void repeatedHealingAccumulates() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);
        wizard.heal(hero);
        wizard.heal(hero);

        // then: 같은 용사를 세 번 회복하면 회복량이 누적된다
        Assertions.assertEquals(100, hero.getHp());
        Assertions.assertEquals(30, wizard.getMp());
        Assertions.assertEquals(80, wizard.getHp());
    }

    @Test
    @DisplayName("[Wizard] 파티원 중 선택한 용사만 회복한다")
    void healOnlySelectedPartyMember() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), other = createHero("동료", 70)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Hero other = createHero("동료", 70);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 파티원 중 선택한 용사만 회복한다
        Assertions.assertEquals(60, hero.getHp());
        Assertions.assertEquals(70, other.getHp());
        Assertions.assertEquals("용사", hero.getName());
        Assertions.assertEquals("동료", other.getName());
    }

    @Test
    @DisplayName("[Wizard] 한 마법사가 여러 용사를 차례대로 회복한다")
    void healMultiplePartyMembers() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), other = createHero("동료", 70)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Hero other = createHero("동료", 70);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);
        wizard.heal(other);

        // then: 한 마법사가 여러 용사를 차례대로 회복한다
        Assertions.assertEquals(60, hero.getHp());
        Assertions.assertEquals(90, other.getHp());
        Assertions.assertEquals(30, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] 두 마법사가 같은 용사를 회복하면 각각의 마력이 적용된다")
    void twoWizardsHealSameHero() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), other = createWizard(80, 30, 3.0)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Wizard other = createWizard(80, 30, 3.0);

        // when: wizard.heal(), other.heal()을 실행한다.
        wizard.heal(hero);
        other.heal(hero);

        // then: 두 마법사가 같은 용사를 회복하면 각각의 마력이 적용된다
        Assertions.assertEquals(90, hero.getHp());
        Assertions.assertEquals(30, wizard.getMp());
        Assertions.assertEquals(30, other.getMp());
    }

    @Test
    @DisplayName("[Wizard] 전투 중 지팡이를 교체하면 다음 회복부터 새 마력이 적용된다")
    void replaceWandBetweenHeals() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), oldWand = wizard.getWand(), newWand = createWand(5.0)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Wand oldWand = wizard.getWand();
        Wand newWand = createWand(5.0);

        // when: wizard.heal(), wizard.setWand()을 실행한다.
        wizard.heal(hero);
        wizard.setWand(newWand);
        wizard.heal(hero);

        // then: 전투 중 지팡이를 교체하면 다음 회복부터 새 마력이 적용된다
        Assertions.assertEquals(110, hero.getHp());
        Assertions.assertSame(newWand, wizard.getWand());
        Assertions.assertEquals(2.0, oldWand.getPower());
    }

    @Test
    @DisplayName("[Wizard] 장착한 지팡이를 강화하면 다음 회복에 즉시 반영된다")
    void upgradeEquippedWand() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);
        wizard.getWand().setPower(4.0);
        wizard.heal(hero);

        // then: 장착한 지팡이를 강화하면 다음 회복에 즉시 반영된다
        Assertions.assertEquals(100, hero.getHp());
        Assertions.assertEquals(4.0, wizard.getWand().getPower());
    }

    @Test
    @DisplayName("[Wizard] 같은 지팡이를 공유하는 두 마법사는 강화 결과도 공유한다")
    void sharedWandUpgradeAffectsBothWizards() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), other = createWizard(80, 30, 1.0), otherHero = createHero("동료", 40)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Wizard other = createWizard(80, 30, 1.0);
        other.setWand(wizard.getWand());
        Hero otherHero = createHero("동료", 40);

        // when: wizard.heal(), other.heal()을 실행한다.
        wizard.getWand().setPower(3.0);
        wizard.heal(hero);
        other.heal(otherHero);

        // then: 같은 지팡이를 공유하는 두 마법사는 강화 결과도 공유한다
        Assertions.assertSame(wizard.getWand(), other.getWand());
        Assertions.assertEquals(70, hero.getHp());
        Assertions.assertEquals(70, otherHero.getHp());
    }

    @Test
    @DisplayName("[Wizard] 교체 후 예전 지팡이를 강화해도 현재 회복량은 변하지 않는다")
    void unequippedWandChangesDoNotAffectHealing() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), oldWand = wizard.getWand()을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Wand oldWand = wizard.getWand();
        wizard.setWand(createWand(3.0));

        // when: oldWand.setPower(), wizard.heal()을 실행한다.
        oldWand.setPower(100.0);
        wizard.heal(hero);

        // then: 교체 후 예전 지팡이를 강화해도 현재 회복량은 변하지 않는다
        Assertions.assertEquals(70, hero.getHp());
        Assertions.assertNotSame(oldWand, wizard.getWand());
    }

    @Test
    @DisplayName("[Wizard] null 장착에 실패해도 기존 지팡이로 회복할 수 있다")
    void failedUnequipKeepsUsableWand() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), original = wizard.getWand()을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Wand original = wizard.getWand();

        // when: wizard.setWand(), wizard.heal()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(IllegalArgumentException.class, () -> wizard.setWand(null));
        wizard.heal(hero);

        // then: null 장착에 실패해도 기존 지팡이로 회복할 수 있다
        Assertions.assertSame(original, wizard.getWand());
        Assertions.assertEquals(60, hero.getHp());
    }

    @Test
    @DisplayName("[Wizard] 지팡이 없이 실패한 뒤 장착하고 재시도하면 회복된다")
    void equipAfterFailedHeal() {
        // given: wizard = new Wizard(), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = new Wizard();
        Hero hero = createHero("용사", 40);

        // when: wizard.heal(), wizard.setWand()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(IllegalStateException.class, () -> wizard.heal(hero));
        Assertions.assertEquals(40, hero.getHp()); // 실패 시 대상 상태 보존
        wizard.setWand(createWand(2.0));
        wizard.heal(hero);

        // then: 지팡이 없이 실패한 뒤 장착하고 재시도하면 회복된다
        Assertions.assertEquals(60, hero.getHp());
    }

    @Test
    @DisplayName("[Wizard] null 대상으로 실패한 뒤 정상 대상을 회복할 수 있다")
    void nullTargetDoesNotDamageCaster() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), original = wizard.getWand()을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Wand original = wizard.getWand();

        // when: wizard.heal()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(IllegalArgumentException.class, () -> wizard.heal(null));
        Assertions.assertEquals(80, wizard.getHp());
        Assertions.assertEquals(30, wizard.getMp());
        wizard.heal(hero);

        // then: null 대상으로 실패한 뒤 정상 대상을 회복할 수 있다
        Assertions.assertEquals(60, hero.getHp());
        Assertions.assertSame(original, wizard.getWand());
        Assertions.assertEquals("마법사", wizard.getName());
    }

    @Test
    @DisplayName("[Wizard] HP 오버플로 실패 후 다른 용사는 정상 회복할 수 있다")
    void overflowFailureDoesNotPreventLaterHeal() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40), full = createHero("고체력용사", Integer.MAX_VALUE)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        Hero full = createHero("고체력용사", Integer.MAX_VALUE);

        // when: wizard.heal()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(ArithmeticException.class, () -> wizard.heal(full));
        wizard.heal(hero);

        // then: HP 오버플로 실패 후 다른 용사는 정상 회복할 수 있다
        Assertions.assertEquals(Integer.MAX_VALUE, full.getHp());
        Assertions.assertEquals(60, hero.getHp());
        Assertions.assertEquals(80, wizard.getHp());
        Assertions.assertEquals(30, wizard.getMp());
        Assertions.assertEquals(2.0, wizard.getWand().getPower());
    }

    @Test
    @DisplayName("[Wizard] 현재 규칙: HP 0인 용사도 회복 대상이 된다")
    void healAtZeroHpCharacterization() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 0)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 0);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 현재 규칙: HP 0인 용사도 회복 대상이 된다
        Assertions.assertEquals(20, hero.getHp());
    }

    @Test
    @DisplayName("[Wizard] 현재 규칙: 마법사 HP가 0이어도 회복을 시전한다")
    void zeroHpCasterCanHealCharacterization() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        wizard.setHp(0);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 현재 규칙: 마법사 HP가 0이어도 회복을 시전한다
        Assertions.assertEquals(60, hero.getHp());
        Assertions.assertEquals(0, wizard.getHp());
    }

    @Test
    @DisplayName("[Wizard] 현재 규칙: sleep의 HP 200은 회복 상한이 아니다")
    void healingHasNoTwoHundredHpCap() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);
        hero.sleep();

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 현재 규칙: sleep의 HP 200은 회복 상한이 아니다
        Assertions.assertEquals(220, hero.getHp());
    }

    @Test
    @DisplayName("[Wizard] 현재 한계: 마력을 설정하지 않은 지팡이는 0만큼 회복한다")
    void uninitializedWandHealsZeroCharacterization() {
        // given: wizard = new Wizard(), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = new Wizard();
        wizard.setWand(new Wand());
        Hero hero = createHero("용사", 40);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 현재 한계: 마력을 설정하지 않은 지팡이는 0만큼 회복한다
        Assertions.assertEquals(40, hero.getHp());
        Assertions.assertEquals(0.0, wizard.getWand().getPower());
    }

    @Test
    @DisplayName("[Wizard] 피해량이 현재 HP보다 크면 0으로 보정되고 이후 HP 설정으로 회복된다")
    void damageThenRestoreHp() {
        // given: wizard = createWizard(80, 30, 2.0)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);

        // when: wizard.setHp()을 실행한다.
        wizard.setHp(wizard.getHp() - 100);
        Assertions.assertEquals(0, wizard.getHp()); // 쓰러진 상태로 전이
        wizard.setHp(50);

        // then: 피해량이 현재 HP보다 크면 0으로 보정되고 이후 HP 설정으로 회복된다
        Assertions.assertEquals(50, wizard.getHp());
        Assertions.assertEquals(30, wizard.getMp());
        Assertions.assertEquals(2.0, wizard.getWand().getPower());
    }

    @Test
    @DisplayName("[Wizard] 한 마법사의 이름과 HP 및 MP를 바꿔도 다른 마법사는 그대로다")
    void separateWizardsKeepIndependentState() {
        // given: first = createWizard(80, 30, 2.0), second = createWizard(80, 30, 3.0)을 준비한다.
        Wizard first = createWizard(80, 30, 2.0);
        Wizard second = createWizard(80, 30, 3.0);

        // when: first.setName(), first.setHp(), first.setMp()을 실행한다.
        first.setName("대마법사");
        first.setHp(5);
        first.setMp(0);

        // then: 한 마법사의 이름과 HP 및 MP를 바꿔도 다른 마법사는 그대로다
        Assertions.assertEquals("마법사", second.getName());
        Assertions.assertEquals(80, second.getHp());
        Assertions.assertEquals(30, second.getMp());
        Assertions.assertEquals(3.0, second.getWand().getPower());
        Assertions.assertNotSame(first.getWand(), second.getWand());
    }

    @Test
    @DisplayName("[Wizard] 동일한 HP와 MP를 재설정하면 기존 값에 더하지 않는다")
    void repeatedSetterDoesNotAccumulate() {
        // given: wizard = createWizard(80, 30, 2.0)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);

        // when: wizard.setHp(), wizard.setMp()을 실행한다.
        wizard.setHp(80);
        wizard.setMp(30);

        // then: 동일한 HP와 MP를 재설정하면 기존 값에 더하지 않는다
        Assertions.assertEquals(80, wizard.getHp());
        Assertions.assertEquals(30, wizard.getMp());
    }

    @Test
    @DisplayName("[Wizard] 소수 회복량은 매 시전마다 버리며 여러 회복 후 합산하지 않는다")
    void roundEachHealSeparately() {
        // given: wizard = createWizard(80, 30, 1.25), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = createWizard(80, 30, 1.25);
        Hero hero = createHero("용사", 40);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);
        wizard.heal(hero);

        // then: 소수 회복량은 매 시전마다 버리며 여러 회복 후 합산하지 않는다
        Assertions.assertEquals(64, hero.getHp()); // 12 + 12이며 합산 후 반올림한 25가 아니다.
    }

    @Test
    @DisplayName("[Wizard] 정수 최댓값에 정확히 도달하는 회복은 성공한다")
    void healExactlyToMaximumInteger() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", Integer.MAX_VALUE - 20)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", Integer.MAX_VALUE - 20);

        // when: wizard.heal()을 실행한다.
        wizard.heal(hero);

        // then: 정수 최댓값에 정확히 도달하는 회복은 성공한다
        Assertions.assertEquals(Integer.MAX_VALUE, hero.getHp());
    }

    @Test
    @DisplayName("[Wizard] 이름 변경 실패는 HP와 MP 및 장비에 영향을 주지 않는다")
    void invalidRenameDoesNotChangeCombatState() {
        // given: wizard = createWizard(80, 30, 2.0), wand = wizard.getWand()을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Wand wand = wizard.getWand();

        // when: wizard.setName()을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(IllegalArgumentException.class, () -> wizard.setName("가"));

        // then: 이름 변경 실패는 HP와 MP 및 장비에 영향을 주지 않는다
        Assertions.assertEquals("마법사", wizard.getName());
        Assertions.assertEquals(80, wizard.getHp());
        Assertions.assertEquals(30, wizard.getMp());
        Assertions.assertSame(wand, wizard.getWand());
    }

    @Test
    @DisplayName("[Wizard] 마법사와 장비 이름을 변경해도 회복량은 변하지 않는다")
    void renameThenHealKeepsPower() {
        // given: wizard = createWizard(80, 30, 2.0), hero = createHero("용사", 40)을 준비한다.
        Wizard wizard = createWizard(80, 30, 2.0);
        Hero hero = createHero("용사", 40);

        // when: wizard.setName(), wizard.heal()을 실행한다.
        wizard.setName("대마법사");
        wizard.getWand().setName("강화지팡이");
        wizard.heal(hero);

        // then: 마법사와 장비 이름을 변경해도 회복량은 변하지 않는다
        Assertions.assertEquals(60, hero.getHp());
        Assertions.assertEquals("대마법사", wizard.getName());
        Assertions.assertEquals("강화지팡이", wizard.getWand().getName());
    }

    // 여러 시나리오에서 사용하는 정상 상태를 만드는 테스트 전용 도우미다.
    private Wand createWand(double power) {
        Wand wand = new Wand();
        wand.setName("나무지팡이");
        wand.setPower(power);
        return wand;
    }

    private Wizard createWizard(int hp, int mp, double power) {
        Wizard wizard = new Wizard();
        wizard.setName("마법사");
        wizard.setHp(hp);
        wizard.setMp(mp);
        wizard.setWand(createWand(power));
        return wizard;
    }

    private Hero createHero(String name, int hp) {
        Hero hero = new Hero();
        hero.name = name; // 현재 Hero는 같은 패키지에서 이름을 설정하는 수업용 구조다.
        hero.setHp(hp);
        return hero;
    }
}
