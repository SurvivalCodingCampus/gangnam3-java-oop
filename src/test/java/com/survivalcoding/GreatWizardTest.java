package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 연습문제 5 - {@link GreatWizard} 의 테스트.
 * <p>
 * 검증 대상: MP 초기값이 150 인 것, {@code heal} 이 부모 대신 재정의된 값
 * (HP +25 / MP −5)으로 동작하는지, 그리고 부모에 없던 {@code superHeal} 이
 * HP 를 {@link Hero#MAX_HP} 로 채우고 MP 50 을 소모하는지.
 * <p>
 * 마나가 모자란 경우({@code heal} 5 미만, {@code superHeal} 50 미만)에는
 * "마나가 부족합니다" 만 출력하고 HP 와 MP 가 전혀 바뀌지 않아야 한다.
 * 이 두 경우를 확인해야 heal 과 superHeal 이 원자적(전부 되거나 전부 안 되거나)임을 알 수 있다.
 */
class GreatWizardTest {

    /** 마나 부족 시 출력되는 메시지. Wizard 와 GreatWizard 가 함께 쓰는 문자열이다. */
    private static final String NOT_ENOUGH_MP = "마나가 부족합니다";

    /** 슈퍼 힐 성공 시 출력되는 메시지의 앞부분. */
    private static final String SUPER_HEAL_PREFIX = "슈퍼 힐을 시전했습니다. 대상 HP: ";

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

    /**
     * 이름과 HP를 지정해 용사를 만들어 주는 헬퍼.
     *
     * @param hp 용사에게 줄 HP
     * @return 이름이 설정된 용사
     */
    private Hero heroWithHp(int hp) {
        Hero hero = new Hero();
        hero.setName("준석이");
        hero.setHp(hp);
        return hero;
    }

    @Test
    @DisplayName("슈퍼 마법사는 Wizard 의 한 종류이며 MP 초기값은 150 이다")
    void greatWizardIsWizardWithDefaultMp() {
        GreatWizard greatWizard = new GreatWizard("신초");

        assertInstanceOf(Wizard.class, greatWizard);
        assertEquals("신초", greatWizard.getName());
        assertEquals(150, greatWizard.getMp());
    }

    @Test
    @DisplayName("heal 은 대상 HP를 25 회복시키고 자신의 MP를 5 소모한다")
    void healOverridesWizard() {
        // Given
        GreatWizard greatWizard = new GreatWizard("신초");
        Hero hero = heroWithHp(100);

        // When
        String output = captureOutput(() -> greatWizard.heal(hero));

        // Then : Wizard 와 달리 25 회복 / 5 소모
        assertEquals(125, hero.getHp());
        assertEquals(145, greatWizard.getMp());
        assertTrue(output.contains(SUPER_HEAL_PREFIX + "125"));
    }

    @Test
    @DisplayName("MP가 5 보다 적으면 heal 은 마나가 부족하다고 출력하고 회복하지 않는다")
    void healWithoutEnoughMp() {
        // Given
        GreatWizard greatWizard = new GreatWizard("신초");
        greatWizard.setMp(4);
        Hero hero = heroWithHp(100);

        // When
        String output = captureOutput(() -> greatWizard.heal(hero));

        // Then
        assertTrue(output.strip().equals(NOT_ENOUGH_MP));
        assertEquals(100, hero.getHp());
        assertEquals(4, greatWizard.getMp());
    }

    @Test
    @DisplayName("superHeal 은 대상 HP를 최대로 회복시키고 자신의 MP를 50 소모한다")
    void superHeal() {
        // Given
        GreatWizard greatWizard = new GreatWizard("신초");
        Hero hero = heroWithHp(30);

        // When
        String output = captureOutput(() -> greatWizard.superHeal(hero));

        // Then
        assertEquals(Hero.MAX_HP, hero.getHp());
        assertEquals(100, greatWizard.getMp());
        assertTrue(output.contains(SUPER_HEAL_PREFIX + Hero.MAX_HP));
    }

    @Test
    @DisplayName("MP가 50 보다 적으면 superHeal 은 마나가 부족하다고 출력하고 회복하지 않는다")
    void superHealWithoutEnoughMp() {
        // Given
        GreatWizard greatWizard = new GreatWizard("신초");
        greatWizard.setMp(49);
        Hero hero = heroWithHp(30);

        // When
        String output = captureOutput(() -> greatWizard.superHeal(hero));

        // Then
        assertTrue(output.strip().equals(NOT_ENOUGH_MP));
        assertEquals(30, hero.getHp());
        assertEquals(49, greatWizard.getMp());
    }

    @Test
    @DisplayName("이름, HP, MP를 지정하는 생성자로 초기 상태를 만들 수 있다")
    void greatWizardConstructorTest() {
        GreatWizard greatWizard = new GreatWizard("신초", 70, 200);

        assertEquals("신초", greatWizard.getName());
        assertEquals(70, greatWizard.getHp());
        assertEquals(200, greatWizard.getMp());
    }
}
