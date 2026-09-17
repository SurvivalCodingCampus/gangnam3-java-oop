package com.survivalcoding.day04.exam;

import java.util.Random;

public class Cleric {
    final Random random = new Random();
    final int costForSelfAid = 5;
    final int maxCorrectionValue = 2;
    final int maxHp = 50;
    final int maxMp = 10;

    int hp = maxHp;
    int mp = maxMp;

    void selfAid() {
        if (mp - costForSelfAid < 0) {
            System.out.println("마나가 부족합니다");
        } else {
            mp -= costForSelfAid;
            hp = maxHp;
            System.out.println("MP " + costForSelfAid + "를 소비하여 MAX HP로 회복했습니다");
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
        if (mp == maxMp) {
            System.out.println("이미 최대 마나입니다");
            return 0;
        } else if (durationSecond <= 0) {
            System.out.println("기도 시간이 잘못되었습니다");
            return -1;
        }

        // 보정치는 0 ~ 2까지지만 nextInt 특성상 + 1
        final int randomCorrectionValue = random.nextInt(maxCorrectionValue + 1);
        int mpRestoreAmount = durationSecond + randomCorrectionValue;

        if (maxMp < mp + mpRestoreAmount) {
            mpRestoreAmount = maxMp - mp;
        }

        mp += mpRestoreAmount;
        System.out.println(mpRestoreAmount + " 만큼의 마나를 회복을 했습니다");

        return mpRestoreAmount;
    }
}
