package com.survivalcoding.day04_calss_instance;

import java.util.Random;

public class Cleric {
    final int MAX_HP = 50;
    final int MAX_MP = 10;
    final int SELFAID_MP_COST = 5;
    final int PRAY_RECOVERY_RANDOM_BOUND = 3;  // 0 이상 bound 미만
    
    String name;
    int hp;
    int mp;
    
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
        int recoveryAmount = randomPoint + sec;
        this.mp = Math.max((this.mp + recoveryAmount), MAX_MP);
        
        return recoveryAmount;
    }
}

