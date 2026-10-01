package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Cleric {
    private static final int MP_COST = 5;

    public static final int maxHp = 50;
    public static final int maxMp = 10;

    private String name;
    private int hp = maxHp;
    private int mp = maxMp;

    public Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    public Cleric(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    public Cleric(String name) {
        this.name = name;
    }

    public String getName() {
        return name + " 천재";
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        if (mp < 0) {
            throw new IllegalArgumentException("mp 는 음수일 수 없다 " + mp);
        }
        this.mp = mp;
    }

    void selfAid() {
        if (mp < MP_COST) {
            return;
        }

        mp -= MP_COST;
        hp = maxHp;
    }

    int pray(int sec) {
        // 0 ~ 2 포인트 랜덤 보정값 적용
        int bonus = new Random().nextInt(3);
        int recovery = sec + bonus;

        // 최대 MP를 초과하지 않도록 실제 회복 가능량 계산
        int maxRecovery = this.maxMp - this.mp;
        int actualRecovery = Math.min(recovery, maxRecovery);

        // MP 회복 적용 및 실제 회복량 반환
        this.mp += actualRecovery;
        return actualRecovery;
    }
}
