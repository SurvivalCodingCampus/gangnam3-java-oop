package com.survivalcoding.day06_generic

class Word(var word: String) {

    val letter: (Int) -> String = { index ->
        val startIndex = index.coerceIn(0, word.lastIndex)
        val endIndex = index.coerceIn(startIndex, word.lastIndex) + 1
        word.substring(startIndex, endIndex).lowercase()
    }

    private val vowel = setOf("a", "e", "i", "o", "u")

    fun isVowel(i: Int): Boolean = letter(i) in vowel

    fun isConsonant(i: Int): Boolean = letter(i).all { it.isLetter() } && letter(i) !in vowel
}