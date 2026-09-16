package com.survivalcoding;

import java.util.Random;

public class Cleric {
    String name;
    int hp;
    int mp;
    final int MAXHP = 50;
    final int MAXMP =10;
    int oldmp = this.mp;

    void selfAid() {
        this.mp -= 5;
        this.hp += MAXHP;
        System.out.println("스킬 사용 self aid!!");
    }
    int pray(int sec) {
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
