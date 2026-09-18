package com.survivalcoding.day04.exam;

import java.util.Random;

public class Cleric {
    static final Random RANDOM = new Random();
    static final int COST_FOR_SELF_AID = 5;
    static final int MAX_CORRECTION_VALUE = 2;
    static final int MAX_HP = 50;
    static final int MAX_MP = 10;
    static final int MIN_HP = 0;
    static final int MIN_MP = 0;

    int hp;
    int mp;
    String name;

    // 생성자를 바로 이용해서 생성하지 않음
    Cleric(final String name, final int hp, final int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // 생성자를 바로 이용해서 생성하지 않음
    Cleric(final String name, final int hp) {
        this(name, hp, MAX_MP);
    }

    // 생성자를 바로 이용해서 생성하지 않음
    Cleric(final String name) {
        this(name, MAX_HP, MAX_MP);
    }

    /**
     * @param name
     * @return 유효한 값이면 생성 아니면 null
     */
    static Cleric CreateOrNull(final String name) {
        boolean hasInValidValue = false;

        if (!isValidName(name)) {
            System.out.println("올바른 이름을 넣어주세요");
            hasInValidValue = true;
        }

        if (hasInValidValue) {
            return null;
        }

        return new Cleric(name);
    }

    /**
     * @param name
     * @param hp
     * @return 유효한 값이면 생성 아니면 null
     */
    static Cleric CreateOrNull(final String name, final int hp) {
        boolean hasInValidValue = false;

        if (!isValidName(name)) {
            System.out.println("올바른 이름을 넣어주세요");
            hasInValidValue = true;
        }

        if (!isWithinRange(hp, MAX_HP, MIN_HP)) {
            System.out.println("올바른 HP를 넣어주세요 MaxHp(" + MAX_HP + ") MinHp(" + MIN_HP + ")");
            hasInValidValue = true;
        }

        if (hasInValidValue) {
            return null;
        }

        return new Cleric(name, hp);
    }

    /**
     * @param name
     * @param hp
     * @param mp
     * @return 유효한 값이면 생성 아니면 null
     */
    static Cleric CreateOrNull(final String name, final int hp, final int mp) {

        boolean hasInValidValue = false;

        if (!isValidName(name)) {
            System.out.println("올바른 이름을 넣어주세요");
            hasInValidValue = true;
        }

        if (!isWithinRange(hp, MAX_HP, MIN_HP)) {
            System.out.println("올바른 HP를 넣어주세요 MaxHp(" + MAX_HP + ") MinHp(" + MIN_HP + ")");
            hasInValidValue = true;
        }

        if (!isWithinRange(mp, MAX_MP, MIN_MP)) {
            System.out.println("올바른 MP를 넣어주세요 MaxMp(" + MAX_MP + ") MinMp(" + MIN_MP + ")");
            hasInValidValue = true;
        }

        if (hasInValidValue) {
            return null;
        }

        return new Cleric(name, hp, mp);
    }

    static boolean isValidName(final String name) {
        if (name == null || name.isBlank()) {
            return false;
        }

        return true;
    }

    /**
     * 주어진 값이 최소값과 최대값 범위 내에 있는지 확인 (경계값 포함)
     *
     * @param value
     * @param max
     * @param min
     * @return 값이 범위 내에 있으면 true, 아니면 false
     */
    static boolean isWithinRange(final int value, final int max, final int min) {
        return min <= value && value <= max;
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
