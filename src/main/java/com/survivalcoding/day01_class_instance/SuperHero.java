package com.survivalcoding.day01_class_instance;

public class SuperHero extends Hero {

    // field
    private boolean isFlying;

    // constructor
    public SuperHero(String name) {
        this(name, new Sword("불의 검", 10), false);
        System.out.println("SuperHero 생성자 호출");
    }

    public SuperHero(String name, Sword sword) {
        this(name, sword, false);
    }

    public SuperHero(String name, Sword sword, boolean isFlying) {
        super(name, sword);
        this.isFlying = isFlying;
    }

    // method
    public void fly() {
        isFlying = true;
    }

    public void land() {
        isFlying = false;
    }

    @Override
    public void attack(Kinoko enemy) {
        super.attack(enemy);

        if (isFlying) {
            int damage = 5;
            enemy.takeDamage(damage);
            System.out.println("5포인트의 추가 피해를 입혔다");
        }
    }

    @Override
    public void run() {
        System.out.println("멋지게 퇴각했다");
    }

    // getter
    public boolean isFlying() {
        return isFlying;
    }

    // setter
    public void setFlying(boolean flying) {
        this.isFlying = flying;
    }
}
