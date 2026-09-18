package com.survivalcoding.day04_calss_instance;

public class Wizard {
    private String name;
    private int hp;
    private int mp;
    private Wand wand;
    
    // constructor
    public Wizard() {
        this("박마법", 100);
    }
    
    public Wizard(String name) {
        this(name, 100);
    }
    
    public Wizard(String name, int hp) {
        this(name, hp, 10, new Wand());
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
    
    // getter/setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
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
    
    // method
    public void heal(Hero hero) {
        hero.setHp(hero.getHp() + 10);
        System.out.printf("%s의 HP 10을 회복했다!%n", hero.getName());
    }
}
