package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Cleric {
    // main 엔터
    public static void main(String[] args) {
        Cleric cleric = new Cleric("아서스", 10, -10);

//        cleric.setMp(-10);

        int a = 10;
        int b = a;  // 10 복사

        a = 20;

        System.out.println(b);  // 10

//        String name = "";
//        Math.min(10, 20);
//        Hero hero = new Hero();
//        Main main = new Main();
//
//        Cleric cleric = new Cleric("홍길동");
//        System.out.println(cleric.name);
//        System.out.println(cleric.hp);
//        System.out.println(cleric.mp);
//
//
    }

    // 정적 상수
    private static final int MAX_HP = 50;
    private static final int MAX_MP = 10;
    private static final int SELF_AID_COST = 5;

    private String name;
    private int hp;
    private int mp;

    // 생성자
    public Cleric(String name) {
        this(name, MAX_HP);     // 재사용
//        this.name = name;
//        this.hp = MAX_HP;
//        this.mp = MAX_MP;
    }

    public Cleric(String name, int hp) {
        this(name, hp, MAX_MP);     // 재사용
//        this.name = name;
//        this.hp = hp;
//        this.mp = MAX_MP;
    }

    public Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        setMp(mp);
    }

    // 메서드
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        if (mp < 0) {
            throw new IllegalArgumentException("MP는 음수일 수 없습니다.");
        }

        if (mp > MAX_MP) {
            throw new IllegalArgumentException("MP는 최대 MP를 넘을 수 없습니다.");
        }

        this.mp = mp;
    }

    // mp 를 5를 써서 hp 꽉 채우기
    public void selfAid() {
//        1. MP가 5보다 적으면 아무것도 하지 않습니다.
        if (mp < SELF_AID_COST) {
            return;
        }
//        2. MP가 5 이상이면 MP를 5 줄입니다.
        mp -= SELF_AID_COST;

//        3. HP를 최대 HP로 바꿉니다.
        hp = MAX_HP;

//        4. 반환값은 없습니다.
    }

    public int pray(int seconds) {
        Random random = new Random();
        int bonus = random.nextInt(3);
        return 0;
    }


}
