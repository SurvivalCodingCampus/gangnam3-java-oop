package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link SuperHero} 클래스의 테스트 (11장 - 상속).
 * <p>
 * 검증 대상: 날기/착륙으로 비행 상태가 바뀌는지, 그리고 무엇보다
 * {@code run()} 이 부모 {@link Hero#run()} 이 아니라 자식 버전을 실행하는지.
 * 후자는 "멋지게 퇴각했다"가 출력되고 부모의 "GAME OVER"가 출력되지 않는지로 확인한다.
 * 이것이 오버라이드가 실제로 동작한다는 증거가 된다.
 */
class SuperHeroTest {

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

    @Test
    @DisplayName("생성 직후에는 날고 있지 않다")
    void isFlying() {
        SuperHero superHero = new SuperHero("한석봉", 100);

        assertFalse(superHero.isFlying());
    }

    @Test
    @DisplayName("비행 상태를 직접 설정할 수 있다")
    void setFlying() {
        SuperHero superHero = new SuperHero("한석봉", 100);

        superHero.setFlying(true);

        assertTrue(superHero.isFlying());
    }

    @Test
    @DisplayName("공격하면 Hero 처럼 자신의 HP가 10 감소한다 (수업 원본 설계)")
    void attack() {
        // Given : 수업에서 의도한 동작을 그대로 검증한다
        // (SuperHero 는 Hero 를 상속받아 자기 HP 를 10 감소시킨다)
        SuperHero superHero = new SuperHero("한석봉", 50);
        Slime slime = new Slime("슬라임A");

        // When
        superHero.attack(slime);

        // Then
        assertEquals(40, superHero.getHp());
    }

    @Test
    @DisplayName("run 은 Hero 가 아니라 SuperHero 의 것으로 오버라이드되어 있다")
    void run() {
        SuperHero superHero = new SuperHero("한석봉", 100);

        String output = captureOutput(superHero::run);

        assertTrue(output.contains("멋지게 퇴각했다"));
        // Hero.run 의 내용(GAME OVER)이 출력되지 않아야 오버라이드 성공
        assertFalse(output.contains("GAME OVER"));
    }

    @Test
    @DisplayName("fly 는 날아올라 비행 상태가 된다")
    void fly() {
        SuperHero superHero = new SuperHero("한석봉", 100);

        String output = captureOutput(superHero::fly);

        assertTrue(superHero.isFlying());
        assertTrue(output.contains("한석봉이 날아올랐다."));
    }

    @Test
    @DisplayName("land 는 착륙하여 비행 상태가 해제 된다")
    void land() {
        SuperHero superHero = new SuperHero("한석봉", 100);
        superHero.fly();

        String output = captureOutput(superHero::land);

        assertFalse(superHero.isFlying());
        assertTrue(output.contains("한석봉이 착륙했다."));
    }

    @Test
    @DisplayName("toString 은 이름, HP, 비행 상태를 모두 보여준다")
    void testToString() {
        SuperHero superHero = new SuperHero("한석봉", 100);

        String result = superHero.toString();

        assertTrue(result.contains("SuperHero"));
        assertTrue(result.contains("한석봉"));
        assertTrue(result.contains("hp=100"));
        assertTrue(result.contains("isFlying=false"));
    }
}
