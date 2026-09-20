package com.survivalcoding.day04.exam;

public class Wizard {
    public static final int MIN_MP = 0;

    private int hp;
    private int mp;
    private String name;
    private Wand wand;

    private void heal(final Hero hero) {
        int basePoint = 10; // 기본회복 포인트
        int recovPoint = (int) (basePoint * wand.getPower()); // 지팡이에 의한 증폭
        hero.setHp(hero.getHp() + recovPoint); // 용사의 HP 회복
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        if (hp < 0) {
            this.hp = 0;
            System.out.println("음수여서 0으로 설정함");
            return;
        }

        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(final int mp) {
        if (mp < MIN_MP) {
            throw new IllegalArgumentException(
                "%d 이상의 mp 입력"
                    .formatted(MIN_MP));
        }

        this.mp = mp;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }

        this.name = name;
    }

    public Wand getWand() {
        return wand;
    }

    public void setWand(final Wand wand) {
        if (wand == null) {
            throw new IllegalArgumentException("null 금지");
        }

        this.wand = wand;
    }
}
