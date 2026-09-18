package com.survivalcoding;

public class Person {
    final String name;
    final int birthYear;
    int currentYear;
    //null 값을 받아도 된다.그래서 예외케이스를 짜야한다.
    public Person(int birthYear, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 정확하지가 않습니다.");
        }
        this.currentYear = java.time.Year.now().getValue();
        this.birthYear = birthYear;
        this.name = name;
    }

    int getAge() {

        return currentYear - birthYear;
    }

    int getBirthYear() {
        return birthYear;
    }

    String getName() {
        return name;
    }
}
