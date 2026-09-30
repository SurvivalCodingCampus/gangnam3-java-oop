package com.survivalcoding.day04_calss_instance;

public class Wizard {
    static final int MIN_HP = 0;
    static final int MAX_HP = 50;
    static final int MIN_MP = 0;
    static final int MAX_MP = 100;
    static final int BASE_HEAL_POINT = 20;  // 기본 회복 포인트
    static final int HEAL_MP_COST = 10;
    static final int MIN_NAME_LENGTH = 2;
    
    private int mp;
    
    private int hp;
    private String name;
    private Wand wand;
    
    // constructor
    public Wizard() {
        this("박마법", MAX_HP);
    }
    
    public Wizard(String name) {
        this(name, MAX_HP);
    }
    
    public Wizard(String name, int hp) {
        this(name, hp, MAX_MP, new Wand());
    }
    
    public Wizard(String name, int hp, int mp) {
        this(name, hp, mp, new Wand());
    }
    
    public Wizard(String name, int hp, int mp, Wand wand) {
        setName(name);
        setHp(hp);
        setMp(mp);
        setWand(wand);
    }
    
    // getter/setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        Validator.validateNotNull(name);
        Validator.validateMinLength(name, MIN_NAME_LENGTH);
        
        this.name = name;
    }
    
    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
        Validator.validateAtMost(hp, MAX_HP);
        
        this.hp = preventNegativeValue(hp);
    }
    
    public int getMp() {
        return mp;
    }
    
    public int getMaxMp() {
        return MAX_MP;
    }
    
    public void setMp(int mp) {
        Validator.validateAtMost(mp, getMaxMp());
        Validator.validateAtLeast(mp, MIN_MP);
        
        this.mp = preventNegativeValue(mp);
    }
    
    public Wand getWand() {
        return wand;
    }
    
    public void setWand(Wand wand) {
        Validator.validateNotNull(wand);
        
        this.wand = wand;
    }
    
    public int getBaseHealPoint() {
        return BASE_HEAL_POINT;
    }
    
    public int getHealMpCost() {
        return HEAL_MP_COST;
    }
    
    // method
    private int preventNegativeValue(int value) {
        return Math.max(value, 0);
    }
    
    public boolean hasEnoughMp(int mp) {
        if (mp < getHealMpCost()) {
            System.out.printf("마나가 부족합니다. 현재 MP: %d%n", mp);
            return false;
        }
        return true;
    }
    
    public void heal(Hero hero) {
        if (!hasEnoughMp(mp)) {
            return;
        }
        
        hero.setHp(hero.getHp() + getBaseHealPoint());
        setMp(mp - getHealMpCost());
        
        System.out.printf("힐을 시전했습니다. 대상 HP: %d%n", hero.getHp());
    }
}
