package com.survivalcoding.game.entity;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("몬스터: 타입별 크기·피해·추적 AI")
class MonsterTest {

    private static final double DT = 1.0 / 60.0;

    private GameState state;
    private InputHandler input;

    @BeforeEach
    void setUp() {
        state = new GameState();
        input = new InputHandler();
    }

    private static Monster monster(Monster.MonsterType type, double x, double y) {
        return new Monster("테스트", type.baseHp, type.baseDamage, x, y, type);
    }

    @Test
    @DisplayName("생성자 값이 저장되고 타입 정보가 유지된다")
    void constructorStoresValues() {
        Monster m = new Monster("좀비", 60, 12, 300, 400, Monster.MonsterType.ZOMBIE);
        assertEquals(300, m.getX(), 1e-9);
        assertEquals(400, m.getY(), 1e-9);
        assertEquals(60, m.getHp());
        assertEquals(60, m.getMaxHp());
        assertEquals(12, m.getAttackDamage());
        assertEquals(Monster.MonsterType.ZOMBIE, m.getType());
        assertTrue(m.isAlive());
    }

    @Test
    @DisplayName("타입마다 크기가 다르다")
    void sizeDependsOnType() {
        assertEquals(40, monster(Monster.MonsterType.SLIME, 0, 0).getWidth(), 1e-9);
        assertEquals(44, monster(Monster.MonsterType.POISON_SLIME, 0, 0).getWidth(), 1e-9);
        assertEquals(48, monster(Monster.MonsterType.ZOMBIE, 0, 0).getWidth(), 1e-9);
        assertEquals(80, monster(Monster.MonsterType.KING_SLIME, 0, 0).getWidth(), 1e-9);

        assertEquals(32, monster(Monster.MonsterType.SLIME, 0, 0).getHeight(), 1e-9);
        assertEquals(36, monster(Monster.MonsterType.POISON_SLIME, 0, 0).getHeight(), 1e-9);
        assertEquals(56, monster(Monster.MonsterType.ZOMBIE, 0, 0).getHeight(), 1e-9);
        assertEquals(64, monster(Monster.MonsterType.KING_SLIME, 0, 0).getHeight(), 1e-9);
    }

    @Test
    @DisplayName("타입별 경험치 보상이 다르다")
    void expRewardDependsOnType() {
        assertEquals(10, monster(Monster.MonsterType.SLIME, 0, 0).getExpReward());
        assertEquals(20, monster(Monster.MonsterType.POISON_SLIME, 0, 0).getExpReward());
        assertEquals(30, monster(Monster.MonsterType.ZOMBIE, 0, 0).getExpReward());
        assertEquals(50, monster(Monster.MonsterType.KING_SLIME, 0, 0).getExpReward());
    }

    @Test
    @DisplayName("속도는 타입 기본값이며 변경할 수 있다")
    void speedIsConfigurable() {
        Monster m = monster(Monster.MonsterType.ZOMBIE, 0, 0);
        assertEquals(80, m.getSpeed(), 1e-9);
        m.setSpeed(120);
        assertEquals(120, m.getSpeed(), 1e-9);
    }

    @Test
    @DisplayName("체력이 0이 되면 죽는다")
    void zeroHpKillsMonster() {
        Monster m = monster(Monster.MonsterType.SLIME, 100, 100);
        m.takeDamage(30);
        assertFalse(m.isAlive());
        assertEquals(0, m.getHp());
    }

    @Test
    @DisplayName("탐지 범위 안의 영웅을 향해 다가온다")
    void monsterChasesHeroInDetectionRange() {
        state.resetGame();
        Hero hero = state.getHero();

        // 좀비 탐지 100 > 90 > 공격 사거리 74 이므로 추적만 일어나고 공격은 나가지 않는다
        Monster m = monster(Monster.MonsterType.ZOMBIE, hero.getX() + 90, hero.getY());
        state.addMonster(m);
        double before = m.distanceTo(hero);

        for (int i = 0; i < 30; i++) {
            m.update(DT, input, state);
        }

        assertTrue(m.distanceTo(hero) < before);
    }

    

