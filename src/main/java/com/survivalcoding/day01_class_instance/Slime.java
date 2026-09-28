package com.survivalcoding.day01_class_instance;

public class Slime {
    private int hp;

    final int level = 10;   // 정해진 값


    public int getHp() {
        return hp;
    }

    // HP 기본값 초기화 = 100
    public void setHp() {
        this.hp = 100;
    }

    // HP 입력 시 = 입력밧
    public void setHp(int hp) {
        this.hp = hp;
    }


}
