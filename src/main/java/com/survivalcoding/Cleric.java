package com.survivalcoding;

import java.util.Random;

public class Cleric {
    static final int MAXHP = 50;
    static final int MAXMP = 10;
    String name;

    int hp;
    int mp;

    Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;

    }

    Cleric(String name, int hp) {
        this(name, hp, MAXMP);

    }

    Cleric(String name) {
        this(name, MAXHP, MAXMP);


    }

    public int getMp() {
        return mp;
    }

    void selfAid() {
        this.mp -= 5;
        this.hp += MAXHP;
        System.out.println("스킬 사용 self aid!!");
    }

    int pray(int sec) {
        int oldmp = this.mp;
        Random random = new Random();
        int bonus = random.nextInt(3);
        int prayTime = sec + bonus;
        int prayDo = prayTime + mp;
        if (prayDo > MAXMP) {
            prayDo = MAXMP;
            System.out.println("이미 마나가 최대입니다.");
        }
        this.mp = prayDo;
        return prayDo - oldmp;

    }


}
