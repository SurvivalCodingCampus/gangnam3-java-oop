package com.survivalcoding;

public class Slime {
    private String name;
    private int hp;

    Slime() {
    }

    ;

    Slime(String suffix, int hp) {
        this.name = suffix;
        this.hp = hp;

    }

    public void attack(Hero hero) {
        System.out.println("슬라임" + name + "이 공격했다");
        System.out.println("10의 데미지");
        hero.setHp(hero.getHp() - 10);
    }
}
