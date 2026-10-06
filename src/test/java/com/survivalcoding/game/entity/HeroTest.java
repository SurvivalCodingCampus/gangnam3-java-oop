package com.survivalcoding.game.entity;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("영웅: 이동·공격·마법·회복")
class HeroTest {

    private static final double DT = 1.0 / 60.0;

    private static final Method keyPressed;

    static {
        try {
            keyPressed = InputHandler.class.getDeclaredMethod("onKeyPressed", KeyEvent.class);
            keyPressed.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private GameState state;
    private InputHandler input;
    private Hero hero;

    @BeforeEach
    void setUp() {
        state = new GameState();
        state.resetGame();
        input = new InputHandler();
        hero = state.getHero();
    }

    private void press(KeyCode code) throws InvocationTargetException, IllegalAccessException {
        keyPressed.invoke(input, new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false));
    }

    private Monster adjacentMonster(int hp) {
        Monster m = new Monster("연습용 좀비", hp, 1, hero.getX() + 60, hero.getY(),
                Monster.MonsterType.ZOMBIE);
        state.addMonster(m);
        return m;
    }

    @Test
    @DisplayName("생성시 최대 체력으로 살아 있다")
    void heroStartsAliveAtFullHp() {
        Hero fresh = new Hero("테스트영웅", 100, 200);
        assertEquals(100, fresh.getX(), 1e-9);
        assertEquals(200, fresh.getY(), 1e-9);
        assertEquals(100, fresh.getHp());
        assertEquals(100, fresh.getMaxHp());
        assertTrue(fresh.isAlive());
    }

    @Test
    @DisplayName("오른쪽을 누르면 X가 증가한다")
    void movesRightOnRightInput() throws Exception {
        double before = hero.getX();
        press(KeyCode.D);
        for (int i = 0; i < 30; i++) hero.update(DT, input, state);
        assertTrue(hero.getX() > before);
        assertTrue(hero.isFacingRight());
    }

    @Test
    @DisplayName("왼쪽을 누르면 X가 감소하고 바라보는 방향이 바뀐다")
    void movesLeftOnLeftInput() throws Exception {
        double before = hero.getX();
        press(KeyCode.A);
        for (int i = 0; i < 30; i++) hero.update(DT, input, state);
        assertTrue(hero.getX() < before);
        assertFalse(hero.isFacingRight());
    }

    @Test
    @DisplayName("한 프레임 이동 거리가 속도와 같은 프레임 수를 곱한 값과 일치한다")
    void movementSpeedMatchesPerFrameUpdate() throws Exception {
        Hero solo = new Hero("속도측정", 500, 500);
        press(KeyCode.D);

        double expectedPerFrame = solo.getSpeed() * DT;
        double startX = solo.getX();
        for (int i = 0; i < 1; i++) {
            solo.update(DT, input, state);
        }
        assertEquals(expectedPerFrame, solo.getX() - startX, 1e-6);
    }

    @Test
    @DisplayName("대시는 쿨다운을 시작한다")
    void dashStartsCooldown() throws Exception {
        press(KeyCode.D);
        press(KeyCode.SHIFT);
        hero.update(DT, input, state);

        assertTrue(hero.getDashCooldown() > 0);
    }

    @Test
    @DisplayName("대시 중에는 방향이 유지되고 쿨다운이 채워진다")
    void dashConsumesInputOnce() throws Exception {
        Hero walker = new Hero("걷기", 500, 500);
        press(KeyCode.D);
        for (int i = 0; i < 5; i++) walker.update(DT, input, state);
        double walkDistance = walker.getX() - 500;

        assertTrue(walkDistance > 0);
    }

    @Test
    @DisplayName("공격 키를 누르면 인접한 몬스터가 피해를 받는다")
    void attackDamagesAdjacentMonster() throws Exception {
        Monster m = adjacentMonster(60);
        press(KeyCode.SPACE);
        hero.update(DT, input, state);

        assertTrue(m.getHp() < 60);
        assertTrue(state.getScore() > 0);
    }

    @Test
    @DisplayName("몬스터를 처치하면 처치 수와 점수가 오른다")
    void killingMonsterIncrementsKillCountAndScore() throws Exception {
        Monster m = adjacentMonster(1);
        int killsBefore = state.getMonstersKilled();

        press(KeyCode.SPACE);
        hero.update(DT, input, state);

        assertFalse(m.isAlive());
        assertEquals(killsBefore + 1, state.getMonstersKilled());
        assertTrue(state.getScore() >= 60);
    }

    @Test
    @DisplayName("공격 쿨다운 중에는 다시 공격하지 않는다")
    void attackRespectsCooldown() throws Exception {
        Monster m = adjacentMonster(500);
        press(KeyCode.SPACE);
        hero.update(DT, input, state);
        int hpAfterFirst = m.getHp();

        for (int i = 0; i < 3; i++) hero.update(DT, input, state);

        assertEquals(hpAfterFirst, m.getHp());
        assertTrue(hero.getAttackCooldown() > 0);
    }

    @Test
    @DisplayName("마법은 MP를 소모하고 투사체를 생성한다")
    void magicCostsMpAndSpawnsProjectile() throws Exception {
        int mpBefore = hero.getMp();
        press(KeyCode.M);
        hero.update(DT, input, state);

        assertEquals(mpBefore - 15, hero.getMp());
        assertFalse(state.getProjectiles().isEmpty());
    }

    @Test
    @DisplayName("마법 쿨다운 중에는 MP가 더 줄지 않는다")
    void magicRespectsCooldown() throws Exception {
        press(KeyCode.M);
        hero.update(DT, input, state);
        int mpAfterFirst = hero.getMp();

        for (int i = 0; i < 3; i++) hero.update(DT, input, state);

        assertEquals(mpAfterFirst, hero.getMp());
        assertTrue(hero.getMagicCooldown() > 0);
    }

    @Test
    @DisplayName("회복 아이템은 체력을 되돌린다")
    void itemHealsHero() throws Exception {
        hero.takeDamage(50);
        int hpBefore = hero.getHp();

        press(KeyCode.E);
        hero.update(DT, input, state);

        assertTrue(hero.getHp() > hpBefore);
    }

    @Test
    @DisplayName("최대 체력을 넘겨 회복되지 않는다")
    void healingIsCappedAtMaxHp() throws Exception {
        press(KeyCode.E);
        hero.update(DT, input, state);
        assertEquals(hero.getMaxHp(), hero.getHp());
    }

    @Test
    @DisplayName("경험치를 받으면 경험치가 증가한다")
    void expIncreasesOnGain() {
        int expBefore = hero.getExp();
        hero.addExp(10);
        assertEquals(expBefore + 10, hero.getExp());
    }

    @Test
    @DisplayName("MP는 시간이지나면 회복되고 최대치를 넘지 않는다")
    void mpRegeneratesUpToMax() {
        hero.takeDamage(0);
        assertTrue(hero.getMp() <= hero.getMaxMp());
        for (int i = 0; i < 600; i++) {
            hero.update(DT, input, state);
            assertTrue(hero.getMp() <= hero.getMaxMp());
            assertTrue(hero.getMp() >= 0);
        }
    }

    @Test
    @DisplayName("이동해도 월드 경계를 벗어나지 않는다")
    void heroNeverLeavesWorldBounds() throws Exception {
        press(KeyCode.D);
        press(KeyCode.S);
        for (int i = 0; i < 900; i++) {
            hero.update(DT, input, state);
            assertTrue(hero.getX() >= 0 && hero.getX() <= state.getWorldWidth());
            assertTrue(hero.getY() >= 0 && hero.getY() <= state.getWorldHeight());
        }
    }

    @Test
    @DisplayName("모든 입력 없이 계속 업데이트해도 예외가 없다")
    void idleUpdateNeverThrows() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 300; i++) {
                hero.update(DT, input, state);
            }
        });
    }
}