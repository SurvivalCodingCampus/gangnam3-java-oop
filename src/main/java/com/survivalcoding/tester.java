package com.survivalcoding;

import java.util.ArrayList;
import java.util.List;

public class tester {
    public static void main(String[] args) {
        List<shape> A = new ArrayList<>();
        Circle P = new Circle("im good", 6);
        Rectangle R = new Rectangle("i too", 5, 7);
        Triangle T = new Triangle("sharp", 5, 5);
        A.addAll(List.of(P, R, T));
        System.out.println(R.Resizeable());
        double ADDED = 0;
        for (shape s : A) {
            if (s.resizeable()) {
                ADDED += s.Resizeable();
            }
        }
        System.out.println(ADDED);

    }

    interface Resizeable {
        double resize(double ratio);
    }

    public static abstract class shape {
        String name;

        shape(String name) {
            this.name = name;
        }

        boolean resizeable() {
            return false;
        }

        abstract double area();

        abstract double perimeter();

        double Resizeable() {
            return 0;
        }

    }

    ;

    public static class Circle extends shape implements Resizeable {
        double r;

        Circle(String x, double r) {
            super(x);
            this.r = r;
        }

        @Override
        boolean resizeable() {
            return true;
        }

        double area() {
            return r * r * 3.14;
        }

        double perimeter() {
            return r * 2 * 3.14;
        }

        public double resize(double ratio) {
            return (area() * ratio);
        }

        ;

        double Resizeable() {
            return resize(2);
        }

        ;
    }

    public static class Rectangle extends shape implements Resizeable {
        double x;
        double y;

        Rectangle(String S, double x, double y) {
            super(S);
            this.x = x;
            this.y = y;
        }

        @Override
        boolean resizeable() {
            return true;
        }

        double area() {
            return this.x * this.y;
        }

        double perimeter() {
            return this.x * 2 + this.y * 2;
        }

        public double resize(double ratio) {
            return area() * ratio;
        }

        double Resizeable() {
            return resize(2);
        }

    }


    ;

    public static class Triangle extends shape {
        double h;
        double w;


        Triangle(String x, double h, double w) {
            super(x);
            this.h = h;
            this.w = w;
        }

        double area() {
            return w * h / 2;
        }

        ;

        double perimeter() {
            return 2 * Math.sqrt(h * h + w / 2 * w / 2) + h;
        }

        ;
    }
}
