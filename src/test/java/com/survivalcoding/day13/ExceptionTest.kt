package com.survivalcoding.day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ExceptionTest {

    @Test
    @DisplayName("정수 문자열은 그 숫자로 바뀐다")
    fun validInteger() {
        assertEquals(10, convertToInt("10"))
    }

    @Test
    @DisplayName("음수 문자열도 그 숫자로 바뀐다")
    fun validNegativeInteger() {
        assertEquals(-5, convertToInt("-5"))
    }

    @Test
    @DisplayName("소수 문자열은 0")
    fun decimalString() {
        assertEquals(0, convertToInt("10.5"))
    }

    @Test
    @DisplayName("숫자가 아닌 문자열은 0")
    fun notNumberString() {
        assertEquals(0, convertToInt("a"))
    }

    @Test
    @DisplayName("빈 문자열은 0")
    fun emptyString() {
        assertEquals(0, convertToInt(""))
    }
}