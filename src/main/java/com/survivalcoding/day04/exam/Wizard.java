package com.survivalcoding.day04.exam;

public class Wizard {
    private static final int MIN_MP = 0;
    private static final int MAX_MP = 100;
    private static final int HEAL_HP_AMOUNT = 20;
    private static final int COST_FOR_HEAL = 10;
    private static final int MIN_NAME_LENGTH = 3;

    protected int hp;
    protected int mp;
    protected String name;
    protected Wand wand;

    public Wizard() {
        mp = MAX_MP;
    }

    public void heal(final Hero hero) {
        heal(hero, COST_FOR_HEAL, HEAL_HP_AMOUNT, "힐을 시전했습니다.");
    }

    protected void heal(final Hero hero, int cost, int amount, String msg) {
        if (mp - cost < MIN_MP) {
            System.out.println("마나가 부족합니다");
            return;
        }

        int heroHp = hero.getHp();

        if (Hero.MAX_HP <= heroHp) {
            System.out.println("대상 체력이 MAX HP입니다");
            return;
        }

        mp -= cost;
        hero.setHp(Math.min(Hero.MAX_HP, heroHp + amount));

        System.out.println(msg + " 대상 HP: " + hero.getHp());
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

        if (name.length() < MIN_NAME_LENGTH) {
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
