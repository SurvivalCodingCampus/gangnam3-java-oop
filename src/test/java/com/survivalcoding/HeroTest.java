package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Hero} 클래스의 테스트.
 * <p>
 * Hero 는 이 저장소 전체의 부모 클래스이므로(SuperHero, PoisonSlime 가 상속),
 * 다른 모든 클래스의 기반이 되는 가장 핵심적인 클래스다.
 * <p>
 * 검증 대상
 * <ul>
 *     <li>상수와 정적 필드(MAX_HP, money)</li>
 *     <li>생성자 2종의 동작 차이 — 기본 생성자는 검증 없음, 2-arg 생성자도 검증을 거치지 않는다</li>
 *     <li>setName 의 검증(3글자 이상)과 setHp 의 0 하한 보정</li>
 *     <li>run / sit / slip / sleep / bye / attack(Kinoko) 의 HP 변화와 출력</li>
 *     <li>{@code die()} 가 private 이라 attack(Kinoko) 를 통해서만 트리거되는 경계값 검증</li>
 * </ul>
 */
class HeroTest {

    /**
     * {@code System.out} 을 잠시 가로채어 출력된 내용을 돌려주는 헬퍼.
     *
     * @param action 출력을 발생시킬 동작
     * @return 해당 동작이 출력한 문자열
     */
    private String captureOutput(Runnable action) {
        java.io.PrintStream original = System.out;
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();

        try (java.io.PrintStream capture =
                     new java.io.PrintStream(buffer, true, java.nio.charset.StandardCharsets.UTF_8)) {
            System.setOut(capture);
            action.run();
        } finally {
            System.setOut(original);
        }

        return buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
    }

    // ==================== 생성 / 상수 ====================

    @Test
    @DisplayName("기본 생성자로 만든 용사가 null 이 아닌지 확인한다")
    void testHeroCreation() {
        Hero hero = new Hero();

        assertNotNull(hero);
    }

    @Test
    @DisplayName("기본 생성자로 만든 용사의 HP 는 0 이다")
    void defaultConstructorLeavesHpZero() {
        Hero hero = new Hero();

        assertEquals(0, hero.getHp());
    }

    @Test
    @DisplayName("이름과 HP를 지정하는 생성자로 초기 상태를 만들 수 있다")
    void heroConstructorTest() {
        Hero hero = new Hero("한석봉", 77);

        assertEquals("한석봉", hero.getName());
        assertEquals(77, hero.getHp());
    }

    @Test
    @DisplayName("생성자는 검증을 하지 않으므로 2글자 이름도 그대로 들어간다")
    void heroConstructorSkipsValidation() {
        // 생성자는 초기 상태만 세우는 곳이고, 검증은 setName 의 몫이다.
        // 이 차이를 문서화하기 위한 테스트.
        Hero hero = new Hero("AB", 10);

        assertEquals("AB", hero.getName());
    }

    @Test
    @DisplayName("생성자는 검증을 하지 않으므로 음수 HP도 그대로 들어간다")
    void heroConstructorAcceptsNegativeHp() {
        Hero hero = new Hero("한석봉", -30);

        assertEquals(-30, hero.getHp());
    }

    @Test
    @DisplayName("MAX_HP 는 100 이다")
    void maxHpTest() {
        assertEquals(100, Hero.MAX_HP);
    }

