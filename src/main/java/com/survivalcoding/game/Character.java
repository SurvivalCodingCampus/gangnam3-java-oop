package com.survivalcoding.game;

public abstract class Character {
    protected String name;
    protected int hp;
    
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
    
    public void run() {
        System.out.println(name + "은 도망쳤다!");
    }
}
