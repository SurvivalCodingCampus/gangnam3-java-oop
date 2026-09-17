package com.survivalcoding;

public class Cleric {

    // 필드(field), 멤버변수(member variable), 속성(property), 전역변수
    String name;
    int hp;
    int mp;
    final int max_hp = 50;
    final int max_mp = 10;


    void selfAid() {
        if (mp - 5 != 0) {
            hp = max_hp;
        }
    }

    int pray(int sec) {
        while (sec > 0) {
            mp = mp + (int) (Math.random() * 3); // 0, 1, 2 중 하나
            if (mp > max_mp) {
                mp = max_mp;
            }
            sec--;
        }
        return mp;
    }
}