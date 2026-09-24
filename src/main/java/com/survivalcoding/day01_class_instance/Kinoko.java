package com.survivalcoding.day01_class_instance;

public class Kinoko {

    // constant
    private static final int LEVEL = 10;

    // field
    private String suffix;
    private int hp;
    private final int level;

    // constructor
    public Kinoko(String suffix, int hp) {
        this.suffix = suffix;
        this.hp = hp;
        this.level = LEVEL;
    }

    // method
     public void takeDamage(int damage) {
        setHp(this.hp - damage);
    }

     private void die() {
        System.out.println(this.suffix + "는 죽었다");
    }

    // getter
    public String getSuffix() {
        return suffix;
    }

    public int getHp() {
        return hp;
    }

    public int getLevel() {
        return level;
    }

    // setter
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        if (this.hp == 0) {
            die();
        }
    }
}
