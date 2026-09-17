package com.survivalcoding.day04_calss_instance;

import java.util.Random;

class Cleric {
    static final int MAX_HP = 50;
    static final int MAX_MP = 10;
    static final int SELFAID_MP_COST = 5;
    static final int PRAY_RECOVERY_RANDOM_BOUND = 3;  // 0 이상 bound 미만
    
    int hp;
    int mp;
    String name;
    
    Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }
    
    Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }
    
    Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }
    
    void selfAid() {  // 셀프 에이드 마법 사용
        if ((this.mp - SELFAID_MP_COST) >= 0) {
            this.mp -= SELFAID_MP_COST;
            this.hp = MAX_HP;
        }
    }
    
    int pray(int sec) {
        if (sec <= 0) {
            return 0;
        }
        
        Random random = new Random();
        
        int randomPoint = random.nextInt(PRAY_RECOVERY_RANDOM_BOUND);
        int recoveryAmount = Math.min(randomPoint + sec, MAX_MP - this.mp);
        
        this.mp += recoveryAmount;
        
        return recoveryAmount;
    }
}