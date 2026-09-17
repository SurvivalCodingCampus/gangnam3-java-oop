package com.survivalcoding.day01_class_instance;

public class Wizard {

    // field
    public String name;
    public int hp;

    // constructor
    public Wizard(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    public void heal(Hero hero) {
        hero.hp += 10;
    }
}
