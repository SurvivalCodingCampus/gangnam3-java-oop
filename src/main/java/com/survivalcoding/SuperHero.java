package com.survivalcoding;

public class SuperHero extends Hero {

    private boolean isFlying;

    // 생성자
    public SuperHero(String name, int hp) {
        super(name, hp);
    }

    // 비행 상태 확인
    public boolean isFlying() {
        return isFlying;
    }

    // 비행 상태 설정
    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    // 공격
    public void attack(Slime slime) {
        System.out.println(getName() + "이 공격했다");

        // Hero의 hp를 사용한다고 가정
        setHp(getHp() - 10);

        if (getHp() <= 0) {
            System.out.println(getName() + "은 쓰러졌다.");
        }
    }

    // 퇴각
    @Override
    public void run() {
        System.out.println("멋지게 퇴각했다");
    }

    // 날기
    public void fly() {
        isFlying = true;
        System.out.println(getName() + "이 날아올랐다.");
    }

    // 착륙
    public void land() {
        isFlying = false;
        System.out.println(getName() + "이 착륙했다.");
    }

    @Override
    public String toString() {
        return "SuperHero{" +
                "name=" + getName() +
                ", hp=" + getHp() +
                ", isFlying=" + isFlying +
                '}';
    }
}