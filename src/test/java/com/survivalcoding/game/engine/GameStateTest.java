package com.survivalcoding.game.engine;

import com.survivalcoding.game.animation.ParticleEffect;
import com.survivalcoding.game.entity.GameEntity;
import com.survivalcoding.game.entity.Hero;
import com.survivalcoding.game.entity.Monster;
import com.survivalcoding.game.input.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("게임 상태: 초기값·Wave 진행·승패 판정")
class GameStateTest {

    private static final double DT = 1.0 / 60.0;

    private GameState state;
    private InputHandler input;

    private static java.lang.reflect.Method keyPressed;
    private static java.lang.reflect.Method keyReleased;

    static {
        try {
            keyPressed = InputHandler.class.getDeclaredMethod("onKeyPressed", KeyEvent.class);
            keyPressed.setAccessible(true);
            keyReleased = InputHandler.class.getDeclaredMethod("onKeyReleased", KeyEvent.class);
            keyReleased.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @BeforeEach
    void setUp() {
        state = new GameState();
        input = new InputHandler();
    }

    private void press(KeyCode code) throws Exception {
        keyPressed.invoke(input, new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false));
    }

    private void release(KeyCode code) throws Exception {
        keyReleased.invoke(input, new KeyEvent(
                KeyEvent.KEY_RELEASED, "", "", code, false, false, false, false));
    }

    private static void setWave(GameState state, int wave) {
        try {
            Field f = GameState.class.getDeclaredField("wave");
            f.setAccessible(true);
            f.setInt(state, wave);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @DisplayName("초기 상태는 메인 메뉴이며 영점이다")
    void initialStateIsMainMenu() {
        assertEquals(GameState.GameMode.MAIN_MENU, state.getCurrentMode());
        assertEquals(1, state.getWave());
        assertEquals(0, state.getScore());
        assertEquals(0, state.getMonstersKilled());
        assertEquals(0.0, state.getGameTime(), 1e-9);
        assertTrue(state.getMonsters().isEmpty());
    }

    @Test
    @DisplayName("영웅이 생성되어 있고 엔티티 목록에 포함된다")
    void heroIsCreatedAndRegistered() {
        Hero hero = state.getHero();
        assertNotNull(hero);
        assertTrue(hero.isAlive());
        assertEquals(100, hero.getHp());
        assertEquals(100, hero.getMaxHp());
        assertTrue(state.getEntities().contains(hero));
    }

    @Test
    @DisplayName("조회용 목록은 방어적 복사본이다")
    void gettersReturnDefensiveCopies() {
        state.addMonster(new Monster("조용한 슬라임", 10, 1, 100, 100, Monster.MonsterType.SLIME));
        int before = state.getMonsters().size();

        List<Monster> copy = state.getMonsters();
        copy.clear();

        assertEquals(before, state.getMonsters().size());
    }

    @Test
    @DisplayName("점수와 처치 수가 누적된다")
    void scoreAndKillCountAccumulate() {
        state.addScore(10);
        state.addScore(15);
        assertEquals(25, state.getScore());

        state.incrementMonstersKilled();
        state.incrementMonstersKilled();
        assertEquals(2, state.getMonstersKilled());
    }

    @Test
    @DisplayName("화면 크기를 바꿀 수 있고 월드 경계는 고정이다")
    void screenSizeIsConfigurable() {
        state.setScreenSize(1920, 1080);
        assertEquals(1920, state.getScreenWidth(), 1e-9);
        assertEquals(1080, state.getScreenHeight(), 1e-9);
        assertEquals(3000, state.getWorldWidth(), 1e-9);
        assertEquals(2000, state.getWorldHeight(), 1e-9);
    }

    @Test
    @DisplayName("addMonster는 몬스터 목록과 엔티티 목록에 함께 등록한다")
    void addMonsterRegistersInBothLists() {
        Monster m = new Monster("슬라임", 30, 5, 200, 200, Monster.MonsterType.SLIME);
        state.addMonster(m);
        assertEquals(1, state.getMonsters().size());
        assertTrue(state.getEntities().contains(m));
    }

    @Test
    @DisplayName("addProjectile는 투사체 목록과 엔티티 목록에 함께 등록한다")
    void addProjectileRegistersInBothLists() {
        GameEntity p = new GameEntity(0, 0, 10, 10, 1) {
            @Override
            public void update(double deltaTime, InputHandler input, GameState gameState) {
            }
        };
        state.addProjectile(p);
        assertEquals(1, state.getProjectiles().size());
        assertTrue(state.getEntities().contains(p));
    }

    @Test
    @DisplayName("카메라 흔들기를 켜도 게임이 계속 동작한다")
    void shakeCameraIsSafe() {
        assertDoesNotThrow(() -> {
            state.shakeCamera(0.5);
            state.update(DT, input);
        });
        assertNotNull(state.getCameraPosition());
    }

    @Test
    @DisplayName("resetGame은 진행 상황을 초기화하고 플레이 상태로 만든다")
    void resetGameClearsProgress() {
        state.addMonster(new Monster("슬라임", 30, 5, 200, 200, Monster.MonsterType.SLIME));
        state.addScore(100);
        state.incrementMonstersKilled();
        state.addParticle(new ParticleEffect(0, 0, ParticleEffect.ParticleType.HIT, Color.RED, 3));

        state.resetGame();

        assertEquals(GameState.GameMode.PLAYING, state.getCurrentMode());
        assertTrue(state.getMonsters().isEmpty());
        assertTrue(state.getProjectiles().isEmpty());
        assertTrue(state.getParticles().isEmpty());
        assertEquals(0, state.getScore());
        assertEquals(1, state.getWave());
        assertEquals(0, state.getMonstersKilled());
        assertEquals(0.0, state.getGameTime(), 1e-9);
        assertNotNull(state.getHero());
        assertTrue(state.getHero().isAlive());
    }

    @Test
    @DisplayName("메인 메뉴에서는 Enter를 눌러야 플레이 상태가 된다")
    void mainMenuRequiresActionToStart() {
        assertEquals(GameState.GameMode.MAIN_MENU, state.getCurrentMode());
        state.update(DT, input);
        assertEquals(GameState.GameMode.MAIN_MENU, state.getCurrentMode());
        assertTrue(state.getMonsters().isEmpty());
    }

    @Test
    @DisplayName("플레이 시작 후 첫 프레임에 첫 웨이브가 생성된다")
    void firstWaveSpawnsOnFirstFrame() {
        state.resetGame();
        assertTrue(state.getMonsters().isEmpty());

        state.update(DT, input);

        assertFalse(state.getMonsters().isEmpty());
        assertEquals(2, state.getWave());
    }

    @Test
    @DisplayName("플레이 중 ESC로 일시정지/해제 토글")
    void pauseToggleWithEsc() throws Exception {
        state.resetGame();
        assertEquals(GameState.GameMode.PLAYING, state.getCurrentMode());

        // Pause
        press(KeyCode.ESCAPE);
        state.update(DT, input);
        assertEquals(GameState.GameMode.PAUSED, state.getCurrentMode());
        input.update();
        release(KeyCode.ESCAPE);

        // Resume with ESC
        press(KeyCode.ESCAPE);
        state.update(DT, input);
        assertEquals(GameState.GameMode.PLAYING, state.getCurrentMode());
    }

    @Test
    @DisplayName("일시정지 상태에서 ENTER로 계속")
    void resumeFromPauseWithEnter() throws Exception {
        state.resetGame();
        // Enter pause first
        press(KeyCode.ESCAPE);
        state.update(DT, input);
        assertEquals(GameState.GameMode.PAUSED, state.getCurrentMode());
        input.update();

        // Resume with ENTER (action)
        press(KeyCode.ENTER);
        state.update(DT, input);
        assertEquals(GameState.GameMode.PLAYING, state.getCurrentMode());
    }

    @Test
    @DisplayName("F3로 FPS 표시 토글")
    void toggleFpsOverlayWithF3() throws Exception {
        assertFalse(state.isShowFps());

        press(KeyCode.F3);
        state.update(DT, input);
        assertTrue(state.isShowFps());
        input.update();
        release(KeyCode.F3);

        press(KeyCode.F3);
        state.update(DT, input);
        assertFalse(state.isShowFps());
    }

    @Test
    @DisplayName("플레이 중 update는 예외 없이 계속 동작한다")
    void gameplayUpdatesDoNotThrow() {
        state.resetGame();
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 300; i++) {
                state.update(DT, input);
            }
        });
    }

