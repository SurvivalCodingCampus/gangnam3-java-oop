package com.survivalcoding.day04.exam;

public class Wand {
    private static final double MAX_POWER = 100;
    private static final double MIN_POWER = 0.5;
    private static final double DEFAULT_POWER = 5;
    private static final String DEFAULT_NAME = "지팡이";
    private static final int MIN_NAME_LENGTH = 3;

    private String name;
    private double power;

    private Wand(Builder builder) {
        if (builder.name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }

        if (builder.name.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }

        if (!Utils.isWithinRange(builder.power, MAX_POWER, MIN_POWER)) {
            throw new IllegalArgumentException(
                    "유효한 범위를 입력해 주세용 max(%f) min(%.1f)"
                            .formatted(MAX_POWER, MIN_POWER)
            );
        }

        name = builder.name;
        power = builder.power;
    }

    // region Getter

    public String getName() {
        return name;
    }

    public double getPower() {
        return power;
    }

    // endregion

    public static class Builder {
        private String name = DEFAULT_NAME;
        private double power = DEFAULT_POWER;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder power(double power) {
            this.power = power;
            return this;
        }

        public Wand build() {
            return new Wand(this);
        }
    }
}