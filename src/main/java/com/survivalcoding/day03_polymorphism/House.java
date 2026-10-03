package com.survivalcoding.day03_polymorphism;

import java.awt.Color;

public class House implements Drawable {

    // field
    private String address;
    private int area;
    private Color color;

    // constructor
    public House(String address) {
        this(address, 100, Color.WHITE);
    }

    public House(String address, int area) {
        this(address, area, Color.WHITE);
    }

    public House(String address, int area, Color color) {
        this.address = address;
        this.area = area;
        this.color = color;
    }

    // method
    @Override
    public void draw() {
        System.out.println("집을 그립니다");
    }

    // getter
    public String getAddress() {
        return address;
    }

    public int getArea() {
        return area;
    }

    public Color getColor() {
        return color;
    }

    // setter
    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
