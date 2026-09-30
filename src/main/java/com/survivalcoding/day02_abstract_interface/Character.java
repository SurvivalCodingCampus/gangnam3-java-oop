package com.survivalcoding.day02_abstract_interface;

import com.survivalcoding.day01_class_instance.Slime;

public abstract class Character {

    public String name;
    public int hp;

    public void run() {
        System.out.println(name + "은 도망쳤다");
    };

    public abstract void attack(Slime slime);
}