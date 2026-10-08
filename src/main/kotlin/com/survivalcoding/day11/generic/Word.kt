package com.survivalcoding.day11.generic

class Word(word: String) {
    companion object {
        private val VOWELS = charArrayOf('a', 'e', 'i', 'o', 'u')
    }

    private val _word = word

    fun isVowel(i: Int): Boolean {
        VOWELS.forEach {
            if (_word[i].lowercaseChar() == it) {
                return true;
            }
        }

        return false
    }

    fun isConsonant(i: Int): Boolean {
        return !isVowel(i)
    }
}