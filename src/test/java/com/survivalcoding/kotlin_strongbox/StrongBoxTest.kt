package com.survivalcoding.kotlin_strongbox

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource

class StrongBoxTest {
	val item = "짜잔"
	
	@ParameterizedTest
	@EnumSource(KeyType::class)  // ::class = 해당 클래스의 정보 가져오는 참조 연산자
	fun `키 타입에 따라 정해진 횟수가 되면 열린다`(keyType: KeyType) {
		val sb = StrongBox<String>(keyType)
		sb.put(item)
		
		val attemptCount = keyType.attemptCount
		val testCount = attemptCount - 1
		
		repeat(testCount) {
			sb.get()
		}
		val result = sb.get()
		
		assertEquals(item, result)
	}
	
	@ParameterizedTest
	@EnumSource(KeyType::class)
	fun `키 타입에 따라 정해진 횟수가 되지 않으면 열리지 않는다 - 경계값`(keyType: KeyType) {
		val sb = StrongBox<String>(keyType)
		sb.put(item)
		
		val attemptCount = keyType.attemptCount
		val testCount = attemptCount - 2
		
		repeat(testCount) {
			sb.get()
		}
		val result = sb.get()
		
		assertEquals(null, result)
	}
	
	@ParameterizedTest
	@EnumSource(KeyType::class)
	fun `키 타입에 따라 정해진 횟수를 넘으면 열린다 - 경계값`(keyType: KeyType) {
		val sb = StrongBox<String>(keyType)
		sb.put(item)
		
		val attemptCount = keyType.attemptCount
		val testCount = attemptCount
		
		repeat(testCount) {
			sb.get()
		}
		val result = sb.get()
		
		assertEquals(item, result)
	}
}