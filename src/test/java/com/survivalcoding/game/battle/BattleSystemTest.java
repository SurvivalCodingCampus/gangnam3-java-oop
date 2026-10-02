package com.survivalcoding.game.battle;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.entity.Hero;
import com.survivalcoding.game.entity.Monster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("전투 시스템: 공격·마법·투사체 판정")
class BattleSystemTest {

    private GameState state;
    private BattleSystem battle;
    private Hero hero;

    @BeforeEach
    void setUp() {
        state = new GameState();
        state.resetGame();
        battle = new BattleSystem(state, new Random(0));
        hero = state.getHero();
    }

    private Monster monster(int hp) {
        Monster m = new Monster("전환 practise사", hp, 5, hero.getX() + 100, hero.getY(),
                Monster.MonsterType.SLIME);
        state.addMonster(m);
        return m;
    }

    @Test
    @DisplayName("생성자에 GameState만 있으면 된다")
    void constructsWithGameState() {
        assertDoesNotThrow(() -> new BattleSystem(new GameState()));
    }

    @Test
    @DisplayName("살아 있는 몬스터는 공격을 받는다")
    void heroAttackDamagesMonster() {
        Monster m = monster(60);
        int hpBefore = m.getHp();

        boolean hit = battle.heroAttack(m, 20);

        assertTrue(hit);
        assertTrue(m.getHp() < hpBefore);
    }

    @Test
    @DisplayName("이미 죽은 몬스터는 공격할 수 없다")
    void cannotAttackDeadMonster() {
        Monster m = monster(10);
        m.takeDamage(999);

        assertFalse(battle.heroAttack(m, 20));
    }

    @Test
    @DisplayName("null 몬스터는 공격할 수 없다")
    void cannotAttackNull() {
        assertFalse(battle.heroAttack(null, 20));
    }

    @Test
    @DisplayName("영웅이 죽으면 공격이 실패한다")
    void cannotAttackWhenHeroIsDead() {
        Monster m = monster(60);
        hero.takeDamage(9999);

        assertFalse(battle.heroAttack(m, 20));
    }

    @Test
    @DisplayName("공격은 점수를 올린다")
    void heroAttackAddsScore() {
        Monster m = monster(60);
        int scoreBefore = state.getScore();

        battle.heroAttack(m, 20);

        assertTrue(state.getScore() > scoreBefore);
    }

    @Test
    @DisplayName("마법은 몬스터를 공격하고 더 많은 점수를 준다")
    void heroMagicDamagesMonster() {
        Monster m = monster(60);
        int hpBefore = m.getHp();

        assertTrue(battle.heroMagic(m, 20));
        assertTrue(m.getHp() < hpBefore);
    }

    @Test
    @DisplayName("마법으로 처치하면 처치 수가 증가한다")
    void magicKillIncrementsKillCount() {
        Monster m = monster(1);
        int killsBefore = state.getMonstersKilled();

        battle.heroMagic(m, 50);

        assertFalse(m.isAlive());
        assertTrue(state.getMonstersKilled() > killsBefore);
    }

    @Test
    @DisplayName("몬스터는 영웅을 공격할 수 있다")
    void monsterAttackHurtsHero() {
        Monster m = monster(60);
        m.setPosition(hero.getX() + 20, hero.getY());
        int hpBefore = hero.getHp();

        boolean hit = battle.monsterAttack(m);

        assertTrue(hit);
        assertTrue(hero.getHp() < hpBefore);
    }

    @Test
    @DisplayName("영웅이 죽은 뒤에는 몬스터가 공격할 수 없다")
    void monsterCannotAttackDeadHero() {
        Monster m = monster(60);
        hero.takeDamage(9999);

        assertFalse(battle.monsterAttack(m));
    }

    @Test
    @DisplayName("투사체는 몬스터에게 피해를 주고 점수를 준다")
    void projectileHitDamagesMonster() {
        Monster m = monster(60);
        int hpBefore = m.getHp();
        int scoreBefore = state.getScore();

        assertTrue(battle.projectileHit(m, 15, Color.ORANGE));

        assertTrue(m.getHp() < hpBefore);
        assertTrue(state.getScore() > scoreBefore);
    }

    @Test
    @DisplayName("죽은 몬스터에는 투사체가 맞지 않는다")
    void projectileMissesDeadMonster() {
        Monster m = monster(10);
        m.takeDamage(999);

        assertFalse(battle.projectileHit(m, 15, Color.ORANGE));
    }

    @Test
    @DisplayName("반복 공격에도 예외가 없다")
    void repeatedCombatDoesNotThrow() {
        Monster m = monster(500);
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                battle.heroAttack(m, 5);
                battle.heroMagic(m, 5);
                battle.monsterAttack(m);
                battle.projectileHit(m, 5, Color.CYAN);
            }
        });
    }
}