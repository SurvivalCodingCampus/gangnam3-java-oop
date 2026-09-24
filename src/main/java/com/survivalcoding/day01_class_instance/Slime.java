package com.survivalcoding.day01_class_instance;

public class Slime {

    // constant
    private static final int LEVEL = 10;

    // field
    private String suffix;
    private int hp;
    private final int level;

    // constructor
    public Slime(String suffix, int hp) {
        this.suffix = suffix;
        this.hp = hp;
        this.level = LEVEL;
    }

    // method
    public void attack(Hero hero) {
        System.out.println("슬라임" + this.suffix + "이/가 공격했다!");
        System.out.println("10의 데미지");

        int damage = 10;
        hero.takeDamage(damage);
    }

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

    // getter
    public String getSuffix() {
        return suffix;
    }

    public int getHp() {
        return hp;
    }

    public int level() {
        return level;
    }

    // setter
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
