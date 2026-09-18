package com.survivalcoding.day04.exam;

public class Wizard {
    private int hp;
    private int mp;
    private String name;
    private Wand wand;

    private void heal(final Hero hero) {
        int basePoint = 10; // 기본회복 포인트
        int recovPoint = (int) (basePoint * wand.getPower()); // 지팡이에 의한 증폭
        hero.setHp(hero.getHp() + recovPoint); // 용사의 HP 회복
    }
}
