package com.survivalcoding.day04_calss_instance;

public class Slime {
    static final int LEVEL = 10;
    
    private int hp;
    private String suffix;
    
    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    void run() {
        System.out.printf("슬라임 %s가 도망갔다%n", suffix);
    }
}
