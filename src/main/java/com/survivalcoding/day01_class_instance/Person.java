package com.survivalcoding.day01_class_instance;

import java.time.LocalDate;

public class Person {

    // field
    private final String name;
    private final int birthYear;

    // constructor
    public Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    // method
    public int getAge(LocalDate date) {
        return date.getYear() - birthYear;
    }

    // getter
    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }
}
