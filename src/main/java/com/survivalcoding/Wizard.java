package com.survivalcoding;

public class Wizard {
    private final int INIT_MP = 100;
    private final int INIT_HP = 60;
    protected int hp = INIT_HP;
    private String name;
    private int mp = INIT_MP;

    Wizard() {
        mp = INIT_MP;
    }

    Wizard(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMp() {
        return mp;
    }

    void setMp(int mp) {
        this.mp = mp;
    }

    //this 은 자기자신기준으로 부모가서 찾음 없으면 error;
    public void heal(Hero hero) {
        if (mp >= 10) {
            mp = mp - 10;//TIP gethp는 값자체를 반환 변수 반환 아님
            //ERROR hero.getHp() = hero.getHp() + 20;
            hero.setHp(hero.getHp() + 20);
            System.out.println("힐을 시전했습니다" + "대상 HP:" + hero.getHp());
        } else {
            System.out.println("마나가 부족합니다");
        }
    }
}
