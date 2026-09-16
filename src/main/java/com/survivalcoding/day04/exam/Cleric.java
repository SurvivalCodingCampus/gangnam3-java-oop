package com.survivalcoding.day04.exam;

public class Cleric {

    final int COST_FOR_SELF_AID = 5;

    int MAX_HP = 50;
    int MAX_MP = 10;

    int hp = MAX_HP;
    int mp = MAX_MP;

    void selfAid() {
        if (mp - COST_FOR_SELF_AID < 0) {
            System.out.println("마나가 부족합니다");
        } else {
            mp -= COST_FOR_SELF_AID;
            hp = MAX_HP;
            System.out.println("MP 5를 소비하여 MAX HP로 회복했습니다");
        }
    }
}
