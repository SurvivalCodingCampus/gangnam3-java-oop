package com.survivalcoding.day04.exam;

public class Slime {
    private static final int INIT_POWER = 10;
    private String suffix;
    private int hp;
    private int power;

    public Slime(String suffix) {
        setSuffix(suffix);
        setPower(power);
    }

    void attack(final Hero hero) {
        System.out.println("슬라임 " + suffix + "이/가 공격했다");
        System.out.println(power + "의 데미지");
        hero.takeDamage(power);
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

    public void setSuffix(final String name) {
        if (!Utils.isValidName(suffix)) {
            throw new IllegalArgumentException("이름에 널, 공란 불가");
        }
    }

    public int getPower() {
        return power;
    }
}
