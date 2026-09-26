package com.survivalcoding.day04.exam;

import java.util.Random;

public class Hero {
    static final int MAX_HP = 100;
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 8;
    private static final int MAX_RANDOM_MONEY = 1000;
    private static final int SLIP_DAMAGE = 5;
    private static final int DEFAULT_POWER = 10;
    private static final String DEFAULT_NAME = "김영웅";

    private static int money;

    private String name;
    private int hp;
    private Sword sword;
    private int power;
    private boolean isDead;

    protected Hero(Builder builder) {
        if (!Utils.isValidName(builder.name)) {
            throw new IllegalArgumentException("이름은 공란 불가");
        }

        if (builder.name.length() <= MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("이름이 너무 짧음");
        }

        if (builder.name.length() >= MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름이 너무 긺");
        }

        if (!Utils.isWithinRange(builder.hp, MAX_HP, 1)) {
            throw new IllegalArgumentException("1이상 " + MAX_HP + "이하 입력");
        }

        if (builder.sword == null) {
            throw new IllegalArgumentException("sword에 널");
        }

        if (builder.power < 1) {
            throw new IllegalArgumentException("파워는 1보다 커야 함");
        }

        name = builder.name;
        hp = builder.hp;
        sword = builder.sword;
        power = builder.power;
        money = new java.util.Random().nextInt(MAX_RANDOM_MONEY);
        isDead = false;
    }

    // region Func

    public void bye() {
        System.out.println("용자는 이별을 고했다");
    }

    private void die() {
        isDead = true;
        System.out.println(name + "는 죽었다");
    }

    public void sleep() {
        hp = MAX_HP;
        System.out.println(name + "는 잠을 자고 회복했다!");
    }

    public void attack(final Slime slime) {
        attack(slime, power);
    }

    private void attack(final Slime slime, final int damage) {
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
        if (isDead) {
            System.out.println("이미 죽음");
            return;
        }

        if (damage < 1) {
            throw new IllegalArgumentException("1보다 작은값은 불가");
        }

        hp -= damage;

        if (hp <= 0) {
            hp = 0;
            die();
        }
    }

    public void takeHeal(final int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("힐은 0보다 커야 함");
        }

        hp = Math.min(Hero.MAX_HP, hp + amount);
    }

    // endregion Func

    // region Getter

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }

    public boolean isDead() {
        return isDead;
    }

    public Sword getSword() {
        return sword;
    }

    public int getPower() {
        return power;
    }

    public static int getMoney() {
        return money;
    }

    // endregion

    public static class Builder {
        protected String name = DEFAULT_NAME;
        protected int hp = MAX_HP;
        protected Sword sword = new Sword.Builder().build();
        protected int power = DEFAULT_POWER;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public Builder sword(Sword sword) {
            this.sword = sword;
            return this;
        }

        public Builder power(int power) {
            this.power = power;
            return this;
        }

        public Hero build() {
            return new Hero(this);
        }
    }
}