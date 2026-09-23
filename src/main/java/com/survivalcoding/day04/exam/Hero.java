package com.survivalcoding.day04.exam;

import java.util.Random;

public class Hero {
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 8;
    public static final int MAX_RANDOM_MONEY = 1000;
    public static final int MAX_HP = 100;
    private static final int SLIP_DAMAGE = 5;

    private static int money = 100;

    private String name;
    private int hp;
    private Sword sword;
    private int power;

    public Hero() {
        this("김영웅");
    }

    public Hero(final String name) {
        this(name, MAX_HP);
    }

    public Hero(String name, int hp) {
        setName(name);
        setHp(hp);
    }

    public Hero(Sword sword, int hp, String name) {
        this.sword = sword;
        this.hp = hp;
        this.name = name;
        power = 10;
    }

    public void setRandomMoney() {
        money = new Random().nextInt(MAX_RANDOM_MONEY);
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

    public void attack(final Slime slime) {
        System.out.println(name + "이 공격했다");
        slime.takeDamage(slime.getPower());
    }

    public void run() {
        System.out.println(name + "이 도망쳤다!");
    }

    public void slip() {
        System.out.println(name + "는 넘어졌다!");
        System.out.println(SLIP_DAMAGE + "의 데미지");
        takeDamage(SLIP_DAMAGE);
    }

    public void sit(final int sec) {
        hp += sec;
        System.out.println(name + "는 " + sec + "초 앉았다");
        System.out.println("HP가 " + sec + "포인트 회복되었다");
    }

    public void setHp(final int hp) {
        if (hp <= 0) {
            throw new IllegalArgumentException("0보다 작은값은 불가 ");
        }

        this.hp = hp;
    }

    public void takeDamage(final int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("1보다 작은값은 불가 ");
        }

        if (hp - amount < 1) {
            die();
        }
    }

    public void takeHeal(final int amount) {
        setHp(Math.min(Hero.MAX_HP, hp + amount));
    }

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (!Utils.isValidName(name)) {
            throw new IllegalArgumentException("이름은 공란 불가");
        }

        if (name.length() <= MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("이름이 너무 짧음");
        }

        if (name.length() >= MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름이 너무 긺");
        }

        this.name = name;
    }

    public void setPower(final int power) {
        if (power < 1) {
            throw new IllegalArgumentException("파워는 1보다 커야 함");
        }

        this.power = power;
    }
}
