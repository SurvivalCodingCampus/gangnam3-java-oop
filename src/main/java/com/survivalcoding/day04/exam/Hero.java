package com.survivalcoding.day04.exam;

import java.util.Random;

public class Hero {
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 8;
    private static final int MAX_RANDOM_MONEY = 1000;
    static final int MAX_HP = 100;
    private static final int SLIP_DAMAGE = 5;
    private static final int INIT_POWER = 10;

    private static int money = 100;

    private String name;
    private int hp;
    private Sword sword;
    int power;
    private boolean isDead;

    public Hero() {
        this("김영웅", MAX_HP);
    }

    public Hero(final String name) {
        this(name, MAX_HP);
    }

    public Hero(String name, int hp) {
        this(new Sword(), hp, name);
    }

    public Hero(Sword sword, int hp, String name) {
        setSword(sword);
        setHp(hp);
        setName(name);
        setPower(INIT_POWER);
    }

    public void setRandomMoney() {
        money = new Random().nextInt(MAX_RANDOM_MONEY);
    }

    public void bye() {
        System.out.println("용자는 이별을 고했다");
    }

    private void die() {
        isDead = true;
        System.out.println(name + "는 죽었다");
    }

    public void sleep() {
        hp = MAX_HP;
        System.out.println(name +"는 잠을 자고 회복했다!");
    }

    public void attack(final Slime slime, final int damage) {
        System.out.println(name + "이 공격했다");
        slime.takeDamage(damage);
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

    public void takeDamage(final int damage) {

        if (damage <= 0) {
            throw new IllegalArgumentException("1보다 작은값은 불가");
        }

        if (hp - damage <= 0) {
            die();
            hp = 0;
            return;
        }

        hp -= damage;
    }

    public void takeHeal(final int amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("힐은 0보다 커야 함");
        }

        hp = Math.min(Hero.MAX_HP, hp + amount);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        if (hp <= 0) {
            throw new IllegalArgumentException("0보다 작은값은 불가 ");
        }

        this.hp = hp;
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

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {

        if (sword == null) {
            throw new IllegalArgumentException("sword에 널");
        }

        this.sword = sword;
    }
}
