package com.survivalcoding.day06_generic

class Word(word: String) {

    var word: String = word
        set(value) {
            validateWord(value)
            field = value
        }

    init {
        validateWord(word)
    }

    val letter: (Int) -> String = { index ->
        val startIndex = index.coerceIn(0, word.lastIndex)
        val endIndex = index.coerceIn(startIndex, word.lastIndex) + 1
        word.substring(startIndex, endIndex).lowercase()
    }

    private val vowel = setOf("a", "e", "i", "o", "u")

    fun isVowel(i: Int): Boolean = letter(i) in vowel

    fun isConsonant(i: Int): Boolean = letter(i).all { it.isLetter() } && letter(i) !in vowel

    private fun validateWord(word: String) {
        require(word.isNotEmpty()) { "word는 비어 있을 수 없습니다" }
    }
}