package com.survivalcoding.day04.exam;

public class Wand {
    private static final double MAX_POWER = 100;
    private static final double MIN_POWER = 0.5;
    private static final double DEFAULT_POWER = 5;
    private static final String DEFAULT_NAME = "지팡이";
    private static final int MIN_NAME_LENGTH = 3;

    private String name;
    private double power;

    // Builder를 통해서만 객체 생성 가능
    private Wand(Builder builder) {
        setName(builder.name);
        setPower(builder.power);
    }

    // region Getter Setter

    public String getName() {
        return name;
    }

    public double getPower() {
        return power;
    }

    private void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }

        if (name.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }

        this.name = name;
    }

    /**
     * 유효범위 max 100 min 0.5
     */
    private void setPower(final double power) {
        if (!Utils.isWithinRange(power, MAX_POWER, MIN_POWER)) {
            throw new IllegalArgumentException(
                    "유효한 범위를 입력해 주세용 max(%f) min(%.1f)"
                            .formatted(MAX_POWER, MIN_POWER)
            );
        }

        this.power = power;
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