    @Test
    @DisplayName("영웅은 월드 경계 밖으로 나가지 않는다")
    void heroStaysInsideWorldBounds() {
        state.resetGame();
        for (int i = 0; i < 600; i++) {
            state.update(DT, input);
            Hero h = state.getHero();
            assertTrue(h.getX() >= 0 && h.getX() <= state.getWorldWidth());
            assertTrue(h.getY() >= 0 && h.getY() <= state.getWorldHeight());
        }
    }

    @Test
    @DisplayName("영웅 체력은 최대치를 넘지 않는다")
    void heroHpNeverExceedsMax() {
        state.resetGame();
        for (int i = 0; i < 300; i++) {
            state.update(DT, input);
            Hero h = state.getHero();
            assertTrue(h.getHp() <= h.getMaxHp());
        }
    }

    @Test
    @DisplayName("10웨이브를 넘고 몬스터를 모두 처치하면 승리 상태가 된다")
    void victoryTriggersAfterFinalWaveCleared() {
        state.resetGame();
        setWave(state, 11);

        assertDoesNotThrow(() -> state.update(DT, input));

        assertEquals(GameState.GameMode.VICTORY, state.getCurrentMode());
        assertTrue(state.getMonsters().isEmpty());
    }

    @Test
    @DisplayName("승리 상태에서는 다음 웨이브가 생성되지 않는다")
    void noWaveSpawnsAfterVictory() {
        state.resetGame();
        setWave(state, 11);
        state.update(DT, input);

        assertEquals(GameState.GameMode.VICTORY, state.getCurrentMode());
        int waveAtVictory = state.getWave();
        state.update(DT, input);

        assertTrue(state.getMonsters().isEmpty());
        assertEquals(waveAtVictory, state.getWave());
    }
}
