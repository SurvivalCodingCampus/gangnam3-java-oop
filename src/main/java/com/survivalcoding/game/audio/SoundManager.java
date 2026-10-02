package com.survivalcoding.game.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SoundManager {

    public enum Effect {
        ATTACK(700, 60),
        MAGIC(920, 120),
        HIT(320, 50),
        HURT(180, 150),
        MENU(520, 80);

        final int frequency;
        final int durationMs;

        Effect(int frequency, int durationMs) {
            this.frequency = frequency;
            this.durationMs = durationMs;
        }
    }

    private static final SoundManager INSTANCE = new SoundManager();
    private static final float SAMPLE_RATE = 44100f;

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "sound");
        thread.setDaemon(true);
        return thread;
    });

    private final boolean available;

    private volatile boolean muted = false;
    private volatile double volume = 0.6;

    private SoundManager() {
        this.available = detectAudio();
    }

    public static SoundManager get() {
        return INSTANCE;
    }

    private static boolean detectAudio() {
        try {
            return AudioSystem.getMixerInfo().length > 0;
        } catch (Throwable unavailable) {
            return false;
        }
    }

    public void play(Effect effect) {
        if (effect == null || muted || !available) {
            return;
        }
        double currentVolume = volume;
        executor.submit(() -> {
            try {
                emit(effect, currentVolume);
            } catch (Throwable ignored) {
            }
        });
    }

    private void emit(Effect effect, double currentVolume) throws Exception {
        byte[] data = tone(effect.frequency, effect.durationMs, currentVolume);
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
            line.open(format);
            line.start();
            line.write(data, 0, data.length);
            line.drain();
            line.stop();
        }
    }

    private byte[] tone(int frequency, int durationMs, double currentVolume) {
        int samples = (int) (SAMPLE_RATE * durationMs / 1000.0);
        byte[] buffer = new byte[samples * 2];
        for (int i = 0; i < samples; i++) {
            double angle = 2.0 * Math.PI * frequency * i / SAMPLE_RATE;
            double envelope = 1.0 - (double) i / samples;
            short value = (short) (Math.sin(angle) * envelope * currentVolume * Short.MAX_VALUE * 0.6);
            buffer[i * 2] = (byte) (value & 0xFF);
            buffer[i * 2 + 1] = (byte) ((value >> 8) & 0xFF);
        }
        return buffer;
    }

    public void toggleMute() {
        muted = !muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
    }

    public double getVolume() {
        return volume;
    }

    public boolean isAvailable() {
        return available;
    }
}
