package com.survivalcoding.day04.exam;

public class Slime {
    private final String suffix;
    private int hp;
    private int power;

    public Slime(String suffix) {
        this.suffix = suffix;
        power = 10;
    }

    void attack(final Hero hero) {
        System.out.println("슬라임 " + suffix + "이/가 공격했다");
        System.out.println(power + "의 데미지");
        hero.setHp(hero.getHp() - power);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        this.hp = hp;
    }

    public int getPower() {
        return power;
    }
}
