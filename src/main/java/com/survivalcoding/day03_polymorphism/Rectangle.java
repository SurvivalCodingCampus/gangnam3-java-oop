package com.survivalcoding.day03_polymorphism;

import java.awt.Color;

public class Rectangle implements Drawable {

    // field
    private int width;
    private int height;
    private Color color;
    private int borderStyle;

    // constructor
    public Rectangle(int width, int height, Color color, int borderStyle) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderStyle = borderStyle;
    }

    // method
    @Override
    public void draw() {
        System.out.println("사각형을 그립니다");
    }

    // getter
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public int getBorderStyle() {
        return borderStyle;
    }

    // setter
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBorderStyle(int borderStyle) {
        this.borderStyle = borderStyle;
    }
}
