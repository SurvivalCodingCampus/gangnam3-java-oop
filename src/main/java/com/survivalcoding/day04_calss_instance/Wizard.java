package com.survivalcoding.day04_calss_instance;

public class Wizard {
    static final int MIN_HP = 0;
    static final int MAX_HP = 50;
    static final int MIN_MP = 0;
    static final int MAX_MP = 10;
    static final int BASE_HEAL_POINT = 10;  // 기본 회복 포인트
    
    private String name;
    private int hp;
    private int mp;
    private Wand wand;
    
    // ==========================================
    // constructor
    // ==========================================
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
        this.name = name;
        setHp(hp);
        setMp(mp);
        setWand(wand);
    }
    
    // ==========================================
    // getter/setter
    // ==========================================
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        Validator.validateNotNull(name);
        Validator.validateMinLength(name, 2);
        
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
    
    public void setMp(int mp) {
        Validator.validateAtLeast(mp, MIN_MP);
        Validator.validateAtMost(mp, MAX_MP);
        
        this.mp = mp;
    }
    
    public Wand getWand() {
        return wand;
    }
    
    public void setWand(Wand wand) {
        Validator.validateNotNull(wand);
        
        this.wand = wand;
    }
    
    // ==========================================
    // method
    // ==========================================
    private int ensureAtLeast(int value, int min) {  // 최솟값 보장
        return Math.max(value, min);
    }
    
    private int preventNegativeValue(int value) {
        return Math.max(value, 0);
    }
    
    public void heal(Hero hero) {
        int recovPoint = (int) (BASE_HEAL_POINT * this.wand.getPower());
        hero.setHp(hero.getHp() + recovPoint);
        
        System.out.printf("%s의 HP %d을 회복했다!%n", hero.getName(), BASE_HEAL_POINT);
    }
}
