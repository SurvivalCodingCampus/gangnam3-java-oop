package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Hero {
    static int money = 100;

    static void setRandomMoney() {
        Random random = new Random();
        money = random.nextInt(1000); // 0~999
    }

    // 필드(field), 멤버변수(member variable),속성(property), 전역변수,
    String name;
    int hp;

    Hero() {
        this("홍길동");
        sleep();
    }

    Hero(String name) {
        hp = 100;
        this.name = name;
    }

    Hero(int hp) {
        this.hp = hp;
    }

    // 기능 (method)
    void attack() {}
    void run() {}
    void sit(int sec) {}
    void slip() {}
    void sleep() {
        hp = 100;
    }
}
