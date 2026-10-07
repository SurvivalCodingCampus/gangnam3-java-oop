package com.survivalcoding.day04_operation_instance;

public class Sword implements Cloneable {

    @Override
    protected Sword clone() throws CloneNotSupportedException {
        Sword result = new Sword();
        return result;
    }
}
