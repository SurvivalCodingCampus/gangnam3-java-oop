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
        if (power <= 0.5) {
            throw new IllegalArgumentException("완드의 파워는 0.5 미만 일 수 없습니다.");
        }
        if (power >= 100) {
            throw new IllegalArgumentException("완드의 파워는 100이상 일 수 없습니다.");
        }
        this.power = power;
    }
}

