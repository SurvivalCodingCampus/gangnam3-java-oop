package com.survivalcoding.day01_class_instance.generic

class Word(var word: String) {

    fun isVowel(i: Int): Boolean {
        return "aeiou".contains(word.substring(i, i + 1).lowercase())
    }

    fun isConsonant(i: Int): Boolean {
        return !isVowel(i)
    }
}

fun main() {
    val wordCheck: Word = Word("Apple")

    repeat(wordCheck.word.length) {
        println(wordCheck.word.substring(it, it + 1) + "모음" + wordCheck.isVowel(it))
    }
}