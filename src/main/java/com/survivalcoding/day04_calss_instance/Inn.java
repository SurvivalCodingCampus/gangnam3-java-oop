package com.survivalcoding.day04_calss_instance;

public class Inn {
    static final int INN_FEE_HP = 100;
    
    public void checkIn(Hero hero) {
        int hero_hp = hero.getHp() - INN_FEE_HP;
        hero.setHp(hero_hp);
    }
}
