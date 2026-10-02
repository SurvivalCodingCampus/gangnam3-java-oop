package com.survivalcoding.game.engine;

import com.survivalcoding.game.input.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("게임 엔진: 한 스텝의 입력 소비 순서")
class GameEngineTest {

    private static final double DT = 1.0 / 60.0;

    private static final Method UPDATE;
    private static final Method KEY_PRESSED;

    static {
        try {
            UPDATE = GameEngine.class.getDeclaredMethod("update", double.class);
            UPDATE.setAccessible(true);
            KEY_PRESSED = InputHandler.class.getDeclaredMethod("onKeyPressed", KeyEvent.class);
            KEY_PRESSED.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private GameState state;
    private InputHandler input;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        state = new GameState();
        input = new InputHandler();
        engine = new GameEngine(null, input, state);
    }

    private void press(KeyCode code) throws Exception {
        KEY_PRESSED.invoke(input, new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false));
    }

    private void step() throws Exception {
        UPDATE.invoke(engine, DT);
    }

    @Test
    @DisplayName("Enter를 누르면 한 스텝 만에 메인 메뉴에서 게임이 시작된다")
    void actionKeyStartsGameInOneStep() throws Exception {
        assertEquals(GameState.GameMode.MAIN_MENU, state.getCurrentMode());

        press(KeyCode.ENTER);
        step();

        assertEquals(GameState.GameMode.PLAYING, state.getCurrentMode());
    }

    @Test
    @DisplayName("입력이 없으면 메인 메뉴에 머문다")
    void staysInMenuWithoutInput() throws Exception {
        step();

        assertEquals(GameState.GameMode.MAIN_MENU, state.getCurrentMode());
    }

    @Test
    @DisplayName("소비된 순간 입력은 같은 스텝에서 정리된다")
    void consumedInputIsClearedAfterTheStep() throws Exception {
        press(KeyCode.ENTER);
        step();

        assertFalse(input.isActionPressed());
    }
}
