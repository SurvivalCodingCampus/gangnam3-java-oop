package com.survivalcoding.day14_extension

fun main() {
    val name = "홍길동"

    println(name.myFunction())
}

// 확장함수
fun String.myFunction(): String {
    return "Hello World" + substring(0, 2)
}