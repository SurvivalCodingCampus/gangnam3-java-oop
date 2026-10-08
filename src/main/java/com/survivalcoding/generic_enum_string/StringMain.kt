package com.survivalcoding.generic_enum_string

fun main() {
    val strings = "1,2,3"
    val parts: List<String> = strings.split(",")

    println(parts[0])

    parts.forEach(::println)

    val string = "HELLO"
    println(string.lowercase())

    val s1 = "KOTLIN"
    val s2 = "kotlin"

    println(s1 == s2)

//    var isEmpty: Boolean = false
//    if (s1.length == 0) {
//        isEmpty = true
//    }

    println(s1.isEmpty())

    val i1 = 10
    val i2 = 20
    println(i1 == i2)
}