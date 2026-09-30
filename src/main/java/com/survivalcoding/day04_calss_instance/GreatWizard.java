package com.survivalcoding.day04_calss_instance;

public class GreatWizard extends Wizard {
    static final int MAX_MP = 150;
    static final int BASE_HEAL_POINT = 25;
    static final int HEAL_MP_COST = 5;
    
    public GreatWizard() {
        super("대마법", MAX_HP, MAX_MP);
    }
    
    public GreatWizard(String name) {
        super(name, MAX_HP, MAX_MP);
    }
    
    public GreatWizard(String name, int hp) {
        super(name, hp, MAX_MP);
    }
    
    public GreatWizard(String name, int hp, int mp) {
        super(name, hp, mp);
    }
    
    public GreatWizard(String name, int hp, int mp, Wand wand) {
        super(name, hp, mp, wand);
    }
    
    @Override
    public int getMaxMp() {
        return MAX_MP;
    }
    
    @Override
    public int getBaseHealPoint() {
        return BASE_HEAL_POINT;
    }
    
    @Override
    public int getHealMpCost() {
        return HEAL_MP_COST;
    }
    
    public void superHeal(Hero hero) {
        if (!hasEnoughMp(getMp())) {
            return;
        }
        
        hero.setHp(Hero.MAX_HP);
        setMp(getMp() - getHealMpCost());
        
        System.out.printf("슈퍼 힐을 시전했습니다. 대상 HP: %d%n", hero.getHp());
    }
}
