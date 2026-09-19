package com.survivalcoding.day01_class_instance;

public class Wizard {

    // field
    private String name;
    private int hp;
    private int mp;
    private Wand wand;

    // constructor
    public Wizard(String name, int hp, int mp, Wand wand) {
        this.hp = hp;
        this.mp = mp;
        this.name = name;
        this.wand = wand;
    }

    // method
    public void heal(Hero hero) {
        int basePoint = 10;
        int recovPoint = (int) (basePoint * this.wand.getPower());

        hero.setHp(hero.getHp() + recovPoint);
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
