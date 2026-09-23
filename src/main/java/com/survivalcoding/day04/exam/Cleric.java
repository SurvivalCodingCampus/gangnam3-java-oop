package com.survivalcoding.day04.exam;

public class Cleric {
    public static final int COST_FOR_SELF_AID = 5;
    public static final int MAX_CORRECTION_VALUE = 2;
    public static final int MAX_HP = 50;
    public static final int MAX_MP = 10;
    public static final int MIN_HP = 0;
    public static final int MIN_MP = 0;

    private int hp;
    private int mp;
    private String name;

    // 생성자를 바로 이용해서 생성하지 않음
    public Cleric(final String name, final int hp, final int mp) {

        String validationErrorMsg = "";

        if (!Utils.isValidName(name)) {
            validationErrorMsg += "올바른 이름을 넣어주세요\n";
        }

        if (!Utils.isWithinRange(hp, MAX_HP, MIN_HP)) {
            validationErrorMsg += "올바른 HP를 넣어주세요 MaxHp(" + MAX_HP + ") MinHp(" + MIN_HP + ")\n";
        }

        if (!Utils.isWithinRange(mp, MAX_MP, MIN_MP)) {
            validationErrorMsg += "올바른 MP를 넣어주세요 MaxMp(" + MAX_MP + ") MinMp(" + MIN_MP + ")\n";
        }

        if (!validationErrorMsg.isBlank()) {
            throw new IllegalArgumentException(validationErrorMsg);
        }

        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // 생성자를 바로 이용해서 생성하지 않음
    public Cleric(final String name, final int hp) {
        this(name, hp, MAX_MP);
    }

    // 생성자를 바로 이용해서 생성하지 않음
    public Cleric(final String name) {
        this(name, MAX_HP, MAX_MP);
    }

    public void selfAid() {
        if (mp - COST_FOR_SELF_AID < MIN_MP) {
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
    public int pray(int durationSecond) {
        if (mp == MAX_MP) {
            System.out.println("이미 최대 마나입니다");
            return 0;
        } else if (durationSecond <= 0) {
            throw new IllegalArgumentException("기도시간은 1보다 커야 함");
        }

        // 보정치는 0 ~ 2까지지만 nextInt 특성상 + 1
        final int randomCorrectionValue = Utils.nextInt(MAX_CORRECTION_VALUE);
        int mpRestoreAmount = durationSecond + randomCorrectionValue;

        if (MAX_MP < mp + mpRestoreAmount) {
            mpRestoreAmount = MAX_MP - mp;
        }

        mp += mpRestoreAmount;
        System.out.println(mpRestoreAmount + " 만큼의 마나를 회복을 했습니다");

        return mpRestoreAmount;
    }

    public int getMp() {
        return mp;
    }

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }
}
