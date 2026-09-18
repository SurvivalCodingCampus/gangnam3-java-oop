package com.survivalcoding;

import java.util.Random;

public class Cleric {
    // 최대치 상수 (전체 공유)
    static final int MP_COST = 5;
    static final int maxHp = 50;
    static final int maxMp = 10;

    String name;
    int hp;
    int mp;


    // 생성자 1 (이름, HP, MP 모두 지정)
    Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // 생성자 2 (이름, HP 지정, MP는 지정X)
    Cleric(String name, int hp) {
        this(name, hp, maxMp);
    }

    // 생성자 3 (이름만 지정)
    Cleric(String name) {
        this(name, maxHp, maxMp);
    }

    public static void main(String[] args) {

        Cleric cleric1 = new Cleric("아서스", 40, 5);
        System.out.println(cleric1.name + "생성되었습니다! [체력: " + cleric1.hp + " 마나: " + cleric1.mp + "]");

        Cleric cleric2 = new Cleric("아서스2", 35, maxMp);
        System.out.println(cleric2.name + "생성되었습니다! [체력: " + cleric2.hp + " 마나: " + cleric2.mp + "]");

        Cleric cleric3 = new Cleric("아서스3", maxHp, maxMp);
        System.out.println(cleric3.name + "생성되었습니다! [체력: " + cleric3.hp + " 마나: " + cleric3.mp + "]");

    }


    void selfAid() {
        if (mp < 5) {
            return;
        }

        mp -= 5;
        hp = maxHp;
    }

    int pray(int sec) {
        // 0 ~ 2 포인트 랜덤 보정값 적용
        int bonus = new Random().nextInt(3);
        int recovery = sec + bonus;

        // 최대 MP를 초과하지 않도록 실제 회복 가능량 계산
        int maxRecovery = this.maxMp - this.mp;
        int actualRecovery = Math.min(recovery, maxRecovery);

        // MP 회복 적용 및 실제 회복량 반환
        this.mp += actualRecovery;
        return actualRecovery;
    }
}