package com.survivalcoding.day04_calss_instance;

public class Wizard {
    String name;
    int hp;
    
    void heal(Hero hero) {
        hero.setHp(hero.getHp() + 10);
        System.out.printf("%s의 HP 10을 회복했다!%n", hero.getName());
    }
}
