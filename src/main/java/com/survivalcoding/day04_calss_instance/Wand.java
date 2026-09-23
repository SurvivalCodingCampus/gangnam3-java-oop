package com.survivalcoding.day04_calss_instance;

public class Wand {
    static final double MIN_POWER = 0.5;
    static final double MAX_POWER = 100.0;
    static final int MIN_NAME_LENGTH = 2;
    
    private double power;
    private String name;
    
    // constructor
    public Wand() {
        this("Mr.지팡", MAX_POWER);
    }
    
    public Wand(String name) {
        this(name, MAX_POWER);
    }
    
    public Wand(String name, double power) {
        setName(name);
        setPower(power);
    }
    
    // getter/setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        Validator.validateNotNull(name);
        Validator.validateMinLength(name, MIN_NAME_LENGTH);
        
        this.name = name;
    }
    
    public double getPower() {
        return power;
    }
    
    public void setPower(double power) {
        Validator.validateRangeInclusive(power, MIN_POWER, MAX_POWER);
        
        this.power = power;
    }
}
