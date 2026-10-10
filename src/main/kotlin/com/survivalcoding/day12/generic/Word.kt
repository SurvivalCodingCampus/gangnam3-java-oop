package com.survivalcoding.day12.generic

import java.util.Arrays

class Word(word: String) {
    companion object {
        private val VOWELS = charArrayOf('a', 'e', 'i', 'o', 'u')
    }

    init {
        require(word.all { it in 'A'..'Z' || it in 'a'..'z' }) {
            "영문자만 입력 가능"
        }
    }

    private val _word = word

    fun isVowel(i: Int): Boolean = _word[i].lowercaseChar() in VOWELS

    fun isConsonant(i: Int): Boolean = !isVowel(i)
}