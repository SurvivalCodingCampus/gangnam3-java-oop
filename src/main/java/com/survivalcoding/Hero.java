package com.survivalcoding;

public class Hero {
    //필드다 private 함수는 다 public
    private final int maxHp = 999;
    private int INITHP = 100;
    private int hp = INITHP;


    //insert 는 0키라고 생각할수도 있다.
    public Hero() {
    }

    public Hero(int hp) {
        hp = this.hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getmaxHp() {
        return maxHp;
    }
}
