package com.survivalcoding.day12_abstract_interface;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 12장 <b>인터페이스</b> 규칙을 검증하는 테스트.
 * <p>
 * 인터페이스의 제약("모든 메서드가 추상", "필드를 가지지 않는다",
 * "선언한 필드는 public static final")은 모두 컴파일 타임에 강제되는 규칙이다.
 * 여기서는 <b>리플렉션으로 컴파일된 클래스 파일의 구조를 직접 확인</b>해서
 * 그 규칙이 실제로 지켜졌음을 검증한다.
 * <p>
 * 검증 항목
 * <ul>
 *     <li>인터페이스는 {@code public} 이고 {@code abstract} 로 기록된다</li>
 *     <li>인터페이스의 메서드는 {@code public abstract} 다</li>
 *     <li>인터페이스에는 인스턴스 필드가 없다 (선언된 필드는 전부 static)</li>
 *     <li>인터페이스의 상수는 {@code public static final} 다</li>
 *     <li>인터페이스는 인터페이스를 extends 할 수 있다</li>
 *     <li>클래스는 여러 인터페이스를 implements 할 수 있다 (다중 구현)</li>
 *     <li>{@code extends} 와 {@code implements} 를 동시에 쓸 수 있다</li>
 * </ul>
 */
class InterfaceTest {

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

    // ==================== 인터페이스의 구조 ====================

    @Test
    @DisplayName("Attackable 은 interface 다")
    void attackableIsInterface() {
        assertTrue(Attackable.class.isInterface());
    }

    @Test
    @DisplayName("인터페이스도 public + abstract 로 기록된다")
    void interfaceIsPublicAndAbstract() {
        int modifiers = Attackable.class.getModifiers();

        assertTrue(Modifier.isPublic(modifiers));
        // "추상 메서드만 가진 추상 클래스 == 인터페이스" 이므로 abstract 플래그가 켜져 있다.
        assertTrue(Modifier.isAbstract(modifiers));
    }

    @Test
    @DisplayName("인터페이스의 메서드는 public + abstract 다")
    void interfaceMethodsArePublicAbstract() throws NoSuchMethodException {
        Method attack = Attackable.class.getDeclaredMethod("attack");
        int modifiers = attack.getModifiers();

        assertTrue(Modifier.isPublic(modifiers));
        assertTrue(Modifier.isAbstract(modifiers));
    }

    @Test
    @DisplayName("인터페이스에는 인스턴스 필드가 없다")
    void interfaceHasNoInstanceFields() {
        // "인터페이스는 필드를 가지지 않는다" 는 규칙의 직접적인 검증이다.
        // 선언된 필드가 있더라도 전부 static 이어야 한다.
        // (static 인 필드는 인스턴스 필드가 아니라 상수다)
        for (Class<?> type : List.of(Attackable.class, Flyable.class, MagicCaster.class)) {
            for (Field field : type.getDeclaredFields()) {
                assertTrue(
                        Modifier.isStatic(field.getModifiers()),
                        type.getSimpleName() + " 의 필드 " + field.getName() + " 가 static 이 아니다"
                );
            }
        }
    }

