package com.survivalcoding.game.input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles keyboard input for the game.
 * Tracks key states for smooth movement and action detection.
 */
public class InputHandler {
    
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final Set<KeyCode> justPressedKeys = new HashSet<>();
    private final Set<KeyCode> justReleasedKeys = new HashSet<>();
    
    // Movement keys
    private static final KeyCode KEY_UP = KeyCode.W;
    private static final KeyCode KEY_DOWN = KeyCode.S;
    private static final KeyCode KEY_LEFT = KeyCode.A;
    private static final KeyCode KEY_RIGHT = KeyCode.D;
    private static final KeyCode KEY_UP_ALT = KeyCode.UP;
    private static final KeyCode KEY_DOWN_ALT = KeyCode.DOWN;
    private static final KeyCode KEY_LEFT_ALT = KeyCode.LEFT;
    private static final KeyCode KEY_RIGHT_ALT = KeyCode.RIGHT;
    
    public enum Action {
        ATTACK, MAGIC, ITEM, DASH, PAUSE, ACTION
    }

    private KeyCode keyAttack = KeyCode.SPACE;
    private KeyCode keyMagic = KeyCode.M;
    private KeyCode keyItem = KeyCode.E;
    private KeyCode keyDash = KeyCode.SHIFT;
    private KeyCode keyPause = KeyCode.ESCAPE;
    private KeyCode keyAction = KeyCode.ENTER;
    private static final KeyCode KEY_FPS_TOGGLE = KeyCode.F3;
    private static final KeyCode KEY_MUTE_TOGGLE = KeyCode.F4;
    private static final KeyCode KEY_VOLUME_DOWN = KeyCode.F5;
    private static final KeyCode KEY_VOLUME_UP = KeyCode.F6;
    
    public void initialize(Scene scene) {
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
    }
    
    private void onKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if (!pressedKeys.contains(code)) {
            justPressedKeys.add(code);
        }
        pressedKeys.add(code);
        event.consume();
    }
    
    private void onKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        pressedKeys.remove(code);
        justReleasedKeys.add(code);
        event.consume();
    }
    
    /**
     * Call once per frame to clear transient key states.
     */
    public void update() {
        justPressedKeys.clear();
        justReleasedKeys.clear();
    }
    
    // Movement queries
    public boolean isUpPressed() {
        return pressedKeys.contains(KEY_UP) || pressedKeys.contains(KEY_UP_ALT);
    }
    
    public boolean isDownPressed() {
        return pressedKeys.contains(KEY_DOWN) || pressedKeys.contains(KEY_DOWN_ALT);
    }
    
    public boolean isLeftPressed() {
        return pressedKeys.contains(KEY_LEFT) || pressedKeys.contains(KEY_LEFT_ALT);
    }
    
    public boolean isRightPressed() {
        return pressedKeys.contains(KEY_RIGHT) || pressedKeys.contains(KEY_RIGHT_ALT);
    }
    
    public double getMoveX() {
        double x = 0;
        if (isLeftPressed()) x -= 1;
        if (isRightPressed()) x += 1;
        return x;
    }
    
    public double getMoveY() {
        double y = 0;
        if (isUpPressed()) y -= 1;
        if (isDownPressed()) y += 1;
        return y;
    }
    
    public KeyCode getBinding(Action action) {
        return switch (action) {
            case ATTACK -> keyAttack;
            case MAGIC -> keyMagic;
            case ITEM -> keyItem;
            case DASH -> keyDash;
            case PAUSE -> keyPause;
            case ACTION -> keyAction;
        };
    }
    
    public void rebind(Action action, KeyCode key) {
        if (key == null) {
            return;
        }
        switch (action) {
            case ATTACK -> keyAttack = key;
            case MAGIC -> keyMagic = key;
            case ITEM -> keyItem = key;
            case DASH -> keyDash = key;
            case PAUSE -> keyPause = key;
            case ACTION -> keyAction = key;
        }
    }
    
    public boolean isAttackPressed() {
        return justPressedKeys.contains(keyAttack);
    }
    
    public boolean isAttackHeld() {
        return pressedKeys.contains(keyAttack);
    }
    
    public boolean isMagicPressed() {
        return justPressedKeys.contains(keyMagic);
    }
    
    public boolean isItemPressed() {
        return justPressedKeys.contains(keyItem);
    }
    
    public boolean isDashPressed() {
        return justPressedKeys.contains(keyDash);
    }
    
    public boolean isPausePressed() {
        return justPressedKeys.contains(keyPause);
    }
    
    public boolean isActionPressed() {
        return justPressedKeys.contains(keyAction);
    }
    
    public boolean isFpsTogglePressed() {
        return justPressedKeys.contains(KEY_FPS_TOGGLE);
    }
    
    public boolean isMuteTogglePressed() {
        return justPressedKeys.contains(KEY_MUTE_TOGGLE);
    }

    public boolean isVolumeDownPressed() {
        return justPressedKeys.contains(KEY_VOLUME_DOWN);
    }

    public boolean isVolumeUpPressed() {
        return justPressedKeys.contains(KEY_VOLUME_UP);
    }
    
    // Generic key queries
    public boolean isPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }
    
    public boolean isJustPressed(KeyCode key) {
        return justPressedKeys.contains(key);
    }
    
    public boolean isJustReleased(KeyCode key) {
        return justReleasedKeys.contains(key);
    }
}
