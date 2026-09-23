package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HappyBirthDayTest {
    @Test
    void getAge() {
        HappyBirthDay happyBirthDay = new HappyBirthDay("Titus", 1000);

        assertEquals(20, happyBirthDay.getAge());
    }


}