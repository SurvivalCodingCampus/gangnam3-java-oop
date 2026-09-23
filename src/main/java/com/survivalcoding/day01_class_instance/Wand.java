package com.survivalcoding.day01_class_instance;

public class Wand {
    private String name;    // 지팡이의 이름
    double power;   // 지팡이의 마력

    // 생성자
    Wand (String name, double power) {
        this.name = name;
        this.power = power;
    }

    // 캡슐화 (getter & setter)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 아니어야 함");
        }
        if (name.length() <= 3) {
            throw new IllegalArgumentException("이름이 너무 짧음");
        }
        this.name = name;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        if (power < 0.5) {
            throw new IllegalArgumentException("지팡이로서 가치가 없습니다");
        }
        if (power > 100.0) {
            throw new IllegalArgumentException("지팡이로서 가치가 지나칩니다");
        }
        this.power = power;
    }
}
