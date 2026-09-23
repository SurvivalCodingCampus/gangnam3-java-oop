package com.survivalcoding.day01_class_instance;

public class Wizard {
    private int hp;
    private int mp;
    private String name;
    private Wand wand;

    // 캡슐화 (getter & setter)
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            throw new IllegalArgumentException("HP가 0입니다. 물약을 드세용");
        }
        this.hp = hp;
    }


    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        if (mp <= 0) {
            throw new IllegalArgumentException("MP가 0입니다. 물약을 드세용");
        }
        this.mp = mp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 아니어야 함");
        }
        if (name.length() <= 3) {
            throw new IllegalArgumentException("이름이 너무 짧음");
        }
        this.name = name;
    }

    public Wand getWand() {
        return wand;
    }

    public void setWand(Wand wand) {
        if (wand == null) {
            throw new IllegalArgumentException("지팡이 없는 법사가 법사냐!?");
            this.wand = wand;
        }
    }

    // 스킬 heal --> hero
    void heal(Hero hero) {
        int basePoint = 10;     // 기본회복 포인트
        int recovPoint = (int) (basePoint * this.wand.power);       // 지팡이에 의한 증폭
        hero.setHp(hero.getHp() + recovPoint);                      // 용사의 HP를 회복
    }
}
