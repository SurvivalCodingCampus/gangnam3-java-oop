package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Cleric {

    public String name;
    public int hp = 50;
    public int mp = 10;

    public static final int MAX_HP = 50;
    public static final int MAX_MP = 10;
    public static final int RANDOM_BOUND = 3;
    public static final Random random = new Random();

    public void selfAid() {
        this.mp -= 5;
        this.hp = MAX_HP;
    }

    public int pray(int sec) {
        int heal = random.nextInt(RANDOM_BOUND) + sec;
        int amount = this.mp + heal;

        if (amount > MAX_MP) {
            this.mp = MAX_MP;
        } else {
            this.mp += heal;
        }

        return heal;
    }
}
