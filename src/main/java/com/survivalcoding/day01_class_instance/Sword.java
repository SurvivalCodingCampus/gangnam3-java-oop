package com.survivalcoding.day01_class_instance;

import java.util.Objects;

public class Sword {
    private String name;
    private int damage;

    public Sword(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Sword sword)) return false;

        return damage == sword.damage && Objects.equals(name, sword.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + damage;
        return result;
    }

    @Override
    public String toString() {
        return "Sword{" +
                "name='" + name + '\'' +
                ", damage=" + damage +
                '}';
    }
}
