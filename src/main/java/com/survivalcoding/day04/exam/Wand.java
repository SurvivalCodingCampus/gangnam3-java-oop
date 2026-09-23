package com.survivalcoding.day04.exam;

public class Wand {
    private static final double MAX_POWER = 100;
    private static final double MIN_POWER = 0.5;
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
        if (!Utils.isWithinRange(power, MAX_POWER, MIN_POWER)) {
            throw new IllegalArgumentException(
                "유효한 범위를 입력해 주세용 max(%f) min(%.1f)"
                    .formatted(MAX_POWER, MIN_POWER)
            );
        }

        this.power = power;
    }
}
