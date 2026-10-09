package com.survivalcoding.day12.generic

class Word(word: String) {
    companion object {
        private val VOWELS = charArrayOf('a', 'e', 'i', 'o', 'u')
    }

    init {
        word.forEach {
            require (it in 'A'.. 'Z' || it in 'a'..'z') {
                "영문자만 입력 가능"
            }
        }
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