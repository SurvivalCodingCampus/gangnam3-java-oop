package com.survivalcoding.day07_exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("toInt 예외 처리 테스트")
class ToIntTest {

    @Test
    fun `정수 형식의 문자열은 toInt로 변환된다`() {
        // given
        val numString = "10"

        // when
        val num = numString.toInt()

        // then
        assertEquals(10, num)
    }

    @Test
    fun `정수 형식이 아닌 문자열을 toInt하면 NumberFormatException 예외를 던진다`() {
        // given
        val numString = "10.5"

        // when & then
        assertThrows(NumberFormatException::class.java) {
            numString.toInt()
        }
    }

    @Test
    fun `try-catch로 NumberFormatException을 처리하면 기본값 0을 반환한다`() {
        // given
        val numString = "10.5"

        // when
        val num: Int = try {
            numString.toInt()
        } catch (e: NumberFormatException) {
            0
        }

        // then
        assertEquals(0, num)
    }

    @Test
    fun `try-catch에서 예외가 발생하지 않으면 변환된 값을 반환한다`() {
        // given
        val numString = "10"

        // when
        val num: Int = try {
            numString.toInt()
        } catch (e: NumberFormatException) {
            0
        }

        // then
        assertEquals(10, num)
    }

    @Test
    fun `toIntOrNull은 변환에 실패하면 null을 반환하고 엘비스 연산자로 기본값을 지정할 수 있다`() {
        // given
        val numString = "10.5"

        // when
        val nullable = numString.toIntOrNull()
        val num = numString.toIntOrNull() ?: 0

        // then
        assertNull(nullable)
        assertEquals(0, num)
    }

    @Test
    fun `runCatching은 변환에 실패하면 getOrDefault로 기본값을 반환한다`() {
        // given
        val numString = "10.5"

        // when
        val num = numString.runCatching { toInt() }.getOrDefault(0)

        // then
        assertEquals(0, num)
    }

    @Test
    fun `runCatching은 변환에 실패하면 NumberFormatException을 담은 실패 결과를 반환한다`() {
        // given
        val numString = "10.5"

        // when
        val result = numString.runCatching { toInt() }

        // then
        assertTrue(result.isFailure)
        assertInstanceOf(NumberFormatException::class.java, result.exceptionOrNull())
    }
}
