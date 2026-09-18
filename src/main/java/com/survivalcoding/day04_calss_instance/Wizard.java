package com.survivalcoding.day04_calss_instance;

public class Wizard {
    private String name;
    private int hp;
    
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
    
    // method
    public void heal(Hero hero) {
        hero.setHp(hero.getHp() + 10);
        System.out.printf("%s의 HP 10을 회복했다!%n", hero.getName());
    }
}
