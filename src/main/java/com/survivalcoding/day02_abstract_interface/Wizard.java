package com.survivalcoding.day02_abstract_interface;

public class Wizard extends Hero implements Healable {

    public Wizard(String name, int hp) {
        super(name, hp);
    }

    @Override
    public void heal(com.survivalcoding.day01_class_instance.Hero hero) {

    }
}
