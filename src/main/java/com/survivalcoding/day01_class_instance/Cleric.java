package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Cleric {

    public static final int MAX_HP = 50;
    public static final int MAX_MP = 10;
    public static final int COST_MP = 5;
    public static final int RANDOM_BOUND = 3;
    public static final Random RANDOM = new Random();

    public String name;
    public int hp = MAX_HP;
    public int mp = MAX_MP;

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

    public void selfAid() {
        if (COST_MP > this.mp) {
            System.out.println("MP가 부족합니다");
            return;
        }
        this.mp -= COST_MP;
        this.hp = MAX_HP;
    }

    public int pray(int sec) {
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
