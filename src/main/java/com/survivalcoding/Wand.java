package com.survivalcoding;

/**
 * 지팡이(Wand)를 나타내는 클래스.
 * <p>
 * {@link Wizard} 가 사용하는 장비다.
 * 마법사와 마찬가지로 private 필드 + getter/setter + setter 에서 값 검증
 * (프록시 패턴의 단순한 형태)으로 구성되어 있다.
 * <p>
 * 이 클래스의 제약 조건은 MainTest 에서 검증된다.
 * - 이름: null 불가, 3글자 이상
 * - 마력: 0.5 이상 100.0 이하
 */
public class Wand {

    /** 지팡이의 이름. */
    private String name;

    /** 지팡이의 마력. */
    private double power;

    // ==================== 이름 ====================

    /**
     * @return 지팡이의 이름
     */
    public String getName() {
        return this.name;
    }

    /**
     * 지팡이의 이름을 설정한다.
     *
     * @param name 지팡이의 이름. null 이거나 3글자 미만이면 예외가 발생한다.
     * @throws IllegalArgumentException 이름이 null 이거나 길이가 3 미만인 경우
     */
    public void setName(String name) {

        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(
                    "지팡이의 이름은 null일 수 없으며 3문자 이상이어야 합니다."
            );
        }

        this.name = name;
    }

    // ==================== 마력 ====================

    /**
     * @return 지팡이의 마력
     */
    public double getPower() {
        return this.power;
    }

    /**
     * 지팡이의 마력을 설정한다.
     *
     * @param power 지팡이의 마력. 0.5 이상 100.0 이하여야 한다.
     * @throws IllegalArgumentException 마력이 범위를 벗어난 경우
     */
    public void setPower(double power) {

        if (power < 0.5 || power > 100.0) {
            throw new IllegalArgumentException(
                    "지팡이의 마력은 0.5 이상 100.0 이하여야 합니다."
            );
        }

        this.power = power;
    }
}
