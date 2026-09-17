package com.survivalcoding;

public class Hero {
    // 필드(field), 멤버변수(member variable),속성(property), 전역변수,
    String name;
    int hp;

    // 기능 (method)
    void attack() {}
    void run() {}
    void sit(int sec) {}
    void slip() {}
    void sleep() {
        hp = 200;
    }
}