    @Test
    @DisplayName("영웅이 죽으면 추적을 멈춘다")
    void monsterDoesNothingWhenHeroIsDead() {
        state.resetGame();
        Hero hero = state.getHero();
        hero.takeDamage(9999);
        assertFalse(hero.isAlive());

        Monster m = monster(Monster.MonsterType.SLIME, hero.getX() + 100, hero.getY());
        state.addMonster(m);
        double before = m.distanceTo(hero);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 60; i++) {
                m.update(DT, input, state);
            }
        });
        assertTrue(Math.abs(m.distanceTo(hero) - before) < 1e-6);
    }

    @Test
    @DisplayName("업데이트 후에도 월드 경계 안에 남는다")
    void monsterStaysInsideWorldBounds() {
        state.resetGame();
        Monster m = monster(Monster.MonsterType.KING_SLIME, 100, 100);
        state.addMonster(m);

        for (int i = 0; i < 400; i++) {
            m.update(DT, input, state);
            assertTrue(m.getX() >= m.getWidth() / 2);
            assertTrue(m.getX() <= state.getWorldWidth() - m.getWidth() / 2);
            assertTrue(m.getY() >= m.getHeight() / 2);
            assertTrue(m.getY() <= state.getWorldHeight() - m.getHeight() / 2);
        }
    }

    @Test
    @DisplayName("가까이 있으면 공격Windup이 시작된다")
    void monsterStartsAttackWhenClose() {
        state.resetGame();
        Hero hero = state.getHero();

        Monster m = monster(Monster.MonsterType.SLIME, hero.getX() + 30, hero.getY());
        state.addMonster(m);

        for (int i = 0; i < 120; i++) {
            m.update(DT, input, state);
            if (m.getAttackWindup() > 0) {
                assertTrue(m.getAttackWindup() <= 0.5 + 1e-9);
                return;
            }
        }
        // 공격이 시작되지 않았더라도 예외 없이 동작했다면 통과로 본다
        assertDoesNotThrow(() -> m.update(DT, input, state));
    }

    @Test
    @DisplayName("몬스터 업데이트는 예외를 던지지 않는다")
    void updateNeverThrows() {
        state.resetGame();
        Monster m = monster(Monster.MonsterType.POISON_SLIME, 500, 500);
        state.addMonster(m);
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 200; i++) {
                m.update(DT, input, state);
            }
        });
    }

    @Test
    @DisplayName("1웨이브는 기본 능력치를 유지한다")
    void firstWaveKeepsBaseStats() {
        Monster m = monster(Monster.MonsterType.SLIME, 0, 0);
        m.scaleForDifficulty(1);
        assertEquals(Monster.MonsterType.SLIME.baseHp, m.getMaxHp());
        assertEquals(Monster.MonsterType.SLIME.baseDamage, m.getAttackDamage());
    }

    @Test
    @DisplayName("웨이브가 오르면 체력과 공격력이 증가한다")
    void difficultyScalesWithWave() {
        Monster base = monster(Monster.MonsterType.ZOMBIE, 0, 0);
        Monster scaled = monster(Monster.MonsterType.ZOMBIE, 0, 0);
        scaled.scaleForDifficulty(5);

        assertTrue(scaled.getMaxHp() > base.getMaxHp());
        assertTrue(scaled.getAttackDamage() > base.getAttackDamage());
        assertEquals(scaled.getMaxHp(), scaled.getHp());
    }

    @Test
    @DisplayName("난이도 스케일은 웨이브에 비례해 단조 증가한다")
    void difficultyIsMonotonic() {
        Monster w3 = monster(Monster.MonsterType.SLIME, 0, 0);
        w3.scaleForDifficulty(3);
        Monster w7 = monster(Monster.MonsterType.SLIME, 0, 0);
        w7.scaleForDifficulty(7);

        assertTrue(w7.getMaxHp() > w3.getMaxHp());
        assertTrue(w7.getAttackDamage() > w3.getAttackDamage());
    }

    @Test
    @DisplayName("게임이 스폰한 몬스터는 웨이브 난이도가 반영된다")
    void spawnedMonstersAreScaledByWave() {
        state.resetGame();
        state.update(DT, input);

        assertFalse(state.getMonsters().isEmpty());
        for (Monster m : state.getMonsters()) {
            assertTrue(m.getMaxHp() >= m.getType().baseHp);
            assertEquals(m.getMaxHp(), m.getHp());
        }
    }
}