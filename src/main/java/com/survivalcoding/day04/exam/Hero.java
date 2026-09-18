package com.survivalcoding.day04.exam;

import java.util.Random;

public class Hero {
    public static final int MAX_HP = 100;
    private static int money = 100;

    private String name;
    private int hp;
    private Sword sword;

    public Hero() {
        this("김영웅");
    }

    public Hero(String name) {
        this.name = name;
        hp = MAX_HP;
    }

    public void setRandomMoney() {
        money = new Random().nextInt(1000);
    }

    public void bye() {
        System.out.println("용자는 이별을 고했다");
    }

    public void die() {
        System.out.println(name + "는 죽었다");
    }

    public void sleep() {
        hp = MAX_HP;
        System.out.println(name +"는 잠을 자고 회복했다!");
    }

    public void attack() {
        System.out.println(name + "는 공격했다!");
        System.out.println("적에게 5포인트 데미지를 주었다");
    }

    public void setHp(final int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }
}
