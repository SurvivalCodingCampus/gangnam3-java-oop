package com.survivalcoding;


public class wand {
    public int MIN_NAME_LENGTH = 3;
    private String name;
    private double power;

    public String getName() {
        //null 우선 검사
        if (name == null) {
            throw new IllegalArgumentException("이름이 null 하네요");
        } else if (name.length() <= 3) {
            throw new IllegalArgumentException("3글자 이하입니다");
        } else {
            return name;
        }
    }

    public void setPower(double power2) {

        if (power2 <= 100 && 3 <= power2) {
            power = power2;
            return;
        } else {
            throw new IllegalArgumentException("유효하지 않는 파워입니다");
        }
    }

    public double getpower() {
        return power;
    }


}



