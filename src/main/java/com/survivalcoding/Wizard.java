package com.survivalcoding;

public class Wizard {
    private int hp;
    private int mp = 100;
    private Wand wand;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름을 입력하세요.");
        }
        this.name = name;
    }

    public void heal(Hero hero) {
        if (this.getMp() < 10) {
            System.out.println("마나가 부족합니다");
            return;
        }

        hero.setHp(hero.getHp() + 20);
        this.setMp(this.getMp() - 10);

        System.out.println("힐을 시전했습니다. 대상 HP: " + hero.getHp());
    }

    public int getHp() {

        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (hp <= 0) {
            this.hp = 0;
        }
    }

    public Wand getWand() {

        return wand;
    }

    public void setWand(Wand wand) {
        if (wand == null) {
            throw new IllegalArgumentException("무기를 장착하지 않았습니다.");
        }
        this.wand = wand;
    }

    public int getMp() {

        return mp;
    }

    public void setMp(int mp) {
        if (mp <= 0) {
            System.out.println("마나가 진짜로 부족합니다.");
            System.out.println("    ");
            throw new IllegalArgumentException("마나가 부족하여 스킬이 중단됩니다.");
        }
        this.mp = mp;
    }

}
