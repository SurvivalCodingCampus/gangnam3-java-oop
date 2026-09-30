package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Hero {
    // 컴파일 타임 상수 : static 이면서 뒤에 값이 절대 안 변해
    public static final int money = 100;

    // 뒤에 있는게 new 가 있다던지 런타임에 실행되는 코드라면 이건 런타임
    public static final int randomInt = new Random().nextInt();

    static void setRandomMoney() {
        Random random = new Random();
//        money = random.nextInt(1000); // 0~999
    }

    // 필드(field), 멤버변수(member variable),속성(property), 전역변수,
    private String name;
    private int hp;

//    Hero() {
//        System.out.println("1번");
//    }

    public Hero(String name) {
        hp = 100;
        this.name = name;
    }

    public Hero(int hp) {
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // 기능 (method)
    void attack() {
        System.out.println("Hero 의 공격");
    }
    void run() {}
    void sit(int sec) {}
    void slip() {}
    void sleep() {
        hp = 100;
    }
}
