package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 11장 "상속" 개념 자체를 검증하는 테스트.
 * <p>
 * 개별 클래스 테스트(ClassNameTest) 가 "각 클래스가 기대대로 동작하는가" 를 확인한다면,
 * 이 테스트는 "상속 관계가 의도한 대로 성립했는가" 를 확인한다.
 * 즉, 런타임 타입 관계와 메서드 호출 규칙이 깨지지 않았는지 감시하는 안전망 역할을 한다.
 * <p>
 * 검증하는 개념
 * <ul>
 *     <li><b>is-a 관계</b> : 자식 인스턴스는 부모 타입으로 취급될 수 있어야 한다</li>
 *     <li><b>is-a 가 아닌 관계</b> : 남의 부모 타입으로 취급되면 안 된다</li>
 *     <li><b>super 생성자 호출</b> : 자식 생성자가 부모 필드를 초기화하는지</li>
 *     <li><b>오버라이드</b> : 부모 타입 참조로 자식 메서드가 호출되는지(동적 바인딩)</li>
 *     <li><b>다형성</b> : 서로 다른 타입을 한 컬렉션에 담아 같은 메서드를 호출해도
 *         각자 다르게 동작하는지</li>
 * </ul>
 */
class InheritanceTest {

    /**
     * {@code System.out} 을 잠시 가로채어 출력된 내용을 돌려주는 헬퍼.
     *
     * @param action 출력을 발생시킬 동작
     * @return 해당 동작이 출력한 문자열
     */
    private String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            action.run();
        } finally {
            System.setOut(original);
        }

        return buffer.toString(StandardCharsets.UTF_8);
    }

    // ==================== is-a 관계 ====================

    @Test
    @DisplayName("SuperHero 는 Hero 이다")
    void superHeroIsAHero() {
        SuperHero superHero = new SuperHero("한석봉", 100);

        assertInstanceOf(Hero.class, superHero);
    }

    @Test
    @DisplayName("PoisonSlime 은 Slime 이다")
    void poisonSlimeIsASlime() {
        PoisonSlime poisonSlime = new PoisonSlime("독슬라임");

        assertInstanceOf(Slime.class, poisonSlime);
    }

    @Test
    @DisplayName("GreatWizard 는 Wizard 이다")
    void greatWizardIsAWizard() {
        GreatWizard greatWizard = new GreatWizard("신초");

        assertInstanceOf(Wizard.class, greatWizard);
    }

    // ==================== is-a 가 아닌 관계 ====================

    @Test
    @DisplayName("부모 타입으로는 자식 타입을 담을 수 없다")
    void childCannotBeAssignedToUnrelatedParent() {
        // 컴파일러는 타입 계층을 정적으로 알고 있어서
        // `king instanceof Hero` 같은 표현을 컴파일 에러로 막는다.
        // (형 변환 불가능한 타입 — incompatible types)
        // 그래야 정적 타입 안전성이 유지된다.
        // 컴파일 타임에 막히는 것 자체가 "절대 같은 타입이 될 수 없다"는 증거지만,
        // 여기서는 런타임 값으로도 확인하려고 Object 로 한 단계 올려 검사한다.
        Object king = new King();
        Object wizard = new Wizard("마법사", 100, 100);

        assertFalse(king instanceof Hero);
        assertFalse(wizard instanceof Hero);
    }

    @Test
    @DisplayName("sibling 관계인 두 자식 타입은 서로의 부모 타입이 아니다")
    void siblingsAreNotRelatedToEachOther() {
        // SuperHero 와 PoisonSlime 은 모두 Hero 를 상속하지만 서로 형제 관계이다.
        // Object 로 올린 뒤 검사해야 컴파일 에러를 피할 수 있다.
        Object superHero = new SuperHero("한석봉", 100);
        Object poisonSlime = new PoisonSlime("독슬라임");

        assertFalse(superHero instanceof Slime);
        assertFalse(poisonSlime instanceof SuperHero);
    }

    // ==================== super 생성자 호출 ====================

    @Test
    @DisplayName("자식 생성자는 super(...) 를 통해 부모 필드를 초기화한다")
    void childConstructorInitializesParentFields() {
        SuperHero superHero = new SuperHero("한석봉", 77);

        // SuperHero 생성자는 super(name, hp) 를 호출하므로,
        // Hero 에 선언된 private 필드 name/hp 가 정상적으로 채워진다.
        assertEquals("한석봉", superHero.getName());
        assertEquals(77, superHero.getHp());
    }

    @Test
    @DisplayName("자식은 부모의 private 필드를 직접 초기화할 수 없고 super 로만 접근한다")
    void childAccessesParentFieldsOnlyThroughSuper() {
        // PoisonSlime 은 Slime 이 가진 필드가 아니라, Slime 이 가진 HP 를 그대로 쓰고
        // 독 공격 횟수만 자기 필드로 추가한다. 즉 부모 상태는 super 로 넘겨받는다.
        Hero hero = new Hero("한석봉", 100);
        PoisonSlime poisonSlime = new PoisonSlime("독슬라임");

        poisonSlime.attack(hero);

        assertEquals(72, hero.getHp());
    }

    // ==================== 오버라이드 / 동적 바인딩 ====================

    @Test
    @DisplayName("부모 타입 참조로 자식의 오버라이드된 메서드가 호출된다")
    void overrideIsDispatchedThroughParentReference() {
        Hero parentTypedReference = new SuperHero("한석봉", 100);

        // 변수의 선언 타입은 Hero 인데, 실제 객체는 SuperHero 다.
        // Hero.run() 이 아니라 SuperHero.run() 이 실행되어야 한다.
        String output = captureOutput(parentTypedReference::run);

        assertTrue(output.contains("멋지게 퇴각했다"));
        assertFalse(output.contains("도망쳤다"));
        assertFalse(output.contains("GAME OVER"));
    }

    @Test
    @DisplayName("heal 은 호출한 객체의 타입에 따라 회복량이 달라진다")
    void healOverrideChangesRecoveryAmount() {
        // heal 은 대상 용사의 HP 를 바꾸는 부작용이 있으므로,
        // 대상 용사의 참조를 유지한 뒤 회복 후 HP 를 비교한다.
        Hero patientOfGreatWizard = new Hero("환자 주인공", 40);
        Hero patientOfWizard = new Hero("환자 주인공", 40);

        // 변수 타입은 Wizard 인데 담긴 객체는 GreatWizard → GreatWizard.heal 이 실행된다.
        Wizard greatWizardReference = new GreatWizard("대마법사");
        captureOutput(() -> greatWizardReference.heal(patientOfGreatWizard));

        // 같은 Wizard 타입이지만 평범한 마법사 → Wizard.heal 이 실행된다.
        Wizard plainWizardReference = new Wizard("마법사", 100, 100);
        captureOutput(() -> plainWizardReference.heal(patientOfWizard));

        // GreatWizard 는 +25, Wizard 는 +20 회복시킨다.
        assertEquals(65, patientOfGreatWizard.getHp());
        assertEquals(60, patientOfWizard.getHp());
    }

    // ==================== 다형성 ====================

    @Test
    @DisplayName("서로 다른 타입을 한 컬렉션에 담아 같은 메서드를 호출하면 각자 다르게 동작한다")
    void polymorphicDispatchAcrossCollection() {
        Hero plainHero = new Hero("평범한영웅", 100);
        Hero superHero = new SuperHero("한석봉", 100);

        // 두 변수의 타입이 모두 Hero 이지만, 담긴 객체의 실제 타입은 서로 다르다.
        List<Hero> heroes = List.of(plainHero, superHero);

        String plainOutput = captureOutput(() -> heroes.get(0).run());
        String superOutput = captureOutput(() -> heroes.get(1).run());

        // 같은 run() 호출인데 결과가 다르다. 이것이 다형성이다.
        assertTrue(plainOutput.contains("평범한영웅는 도망쳤다!"));
        assertTrue(plainOutput.contains("GAME OVER"));
        assertTrue(superOutput.contains("멋지게 퇴각했다"));
        assertFalse(superOutput.contains("GAME OVER"));
    }

    @Test
    @DisplayName("부모 타입의 메서드를 호출해도 자식이 오버라이드하지 않은 메서드는 부모 구현이 실행된다")
    void nonOverriddenMethodStillResolvesToParent() {
        // SuperHero 는 run 만 오버라이드했다. sit/slip/sleep 은 Hero 구현이 그대로 쓰인다.
        Hero superHero = new SuperHero("한석봉", 100);

        String output = captureOutput(() -> superHero.sit(10));

        assertTrue(output.contains("한석봉는 10초 앉았다"));
        assertEquals(110, superHero.getHp());
    }

    @Test
    @DisplayName("SuperHero 는 toString 을 오버라이드하고 Hero 는 하지 않는다")
    void onlySuperHeroOverridesToString() {
        Hero hero = new Hero("평범한영웅", 100);
        Hero superHero = new SuperHero("한석봉", 100);

        String plainToString = hero.toString();
        String superToString = superHero.toString();

        // Hero 에는 toString 이 없어 Object 의 기본 구현(클래스명@해시)이 쓰인다.
        // 화살표("->")가 없고 "@해시" 가 붙는다는 점으로 구분한다.
        assertTrue(plainToString.startsWith("com.survivalcoding.Hero@"));
        assertFalse(plainToString.contains("Hero{"));

        // SuperHero 만 toString 을 오버라이드해 상태를 사람이 읽는 형태로 보여준다.
        assertTrue(superToString.contains("SuperHero{"));
        assertTrue(superToString.contains("isFlying"));
    }
}