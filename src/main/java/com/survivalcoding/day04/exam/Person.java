package com.survivalcoding.day04.exam;

import java.time.LocalDate;

public class Person {
    private final String name;
    private final int birthYear;

    public Person(final String name, final int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public int getAge() {
        int thisYear = LocalDate.now().getYear();
        int age = thisYear - birthYear;

        return age;
    }
}
