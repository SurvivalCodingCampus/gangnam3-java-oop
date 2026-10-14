package com.survivalcoding.generic_enum_string

class Word(var word: String) {

    fun isVowel(i: Int): Boolean {
        return "aeiou".contains(word.substring(i, i + 1).lowercase())
    }

    fun isConsonant(i: Int): Boolean {
        return !isVowel(i)
    }
}