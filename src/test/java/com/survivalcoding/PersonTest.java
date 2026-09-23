package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTest {

    @Test
    void personTest() {

        Person person = new Person("홍길동", 1971);

        assertEquals("홍길동", person.getName());
        assertEquals(1971, person.getBirthYear());
        assertEquals(55, person.getAge());
    }
}