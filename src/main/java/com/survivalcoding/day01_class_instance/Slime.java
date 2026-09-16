package com.survivalcoding.day01_class_instance;

public class Slime {

    // field
    public int hp;
    public String suffix;
    public final int level = 10;

    public void run() {
        System.out.println("슬라임 " + this.suffix + "가 도망갔다");
    }

    // 테스트 코드 실습용 메서드
    void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }
}
