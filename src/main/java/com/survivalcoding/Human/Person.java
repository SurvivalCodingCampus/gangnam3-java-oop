package com.survivalcoding.Human;

import com.survivalcoding.day04_calss_instance.Validator;

import java.time.LocalDate;

public class Person {
    static final int MIN_NAME_LENGTH = 1;
    static final int MIN_BIRTH_YEAR = 1800;
    
    private final String name;
    private final int birthYear;
    
    public Person(String name, int birthYear) {
        Validator.validateNotNull(name);
        Validator.validateMinLength(name, MIN_NAME_LENGTH);
//        Validator.validateAtLeast(birthYear, MIN_BIRTH_YEAR);
        Validator.validateAtMost(birthYear, getThisYear());
        
        this.name = name;
        this.birthYear = birthYear;
    }
    
    public String getName() {
        return name;
    }
    
    public int getBirthYear() {
        return birthYear;
    }
    
    private int getThisYear() {
        return LocalDate.now().getYear();
    }
    
    public int getAge() {
        return getThisYear() - birthYear;
    }
}
