package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Wizard} 클래스의 테스트 (연습문제 4).
 * <p>
 * 검증 대상: 이름/HP/MP/지팡이 필드의 getter·setter와 setter 의 검증 로직,
 * MP 초기값이 100 인 것, 그리고 {@code heal} 이 대상 HP를 20 회복시키고
 * 자신의 MP를 10 소모하는지. MP가 부족할 때는 아무것도 바꾸지 않는지도 함께 확인한다.
 */
class WizardTest {

    /**
     * {@code System.out} 을 잠시 가로채어 출력된 내용을 돌려주는 헬퍼.
     * <p>
     * {@link Wizard#heal(Hero)} 처럼 결과를 반환하지 않고 콘솔에만 출력하는
     * 메서드의 동작을 검증하려면 표준 출력 스트림을 바꿔야 한다.
     * 어떤 검증도 하지 않은 상태로 스트림이 남지 않도록 {@code finally} 에서 복구한다.
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
    @DisplayName("마법사의 이름을 저장하고 읽을 수 있다")
    void getName() {
        Wizard wizard = new Wizard();
        wizard.setName("마법사");

        assertEquals("마법사", wizard.getName());
    }

    @Test
    @DisplayName("마법사의 이름은 null 이거나 3글자 미만이면 예외가 발생한다")
    void setName() {
        Wizard wizard = new Wizard();

        assertThrows(IllegalArgumentException.class, () -> wizard.setName(null));
        assertThrows(IllegalArgumentException.class, () -> wizard.setName("AB"));
    }

    @Test
    @DisplayName("마법사의 HP를 저장하고 읽을 수 있다")
    void getHp() {
        Wizard wizard = new Wizard();
        wizard.setHp(100);

        assertEquals(100, wizard.getHp());
    }

    @Test
    @DisplayName("마법사의 HP는 음수가 되면 0 이 된다")
    void setHp() {
        Wizard wizard = new Wizard();

        wizard.setHp(-10);

        assertEquals(0, wizard.getHp());
    }

    @Test
    @DisplayName("마법사의 MP 초기값은 100 이다")
    void getMp() {
        assertEquals(100, new Wizard().getMp());
    }

    @Test
    @DisplayName("마법사의 MP는 음수가 되면 예외가 발생한다")
    void setMp() {
        Wizard wizard = new Wizard();

        wizard.setMp(50);
        assertEquals(50, wizard.getMp());

        assertThrows(IllegalArgumentException.class, () -> wizard.setMp(-1));
    }

    @Test
    @DisplayName("마법사의 지팡이를 저장하고 읽을 수 있다")
    void getWand() {
        Wizard wizard = new Wizard();
        Wand wand = new Wand();
        wand.setName("마법지팡이");
        wand.setPower(50.0);

        wizard.setWand(wand);

        assertSame(wand, wizard.getWand());
    }

    @Test
    @DisplayName("마법사의 지팡이는 null 일 수 없다")
    void setWand() {
        Wizard wizard = new Wizard();

        assertThrows(IllegalArgumentException.class, () -> wizard.setWand(null));
    }

    @Test
    @DisplayName("heal 은 대상 HP를 20 회복시키고 자신의 MP를 10 소모한다")
    void heal() {
        // Given
        Wizard wizard = new Wizard();
        Hero hero = heroWithHp(100);

        // When
        wizard.heal(hero);

        // Then
        assertEquals(120, hero.getHp());
        assertEquals(90, wizard.getMp());
    }

    @Test
    @DisplayName("heal 성공 시 대상 HP를 출력한다")
    void healPrintsMessage() {
        Wizard wizard = new Wizard();
        Hero hero = heroWithHp(100);

        String output = captureOutput(() -> wizard.heal(hero));

        assertTrue(output.contains("힐을 시전했습니다. 대상 HP: 120"));
    }

    @Test
    @DisplayName("MP가 10 보다 적으면 heal 은 마나가 부족하다고 출력하고 회복하지 않는다")
    void healWithoutEnoughMp() {
        // Given
        Wizard wizard = new Wizard();
        wizard.setMp(9);
        Hero hero = heroWithHp(100);

        // When
        String output = captureOutput(() -> wizard.heal(hero));

        // Then
        assertTrue(output.contains("마나가 부족합니다"));
        assertEquals(100, hero.getHp());
        assertEquals(9, wizard.getMp());
    }

    @Test
    @DisplayName("이름, HP, MP를 지정하는 생성자로 초기 상태를 만들 수 있다")
    void wizardConstructorTest() {
        Wizard wizard = new Wizard("신초", 80, 60);

        assertEquals("신초", wizard.getName());
        assertEquals(80, wizard.getHp());
        assertEquals(60, wizard.getMp());
    }
}