    @Test
    @DisplayName("money 는 static 필드이므로 인스턴스마다 따로 갖지 않는다")
    void moneyIsStaticField() throws NoSuchFieldException {
        // money 를 실제로 바꾸면 같은 JVM 에서 돌리는 다른 테스트에 영향을 준다.
        // 값을 바꾸지 않고 'static 인지'만 reflection 으로 확인한다.
        int modifiers = Hero.class.getDeclaredField("money").getModifiers();

        assertTrue(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));
    }

    // ==================== 이름 ====================

    @Test
    @DisplayName("용사의 이름을 저장하고 읽을 수 있다")
    void setAndGetName() {
        Hero hero = new Hero();

        hero.setName("준석이");

        assertEquals("준석이", hero.getName());
    }

    @Test
    @DisplayName("이름은 null 일 수 없다")
    void nameCannotBeNull() {
        Hero hero = new Hero();

        assertThrows(IllegalArgumentException.class, () -> hero.setName(null));
    }

    @Test
    @DisplayName("이름은 3글자 이상이어야 한다")
    void nameMustBeAtLeastThreeCharacters() {
        Hero hero = new Hero();

        assertThrows(IllegalArgumentException.class, () -> hero.setName("AB"));
    }

    @Test
    @DisplayName("이름은 정확히 3글자여도 허용된다")
    void nameOfExactlyThreeCharactersIsAllowed() {
        Hero hero = new Hero();

        hero.setName("한석봉");

        assertEquals("한석봉", hero.getName());
    }

    // ==================== HP ====================

    @Test
    @DisplayName("용사의 HP를 저장하고 읽을 수 있다")
    void setAndGetHp() {
        Hero hero = new Hero();

        hero.setHp(100);

        assertEquals(100, hero.getHp());
    }

    @Test
    @DisplayName("HP 는 음수가 되면 0 으로 보정된다")
    void hpIsClampedToZero() {
        Hero hero = new Hero();

        hero.setHp(-50);

        assertEquals(0, hero.getHp());
    }

    @Test
    @DisplayName("HP 에는 상한 보정이 없다")
    void hpHasNoUpperBound() {
        // MAX_HP 는 상한 규칙이 아니라 sleep()/superHeal() 이 회복시키는 기준값일 뿐이다.
        // sit() 으로 MAX_HP 를 넘어선 HP 를 만들 수 있다는 점을 확인한다.
        Hero hero = new Hero("한석봉", 90);

        hero.sit(50);

        assertEquals(140, hero.getHp());
    }

    // ==================== 검 ====================

    @Test
    @DisplayName("검의 이름을 저장하고 읽을 수 있다")
    void setAndGetSword() {
        Hero hero = new Hero();

        hero.setSword("푸른 검");

        assertEquals("푸른 검", hero.getSword());
    }

    // ==================== 행동 ====================

    @Test
    @DisplayName("attack 는 기본 구현이므로 아무 일도 일어나지 않는다")
    void attackDoesNothing() {
        Hero hero = new Hero("한석봉", 100);

        String output = captureOutput(hero::attack);

        assertEquals(100, hero.getHp());
        assertEquals("", output);
    }

    @Test
    @DisplayName("run 은 GAME OVER 와 최종 HP를 출력한다")
    void run() {
        Hero hero = new Hero("한석봉", 42);

        String output = captureOutput(hero::run);

        assertTrue(output.contains("한석봉는 도망쳤다!"));
        assertTrue(output.contains("GAME OVER"));
        assertTrue(output.contains("최종 HP는 42입니다"));
    }

    @Test
    @DisplayName("sit 은 HP를 시간만큼 회복하고 회복량을 출력한다")
    void sitRecoversHp() {
        Hero hero = new Hero("한석봉", 100);

        String output = captureOutput(() -> hero.sit(25));

        assertEquals(125, hero.getHp());
        assertTrue(output.contains("한석봉는 25초 앉았다"));
        assertTrue(output.contains("HP가 25포인트 회복되었다"));
    }

    @Test
    @DisplayName("slip 은 HP를 5 줄이고 0 아래로 내려가지 않는다")
    void slipReducesHpByFive() {
        Hero hero = new Hero("한석봉", 100);

        String output = captureOutput(hero::slip);

        assertEquals(95, hero.getHp());
        assertTrue(output.contains("한석봉는 넘어졌다!"));
        assertTrue(output.contains("5의 데미지!"));
    }

    @Test
    @DisplayName("slip 을 여러 번 불러도 HP 는 0 아래로 내려가지 않는다")
    void slipClampsAtZero() {
        Hero hero = new Hero("한석봉", 3);

        captureOutput(hero::slip);
        captureOutput(hero::slip);
        captureOutput(hero::slip);

        assertEquals(0, hero.getHp());
    }

    @Test
    @DisplayName("sleep 은 HP를 MAX_HP 까지 완전히 회복한다")
    void sleepRestoresToMaxHp() {
        Hero hero = new Hero("한석봉", 3);

        String output = captureOutput(hero::sleep);

        assertEquals(Hero.MAX_HP, hero.getHp());
        assertTrue(output.contains("한석봉는 잠을 자고 HP를 회복했다!"));
    }

    @Test
    @DisplayName("bye 는 작별 인사를 출력한다")
    void bye() {
        Hero hero = new Hero("한석봉", 100);

        String output = captureOutput(hero::bye);

        assertTrue(output.contains("용자는 이별을 고했다. 빠이"));
    }

    // ==================== 괴물버섯 반격 ====================

    /**
     * {@link Kinoko} 를 만들어 주는 헬퍼.
     *
     * @param suffix 버섯의 별명
     * @return 별명이 설정된 괴물버섯
     */
    private Kinoko kinokoWithSuffix(String suffix) {
        Kinoko kinoko = new Kinoko();
        kinoko.suffix = suffix;
        return kinoko;
    }

    @Test
    @DisplayName("attack(Kinoko) 는 반격 2포인트를 입는다")
    void attackKinokoReducesHpByTwo() {
        Hero hero = new Hero("한석봉", 100);

        String output = captureOutput(() -> hero.attack(kinokoWithSuffix("A")));

        assertEquals(98, hero.getHp());
        assertTrue(output.contains("괴물버섯A로부터 2포인트의 반격을 받았다"));
    }

    @Test
    @DisplayName("HP 가 1 이면 죽지 않는다")
    void heroSurvivesWithOneHp() {
        // attack(Kinoko) 는 HP 를 2 줄인 뒤 hp < 1 을 검사한다.
        // 3 - 2 = 1 이므로 1 < 1 은 거짓 → die() 가 호출되지 않는다. 경계값 검증.
        Hero hero = new Hero("한석봉", 3);

        String output = captureOutput(() -> hero.attack(kinokoWithSuffix("A")));

        assertEquals(1, hero.getHp());
        assertFalse(output.contains("죽었다"));
    }

    @Test
    @DisplayName("HP 가 0 이 되면 죽는다")
    void heroDiesAtZeroHp() {
        // 2 - 2 = 0 이므로 0 < 1 은 참 → private die() 가 호출된다.
        // die() 는 private 이므로 이 메서드를 통해서만 검증할 수 있다.
        Hero hero = new Hero("한석봉", 2);

        String output = captureOutput(() -> hero.attack(kinokoWithSuffix("A")));

        assertEquals(0, hero.getHp());
        assertTrue(output.contains("한석봉는 죽었다"));
        assertTrue(output.contains("Game Over"));
    }

    @Test
    @DisplayName("반격을 여러 번 받으면 결국 죽는다")
    void heroDiesAfterRepeatedCounterAttacks() {
        Hero hero = new Hero("한석봉", 5);
        Kinoko kinoko = kinokoWithSuffix("B");

        captureOutput(() -> hero.attack(kinoko));
        captureOutput(() -> hero.attack(kinoko));
        String output = captureOutput(() -> hero.attack(kinoko));

        assertEquals(0, hero.getHp());
        assertTrue(output.contains("한석봉는 죽었다"));
        assertTrue(output.contains("Game Over"));
    }

    @Test
    @DisplayName("반격이 0 아래로 내려가지 않는다")
    void counterAttackRespectsZeroFloor() {
        // regression test.
        // 예전 attack(Kinoko) 는 setHp 를 거치지 않고 this.hp -= 2 를 직접 썼다.
        // 그래서 HP 1 에서 반격을 받으면 -1 이 되었다.
        // 지금은 setHp 를 거쳐 0 에서 멈춘다.
        Hero hero = new Hero("한석봉", 1);

        String output = captureOutput(() -> hero.attack(kinokoWithSuffix("C")));

        assertEquals(0, hero.getHp());
        assertTrue(output.contains("한석봉는 죽었다"));
    }

    // ==================== 오버로딩 ====================

    @Test
    @DisplayName("매개변수가 없는 attack 와 Kinoko 를 받는 attack 가 함께 존재한다")
    void attackIsOverloaded() {
        Hero hero = new Hero("한석봉", 10);
        Kinoko kinoko = kinokoWithSuffix("A");

        // 매개변수 타입이 달라 컴파일러가 구분한다. (오버로딩)
        captureOutput(hero::attack);
        captureOutput(() -> hero.attack(kinoko));

        // 전자는 HP를 그대로 두고, 후자는 2만큼 깎는다.
        assertEquals(8, hero.getHp());
    }
}
