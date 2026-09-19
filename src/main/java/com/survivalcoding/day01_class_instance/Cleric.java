package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Cleric {

    // constant
    public static final int MAX_HP = 50;
    public static final int MAX_MP = 10;
    public static final int COST_MP = 5;
    
    private static final int RANDOM_BOUND = 3;
    private static final Random RANDOM = new Random();

    // field
    private String name;
    private int hp = MAX_HP;
    private int mp = MAX_MP;

    // constructor
    public Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }

    public Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }

    public Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // getter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    // method
    public void selfAid() {
        if (COST_MP > this.mp) {
            System.out.println("MP가 부족합니다");
            return;
        }
        this.mp -= COST_MP;
        this.hp = MAX_HP;
    }

    public int pray(int sec) {
        if(sec <= 0) {
            throw new IllegalArgumentException("sec(초)는 최소 1초 이상 요구됩니다");
        }

        int heal = RANDOM.nextInt(RANDOM_BOUND) + sec;
        int amount = this.mp + heal;

        if (amount > MAX_MP) {
            this.mp = MAX_MP;
        } else {
            this.mp += heal;
        }

        return heal;
    }
}
