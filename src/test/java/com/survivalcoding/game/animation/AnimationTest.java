package com.survivalcoding.game.animation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("게임 애니메이션: 프레임 진행과 종료")
class AnimationTest {

    private static final double DT = 0.1;

    @Test
    @DisplayName("생성자 값이 그대로 저장된다")
    void constructorStoresValues() {
        Animation a = new Animation("walk", 4, 0.25, true);
        assertEquals("walk", a.getName());
        assertEquals(4, a.getFrameCount());
        assertEquals(0.25, a.getFrameDuration());
        assertTrue(a.isLoop());
        assertFalse(a.isFinished());
        assertEquals(0, a.getCurrentFrame());
    }

    @Test
    @DisplayName("frameDuration이 지나야 프레임이 넘어간다")
    void frameStaysBeforeDuration() {
        Animation a = new Animation("walk", 4, 0.2, true);
        a.update(0.1);
        assertEquals(0, a.getCurrentFrame());
    }

    @Test
    @DisplayName("반복 애니메이션은 마지막 프레임 다음에 0으로 돌아간다")
    void loopingAnimationWraps() {
        Animation a = new Animation("walk", 3, 0.1, true);
        for (int i = 0; i < 3; i++) a.update(0.1);
        assertEquals(0, a.getCurrentFrame());
        assertFalse(a.isFinished());
    }

    @Test
    @DisplayName("반복 애니메이션은 frameCount를 넘지 않는다")
    void loopingNeverExceedsFrameCount() {
        Animation a = new Animation("idle", 4, 0.1, true);
        for (int i = 0; i < 50; i++) {
            a.update(0.1);
            assertTrue(a.getCurrentFrame() < 4);
        }
    }

    @Test
    @DisplayName("반복하지 않는 애니메이션은 마지막 프레임에서 종료된다")
    void nonLoopingFinishesOnLastFrame() {
        Animation a = new Animation("attack", 3, 0.1, false);
        a.update(0.1);
        a.update(0.1);
        assertEquals(2, a.getCurrentFrame());
        assertFalse(a.isFinished());

        a.update(0.1);
        assertTrue(a.isFinished());
        assertEquals(2, a.getCurrentFrame());
    }

    @Test
    @DisplayName("종료된 비반복 애니메이션은 더 이상 진행하지 않는다")
    void finishedAnimationIsFrozen() {
        Animation a = new Animation("attack", 2, 0.1, false);
        for (int i = 0; i < 10; i++) a.update(DT);
        assertTrue(a.isFinished());
        assertEquals(1, a.getCurrentFrame());
    }

    @Test
    @DisplayName("reset()으로 프레임과 종료 상태가 초기화된다")
    void resetRestoresInitialState() {
        Animation a = new Animation("attack", 2, 0.1, false);
        for (int i = 0; i < 5; i++) a.update(DT);
        assertTrue(a.isFinished());

        a.reset();

        assertEquals(0, a.getCurrentFrame());
        assertFalse(a.isFinished());
    }

    @Test
    @DisplayName("deltaTime이 0이면 아무 변화가 없다")
    void zeroDeltaChangesNothing() {
        Animation a = new Animation("walk", 4, 0.1, true);
        a.update(0.0);
        assertEquals(0, a.getCurrentFrame());
        assertFalse(a.isFinished());
    }
}