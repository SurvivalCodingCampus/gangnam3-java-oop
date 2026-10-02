package com.survivalcoding.day01_class_instance;

public class Hero {
    // 필드(field), 멤버변수(member variable),속성(property), 전역변수,
    String name;
    int hp;

    // 기능 (method)
    void attack() {
    }

    void run() {
        System.out.println(this.name + "는 도망");
        System.out.println("최종 HP는" + this.hp);
    }

    void sit(int sec) {
        this.hp += sec;
        System.out.println(this.name + "는 " + sed + "초 앉았다");
    }

    void slip() {
        this.hp -= 5;
        System.out.println(this.name + "5의 데미지를 앉고 넘어졌다.");
    }

    void sleep() {
        hp = 200;
    }
}
