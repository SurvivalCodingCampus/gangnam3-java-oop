package com.survivalcoding.day04_calss_instance;

public class Wand {
    static final double MAX_POWER = 10.0;
    
    private String name;
    private double power;
    
    // constructor
    public Wand() {
        this("Mr.지팡", MAX_POWER);
    }
    
    public Wand(String name) {
        this(name, MAX_POWER);
    }
    
    public Wand(String name, double power) {
        this.name = name;
        this.power = power;
    }
    
    // getter/setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        Validator.validateNotNull(name);
        Validator.validateMinLength(name, 2);
        
        this.name = name;
    }
    
    public double getPower() {
        return power;
    }
    
    public void setPower(double power) {
        this.power = power;
    }
}
