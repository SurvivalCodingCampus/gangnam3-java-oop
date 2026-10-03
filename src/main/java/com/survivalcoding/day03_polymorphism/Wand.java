package com.survivalcoding.day03_polymorphism;

public class Wand {

    // field
    private String name;
    private int power;

    // constructor
    public Wand(String name, int power) {
        this.name = name;
        this.power = power;
    }

    // getter
    public String getName() {
        return this.name;
    }

    public int getPower() {
        return this.power;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
