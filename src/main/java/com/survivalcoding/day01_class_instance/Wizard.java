package com.survivalcoding.day01_class_instance;

public class Wizard {
    int hp;
    int mp;
    String name;
    Wand wand;

    void heal(Hero hero) {
        int basePoint = 10;     // 기본회복 포인트
        int recovPoint = (int) (basePoint * this.wand.power);       // 지팡이에 의한 증폭
        hero.setHp(hero.getHp() + recovPoint);                      // 용사의 HP를 회복
    }
}
