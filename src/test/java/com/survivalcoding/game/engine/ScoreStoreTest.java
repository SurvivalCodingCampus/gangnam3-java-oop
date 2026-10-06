package com.survivalcoding.game.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ScoreStore: best score 저장/로드")
class ScoreStoreTest {
    @Test
    void saveAndLoadRoundTrip() throws Exception {
        Path file = Files.createTempFile("best-score", ".txt");
        Files.deleteIfExists(file);

        ScoreStore store = new ScoreStore(file);
        store.save(123);

        assertEquals(123, store.load());
        assertTrue(Files.exists(file));
    }

    @Test
    void loadReturnsZeroWhenMissingOrInvalid() throws Exception {
        Path file = Files.createTempFile("best-score-invalid", ".txt");
        Files.writeString(file, "not-a-number");

        ScoreStore store = new ScoreStore(file);
        assertEquals(0, store.load());
    }
}
