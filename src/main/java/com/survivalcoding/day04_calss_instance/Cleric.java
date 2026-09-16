package com.survivalcoding.day04_calss_instance;

public class Cleric {
    String name;
    int hp;
    int mp;
    
    int maxHp = 50;
    int maxMp = 10;
    
    int mp4selfAid = 5;
    
    void selfAid() {  // 셀프 에이드 마법 사용
        if ((this.mp - this.mp4selfAid) >= 0) {
            this.mp -= this.mp4selfAid;
            this.hp = this.maxHp;
        }
    }
}
