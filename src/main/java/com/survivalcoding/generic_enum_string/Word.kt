package com.survivalcoding.generic_enum_string

class Word(var word: String) {

    fun isVowel(i: Int): Boolean {
        return "aeiouAEIOU".contains(word.substring(i, i + 1))
    }

    fun isConsonant(i: Int) = !isVowel(i)
}