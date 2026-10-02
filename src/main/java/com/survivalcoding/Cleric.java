package com.survivalcoding;

import java.util.Random;

public class Cleric {
    private static final int MAX_HP = 50;
    private static final int MAX_MP = 10;
    private static final int SELF_AID_COST = 5;

    private final String name;
    private final int mp = MAX_MP;
    private int hp = MAX_HP;

    public Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        setMp(mp);
    }

    public Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }

    public Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        if (mp < 0 || mp > MAX_MP) {
            throw new IllegalArgumentException("MP는 0 이상 최대 MP 이하이어야");
        }
        this.mp = mp;
    }

    public int getMaxHp() {
        return MAX_HP;
    }

    public int getMaxMp() {
        return MAX_MP;
    }

    public void selfAid() {
        if (mp < SELF_AID_COST) {
            return;
        }

        mp -= SELF_AID_COST;
        hp = MAX_HP;
    }

    public int pray(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("기도 시간은 음수일 수 없습니다.");
        }

        int bonus = new Random().nextInt(3);
        int recovery = seconds + bonus;
        int maxRecovery = MAX_MP = mp;
        int actualRecovery = Math.min(recovery, maxRecovery);

        mp += actualRecovery;
        return actualRecovery;
    }
}
