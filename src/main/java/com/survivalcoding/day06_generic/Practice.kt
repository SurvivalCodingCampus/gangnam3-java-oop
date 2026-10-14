package com.survivalcoding.day06_generic

fun main() {
    val strongBox = StrongBox<KeyType>(KeyType.PADLOCK)

    repeat(KeyType.PADLOCK.limit) { _ ->
        val data = strongBox.get()
        data?.let { keyType ->
            println("$keyType 잠금해제 성공! 사용횟수: ${strongBox.count}")
        } // ?: println("잠금해제 실패")
    }

    println(strongBox.count)

    strongBox.put(KeyType.BUTTON)
    println(strongBox.count)

    val word = Word("Android")
    val index = 0
    println(word.letter(index))
    println("isVowel: ${word.isVowel(index)}")
    println("isConsonant: ${word.isConsonant(index)}")
}