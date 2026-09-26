package com.survivalcoding.day04.exam;

public class Cleric {
    static final int COST_FOR_SELF_AID = 5;
    static final int MAX_CORRECTION_VALUE = 2;
    static final int MAX_HP = 50;
    static final int MAX_MP = 10;

    private int hp;
    private int mp;
    private String name;

    // Builder를 통해서만 객체 생성 가능
    private Cleric(Builder builder) {
        setName(builder.name);
        setHp(builder.hp);
        setMp(builder.mp);
    }

    // region Func

    public void selfAid() {
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
     * 이미 최대값인 경우 0 <br>
     * 범위를 벗어난 경우 -1
     */
    public int pray(final int durationSecond) {
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

    // endregion Func

    // region Getter Setter

    private void setHp(final int hp) {
        if (!Utils.isWithinRange(hp, MAX_HP, 0)) {
            throw new IllegalArgumentException("올바른 HP를 넣어주세요 MaxHp(" + MAX_HP + ") MinHp(" + 0 + ")");
        }
        this.hp = hp;
    }

    private void setMp(final int mp) {
        if (!Utils.isWithinRange(mp, MAX_MP, 0)) {
            throw new IllegalArgumentException("올바른 MP를 넣어주세요 MaxMp(" + MAX_MP + ") MinHp(" + 0 + ")");
        }
        this.mp = mp;
    }

    private void setName(final String name) {
        if (!Utils.isValidName(name)) {
            throw new IllegalArgumentException("올바른 이름을 넣어주세요");
        }
        this.name = name;
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

    // endregion

    public static class Builder {
        private String name;
        private int hp = MAX_HP;
        private int mp = MAX_MP;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public Builder mp(int mp) {
            this.mp = mp;
            return this;
        }

        public Cleric build() {
            return new Cleric(this);
        }
    }
}