package com.survivalcoding.day03_polymorphism;

public class Dog implements Drawable {

    // field
    private String name;
    private int age;

    // constructor
    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // method
    @Override
    public void draw() {
        System.out.println("개를 그립니다");
    }

    // getter
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
