package com.survivalcoding.day12_abstract_interface;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 12장 <b>추상 클래스</b> 규칙을 검증하는 테스트.
 * <p>
 * 추상 클래스의 제약 두 가지("오버라이드 강제", "인스턴스화 금지")는
 * <b>컴파일 타임에</b> 동작하므로 실행 중에는 확인할 수 없다.
 * 그 제약이 실제로 걸려 있는지 대신 <b>클래스 파일의 구조 자체</b>를 리플렉션으로 확인한다.
 * <p>
 * 검증 항목
 * <ul>
 *     <li>{@code Character} 가 abstract 로 선언되어 있는가</li>
 *     <li>{@code attack()} 이 abstract 로 선언되어 있는가</li>
 *     <li>{@code Character} 는 public 인가 (다른 패키지에서 상속하려면 public 이어야 한다)</li>
 *     <li>{@code name} 필드가 private + final 인가</li>
 *     <li>다계층 상속 — {@code AdvancedMonster} 가 {@code Monster}, {@code Character} 이다</li>
 *     <li>상속 계층을 따라 내려가도 {@code getName()} 이 조부모 필드를 읽는다</li>
 *     <li>같은 메서드 호출이 실제 타입에 따라 다르게 실행된다</li>
 * </ul>
 */
class AbstractClassTest {

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

    // ==================== 추상 클래스의 구조 ====================

    @Test
    @DisplayName("Character 는 abstract 로 선언되어 있다")
    void characterIsAbstract() {
        // abstract 여부가 컴파일 에러를 유발하므로 컴파일 타임에 강제된다.
        // 여기서는 "진짜 abstract 로 기록되어 있는가" 를 파일 구조에서 확인한다.
        assertTrue(Modifier.isAbstract(Character.class.getModifiers()));
    }

    @Test
    @DisplayName("Character 는 다른 패키지에서 상속할 수 있도록 public 이다")
    void characterIsPublic() {
        // abstract 인데 package-private 면 다른 패키지에서 상속 자체가 불가능해진다.
        assertTrue(Modifier.isPublic(Character.class.getModifiers()));
    }

    @Test
    @DisplayName("attack() 은 abstract 로 선언되어 있다")
    void attackIsAbstractMethod() throws NoSuchMethodException {
        Method attack = Character.class.getDeclaredMethod("attack");

        assertTrue(Modifier.isAbstract(attack.getModifiers()));
        assertTrue(Modifier.isPublic(attack.getModifiers()));
        assertEquals(void.class, attack.getReturnType());
    }

    @Test
    @DisplayName("추상 메서드는 본문 없이 세미콜론으로 끝난다")
    void abstractMethodHasNoBody() throws NoSuchMethodException {
        // 리플렉션으로 실제 메서드 선언이 존재하는지 확인한다.
        // 본문이 비어 있는지는 소스 수준에서만 보이지만,
        // abstract 플래그가 있으면 JVM 은 호출 시 AbstractMethodError 로 막아준다.
        Method attack = Character.class.getDeclaredMethod("attack");

        assertTrue(Modifier.isAbstract(attack.getModifiers()));
    }

    @Test
    @DisplayName("name 필드는 private + final 이다")
    void nameFieldIsPrivateAndFinal() throws NoSuchFieldException {
        Field name = Character.class.getDeclaredField("name");

        assertTrue(Modifier.isPrivate(name.getModifiers()));
        assertTrue(Modifier.isFinal(name.getModifiers()));
    }

    // ==================== 자식 클래스는 추상이 아니다 ====================

    @Test
    @DisplayName("Monster 와 AdvancedMonster 은 abstract 가 아니다 — 인스턴스화할 수 있는 클래스")
    void concreteClassesAreNotAbstract() {
        // 추상 클래스를 상속해 attack() 을 구현했으므로 이제 new 로 만들 수 있다.
        assertFalse(Modifier.isAbstract(Monster.class.getModifiers()));
        assertFalse(Modifier.isAbstract(AdvancedMonster.class.getModifiers()));
    }

    @Test
    @DisplayName("Monster 는 attack() 을 직접 구현했다")
    void monsterImplementsAttack() throws NoSuchMethodException {
        // Character 가 선언한 추상 메서드를 Monster 가 새로 정의했는지 확인한다.
        Method attack = Monster.class.getDeclaredMethod("attack");

        assertFalse(Modifier.isAbstract(attack.getModifiers()));
        assertTrue(Modifier.isPublic(attack.getModifiers()));
    }

