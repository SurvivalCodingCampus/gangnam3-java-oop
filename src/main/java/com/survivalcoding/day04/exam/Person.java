package com.survivalcoding.day04.exam;

import java.time.LocalDate;

public class Person {
    private final String name;
    private final int birthYear;

    public Person(final String name, final int birthYear) {
        if (!Utils.isValidName(name)) {
            throw new IllegalArgumentException("이름에 널, 공란 불가");
        }

        if (birthYear > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("미래는 불가");
        }

        this.name = name;
        this.birthYear = birthYear;
    }

    public int getAge() {
        int age = LocalDate.now().getYear() - birthYear;

        return age;
    }
}