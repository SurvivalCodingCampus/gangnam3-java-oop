package com.survivalcoding;

public class Slime {
    private String name;
    private int hp;

    public Slime(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void attack(Hero hero) {
        System.out.println("슬라임 " + getName() + "가 공격했다.");
        System.out.println("10의 데미지");

        hero.setHp(hero.getHp() - 10);
    }
}