package com.survivalcoding.day05_class_instance;

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
    public String getName() { // Wizard가 회복 대상의 이름을 읽을 때 사용한다.
        return name; // 기존 이름을 반환한다.
    } // getName 메서드를 마친다.

    public int getHp() { // Wizard가 현재 HP를 읽을 때 사용한다.
        return hp; // 기존 HP를 반환한다.
    } // getHp 메서드를 마친다.

    public void setHp(int hp) { // Wizard가 회복한 HP를 저장할 때 사용한다.
        this.hp = hp; // 전달받은 HP를 현재 객체에 저장한다.
    } // setHp 메서드를 마친다.
}
