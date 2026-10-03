package com.survivalcoding.day03_polymorphism;

import com.survivalcoding.day01_class_instance.Slime;

public class Wizard extends Character {

    // constant
    private static final int ATTACK_DAMAGE = 5;

    // field
    private Wand wand;
    private int mp = 100;

    // constructor
    public Wizard(String name, int hp, Wand wand) {
        super(name, hp);
        this.wand = wand;
    }

    // method
    @Override
    void attack(Slime slime) {
        System.out.println(this.getName() + "이 " + slime.getSuffix() + "을 공격했다.");
        slime.takeDamage(ATTACK_DAMAGE);
    }

    public void fireball(Slime slime) {
        if (this.mp >= 20) {
            System.out.println(this.getName() + "이 파이어볼을 쏘았다.");
            slime.takeDamage(50);
            this.mp -= 20;
        } else {
            System.out.println("MP가 부족하여 파이어볼을 사용할 수 없습니다.");
        }
    }

    // getter
    public Wand getWand() {
        return this.wand;
    }

    public int getMp() {
        return this.mp;
    }
}
