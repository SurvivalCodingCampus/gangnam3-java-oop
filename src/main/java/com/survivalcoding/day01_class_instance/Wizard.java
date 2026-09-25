package com.survivalcoding.day01_class_instance;

public class Wizard {

    // constant
    private static final int DEFAULT_MP = 100;

    // field
    private String name;
    private int hp;
    private int mp;
    private Wand wand;

    // constructor
    protected Wizard(String name, int hp, int mp, Wand wand) {
        setName(name);
        setHp(hp);
        setMp(mp);
        setWand(wand);
    }

    public Wizard(String name, int hp, Wand wand) {
        this(name, hp, DEFAULT_MP, wand);
    }

    // method
    public void heal(Hero hero) {
        int mpCost = 10;
        if (mp < 10) {
            System.out.println("마나가 부족합니다");
            return;
        }

        int recovPoint = 20;
        hero.setHp(hero.getHp() + recovPoint);
        this.setMp(this.getMp() - mpCost);

        System.out.println("힐을 시전했습니다. 대상 HP: " + hero.getHp());
    }

    // getter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public Wand getWand() {
        return wand;
    }

    // setter
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name(이름)은 null이 아니어야 합니다");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("name(이름)은 3글자 이상이어야 합니다");
        }

        this.name = name;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            throw new IllegalArgumentException("hp(체력)는 0 이상이어야 합니다");
        }

        this.hp = hp;
    }

    public void setMp(int mp) {
        if (mp < 0) {
            throw new IllegalArgumentException("mp(마력)는 0 이상이어야 합니다");
        }

        this.mp = mp;
    }

    public void setWand(Wand wand) {
        if (wand == null) {
            throw new IllegalArgumentException("wand(지팡이)는 null이 아니어야 합니다");
        }

        this.wand = wand;
    }
}