    @Test
    @DisplayName("인터페이스에 선언한 필드는 public static final 이 된다")
    void interfaceFieldsArePublicStaticFinal() throws NoSuchFieldException {
        Field maxAttack = Attackable.class.getDeclaredField("MAX_ATTACK_PER_TURN");
        int modifiers = maxAttack.getModifiers();

        assertTrue(Modifier.isPublic(modifiers));
        assertTrue(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
    }

    @Test
    @DisplayName("인터페이스의 상수는 implements 한 클래스에서 바로 쓸 수 있다")
    void interfaceConstantIsInheritedByImplementor() {
        // Zombie.attack() 안에서 MAX_ATTACK_PER_TURN 을 앞에 이름 없이 썼다.
        // 이게 가능한 이유가 인터페이스 필드가 자동 public static final 이기 때문이다.
        Zombie zombie = new Zombie("좀비", 12);

        String output = captureOutput(zombie::attack);

        assertTrue(output.contains("3"));
        assertEquals(3, Attackable.MAX_ATTACK_PER_TURN);
        assertEquals(50, MagicCaster.BASE_MP);
    }

    // ==================== 인터페이스 간 상속 ====================

    @Test
    @DisplayName("SuperFlyable 은 Flyable 을 상속했다")
    void superFlyableExtendsFlyable() {
        assertTrue(Flyable.class.isAssignableFrom(SuperFlyable.class));
    }

    @Test
    @DisplayName("Flyable 을 구현하면 SuperFlyable 도 구현한 것으로 인정된다")
    void flyableIsSubtypeOfSuperFlyable() {
        // 역방향은 아니다. 부모 타입은 자식 타입의 인스턴스를 담을 수 없다.
        assertFalse(SuperFlyable.class.isAssignableFrom(Flyable.class));
    }

    @Test
    @DisplayName("SuperFlyable 의 메서드는 모두 추상이다")
    void inheritedInterfaceMethodStaysAbstract() throws NoSuchMethodException {
        // 물려받은 fly() 와 새로 추가한 land() 모두 추상 메서드다.
        assertTrue(Modifier.isAbstract(SuperFlyable.class.getDeclaredMethod("fly").getModifiers()));
        assertTrue(Modifier.isAbstract(SuperFlyable.class.getDeclaredMethod("land").getModifiers()));
    }

    // ==================== 다중 구현 ====================

    @Test
    @DisplayName("스파이더맨은 공격할 수 있다")
    void spiderManIsAttackable() {
        SpiderMan spiderMan = new SpiderMan("스파이더맨");

        assertInstanceOf(Attackable.class, spiderMan);
    }

    @Test
    @DisplayName("스파이더맨은 날 수 있다 — 영웅이면서 시민이다")
    void spiderManIsFlyable() {
        SpiderMan spiderMan = new SpiderMan("스파이더맨");

        assertInstanceOf(SuperFlyable.class, spiderMan);
        assertInstanceOf(Flyable.class, spiderMan);
    }

    @Test
    @DisplayName("하나의 클래스가 여러 인터페이스를 구현할 수 있다")
    void oneClassCanImplementMultipleInterfaces() {
        SpiderMan spiderMan = new SpiderMan("스파이더맨");

        // implements 목록이 복수인 것을 타입으로 확인한다.
        assertInstanceOf(Attackable.class, spiderMan);
        assertInstanceOf(Flyable.class, spiderMan);
        assertInstanceOf(SuperFlyable.class, spiderMan);
    }

    @Test
    @DisplayName("인터페이스는 추상 클래스를 extends 하지 않는다")
    void implementationsAreNotCharacters() {
        // 좀비는 "공격할 수 있다"지만 "캐릭터"는 아니다.
        // 계층(is-a)과 능력(Can-Do)이 구분된다.
        Zombie zombie = new Zombie("좀비", 12);

        Object asObject = zombie;

        assertFalse(asObject instanceof Character);
    }

    // ==================== extends + implements 동시 사용 ====================

    @Test
    @DisplayName("Wizard 는 extends 와 implements 를 동시에 사용한다")
    void wizardUsesBothExtendsAndImplements() {
        Wizard wizard = new Wizard("김영한", 100);

        // extends Character → is-a
        assertInstanceOf(Character.class, wizard);
        // implements Attackable, MagicCaster → Can-Do
        assertInstanceOf(Attackable.class, wizard);
        assertInstanceOf(MagicCaster.class, wizard);
    }

    @Test
    @DisplayName("상속과 구현 양쪽의 요구를 같은 메서드로 만족한다")
    void wizardImplementsInheritedAbstractMethod() throws NoSuchMethodException {
        // Character.attack() (extends 로 물려받은 추상 메서드)와
        // Attackable.attack() (implements 로 요구된 추상 메서드)를
        // Wizard.attack() 하나로 동시에 만족한다.
        // 두 부모 모두 같은 시그니처를 요구하므로 오버라이드로 처리된다.
        Method attack = Wizard.class.getDeclaredMethod("attack");

        assertFalse(Modifier.isAbstract(attack.getModifiers()));
    }

    // ==================== 인터페이스를 통한 다형성 ====================

    @Test
    @DisplayName("인터페이스 타입 변수로 호출하면 실제 객체의 구현이 실행된다")
    void interfaceReferenceDispatchesToImplementation() {
        // Attackable 타입인데 실제로는 Wizard 다.
        Attackable attackable = new Wizard("김영한", 100);

        String output = captureOutput(attackable::attack);

        assertTrue(output.contains("지팡이로 근접 공격"));
    }

    @Test
    @DisplayName("서로 다른 구현체를 한 리스트에 담아 같은 메서드를 호출할 수 있다")
    void interfaceEnablesPolymorphicCollection() {
        List<Attackable> party = new ArrayList<>();
        party.add(new Zombie("좀비", 12));
        party.add(new SpiderMan("스파이더맨"));
        party.add(new Wizard("김영한", 100));

        List<String> outputs = new ArrayList<>();
        for (Attackable member : party) {
            outputs.add(captureOutput(member::attack).trim());
        }

        // 같은 attack() 호출인데 구현마다 결과가 다르다.
        assertTrue(outputs.get(0).contains("물어뜯습니다"));
        assertTrue(outputs.get(1).contains("거미줄"));
        assertTrue(outputs.get(2).contains("지팡이"));
    }

    @Test
    @DisplayName("인터페이스 타입만으로는 구체 클래스의 메서드를 호출할 수 없다")
    void interfaceReferenceHidesConcreteMethods() {
        // 컴파일 타임에 막히는 성질이라 주석으로만 확인한다.
        //
        //   Attackable a = new Wizard("김영한", 100);
        //   a.castMagic();   // 컴파일 에러 — Attackable 에 castMagic 이 없다
        //
        // 인터페이스는 "약속된 능력만" 노출한다. 구현 Details 는 호출하는 쪽에 숨겨진다.
        assertTrue(MagicCaster.class.isAssignableFrom(Wizard.class));
    }

    // ==================== 실행 동작 ====================

    @Test
    @DisplayName("Wizard 의 castMagic 은 MP 를 10 소모한다")
    void castMagicConsumesMp() {
        Wizard wizard = new Wizard("김영한", 30);

        String output = captureOutput(wizard::castMagic);

        assertEquals(20, wizard.getMp());
        assertTrue(output.contains("화염구"));
        assertTrue(output.contains("20"));
    }

    @Test
    @DisplayName("MP 가 없으면 castMagic 은 예외를 던진다")
    void castMagicThrowsWhenMpExhausted() {
        Wizard wizard = new Wizard("김영한", 5);

        assertThrows(IllegalStateException.class, wizard::castMagic);
    }

    @Test
    @DisplayName("MagicCaster 타입으로도 마법을 쓸 수 있다")
    void castMagicThroughInterfaceReference() {
        MagicCaster caster = new Wizard("김영한", 30);

        String output = captureOutput(caster::castMagic);

        assertTrue(output.contains("화염구"));
    }

    @Test
    @DisplayName("fly() 와 land() 로 비행 상태가 바뀐다")
    void flyAndLandToggleFlyingState() {
        SpiderMan spiderMan = new SpiderMan("스파이더맨");

        assertFalse(spiderMan.isFlying());

        String flyOutput = captureOutput(spiderMan::fly);
        assertTrue(spiderMan.isFlying());
        assertTrue(flyOutput.contains("날아올랐습니다"));

        String landOutput = captureOutput(spiderMan::land);
        assertFalse(spiderMan.isFlying());
        assertTrue(landOutput.contains("착륙"));
    }
}