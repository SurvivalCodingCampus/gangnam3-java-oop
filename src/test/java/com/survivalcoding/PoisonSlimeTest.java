package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 연습문제 3 - {@link PoisonSlime} 의 테스트.
 * <p>
 * 검증 대상: 일반 공격에 독 공격이 덧붙는 순서, 독 데미지가
 * {@code 용사 HP / 5} 를 정수 나눗셈으로 계산해 소수점 이하가 버려지는 것,
 * 그리고 독 공격 횟수(5회)가 소진되면 더 이상 독 공격이 일어나지 않는 것.
 * <p>
 * 소수점 이하 버림은 HP 를 72 로 준비해 62/5 = 12.4 → 12 가 되는 경우로 확인한다.
 */
class PoisonSlimeTest {

    /** 독 공격 시 출력되는 메시지. 여러 테스트에서 재사용한다. */
    private static final String POISON_MESSAGE = "추가로, 독 포자를 살포했다!";

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
     * <p>
     * 독 데미지가 현재 HP 에 비례하므로, 테스트마다 시작 HP를 정확히 맞춰야 한다.
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
    @DisplayName("독 슬라임은 Slime 의 한 종류이며 이름이 보존된다")
    void poisonSlimeIsSlime() {
        PoisonSlime poisonSlime = new PoisonSlime("A");

        assertInstanceOf(Slime.class, poisonSlime);
        assertEquals("A", poisonSlime.getName());
    }

    @Test
    @DisplayName("독 슬라임은 보통 공격에 더해 독 공격을 추가로 수행한다")
    void attackWithPoison() {
        // Given
        PoisonSlime poisonSlime = new PoisonSlime("A");
        Hero hero = heroWithHp(100);

        // When : 일반 공격 10 데미지 -> 90, 독 데미지 90/5 = 18 -> 72
        String output = captureOutput(() -> poisonSlime.attack(hero));

        // Then
        assertEquals(72, hero.getHp());
        assertTrue(output.contains(POISON_MESSAGE));
        assertTrue(output.contains("18포인트 데미지"));
    }

    @Test
    @DisplayName("독 데미지는 용사의 HP 를 5 로 나눈 값이며 소수점 이하는 버린다")
    void poisonDamageIsTruncated() {
        // Given : 일반 공격 후 62 HP 가 남아 62/5 = 12.4 -> 12 로 버림
        PoisonSlime poisonSlime = new PoisonSlime("A");
        Hero hero = heroWithHp(72);

        // When
        String output = captureOutput(() -> poisonSlime.attack(hero));

        // Then
        assertTrue(output.contains("12포인트 데미지"));
        assertFalse(output.contains("12.4포인트 데미지"));
        assertEquals(50, hero.getHp());
    }

    @Test
    @DisplayName("독 공격은 5회까지만 가능하고 이후에는 보통 공격만 한다")
    void poisonCountRunsOutAfterFiveAttacks() {
        // Given
        PoisonSlime poisonSlime = new PoisonSlime("A");
        Hero hero = heroWithHp(100);

        // When / Then : 5 회 공격 후의 HP 차례로
        captureOutput(() -> poisonSlime.attack(hero));
        assertEquals(72, hero.getHp());

        captureOutput(() -> poisonSlime.attack(hero));
        assertEquals(50, hero.getHp());

        captureOutput(() -> poisonSlime.attack(hero));
        assertEquals(32, hero.getHp());

        captureOutput(() -> poisonSlime.attack(hero));
        assertEquals(18, hero.getHp());

        captureOutput(() -> poisonSlime.attack(hero));
        assertEquals(7, hero.getHp());
    }

    @Test
    @DisplayName("6 번째 공격에서는 독 공격이 발생하지 않는다")
    void sixthAttackHasNoPoison() {
        // Given : 5 회 공격하여 독 공격 횟수를 모두 소진한 상태
        PoisonSlime poisonSlime = new PoisonSlime("A");
        Hero hero = heroWithHp(100);

        for (int i = 0; i < 5; i++) {
            captureOutput(() -> poisonSlime.attack(hero));
        }

        // When
        String output = captureOutput(() -> poisonSlime.attack(hero));

        // Then : 보통 공격만 동작하고, HP 는 0 아래로 내려가지 않는다
        assertFalse(output.contains(POISON_MESSAGE));
        assertEquals(0, hero.getHp());
    }
}
