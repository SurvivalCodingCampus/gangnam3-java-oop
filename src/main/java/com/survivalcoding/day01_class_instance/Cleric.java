package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Cleric {
    static final int MP_COST = 5;

    final int maxHp = 50;
    final int maxMp = 10;

    String name;
    int hp = maxHp;
    int mp = maxMp;

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
