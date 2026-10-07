package com.survivalcoding.day04_operation_instance;

import java.util.Objects;

public class Hero implements Cloneable {

    // field
    private String name;
    private int hp;
    private Sword sword;

    // constructor
    public Hero(String name, int hp, Sword sword) {
        this.name = name;
        this.hp = hp;
        this.sword = sword;
    }

    @Override
    public String toString() {
        return "Hero(name : " + name + ", hp : " + hp + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Hero hero)) {
            return false;
        }
        return Objects.equals(hp, hero.hp) && Objects.equals(name, hero.name) && Objects.equals(sword, hero.sword);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = Objects.hashCode(name);

        result = prime * result + hp;
        result = prime * result + Objects.hashCode(sword);

        return result;
    }

    @Override
    public Hero clone() {
        Hero result = new Hero(this.name, this.hp, this.sword);
        // Hero result = new Hero(this.name, this.hp, this.sword.clone());
        return result;
    }

    // getter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
