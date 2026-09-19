package com.survivalcoding.day01_class_instance;

public class Sword {

    // field
    private String name;
    private int damage;

    // constructor
    public Sword(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    // getter
    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
