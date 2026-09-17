package com.survivalcoding.day04.exam;

import java.util.Random;

public class Cleric {
    static final Random RANDOM = new Random();
    static final int COST_FOR_SELF_AID = 5;
    static final int MAX_CORRECTION_VALUE = 2;
    static final int MAX_HP = 50;
    static final int MAX_MP = 10;

    int hp;
    int mp;
    String name;

    Cleric(String name, int hp, int mp) {
        if (mp <= 0 || MAX_MP < mp) {
            System.out.println("올바른 mp를 입력해 주세요");
            return;
        }

        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    Cleric(String name, int hp) {
        if (hp <= 0 || MAX_HP < hp) {
            System.out.println("올바른 hp를 입력해 주세요");
            return;
        }

        this.name = name;
        this.hp = hp;
        mp = MAX_MP;
    }

    Cleric(String name) {
        if (name == null || name.isBlank()) {
            System.out.println("잘못된 이름 형식입니다");
            return;
        }

        this.name = name;
        hp = MAX_HP;
        mp = MAX_MP;
    }

    void selfAid() {
        if (mp - COST_FOR_SELF_AID < 0) {
            System.out.println("마나가 부족합니다");
        } else {
            mp -= COST_FOR_SELF_AID;
            hp = MAX_HP;
            System.out.println("MP " + COST_FOR_SELF_AID + "를 소비하여 MAX HP로 회복했습니다");
        }
    }

    /**
     * 캐릭터의 MP를 회복시킵니다.
     *
     * @param durationSecond 기도 시간
     * @return 정상 처리 시 회복량 <br>
     *         이미 최대값인 경우 0 <br>
     *         범위를 벗어난 경우 -1
     */
    int pray(int durationSecond) {
        if (mp == MAX_MP) {
            System.out.println("이미 최대 마나입니다");
            return 0;
        } else if (durationSecond <= 0) {
            System.out.println("기도 시간이 잘못되었습니다");
            return -1;
        }

        // 보정치는 0 ~ 2까지지만 nextInt 특성상 + 1
        final int randomCorrectionValue = RANDOM.nextInt(MAX_CORRECTION_VALUE + 1);
        int mpRestoreAmount = durationSecond + randomCorrectionValue;

        if (MAX_MP < mp + mpRestoreAmount) {
            mpRestoreAmount = MAX_MP - mp;
        }

        mp += mpRestoreAmount;
        System.out.println(mpRestoreAmount + " 만큼의 마나를 회복을 했습니다");

        return mpRestoreAmount;
    }
}
