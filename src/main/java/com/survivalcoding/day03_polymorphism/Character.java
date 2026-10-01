package com.survivalcoding.day03_polymorphism;

import com.survivalcoding.day01_class_instance.Slime;

abstract class Character {

    // field
    private String name;
    private int hp;

    // constructor
    public Character(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    // method
    abstract void attack(Slime slime);

    // getter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
