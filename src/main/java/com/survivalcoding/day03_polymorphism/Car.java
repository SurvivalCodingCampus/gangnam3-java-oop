package com.survivalcoding.day03_polymorphism;

import java.awt.Color;

public class Car implements Drawable, Moveable {

    // field
    private final String brand;
    private final Color color;
    private final int speed;

    // constructor
    public Car(String brand, Color color, int speed) {
        this.brand = brand;
        this.color = color;
        this.speed = speed;
    }

    // method
    @Override
    public void draw() {
        System.out.println("자동차를 그립니다");
    }

    @Override
    public void move(int seconds) {
        int distance = speed * seconds;
        System.out.println(brand + " 자동차가 " + distance + "m 이동했습니다");
    }

    // getter
    public String getBrand() {
        return brand;
    }

    public Color getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }
}
