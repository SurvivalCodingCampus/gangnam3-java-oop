package com.survivalcoding;

public class Wizard {

    private String name;
    private int hp;
    private int mp;
    private Wand wand;

    // 이름 getter
    public String getName() {
        return this.name;
    }

    // 이름 setter
    public void setName(String name) {

        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(
                    "마법사의 이름은 null일 수 없으며 3문자 이상이어야 합니다."
            );
        }

        this.name = name;
    }

    // HP getter
    public int getHp() {
        return this.hp;
    }

    // HP setter
    public void setHp(int hp) {

        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    // MP getter
    public int getMp() {
        return this.mp;
    }

    // MP setter
    public void setMp(int mp) {

        if (mp < 0) {
            throw new IllegalArgumentException(
                    "MP는 0 이상이어야 합니다."
            );
        }

        this.mp = mp;
    }

    // 지팡이 getter
    public Wand getWand() {
        return this.wand;
    }

    // 지팡이 setter
    public void setWand(Wand wand) {

        if (wand == null) {
            throw new IllegalArgumentException(
                    "마법사의 지팡이는 null일 수 없습니다."
            );
        }

        this.wand = wand;
    }
}