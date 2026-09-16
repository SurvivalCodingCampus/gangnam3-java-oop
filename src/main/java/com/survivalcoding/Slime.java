package com.survivalcoding;

public class  Slime {
    public int hp;
    public String suffix;
    final int level = 10;

    void run() {
        System.out.println("슬라임 " + suffix + "가 도망갔다");
    }
    void takeDamage(int x){};
}
