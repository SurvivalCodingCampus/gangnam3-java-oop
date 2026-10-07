package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Person} 클래스의 테스트.
 * <p>
 * 검증 대상: 생성자로 받은 이름과 출생연도가 그대로 저장되는지,
 * 그리고 {@code getAge()} 가 현재 연도에서 출생연도를 뺀 값을 돌려주는지.
 * <p>
 * 주의: 나이는 테스트 실행 시점의 연도를 기준으로 계산되므로,
 * 출생연도 1971 에 대해 기대값 55 는 2026 년 기준이다.
 * 몇 년이 지나면 이 단언이 실패하므로, 연도가 바뀔 때마다
 * 기대값도 함께 갱신해야 한다.
 */
public class PersonTest {

    /**
     * 이름, 출생연도, 그리고 계산된 나이가 모두 기대값과 일치하는지 확인한다.
     */
    @Test
    void personTest() {

        Person person = new Person("홍길동", 1971);

        assertEquals("홍길동", person.name());
        assertEquals(1971, person.birthYear());
        assertEquals(55, person.getAge());
    }
}