package com.survivalcoding.game.battle;

import com.survivalcoding.game.entity.Hero;
import com.survivalcoding.game.entity.Monster;
import com.survivalcoding.game.entity.StatusEffect;
import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.GameConfig;
import com.survivalcoding.game.animation.ParticleEffect;
import com.survivalcoding.game.animation.FloatingText;
import com.survivalcoding.game.audio.SoundManager;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Battle system handling combat mechanics, damage calculation, and status effects.
 */
public class BattleSystem {
    
    private final GameState gameState;
    private final Random random;
    
    // Combat constants
    
    public BattleSystem(GameState gameState) {
        this(gameState, new Random());
    }
    
    public BattleSystem(GameState gameState, Random random) {
        this.gameState = gameState;
        this.random = random;
    }
    
    /**
     * Process hero attack on monster.
     */
    public boolean heroAttack(Monster monster, int baseDamage) {
        if (monster == null || !monster.isAlive()) return false;
        
        Hero hero = gameState.getHero();
        if (hero == null || !hero.isAlive()) return false;
        
        // Check dodge
        if (random.nextDouble() < getMonsterDodgeChance(monster)) {
            showMiss(monster);
            return false;
        }
        
        // Calculate damage
        int damage = calculateDamage(baseDamage, hero.getLevel(), monster.getType());
        
        // Check critical hit
        boolean isCritical = random.nextDouble() < GameConfig.CRIT_CHANCE + hero.getLevel() * GameConfig.CRIT_CHANCE_PER_HERO_LEVEL;
        if (isCritical) {
            damage = (int)(damage * GameConfig.CRIT_MULTIPLIER);
            showCriticalHit(monster, damage);
        } else {
            showDamageNumber(monster, damage);
        }
        
        // Apply damage
        monster.takeDamage(damage);
        SoundManager.get().play(SoundManager.Effect.ATTACK);
        gameState.addScore(10);
        
        // Apply knockback
        double angle = Math.atan2(monster.getY() - hero.getY(), monster.getX() - hero.getX());
        monster.applyKnockback(Math.cos(angle) * 300, Math.sin(angle) * 300);
        
        // Hit particles
        gameState.addParticle(new ParticleEffect(
            monster.getX(), monster.getY(),
            ParticleEffect.ParticleType.HIT,
            Color.RED, 8
        ));
        
        // Screen shake
        gameState.shakeCamera(0.3);
        
        // Check death
        if (!monster.isAlive()) {
            onMonsterDeath(monster);
        }
        
        return true;
    }
    
    /**
     * Process hero magic attack.
     */
    public boolean heroMagic(Monster monster, int baseDamage) {
        if (monster == null || !monster.isAlive()) return false;
        
        Hero hero = gameState.getHero();
        if (hero == null || !hero.isAlive()) return false;
        
        int damage = calculateDamage(baseDamage, hero.getLevel(), monster.getType());
        damage = (int)(damage * 1.5); // Magic does more damage
        
        boolean isCritical = random.nextDouble() < GameConfig.CRIT_CHANCE * 2;
        if (isCritical) {
            damage = (int)(damage * GameConfig.CRIT_MULTIPLIER);
            showCriticalHit(monster, damage);
        }
        
        monster.takeDamage(damage);
        SoundManager.get().play(SoundManager.Effect.MAGIC);
        gameState.addScore(15);
        
        // Magic particles
        for (int i = 0; i < 12; i++) {
            gameState.addParticle(new ParticleEffect(
                monster.getX(), monster.getY(),
                ParticleEffect.ParticleType.MAGIC,
                Color.web("#FF8800"), 10
            ));
        }
        
        gameState.shakeCamera(0.5);
        
        if (!monster.isAlive()) {
            onMonsterDeath(monster);
        }
        
        return true;
    }
    
    /**
     * Process monster attack on hero.
     */
    public boolean monsterAttack(Monster monster) {
        Hero hero = gameState.getHero();
        if (hero == null || !hero.isAlive() || monster == null || !monster.isAlive()) {
            return false;
        }
        
        // Check hero dodge (based on level)
        double heroDodge = Math.min(GameConfig.HERO_DODGE_CHANCE_CAP, hero.getLevel() * GameConfig.HERO_DODGE_CHANCE_PER_LEVEL);
        if (random.nextDouble() < heroDodge) {
            showDodge(hero);
            return false;
        }
        
        int damage = monster.getAttackDamage();
        
        // Critical hit chance for monsters
        if (random.nextDouble() < GameConfig.MONSTER_CRIT_CHANCE) {
            damage = (int)(damage * GameConfig.MONSTER_CRIT_MULTIPLIER);
            showCriticalHit(hero, damage);
        }
        
        hero.takeDamage(damage);
        SoundManager.get().play(SoundManager.Effect.HURT);
        showDamageNumber(hero, damage);
        
        // Knockback
        double angle = Math.atan2(hero.getY() - monster.getY(), hero.getX() - monster.getX());
        hero.applyKnockback(Math.cos(angle) * 400, Math.sin(angle) * 400);
        
        gameState.shakeCamera(0.4);
        
        // Monster-specific effects
        applyMonsterEffect(monster, hero);
        
        return true;
    }
    
