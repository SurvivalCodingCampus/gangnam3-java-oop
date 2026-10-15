package com.survivalcoding.day01_class_instance;

public class Wizard {
    public final static int DEFAULT_MP = 100;

    protected String name;
    protected int hp;
    protected int mp;

    public Wizard(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    public Wizard(String name, int hp) {
        this(name, hp, DEFAULT_MP);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public void heal(Hero hero) {
        // AI야 도와줘
        if (mp < 10) {
            System.out.println("마나가 부족합니다");
            return;
        }

        hero.setHp(hero.getHp() + 20);
        this.mp -= 10;

        System.out.println("힐을 시전했습니다. 대상 HP: XX\" 출력");
    }
}
