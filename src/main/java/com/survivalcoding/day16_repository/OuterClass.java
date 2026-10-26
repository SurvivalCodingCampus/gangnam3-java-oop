package com.survivalcoding.day16_repository;

public class OuterClass {
    private int number;

    void someMethod() {
        InnerClass innerClass = new InnerClass();
        number = innerClass.innerNumber;

        innerClass.someMethod();
    }

    // 장점 아예 없음 X
    // inner class
    static class InnerClass {
        int innerNumber;

        void someMethod() {
            // int a = number;
        }
    }
}
