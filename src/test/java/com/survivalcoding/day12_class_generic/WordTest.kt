package com.survivalcoding.day12_class_generic

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WordTest {

    @Test
    fun `소문자와 대문자 모음 다섯 가지를 모음으로 판단한다`() {
        val word = Word("aeiouAEIOU")

        for (i in word.word.indices) {
            assertTrue(word.isVowel(i), "'${word.word[i]}'는 모음이어야 한다")
        }
    }

    @Test
    fun `소문자와 대문자 자음을 자음으로 판단한다`() {
        val word = Word("bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ")

        for (i in word.word.indices) {
            assertTrue(word.isConsonant(i), "'${word.word[i]}'는 자음이어야 한다")
        }
    }

    @Test
    fun `모음은 자음으로 판단하지 않는다`() {
        val word = Word("aeiouAEIOU")

        for (i in word.word.indices) {
            assertFalse(word.isConsonant(i), "'${word.word[i]}'는 자음이 아니다")
        }
    }

    @Test
    fun `자음은 모음으로 판단하지 않는다`() {
        val word = Word("bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ")

        for (i in word.word.indices) {
            assertFalse(word.isVowel(i), "'${word.word[i]}'는 모음이 아니다")
        }
    }

    @Test
    fun `숫자 공백 기호 한글은 영어 모음도 자음도 아니다`() {
        val word = Word("0123456789 !?@#가나다")

        for (i in word.word.indices) {
            assertFalse(word.isVowel(i), "'${word.word[i]}'는 영어 모음이 아니다")
            assertFalse(word.isConsonant(i), "'${word.word[i]}'는 영어 자음이 아니다")
        }
    }

    @Test
    fun `인덱스는 0부터 시작하며 해당 위치의 글자를 판단한다`() {
        val word = Word("Hello")

        assertFalse(word.isVowel(0))
        assertTrue(word.isConsonant(0))
        assertTrue(word.isVowel(1))
        assertFalse(word.isConsonant(1))
        assertFalse(word.isVowel(2))
        assertTrue(word.isConsonant(2))
        assertFalse(word.isVowel(3))
        assertTrue(word.isConsonant(3))
        assertTrue(word.isVowel(4))
        assertFalse(word.isConsonant(4))
    }

    @Test
    fun `문자열 범위를 벗어난 인덱스는 예외를 발생시킨다`() {
        val word = Word("Hello")

        for (i in listOf(-1, 5)) {
            assertThrows(IndexOutOfBoundsException::class.java) { word.isVowel(i) }
            assertThrows(IndexOutOfBoundsException::class.java) { word.isConsonant(i) }
        }
    }

    @Test
    fun `빈 문자열은 확인할 글자가 없어 예외를 발생시킨다`() {
        val word = Word("")

        assertThrows(IndexOutOfBoundsException::class.java) { word.isVowel(0) }
        assertThrows(IndexOutOfBoundsException::class.java) { word.isConsonant(0) }
    }
}
