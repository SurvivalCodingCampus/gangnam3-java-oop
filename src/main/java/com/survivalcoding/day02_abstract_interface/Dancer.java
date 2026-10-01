package com.survivalcoding.day02_abstract_interface;

import com.survivalcoding.day01_class_instance.Slime;

public class Dancer extends Character {

    // Character character = new Character();

    @Override
    public void attack(Slime slime) {
        System.out.println(this.name + "은 정열적으로 춤을 췄다");
    }
}
