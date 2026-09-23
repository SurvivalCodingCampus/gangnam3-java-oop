package com.survivalcoding.day04.exam;

public class Wand {
    private static final int MIN_NAME_LENGTH = 3;
    private String name; // 지팡이의 이름
    private double power; // 지팡이의 마력

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }

        if (name.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }

        this.name = name;
    }

    public double getPower() {
        return power;
    }

    /**
     * 유효범위 max 100 min 0.5
     */
    public void setPower(final double power) {
        final double maxPower = 100;
        final double minPower = 0.5;

        if (!Utils.isWithinRange(power, maxPower, minPower)) {
            throw new IllegalArgumentException(
                "유효한 범위를 입력해 주세용 max(%f) min(%.1f)"
                    .formatted(maxPower, minPower)
            );
        }

        this.power = power;
    }
}