    @Test
    @DisplayName("추상 클래스를 상속한 클래스는 인스턴스를 만들 수 있다")
    void concreteClassCanBeInstantiated() {
        Monster monster = new Monster("고블린", 30, 7);
        AdvancedMonster advanced = new AdvancedMonster("오크 전사", 100, 15, 300);

        assertEquals("고블린", monster.getName());
        assertEquals("오크 전사", advanced.getName());
    }

    // ==================== 다계층 상속 ====================

    @Test
    @DisplayName("AdvancedMonster 는 Monster 이다")
    void advancedMonsterIsAMonster() {
        AdvancedMonster advanced = new AdvancedMonster("오크 전사", 100, 15, 300);

        assertInstanceOf(Monster.class, advanced);
    }

    @Test
    @DisplayName("AdvancedMonster 는 Character 이다 — 3단계 계층")
    void advancedMonsterIsACharacter() {
        AdvancedMonster advanced = new AdvancedMonster("오크 전사", 100, 15, 300);

        assertInstanceOf(Character.class, advanced);
    }

    @Test
    @DisplayName("다계층 상속에서도 조부모 필드에 접근할 수 있다")
    void grandParentFieldIsReachableThroughGetter() {
        // Character.name 은 private 이므로 직접 접근이 안 되고,
        // Character.getName() 이라는 getter 를 통해 읽는다. 이 메서드는 3번째 상속에도 유효하다.
        AdvancedMonster advanced = new AdvancedMonster("오크 전사", 100, 15, 300);

        assertEquals("오크 전사", advanced.getName());
    }

    @Test
    @DisplayName("다계층 상속에서 중간 계층의 메서드를 물려받는다")
    void inheritedMethodFromMiddleTier() {
        // takeDamage() 는 Monster 에 정의되어 있고 AdvancedMonster 는 이를 재정의하지 않았다.
        AdvancedMonster advanced = new AdvancedMonster("오크 전사", 20, 15, 300);

        advanced.takeDamage(5);

        assertEquals(15, advanced.getHp());
    }

    // ==================== 실행 동작 ====================

    @Test
    @DisplayName("Monster 의 attack() 출력")
    void monsterAttackOutput() {
        Monster monster = new Monster("고블린", 30, 7);

        String output = captureOutput(monster::attack);

        assertTrue(output.contains("고블린"));
        assertTrue(output.contains("30"));
        assertTrue(output.contains("공격했습니다"));
    }

    @Test
    @DisplayName("같은 attack() 호출이 실제 타입에 따라 다르게 실행된다")
    void attackIsPolymorphicAcrossHierarchy() {
        Monster monster = new Monster("고블린", 30, 7);
        AdvancedMonster advanced = new AdvancedMonster("오크 전사", 100, 15, 300);

        String monsterOutput = captureOutput(monster::attack);
        String advancedOutput = captureOutput(advanced::attack);

        // Monster 와 AdvancedMonster 는 같은 시그니처지만 내용이 다르다.
        assertTrue(monsterOutput.contains("체력으로"));
        assertFalse(monsterOutput.contains("[고도화]"));
        assertTrue(advancedOutput.contains("[고도화]"));
    }

    @Test
    @DisplayName("부모 타입으로 참조해도 자식의 attack() 이 실행된다")
    void parentReferenceDispatchesToChildImplementation() {
        // 변수의 타입은 Character 인데 실제 객체는 AdvancedMonster 다.
        Character reference = new AdvancedMonster("오크 전사", 100, 15, 300);

        String output = captureOutput(reference::attack);

        assertTrue(output.contains("[고도화]"));
    }

    @Test
    @DisplayName("takeDamage 는 HP 를 0 아래로 내리지 않는다")
    void takeDamageFloorsAtZero() {
        Monster monster = new Monster("고블린", 3, 7);

        monster.takeDamage(10);

        assertEquals(0, monster.getHp());
    }

    @Test
    @DisplayName("die() 는 HP 를 0으로 깎고 보상 경험치를 출력한다")
    void dieConsumesHpAndRewards() {
        AdvancedMonster advanced = new AdvancedMonster("오크 전사", 20, 15, 300);

        String output = captureOutput(advanced::die);

        assertEquals(0, advanced.getHp());
        assertTrue(output.contains("경험치 300 획득"));
    }
}