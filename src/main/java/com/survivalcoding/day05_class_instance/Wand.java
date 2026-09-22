package com.survivalcoding.day05_class_instance; // 마법사와 같은 패키지에 지팡이 클래스를 둔다.

public class Wand { // 외부에서 사용할 수 있는 지팡이 클래스를 선언한다.
    private static final int MIN_NAME_LENGTH = 3; // 지팡이 이름은 최소 3글자여야 한다.
    private static final double MIN_POWER = 0.5; // 모든 지팡이가 공유하는 마력의 최솟값이다.
    private static final double MAX_POWER = 100.0; // 모든 지팡이가 공유하는 마력의 최댓값이다.

    private String name; // 이름을 숨기고 getter/setter로만 접근하게 한다.
    private double power; // 마력을 숨기고 setter에서 범위를 검사한다.

    public String getName() { // 지팡이 이름을 읽는 메서드다.
        return name; // 현재 이름을 반환한다.
    } // getName 메서드를 마친다.

    public void setName(String name) { // 외부에서 이름을 설정하는 메서드다.
        validateName(name); // 저장하기 전에 이름이 유효한지 검사한다.
        this.name = name; // 유효한 이름만 현재 객체에 저장한다.
    } // setName 메서드를 마친다.

    public double getPower() { // 지팡이의 마력을 읽는 메서드다.
        return power; // 현재 마력을 반환한다.
    } // getPower 메서드를 마친다.

    public void setPower(double power) { // 외부에서 마력을 설정하는 메서드다.
        validatePower(power); // 저장하기 전에 허용 범위인지 검사한다.
        this.power = power; // 유효한 마력만 현재 객체에 저장한다.
    } // setPower 메서드를 마친다.

    private void validateName(String name) { // 클래스 내부에서만 쓰는 검사 도우미는 private으로 숨긴다.
        if (name == null || name.trim().length() < MIN_NAME_LENGTH) { // null부터 검사한다. 앞뒤 공백 제외는 기존 코드의 추가 규칙이다.
            throw new IllegalArgumentException( // 잘못된 이름을 거부한다.
                    "지팡이 이름은 앞뒤 공백을 제외하고 " + MIN_NAME_LENGTH + "글자 이상이어야 합니다."); // 잘못된 이름을 거부한다.
        } // 이름 검사를 마친다.
    } // validateName 메서드를 마친다.

    private void validatePower(double power) { // 지팡이 마력을 검사하는 내부 메서드다.
        // NaN은 대소 비교로 걸러지지 않아 따로 검사한다. 무한대는 범위 검사로 거부한다.
        if (Double.isNaN(power) || power < MIN_POWER || power > MAX_POWER) {
            throw new IllegalArgumentException( // 허용 범위를 벗어난 마력을 거부한다.
                    "지팡이 마력은 " + MIN_POWER + " 이상 " + MAX_POWER + " 이하여야 합니다."); // 허용 범위를 벗어난 값을 거부한다.
        } // 마력 검사를 마친다.
    } // validatePower 메서드를 마친다.
} // Wand 클래스를 마친다. 기본 생성 후 이름과 마력은 setter로 설정한다.
