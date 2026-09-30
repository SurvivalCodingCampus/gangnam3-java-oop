package com.survivalcoding.day04_calss_instance;

import java.util.Random;

class Cleric {
    static final int MAX_HP = 50;
    static final int MAX_MP = 10;
    static final int SELF_AID_MP_COST = 5;
    static final int PRAY_RECOVERY_RANDOM_BOUND = 3;  // 0 이상 bound 미만
    
    private int hp;
    private int mp;
    private String name;
    
    public Cleric(String name, int hp, int mp) {
        setName(name);
        setHp(hp);
        setMp(mp);
    }
    
    public Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }
    
    public Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }
    
    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public int getMp() {
        return mp;
    }
    
    public void setMp(int mp) {
        this.mp = mp;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        Validator.validateNotNull(name);
        
        this.name = name;
    }
    
    public void selfAid() {  // 셀프 에이드 마법 사용
        if ((this.mp - SELF_AID_MP_COST) >= 0) {
            this.mp -= SELF_AID_MP_COST;
            this.hp = MAX_HP;
        }
    }
    
    public int pray(int sec) {
        if ((sec <= 0) || (this.mp == MAX_MP)) {
            return 0;
        }
        
        Random random = new Random();
        
        int randomPoint = random.nextInt(PRAY_RECOVERY_RANDOM_BOUND);
        int recoveryAmount = Math.min(randomPoint + sec, MAX_MP - this.mp);
        
        this.mp += recoveryAmount;
        return recoveryAmount;
    }
}