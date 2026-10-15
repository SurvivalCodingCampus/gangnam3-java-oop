package com.survivalcoding.day01_class_instance;

class Main {
    public static void main(String[] args) {
        Slime slime = new Slime("A", 10);
        slime.setHp(35);

        SuperHero superHero = new SuperHero("홍길동");
        superHero.attack();
    }
}