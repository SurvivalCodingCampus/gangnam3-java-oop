package com.survivalcoding.day01_class_instance;

public class Hero {

    public int getHp;
    // name 캡슐화
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // hp 캡슐화
    private int hp;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // 기능 (method)

    void attack() {}
    void run() {}
    void sit(int sec) {}
    void slip() {}
    void sleep() {
        hp = 200;
    }
}
