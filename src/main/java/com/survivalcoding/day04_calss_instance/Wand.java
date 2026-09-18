package com.survivalcoding.day04_calss_instance;

public class Wand {
    private String name;
    private double power;
    
    // constructor
    public Wand() {
        this("지팡이", 10.0);
    }
    
    public Wand(String name) {
        this(name, 10.0);
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
        this.name = name;
    }
    
    public double getPower() {
        return power;
    }
    
    public void setPower(double power) {
        this.power = power;
    }
}
