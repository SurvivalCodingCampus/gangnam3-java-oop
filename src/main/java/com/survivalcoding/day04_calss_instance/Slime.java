package com.survivalcoding.day04_calss_instance;

public class Slime {
    final int level = 10;
    
    int hp;
    String suffix;
    
    void run() {
        System.out.printf("슬라임 %s가 도망갔다%n", this.suffix);
    }
}
