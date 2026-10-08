package com.survivalcoding.day06_generic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Word 클래스 테스트")
class WordTest {

    private lateinit var word: Word

    @BeforeEach
    fun setUp() {
        word = Word("Android")
    }

    @Test
    fun `isVowel을 하면 지정된 글자가 모음인지를 판별해야 한다`() {
        // given
        val index = 0 // `A`ndroid

        // when & then
        assertTrue(word.isVowel(index))
    }

    @Test
    fun `isConsonant를 하면 지정된 글자가 자음인지를 판별해야 한다`() {
        // given
        val index = 1 // A`n`droid

        // when & then
        assertTrue(word.isConsonant(index))
    }

    @Test
    fun `글자가 알파벳이 아니면 false를 반환해야 한다`() {
        // given
        word = Word("###")
        val index = 0 // `#`##

        // when & then
        assertFalse(word.isVowel(index))
        assertFalse(word.isConsonant(index))
    }

    @Test
    fun `글자는 소문자로 변환되어야 한다`() {
        // given
        val index = 0 // `A`ndroid
        val a = word.letter(index) // a

        // when & then
        assertEquals("a", a)
        assertEquals("A".lowercase(), a)
    }

    @Test
    fun `글자 길이 미만의 인덱스는 글자의 첫 번째 인덱스로 보정한다`() {
        // given
        val index = -100 // `A`ndroid
        val a = word.letter(index) // a

        // when & then
        assertEquals("a", a)
    }

    @Test
    fun `글자 길이를 초과하는 인덱스는 글자의 마지막 인덱스로 보정한다`() {
        // given
        val index = 100 // Androi`d`
        val d = word.letter(index) // d

        // when & then
        assertEquals("d", d)
    }
}