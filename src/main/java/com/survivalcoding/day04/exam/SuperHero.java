package com.survivalcoding.day04.exam;

public class SuperHero extends Hero {
    private boolean isFlying;

    public SuperHero(final String name, final int hp) {
        super(name, hp);
    }

    @Override
    public void run() {
        System.out.println("멋지게 퇴각했다");
    }

    @Override
    public void attack(final Slime slime) {
        super.attack(slime);

        if (isFlying) {
            slime.setHp(slime.getHp() - 5);
            System.out.println("5포인트의 추가 피해를 입혔다");
        }
    }

    public void land() {
        isFlying = false;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(final boolean flying) {
        isFlying = flying;
    }
}
