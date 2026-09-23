package com.survivalcoding.day04.exam;

public class Wizard {
    public static final int MIN_MP = 0;
    public static final int HEAL_HP_AMOUNT = 20;
    public static final int COST_FOR_HEAL = 10;

    private int hp;
    private int mp;
    private String name;
    private Wand wand;

    public Wizard() {
        mp = 100;
    }

    private void heal(final Hero hero) {
        if (mp - COST_FOR_HEAL < MIN_MP) {
            System.out.println("마나가 부족합니다");
            return;
        }

        int heroHp = hero.getHp();

        if (Hero.MAX_HP <= heroHp) {
            System.out.println("대상 체력이 MAX HP입니다");
            return;
        }

        mp -= COST_FOR_HEAL;
        hero.setHp(Math.min(Hero.MAX_HP, heroHp + HEAL_HP_AMOUNT));

        System.out.println("힐을 시전했습니다. 대상 HP: " + hero.getHp());
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
