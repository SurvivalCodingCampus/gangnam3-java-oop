package com.survivalcoding.day04_calss_instance;

public class Wizard {
    static final int MAX_HP = 50;
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
        this.hp = hp;
        this.mp = mp;
        this.wand = wand;
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
        this.hp = hp;
    }
    
    public int getMp() {
        return mp;
    }
    
    public void setMp(int mp) {
        this.mp = mp;
    }
    
    public Wand getWand() {
        return wand;
    }
    
    public void setWand(Wand wand) {
        this.wand = wand;
    }
    
    // ==========================================
    // method
    // ==========================================
    public void heal(Hero hero) {
        int recovPoint = (int) (BASE_HEAL_POINT * this.wand.getPower());
        hero.setHp(hero.getHp() + recovPoint);
        
        System.out.printf("%s의 HP %d을 회복했다!%n", hero.getName(), BASE_HEAL_POINT);
    }
}
