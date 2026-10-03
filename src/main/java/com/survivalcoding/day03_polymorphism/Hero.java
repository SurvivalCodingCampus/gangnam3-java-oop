package com.survivalcoding.day03_polymorphism;

import com.survivalcoding.day01_class_instance.Slime;

public class Hero extends Character {

    // constant
    private static final int ATTACK_DAMAGE = 10;

    // constructor
    public Hero(String name, int hp) {
        super(name, hp);
    }

    // method
    @Override
    void attack(Slime slime) {
        System.out.println(this.getName() + "이 " + slime.getSuffix() + "을 공격했다.");
        slime.takeDamage(ATTACK_DAMAGE);
    }
}
