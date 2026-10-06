package com.survivalcoding.game;

/**
 * Central balance values so tuning no longer requires hunting magic numbers.
 */
public final class GameConfig {

    private GameConfig() {
    }

    public static final double HERO_ATTACK_COOLDOWN_SECONDS = 0.4;
    public static final double HERO_MAGIC_COOLDOWN_SECONDS = 2.0;
    public static final double HERO_DASH_COOLDOWN_SECONDS = 1.5;
    public static final double HERO_ITEM_COOLDOWN_SECONDS = 1.0;
    public static final double HERO_DASH_DURATION_SECONDS = 0.2;
    public static final double HERO_DASH_SPEED_MULTIPLIER = 3.0;
    public static final double HERO_ATTACK_RANGE = 80;
    public static final int HERO_POTION_HEAL = 30;
    public static final int HERO_MAGIC_COST = 15;
    public static final double HERO_MAGIC_PROJECTILE_SPEED = 500;
    public static final double HERO_MP_REGEN_PER_SECOND = 5;

    public static final double MONSTER_ATTACK_COOLDOWN_SECONDS = 1.5;
    public static final double MONSTER_ATTACK_WINDUP_SECONDS = 0.5;
    public static final double MONSTER_ATTACK_RANGE = 50;
    public static final double MONSTER_HEALTH_GROWTH_PER_WAVE = 0.15;
    public static final double MONSTER_DAMAGE_GROWTH_PER_WAVE = 0.10;

    public static final double CRIT_CHANCE = 0.1;
    public static final double CRIT_CHANCE_PER_HERO_LEVEL = 0.005;
    public static final double CRIT_MULTIPLIER = 2.0;
    public static final double MAGIC_DAMAGE_MULTIPLIER = 1.5;
    public static final double MONSTER_CRIT_CHANCE = 0.05;
    public static final double MONSTER_CRIT_MULTIPLIER = 1.5;
    public static final double HERO_DODGE_CHANCE_PER_LEVEL = 0.01;
    public static final double HERO_DODGE_CHANCE_CAP = 0.15;

    public static final int FINAL_WAVE = 10;
    public static final int MAX_MONSTERS_PER_WAVE = 15;

    public static final double POISON_DURATION_SECONDS = 3.0;
    public static final int POISON_DAMAGE_PER_TICK = 3;
    public static final double POISON_TICK_INTERVAL_SECONDS = 0.5;
    public static final double STUN_DURATION_SECONDS = 1.0;
    public static final double STUN_CHANCE = 0.1;
}