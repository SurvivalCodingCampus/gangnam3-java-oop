package com.survivalcoding.game.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("입력 처리: 키 상태와 순간 입력 판정")
class InputHandlerTest {

    private InputHandler handler;

    private static final Method keyPressed;
    private static final Method keyReleased;

    static {
        try {
            keyPressed = InputHandler.class.getDeclaredMethod("onKeyPressed", KeyEvent.class);
            keyReleased = InputHandler.class.getDeclaredMethod("onKeyReleased", KeyEvent.class);
            keyPressed.setAccessible(true);
            keyReleased.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @BeforeEach
    void setUp() {
        handler = new InputHandler();
    }

    private void press(KeyCode code) throws InvocationTargetException, IllegalAccessException {
        keyPressed.invoke(handler, new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false));
    }

    private void release(KeyCode code) throws InvocationTargetException, IllegalAccessException {
        keyReleased.invoke(handler, new KeyEvent(
                KeyEvent.KEY_RELEASED, "", "", code, false, false, false, false));
    }

    @Test
    @DisplayName("아무 키도 누르지 않은 초기 상태는 모두 false")
    void freshHandlerHasNoInput() {
        assertFalse(handler.isUpPressed());
        assertFalse(handler.isDownPressed());
        assertFalse(handler.isLeftPressed());
        assertFalse(handler.isRightPressed());
        assertEquals(0.0, handler.getMoveX());
        assertEquals(0.0, handler.getMoveY());
        assertFalse(handler.isAttackPressed());
        assertFalse(handler.isMagicPressed());
        assertFalse(handler.isItemPressed());
        assertFalse(handler.isDashPressed());
        assertFalse(handler.isPausePressed());
        assertFalse(handler.isActionPressed());
    }

    @Test
    @DisplayName("WASD 가동과 이동 벡터를 바꾼다")
    void wasdControlsMovement() throws Exception {
        press(KeyCode.W);
        assertTrue(handler.isUpPressed());
        assertEquals(-1.0, handler.getMoveY());

        release(KeyCode.W);
        press(KeyCode.S);
        assertEquals(1.0, handler.getMoveY());

        release(KeyCode.S);
        press(KeyCode.A);
        assertEquals(-1.0, handler.getMoveX());

        release(KeyCode.A);
        press(KeyCode.D);
        assertEquals(1.0, handler.getMoveX());
    }

    @Test
    @DisplayName("방향키도 WASD와 동일하게 동작한다")
    void arrowKeysAreAlternatives() throws Exception {
        press(KeyCode.UP);
        assertTrue(handler.isUpPressed());
        assertEquals(-1.0, handler.getMoveY());

        release(KeyCode.UP);
        press(KeyCode.DOWN);
        assertEquals(1.0, handler.getMoveY());

        release(KeyCode.DOWN);
        press(KeyCode.LEFT);
        assertEquals(-1.0, handler.getMoveX());

        release(KeyCode.LEFT);
        press(KeyCode.RIGHT);
        assertEquals(1.0, handler.getMoveX());
    }

    @Test
    @DisplayName("대각선 입력은 두 축을 동시에 가진다 (정규화는 Hero가 담당)")
    void diagonalMovementAffectsBothAxes() throws Exception {
        press(KeyCode.W);
        press(KeyCode.A);
        assertEquals(-1.0, handler.getMoveX());
        assertEquals(-1.0, handler.getMoveY());
    }

    @Test
    @DisplayName("키를 놓으면 해당 방향이 false가 된다")
    void releaseClearsDirection() throws Exception {
        press(KeyCode.W);
        assertTrue(handler.isUpPressed());
        release(KeyCode.W);
        assertFalse(handler.isUpPressed());
        assertEquals(0.0, handler.getMoveY());
    }

    @Test
    @DisplayName("행동 키는 누른 직후에만 true이고 update() 후 false가 된다")
    void actionKeysAreTransient() throws Exception {
        press(KeyCode.SPACE);
        assertTrue(handler.isAttackPressed());

        press(KeyCode.M);
        assertTrue(handler.isMagicPressed());

        press(KeyCode.E);
        assertTrue(handler.isItemPressed());

        press(KeyCode.SHIFT);
        assertTrue(handler.isDashPressed());

        press(KeyCode.ESCAPE);
        assertTrue(handler.isPausePressed());

        press(KeyCode.ENTER);
        assertTrue(handler.isActionPressed());

        handler.update();

        assertFalse(handler.isAttackPressed());
        assertFalse(handler.isMagicPressed());
        assertFalse(handler.isItemPressed());
        assertFalse(handler.isDashPressed());
        assertFalse(handler.isPausePressed());
        assertFalse(handler.isActionPressed());
    }

    @Test
    @DisplayName("isJustReleased는 놓은 직후에만 true")
    void justReleasedIsTransient() throws Exception {
        press(KeyCode.W);
        handler.update();
        assertFalse(handler.isJustReleased(KeyCode.W));

        release(KeyCode.W);
        assertTrue(handler.isJustReleased(KeyCode.W));

        handler.update();
        assertFalse(handler.isJustReleased(KeyCode.W));
    }

    @Test
    @DisplayName("isPressed는 update() 후에도 유지된다")
    void heldStateSurvivesUpdate() throws Exception {
        press(KeyCode.W);
        handler.update();
        assertTrue(handler.isPressed(KeyCode.W));
        assertTrue(handler.isUpPressed());
    }

    @Test
    @DisplayName("키 반복 입력은 순간 입력을 중복 발생시키지 않는다")
    void keyRepeatDoesNotDuplicateJustPressed() throws Exception {
        press(KeyCode.W);
        assertTrue(handler.isJustPressed(KeyCode.W));

        press(KeyCode.W);
        press(KeyCode.W);

        handler.update();
        assertFalse(handler.isJustPressed(KeyCode.W));
    }

    @Test
    @DisplayName("손을 뗐다 다시 누르면 순간 입력이 다시 발생한다")
    void justPressedFiresAgainAfterRelease() throws Exception {
        press(KeyCode.W);
        handler.update();
        assertFalse(handler.isJustPressed(KeyCode.W));

        release(KeyCode.W);
        press(KeyCode.W);
        assertTrue(handler.isJustPressed(KeyCode.W));
    }

    @Test
    @DisplayName("isAttackHeld는 누르는 동안 계속 true")
    void attackHeldPersistsWhileDown() throws Exception {
        press(KeyCode.SPACE);
        assertTrue(handler.isAttackHeld());
        handler.update();
        assertTrue(handler.isAttackHeld());
        release(KeyCode.SPACE);
        assertFalse(handler.isAttackHeld());
    }

    @Test
    @DisplayName("기본 바인딩을 조회할 수 있다")
    void defaultBindingsAreQueryable() {
        assertEquals(KeyCode.SPACE, handler.getBinding(InputHandler.Action.ATTACK));
        assertEquals(KeyCode.M, handler.getBinding(InputHandler.Action.MAGIC));
        assertEquals(KeyCode.E, handler.getBinding(InputHandler.Action.ITEM));
        assertEquals(KeyCode.SHIFT, handler.getBinding(InputHandler.Action.DASH));
        assertEquals(KeyCode.ESCAPE, handler.getBinding(InputHandler.Action.PAUSE));
        assertEquals(KeyCode.ENTER, handler.getBinding(InputHandler.Action.ACTION));
    }

    @Test
    @DisplayName("공격 키를 재바인딩하면 새 키가 동작하고 기존 키는 무시된다")
    void attackKeyIsRebindable() throws Exception {
        handler.rebind(InputHandler.Action.ATTACK, KeyCode.J);
        assertEquals(KeyCode.J, handler.getBinding(InputHandler.Action.ATTACK));

        press(KeyCode.J);
        assertTrue(handler.isAttackPressed());

        handler.update();
        release(KeyCode.J);

        press(KeyCode.SPACE);
        assertFalse(handler.isAttackPressed());
    }

    @Test
    @DisplayName("일시정지 키를 재바인딩할 수 있다")
    void pauseKeyIsRebindable() throws Exception {
        handler.rebind(InputHandler.Action.PAUSE, KeyCode.P);
        press(KeyCode.P);
        assertTrue(handler.isPausePressed());
    }

    @Test
    @DisplayName("null 키로는 재바인딩되지 않는다")
    void rebindIgnoresNull() {
        handler.rebind(InputHandler.Action.ACTION, null);
        assertEquals(KeyCode.ENTER, handler.getBinding(InputHandler.Action.ACTION));
    }
}