package com.survivalcoding;

public class Wand {

    private String name;
    private double power;

    // 이름 getter
    public String getName() {
        return this.name;
    }

    // 이름 setter
    public void setName(String name) {

        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(
                    "지팡이의 이름은 null일 수 없으며 3문자 이상이어야 합니다."
            );
        }

        this.name = name;
    }

    // 마력 getter
    public double getPower() {
        return this.power;
    }

    // 마력 setter
    public void setPower(double power) {

        if (power < 0.5 || power > 100.0) {
            throw new IllegalArgumentException(
                    "지팡이의 마력은 0.5 이상 100.0 이하여야 합니다."
            );
        }

        this.power = power;
    }
}