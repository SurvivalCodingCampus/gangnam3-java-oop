package com.survivalcoding;

public class Wizard {
    private int hp;
    private int mp;
    private Wand wand;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (hp <= 0) {
            System.out.println("죽었습니다. ㅋㅋ");
        }
    }

    public Wand getWand() {
        return wand;
    }

    public void setWand(Wand wand) {
        this.wand = wand;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
        if (mp <= 0) {
            System.out.println("마나가 진찌로 부족합니다.");
        }
    }

}
