package com.survivalcoding.day02_abstract_interface;

import com.survivalcoding.day01_class_instance.Hero;

// 치유 기능을 가지고 있는
public interface Healable {
    void heal(Hero hero); // Hero만 치유
}
