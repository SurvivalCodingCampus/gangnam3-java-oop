package com.survivalcoding.day05_class_instance; // 같은 학습 패키지에 Person 클래스를 둔다.

import java.time.Year; // 현재 연도를 구하는 Java 표준 클래스를 가져온다.

public class Person { // 외부에서 사용할 수 있는 사람 클래스를 선언한다.
    private static final int MIN_NAME_LENGTH = 2; // 사람 이름의 최소 길이를 정한다.
    private static final int MIN_BIRTH_YEAR = 1; // 허용할 출생 연도의 최솟값을 정한다.

    private final String name; // 이름은 외부에서 직접 접근할 수 없고 생성 후 재대입할 수 없다.
    private final int birthYear; // 태어난 해도 생성자에서 한 번만 정한다.

    public Person(String name, int birthYear) { // 이름과 태어난 해를 받아 객체를 만든다.
        validateName(name); // 이름을 저장하기 전에 유효성을 검사한다.
        validateBirthYear(birthYear); // 출생 연도를 저장하기 전에 유효성을 검사한다.
        this.name = name; // 검사를 통과한 이름을 현재 객체에 저장한다.
        this.birthYear = birthYear; // 검사를 통과한 출생 연도를 현재 객체에 저장한다.
    } // 생성자를 마친다. 값을 바꾸는 setter는 제공하지 않는다.

    public String getName() { // 외부에서 이름을 읽는 getter를 제공한다.
        return name; // 저장한 이름을 반환한다.
    } // getName 메서드를 마친다.

    public int getBirthYear() { // 외부에서 태어난 해를 읽는 getter를 제공한다.
        return birthYear; // 저장한 출생 연도를 반환한다.
    } // getBirthYear 메서드를 마친다.

    public int getAge() { // 나이는 별도 필드나 setter 없이 호출할 때 계산한다.
        int currentYear = Year.now().getValue(); // 나이를 조회하는 시점의 연도를 구한다.
        return currentYear - birthYear; // 교재의 계산식인 올해 연도 - 출생 연도를 반환한다.
    } // getAge 메서드를 마친다. 생일을 따지는 만 나이 계산은 아니다.

    private void validateName(String name) { // 생성자에서 사용하는 이름 검사 도우미다.
        if (name == null || name.trim().length() < MIN_NAME_LENGTH) { // null과 앞뒤 공백을 제외한 길이를 검사한다.
            throw new IllegalArgumentException( // 잘못된 이름으로 객체를 생성하지 못하게 한다.
                    "이름은 앞뒤 공백을 제외하고 " + MIN_NAME_LENGTH + "글자 이상이어야 합니다."); // 검사 기준과 안내를 일치시킨다.
        } // 이름 검사를 마친다.
    } // validateName 메서드를 마친다.

    private void validateBirthYear(int birthYear) { // 생성자에서 사용하는 출생 연도 검사 도우미다.
        int currentYear = Year.now().getValue(); // 생성 시점의 현재 연도를 구한다.
        if (birthYear < MIN_BIRTH_YEAR || birthYear > currentYear) { // 양수가 아닌 연도와 미래 연도를 거부한다.
            throw new IllegalArgumentException( // 잘못된 출생 연도로 객체를 생성하지 못하게 한다.
                    "태어난 해는 " + MIN_BIRTH_YEAR + " 이상 올해 이하의 연도여야 합니다."); // 최소 연도를 상수와 연결한다.
        } // 출생 연도 검사를 마친다.
    } // validateBirthYear 메서드를 마친다.
} // Person 클래스를 마친다.
