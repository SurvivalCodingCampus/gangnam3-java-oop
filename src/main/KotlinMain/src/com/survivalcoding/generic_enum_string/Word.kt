package com.survivalcoding.generic_enum_string

class Word(var word: String) {
    fun isVowel(i: Int): Boolean {
        // i번째 글자 한 글자를 잘라냅니다.
        val letter = word.substring(i, i + 1)
        // 모음 5개 중 하나와 일치하는지 확인합니다.
        return letter == "a" || letter == "e" || letter == "i" || letter == "o" || letter == "u"
    }
    fun isConsonant(i: Int): Boolean {
        // 모음이 아니라면 자음입니다! (isVowel의 결과가 거짓이면 참)
        return !isVowel(i)
    }
}

