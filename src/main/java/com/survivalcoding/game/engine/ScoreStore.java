package com.survivalcoding.game.engine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Persists the best local score.
 */
public class ScoreStore {
    private final Path file;

    public ScoreStore() {
        this(Paths.get("best-score.txt"));
    }

    public ScoreStore(Path file) {
        this.file = file;
    }

    public int load() {
        try {
            if (!Files.exists(file)) return 0;
            String text = Files.readString(file, StandardCharsets.UTF_8).trim();
            return Math.max(0, Integer.parseInt(text));
        } catch (Exception ignored) {
            return 0;
        }
    }

    public void save(int score) {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, Integer.toString(Math.max(0, score)), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }
}
