package com.survivalcoding.kotlin_exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ToIntOrDefaultTest {
	@Test
	fun `정수 형태의 문자열 입력 시 int 반환`() {
		val input = "123"
		val result = toIntOrDefault(input)
		
		assertEquals(123, result)
	}
	
	@Test
	fun `음수 형태의 문자열 입력 시 음수 int 반환`() {
		val input = "-1"
		val result = toIntOrDefault(input)
		
		assertEquals(-1, result)
	}
	
	@ParameterizedTest
	@ValueSource(strings = ["0.0", "a", "1st", "", " "])
	fun `int 변환 불가한 경우 기본값 0을 반환`(input: String) {
		val result = toIntOrDefault(input)
		
		assertEquals(0, result)
	}
}