package com.survivalcoding.day04.exam;

import java.time.LocalDate;

public class Person {
    private final String name;
    private final int birthYear;
    private final int thisYear = LocalDate.now().getYear();

    public Person(final String name, final int birthYear) {
        if (name == null) {
            throw new IllegalArgumentException("이름에 널 불가");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("이름에 공란 불가");
        }

        if (birthYear > thisYear) {
            throw new IllegalArgumentException("미래는 불가");
        }

        this.name = name;
        this.birthYear = birthYear;
    }

    public int getAge() {
        int age = thisYear - birthYear;

        return age;
    }
}
