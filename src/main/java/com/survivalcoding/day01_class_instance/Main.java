package com.survivalcoding.day01_class_instance;

class Main {
    public static void main(String[] args) {
        Slime slime = new Slime();
        slime.hp = 35;

        SuperHero superHero = new SuperHero();
        superHero.attack();
    }
}