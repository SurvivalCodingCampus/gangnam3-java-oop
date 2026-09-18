package com.survivalcoding.day01_class_instance;

public class Wizard {
    private int hp;
    private int mp;
    private String name;
    private Wand wand;

    // 캡슐화
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wand getWand() {
        return wand;
    }

    public void setWand(Wand wand) {
        this.wand = wand;
    }




    // 스킬 heal --> hero
    void heal(Hero hero) {
        int basePoint = 10;     // 기본회복 포인트
        int recovPoint = (int) (basePoint * this.wand.power);       // 지팡이에 의한 증폭
        hero.setHp(hero.getHp() + recovPoint);                      // 용사의 HP를 회복
    }
}
