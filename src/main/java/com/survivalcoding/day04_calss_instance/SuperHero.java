package com.survivalcoding.day04_calss_instance;

public class SuperHero extends Hero {
    static final int FLYING_ATTACK_DAMAGE = 5;
    
    private boolean isFlying;
    
    public SuperHero() {
        super();
    }
    
    public SuperHero(String name) {
        super(name);
    }
    
    public SuperHero(String name, int hp) {
        super(name, hp);
    }
    
    public boolean isFlying() {
        return isFlying;
    }
    
    public void setFlying(boolean flying) {
        isFlying = flying;
    }
    
    private void applyFlyAttack(Slime slime) {
        slime.setHp(slime.getHp() - FLYING_ATTACK_DAMAGE);
        System.out.printf("%d 포인트의 추가 피해를 입혔다%n", FLYING_ATTACK_DAMAGE);
    }
    
    @Override
    public void attack(Slime slime) {
        super.attack(slime);
        
        if (isFlying) {
            applyFlyAttack(slime);
        }
    }
    
    @Override
    public void run() {
        System.out.println("멋지게 퇴각했다");
    }
}
