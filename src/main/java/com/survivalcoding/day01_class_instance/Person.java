package com.survivalcoding.day01_class_instance;

import java.time.Year;

public class Person {
    public int getBirthYear() {
        return birthYear;
    }

    public String getName() {
        return name;
    }

    // Person class 의 특성? 변수들
    private final String name;
    private final int birthYear;


    // 생성자
    Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }


    // 나이 계산 메서드
    public int getAge() {
        int currentYear = Year.now().getValue();   // 실시간 연도 불러오기
        return currentYear - this.birthYear;

    }

}


