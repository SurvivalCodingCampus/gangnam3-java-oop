package com.survivalcoding.day03_polymorphism;

public class Dancer implements Human {

    // field
    private String name;
    private int hp;

    // constructor
    public Dancer(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    // method
    @Override
    public void speak() {
        System.out.println("안녕하세요, 저는 " + name + "입니다.");
    }

    public void dance() {
        System.out.println(name + "이 춤을 춥니다.");
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
