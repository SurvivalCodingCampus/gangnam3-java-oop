package com.survivalcoding;

public class Cleric {
    String name;
    int hp;
    int mp;
    final int MAXHP = 50;
    final int MAXMP =10;

    void selfAid() {
        this.mp -= 5;
        this.hp += MAXHP;
        System.out.println("스킬 사용 selfaid!!");
    }

    



}
