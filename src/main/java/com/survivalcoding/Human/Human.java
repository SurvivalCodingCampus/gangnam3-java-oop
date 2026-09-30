package com.survivalcoding.Human;

public interface Human extends Creature {
    void talk();
    void watch();
    void hear();
    
    // Creature의 run()도 상속 받고 있음
}
