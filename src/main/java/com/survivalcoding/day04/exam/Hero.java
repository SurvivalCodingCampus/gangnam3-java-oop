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

    public Hero(final String name) {
        this.name = name;
        hp = MAX_HP;
    }

    public void setRandomMoney() {
        money = new Random().nextInt(1000);
    }

    public void bye() {
        System.out.println("용자는 이별을 고했다");
    }

    private void die() {
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

    public void run() {
        System.out.println(name + "는 도망쳤다!");
        System.out.println("GAME OVER");
        System.out.println("최종 HP는" + hp + " 입니다");
    }

    public void slip() {
        hp -= 5;
        System.out.println(name + "는 넘어졌다!");
        System.out.println("5의 데미지");
    }

    public void sit(int sec) {
        hp += sec;
        System.out.println(name + "는 " + sec + "초 앉았다");
        System.out.println("HP가 " + sec + "포인트 회복되었다");
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

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 아니어야함");
        }

        if (name.length() <= 1) {
            throw new IllegalArgumentException("이름이 너무 짧음");
        }

        if (name.length() >= 8) {
            throw new IllegalArgumentException("이름이 너무 긺");
        }
    }
}
