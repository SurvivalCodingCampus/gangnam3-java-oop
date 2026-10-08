package com.survivalcoding.day12_class_generic

fun main() {
    val word = Word("Hello")

    for (i in word.word.indices) {
        val type = when {
            word.isVowel(i) -> "모음"
            word.isConsonant(i) -> "자음"
            else -> "영어 알파벳 아님"
        }

        println("${i + 1}번째 글자 '${word.word[i]}': $type")
    }
}
