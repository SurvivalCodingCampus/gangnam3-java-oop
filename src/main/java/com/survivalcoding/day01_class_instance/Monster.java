package com.survivalcoding.day01_class_instance;

public abstract class Monster {
    public void run() {
        System.out.println("1");
    }

    public static void main(String[] args) {
        Slime slime = new Slime("A", 10);
        Monster monster = new Slime("A", 10);
        slime.run();
        monster.run();
    }
}
