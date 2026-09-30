package com.survivalcoding;

import java.time.LocalDate;

/**
 * 사람(Person)을 나타내는 클래스.
 * <p>
 * "생성된 이후에는 바뀌지 않는 값"을 다루는 방법을 보여 준다.
 * 이름과 출생연도는 {@code final} 로 선언되어 있어,
 * 생성 시 한 번만 정해지고 이후에는 변경할 수 없다.
 * <p>
 * 변경 불가능한 값에는 getter 만 제공하고 setter 를 만들지 않는 것이 일반적이다.
 */
public class Person {

    /** 사람의 이름. final 이므로 생성 후 변경할 수 없다. */
    private final String name;

    /** 사람의 출생 연도. final 이므로 생성 후 변경할 수 없다. */
    private final int birthYear;

    /**
     * 이름과 출생연도를 지정하는 생성자.
     *
     * @param name      사람의 이름
     * @param birthYear 사람의 출생 연도
     */
    public Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    /**
     * @return 사람의 이름
     */
    public String getName() {
        return name;
    }

    /**
     * @return 사람의 출생 연도
     */
    public int getBirthYear() {
        return birthYear;
    }

    /**
     * 현재 연도와 출생 연도의 차이로 나이를 계산한다.
     * <p>
     * {@code LocalDate.now()} 으로 시스템의 현재 날짜를 얻고 연도만 꺼낸다.
     * 생일이 아직 오지 않았을 때의 보정(만 나이)은 고려하지 않는 단순한 구현이다.
     *
     * @return 계산된 나이(세)
     */
    public int getAge() {
        int currentYear = LocalDate.now().getYear();
        return currentYear - birthYear;
    }
}
