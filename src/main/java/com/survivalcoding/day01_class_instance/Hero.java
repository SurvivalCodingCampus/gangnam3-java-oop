package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Hero {
    static int money = 100;

    static void setRandomMoney() {
        Random random = new Random();
        money = random.nextInt(1000); // 0~999
    }

    // 필드(field), 멤버변수(member variable),속성(property), 전역변수,
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름이 null이면 안 됨");
        }
        this.name = name;
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
