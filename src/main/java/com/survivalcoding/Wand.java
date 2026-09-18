package com.survivalcoding;

public class Wand {
    private String name;
    private double power;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름을 입력하세요.");
        }
        if (name.length() <= 3) {
            throw new IllegalArgumentException("이름이 짧아요.");
        }
        this.name = name;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        if (power <= 0.5 && power <= 100) {
            throw new IllegalArgumentException("완드이 파워는 0.5 미만이거나 100을 초과 할 수 없어요.");
        }
        this.power = power;
    }
}

