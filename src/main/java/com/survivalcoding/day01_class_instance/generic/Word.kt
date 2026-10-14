package com.survivalcoding.day01_class_instance.generic

class Word(var word: String) {

    fun isVowel(i: Int): Boolean {
        return "aeiouAEIOU".contains(word.substring(i, i + 1))
    }

    fun isConsonant(i: Int) = !isVowel(i)
    }
}

fun main() {
    val wordCheck: Word = Word("Apple")

    repeat(wordCheck.word.length) {
        println(wordCheck.word.substring(it, it + 1) + "모음" + wordCheck.isVowel(it))
    }
}