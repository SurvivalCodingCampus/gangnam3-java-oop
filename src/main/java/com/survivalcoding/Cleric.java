package com;

public class Cleric {

    static final int MAX_HP = 50;
    static final int MAX_MP = 10;
    String name;
    int hp = 50;
    int mp = 10;

    // 이름, HP, MP를 모두 지정
    Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // 이름과 HP만 지정
    // MP는 MAX_MP로 초기화
    Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }

    // 이름만 지정
    // HP와 MP는 각각 최대치로 초기화
    Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }

    // 자기 자신을 회복
    void selfAid() {
        mp -= 5;
        hp = MAX_HP;
    }

    // 기도
    int pray(int second) {
        int recovery = second + (int) (Math.random() * 3);

        int beforeMp = mp;

        mp += recovery;

        if (mp > MAX_MP) {
            mp = MAX_MP;
        }

        return mp - beforeMp;
    }
}