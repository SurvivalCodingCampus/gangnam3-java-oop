package com.survivalcoding.kotlin_string

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WordTest {
	val testString = "Apple"
	val upperVowelIndex = testString.indexOf("A")
	val lowerVowelIndex = testString.indexOf("e")
	val consonantIndex = testString.indexOf("p")
	
	val word = Word(testString)
	
	@Test
	fun `모든 모음 테스트`() {
		val lowerVowels = "aeiou"
		val upperVowels = lowerVowels.uppercase()
		
		val lowerWord = Word(lowerVowels)
		val upperWord = Word(upperVowels)
		
		repeat(lowerVowels.length) { i ->
			assertTrue(lowerWord.isVowel(i))
			assertFalse(lowerWord.isConsonant(i))
			
			assertTrue(upperWord.isVowel(i))
			assertFalse(upperWord.isConsonant(i))
		}
	}
	
	@Test
	fun `모든 자음 테스트`() {
		val lowerConsonant = "bcdfghjklmnpqrstvwxyz"
		val upperConsonant = lowerConsonant.uppercase()
		
		val lowerWord = Word(lowerConsonant)
		val upperWord = Word(upperConsonant)
		
		repeat(lowerConsonant.length) { i ->
			assertFalse(lowerWord.isVowel(i))
			assertTrue(lowerWord.isConsonant(i))
			
			assertFalse(upperWord.isVowel(i))
			assertTrue(upperWord.isConsonant(i))
		}
	}
	
	@Test
	fun `모음 위치 인덱스 조회 시 모음 판별 확인`() {
		// 대문자 모음
		assertTrue(word.isVowel(upperVowelIndex))
		
		// 소문자 모음
		assertTrue(word.isVowel(lowerVowelIndex))
	}
	
	@Test
	fun `모음 위치 인덱스 조회 시 자음 판별 확인`() {
		// 대문자 모음
		assertFalse(word.isConsonant(upperVowelIndex))
		
		// 소문자 모음
		assertFalse(word.isConsonant(lowerVowelIndex))
	}
	
	@Test
	fun `자음 위치 인덱스 조회 시 자음 판별 확인`() {
		assertTrue(word.isConsonant(consonantIndex))
	}
	
	@Test
	fun `자음 위치 인덱스 조회 시 모음 판별 확인`() {
		assertFalse(word.isVowel(consonantIndex))
	}
	
	@Test
	fun `특수 문자 및 공백, 숫자 처리 확인`() {
		val testString = " 1!\t"
		val word = Word(testString)
		
		// 모음 판별
		repeat(testString.length) { i ->
			assertFalse(word.isVowel(i))
		}
		
		// 자음 판별
		repeat(testString.length) { i ->
			assertFalse(word.isConsonant(i))
		}
	}
	
	@Test
	fun `글자 수를 벗어나거나 음수인 인덱스`() {
		// 벗어났거나 음수이거나
		val outOfIndex = testString.length
		val invalidIndex = -1
		
		assertThrows<IndexOutOfBoundsException> { word.isVowel(outOfIndex) }
		assertThrows<IndexOutOfBoundsException> { word.isConsonant(outOfIndex) }
		
		assertThrows<IndexOutOfBoundsException> { word.isVowel(invalidIndex) }
		assertThrows<IndexOutOfBoundsException> { word.isConsonant(invalidIndex) }
	}
	
	@Test
	fun `첫 글자와 마지막 글자 검증 - 경계값`() {
		val lastIndex = testString.length - 1
		
		assertTrue(word.isVowel(0))
		assertFalse(word.isConsonant(0))
		
		assertTrue(word.isVowel(lastIndex))
		assertFalse(word.isConsonant(lastIndex))
	}
	
	@Test
	fun `빈 문자열 검증`() {
		val testString = ""
		
		val word = Word(testString)
		
		assertThrows<IndexOutOfBoundsException> { word.isVowel(0) }
		assertThrows<IndexOutOfBoundsException> { word.isConsonant(0) }
	}
	
	@Test
	fun `word 속성 변경 후 동작 확인`() {
		val newTestString = "New Orange"
		val newConsonantIndex = newTestString.indexOf("N")
		val newLowerVowelIndex = newTestString.indexOf("e")
		val newUpperVowelIndex = newTestString.indexOf("O")
		
		word.word = newTestString
		
		assertTrue(word.isConsonant(newConsonantIndex))
		assertFalse(word.isVowel(newConsonantIndex))
		
		assertFalse(word.isConsonant(newLowerVowelIndex))
		assertTrue(word.isVowel(newLowerVowelIndex))
		
		assertFalse(word.isConsonant(newUpperVowelIndex))
		assertTrue(word.isVowel(newUpperVowelIndex))
	}
}