package com.survivalcoding;

import java.time.LocalDate;

public class HappyBirthDay {
    private final String name;
    private final int birthYear;
    private int age;

    public HappyBirthDay(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        int currentYear = LocalDate.now().getYear();
        return currentYear - birthYear;
    }


}
