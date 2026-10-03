package com.survivalcoding.day03_polymorphism;

public class Tree implements Drawable {

    // field
    private double height;

    // constructor
    public Tree(double height) {
        this.height = height;
    }

    // method
    @Override
    public void draw() {
        System.out.println("나무를 그립니다");
    }

    // getter
    public double getHeight() {
        return height;
    }

    // setter
    public void setHeight(double height) {
        this.height = height;
    }
}
