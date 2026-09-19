package com.survivalcoding.day01_class_instance;

public class Wand {

    // field
    private String name;
    private double power;

    // constructor
    public Wand(String name, double power) {
        this.name = name;
        this.power = power;
    }

    // getter
    public String getName() {
        return name;
    }

    public double getPower() {
        return power;
    }

    // setter
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name(이름)은 null이 아니어야 합니다");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("name(이름)은 3글자 이상이어야 합니다");
        }

        this.name = name;
    }

    public void setPower(double power) {
        if (power < 0.5) {
            throw new IllegalArgumentException("wand(지팡이)의 power(지력)는 0.5 이상이어야 합니다");
        }

        if (power > 100) {
            throw new IllegalArgumentException("wand(지팡이)의 power(지력)는 100을 넘길 수 없습니다");
        }

        this.power = power;
    }
}
