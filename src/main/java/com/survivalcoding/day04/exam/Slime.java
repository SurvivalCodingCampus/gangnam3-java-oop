package com.survivalcoding.day04.exam;

public class Slime {
    private final String suffix;
    private int hp;

    public Slime(String suffix) {
        this.suffix = suffix;
    }

    void attack(final Hero hero) {
        System.out.println("슬라임 " + suffix + "이/가 공격했다");
        System.out.println("10의 데미지");
        hero.setHp(hero.getHp() - 10);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        this.hp = hp;
    }
}
