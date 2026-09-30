package com.survivalcoding.day02_abstract_interface;

import com.survivalcoding.day01_class_instance.Slime;

public class Hero extends Character implements Attackable, Moveable {

    protected String name;
    protected int hp;

    public Hero() {
        this("김영웅", 100);
    }

    protected Hero(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    @Override
    public void attack(Slime slime) {
        System.out.println(name + "이 공격했다");
        hp -= 10;
    }
}
