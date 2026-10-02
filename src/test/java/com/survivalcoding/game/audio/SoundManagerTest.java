package com.survivalcoding.game.audio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("사운드 매니저: 음소거·볼륨·무예외 재생")
class SoundManagerTest {

    private final SoundManager sound = SoundManager.get();

    @AfterEach
    void tearDown() {
        sound.setMuted(false);
        sound.setVolume(0.6);
    }

    @Test
    @DisplayName("싱글턴 인스턴스를 돌려준다")
    void singleton() {
        assertSame(SoundManager.get(), SoundManager.get());
    }

    @Test
    @DisplayName("음소거를 토글한다")
    void muteToggles() {
        sound.setMuted(false);
        sound.toggleMute();
        assertTrue(sound.isMuted());
        sound.toggleMute();
        assertFalse(sound.isMuted());
    }

    @Test
    @DisplayName("볼륨은 0과 1 사이로 제한된다")
    void volumeIsClamped() {
        sound.setVolume(2.0);
        assertEquals(1.0, sound.getVolume(), 1e-9);
        sound.setVolume(-1.0);
        assertEquals(0.0, sound.getVolume(), 1e-9);
        sound.setVolume(0.5);
        assertEquals(0.5, sound.getVolume(), 1e-9);
    }

    @Test
    @DisplayName("음소거 상태에서 재생은 예외 없이 무시된다")
    void playIsSafeWhenMuted() {
        sound.setMuted(true);
        assertDoesNotThrow(() -> {
            sound.play(SoundManager.Effect.ATTACK);
            sound.play(SoundManager.Effect.MAGIC);
            sound.play(SoundManager.Effect.HIT);
            sound.play(SoundManager.Effect.HURT);
            sound.play(null);
        });
    }

    @Test
    @DisplayName("오디오 가용 여부를 질의할 수 있다")
    void availabilityQueryable() {
        assertDoesNotThrow(sound::isAvailable);
    }
}