    /**
     * Process projectile hit.
     */
    public boolean projectileHit(Monster monster, int damage, Color particleColor) {
        if (monster == null || !monster.isAlive()) return false;
        
        monster.takeDamage(damage);
        gameState.addScore(15);
        
        showDamageNumber(monster, damage);
        
        for (int i = 0; i < 8; i++) {
            gameState.addParticle(new ParticleEffect(
                monster.getX(), monster.getY(),
                ParticleEffect.ParticleType.HIT,
                particleColor, 6
            ));
        }
        
        gameState.shakeCamera(0.2);
        
        if (!monster.isAlive()) {
            onMonsterDeath(monster);
        }
        
        return true;
    }
    
    private int calculateDamage(int baseDamage, int attackerLevel, Monster.MonsterType targetType) {
        double multiplier = 1.0;
        
        // Level bonus
        multiplier += attackerLevel * 0.05;
        
        // Type effectiveness (could be expanded)
        // For now, all types take normal damage
        
        // Random variance ±10%
        multiplier *= 0.9 + random.nextDouble() * 0.2;
        
        return (int)(baseDamage * multiplier);
    }
    
    private double getMonsterDodgeChance(Monster monster) {
        return switch (monster.getType()) {
            case SLIME -> 0.05;
            case POISON_SLIME -> 0.1;
            case ZOMBIE -> 0.02;
            case KING_SLIME -> 0.08;
        };
    }
    
    private void applyMonsterEffect(Monster monster, Hero hero) {
        switch (monster.getType()) {
            case POISON_SLIME -> {
                hero.addStatusEffect(new StatusEffect(StatusEffect.Type.POISON, GameConfig.POISON_DURATION_SECONDS, GameConfig.POISON_DAMAGE_PER_TICK, GameConfig.POISON_TICK_INTERVAL_SECONDS));
                gameState.addParticle(new ParticleEffect(
                    hero.getX(), hero.getY(),
                    ParticleEffect.ParticleType.POISON,
                    Color.GREEN, 8
                ));
            }
            case ZOMBIE -> {
                // Zombie heals on hit
                monster.heal(2);
            }
            case KING_SLIME -> {
                if (random.nextDouble() < GameConfig.STUN_CHANCE) {
                    hero.addStatusEffect(new StatusEffect(StatusEffect.Type.STUN, GameConfig.STUN_DURATION_SECONDS, 0, GameConfig.POISON_TICK_INTERVAL_SECONDS));
                }
            }
        }
    }
    
    private void onMonsterDeath(Monster monster) {
        gameState.incrementMonstersKilled();
        gameState.addScore(50 + monster.getExpReward());
        
        Hero hero = gameState.getHero();
        if (hero != null) {
            hero.addExp(monster.getExpReward());
        }
        
        // Death particles
        for (int i = 0; i < 20; i++) {
            gameState.addParticle(new ParticleEffect(
                monster.getX(), monster.getY(),
                ParticleEffect.ParticleType.DEATH,
                monster.getType().getColor(), 20
            ));
        }
        
        // Experience orb
        gameState.addParticle(new ParticleEffect(
            monster.getX(), monster.getY(),
            ParticleEffect.ParticleType.EXP_ORB,
            Color.web("#FFD700"), 5
        ));
        
        gameState.shakeCamera(0.5);
    }
    
    private void showDamageNumber(Monster monster, int damage) {
        gameState.addFloatingText(new FloatingText(monster.getX(), monster.getY() - 20, "-" + damage, Color.RED));
    }

    private void showDamageNumber(Hero hero, int damage) {
        gameState.addFloatingText(new FloatingText(hero.getX(), hero.getY() - 20, "-" + damage, Color.RED));
    }
    
    private void showCriticalHit(Monster monster, int damage) {
        gameState.addFloatingText(new FloatingText(monster.getX(), monster.getY() - 32, "-" + damage + " CRIT", Color.YELLOW));
        for (int i = 0; i < 10; i++) {
            gameState.addParticle(new ParticleEffect(
                monster.getX(), monster.getY(),
                ParticleEffect.ParticleType.HIT,
                Color.YELLOW, 10
            ));
        }
    }
    
    private void showCriticalHit(Hero hero, int damage) {
        gameState.addFloatingText(new FloatingText(hero.getX(), hero.getY() - 32, "-" + damage + " CRIT", Color.YELLOW));
        for (int i = 0; i < 8; i++) {
            gameState.addParticle(new ParticleEffect(
                hero.getX(), hero.getY(),
                ParticleEffect.ParticleType.HIT,
                Color.RED, 8
            ));
        }
    }
    
    private void showMiss(Monster monster) {
        gameState.addFloatingText(new FloatingText(monster.getX(), monster.getY() - 20, "MISS", Color.WHITE));
        gameState.addParticle(new ParticleEffect(
            monster.getX(), monster.getY() - 20,
            ParticleEffect.ParticleType.WARNING,
            Color.WHITE, 5
        ));
    }
    
    private void showDodge(Hero hero) {
        gameState.addFloatingText(new FloatingText(hero.getX(), hero.getY() - 30, "DODGE", Color.CYAN));
        gameState.addParticle(new ParticleEffect(
            hero.getX(), hero.getY() - 30,
            ParticleEffect.ParticleType.WARNING,
            Color.CYAN, 5
        ));
    }
}