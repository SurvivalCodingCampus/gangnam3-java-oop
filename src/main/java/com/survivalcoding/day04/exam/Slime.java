package com.survivalcoding.day04.exam;

public class Slime {
    static final int MAX_HP = 20;
    static final int INIT_POWER = 10;
    private String suffix;
    private int hp;
    private int power;
    private boolean isDead;

    public Slime(String suffix) {
        setSuffix(suffix);
        setPower(INIT_POWER);
        setHp(MAX_HP);
    }

    public void attack(final Hero hero) {
        attack(hero, power);
    }

    public void attack(final Hero hero, final int damage) {
        if (hero == null) {
            throw new IllegalArgumentException("히어로 널");
        }

        if (damage < 1) {
            throw new IllegalArgumentException("데미지는 1이상이어야 함");
        }

        if (hero.isDead()) {
            return;
        }

        System.out.println("슬라임 " + suffix + "이/가 공격했다");
        System.out.println(damage + "의 데미지");
        hero.takeDamage(damage);
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

    private void die() {
        isDead = true;
        System.out.println("슬라임" + suffix + "는 죽었다");
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        if (hp < 0) {
            throw new IllegalArgumentException("0보다 커야함");
        }

        this.hp = hp;
    }

    public void setPower(final int power) {
        if (power <= 0) {
            throw new IllegalArgumentException("1보다 커야함");
        }

        this.power = power;
    }

    public void setSuffix(final String suffix) {
        if (!Utils.isValidName(suffix)) {
            throw new IllegalArgumentException("이름에 널, 공란 불가");
        }

        this.suffix = suffix;
    }

    public int getPower() {
        return power;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
