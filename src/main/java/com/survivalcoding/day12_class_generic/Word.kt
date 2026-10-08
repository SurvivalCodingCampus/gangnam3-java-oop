package com.survivalcoding.day12_class_generic

class Word(val word: String) {

    fun isVowel(i: Int): Boolean =
        word[i].lowercaseChar() in "aeiou"

    fun isConsonant(i: Int): Boolean {
        val ch = word[i].lowercaseChar()

        return ch in 'a'..'z' && ch !in "aeiou"
    }
}
