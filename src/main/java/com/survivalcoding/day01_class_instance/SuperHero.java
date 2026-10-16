package com.survivalcoding.day01_class_instance;


public class SuperHero extends Hero {
    private boolean isFlying; // false

//    SuperHero() {
//        System.out.println("2번");
//    }

    SuperHero(String name) {
        super(name);
    }

    SuperHero(int hp) {
        super(hp);
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    @Override
    void attack() {
        System.out.println("SuperHero 의 공격");

        super.attack();

        System.out.println("SuperHero 의 공격");
    }
}
