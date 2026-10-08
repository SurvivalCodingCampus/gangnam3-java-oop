package com.survivalcoding.day12_class_generic

class Word(val word: String) {

    fun isVowel(i: Int): Boolean =
        word[i].lowercaseChar() in "aeiou"
}
