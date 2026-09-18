package com.survivalcoding.Human;

import com.survivalcoding.day04_calss_instance.Validator;

import java.time.LocalDate;

public class Person {
    static final int MIN_NAME_LENGTH = 1;
    
    private int thisYear;
    
    private final String name;
    private final int birthYear;
    
    Person(String name, int birthYear) {
        Validator.validateNotNull(name);
        Validator.validateMinLength(name, MIN_NAME_LENGTH);
        Validator.validateAtMost(birthYear, thisYear);
        
        this.name = name;
        this.birthYear = birthYear;
    }
}
