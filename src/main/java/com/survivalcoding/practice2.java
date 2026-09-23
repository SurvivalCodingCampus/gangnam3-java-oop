package com.survivalcoding;

public class practice2 {
    interface Flyable {
    }

    interface Swimable {
    }

    ;

    static abstract class Animal {
        String name;
        int age;

        abstract void makeSound();

        void eat() {
            System.out.println("this is delicious");
        }
    }

    static class Duck extends Animal implements Flyable, Swimable {
        @Override
        void makeSound() {
            System.out.println("quackie");
        }
    }

    static class Penguin extends Animal implements Swimable {
        @Override
        void makeSound() {
            System.out.println("qwackie");
        }
    }

    static class Bat extends Animal implements Flyable {
        @Override
        void makeSound() {
            System.out.println("quackie");
        }
    }

    static class Dog extends Animal {
        @Override
        void makeSound() {
            System.out.println("quackie");
        }
    }
}
