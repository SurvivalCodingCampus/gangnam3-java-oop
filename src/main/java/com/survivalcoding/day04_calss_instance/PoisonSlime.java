package com.survivalcoding.day04_calss_instance;

public class PoisonSlime extends Slime {
    private static final int POISON_DAMAGE_DIVISOR = 5;
    
    private int poisonCount = 5;
    
    public PoisonSlime(String suffix) {
        super(suffix);
    }
    
    public int getPoisonCount() {
        return poisonCount;
    }
    
    public void setPoisonCount(int poisonCount) {
        this.poisonCount = poisonCount;
    }
    
    private int calculatePoisonDamage(int hp) {
        if (hp <= 0) {
            return 0;
        }
        
        return hp / POISON_DAMAGE_DIVISOR;
    }
    
    private void applyPoisonDamage(Hero hero) {
        System.out.println("독 포자를 살포했다!");
        
        int damage = calculatePoisonDamage(hero.getHp());
        hero.setHp(hero.getHp() - damage);
        
        setPoisonCount(poisonCount - 1);
        
        System.out.printf("%d 포인트 데미지%n", damage);
    }
    
    @Override
    public void attack(Hero hero) {
        super.attack(hero);
        
        if (poisonCount <= 0) {
            return;
        }
        
        applyPoisonDamage(hero);
    }
}
