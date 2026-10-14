package com.survivalcoding.day06_generic

import com.survivalcoding.day05_kotlin_basic.Hero
import kotlin.system.measureTimeMillis

fun main() {
    val pocket = Pocket<String>()
    pocket.put("안녕하세요")
    println(pocket.get())

    val authState = AuthState.UNAUTHENTICATED
    something(authState)

    val strongBox = StrongBox<KeyType>(KeyType.PADLOCK)
    println(strongBox.get())

    val string = "HELLO"
    println(string.substring(0, 2))
    println(string.replace("LL", "XX"))
    println(string.lowercase())
    println(string.indexOf("E"))

    val parts = "1,2,3".split(",")
    parts.forEach(::println)

    val s1 = "KOTLIN"
    println(s1.length) // 6
    println(s1.isEmpty()) // false

    val s2 = "kotlin"

    println(s1 == s2) // false
    println(s1.lowercase() == s2.lowercase()) // true

    val s3 = "Kotlin and Android"
    println(s3.contains("Kotlin")) // true
    println(s3.endsWith("Android")) // true
    println(s3.indexOf("Kotlin")) // 0
    println(s3.lastIndexOf("A")) // 11

    println(s3.lowercase()) // 소문자로
    println(s3.uppercase()) // 대문자로
    println(s3.trim()) // 좌우 공백 제거
    println(s3.replace("and", "or")) // 교체

    val sb = StringBuilder("Kotlin")
    sb.append(" and ").append("Android")
    println(sb.toString())

    // val time = measureTimeMillis {
    //     var string = ""
    //     repeat(1_000_000) {
    //         string += it.toString()
    //     }
    // }

    // println(time)

    val time2 = measureTimeMillis {
        val sb = StringBuilder("")
        repeat(1_000_000) {
            sb.append(it.toString())
        }
    }

    println(time2)

    val hero1 = Hero("홍길동", "Mr.홍")
    val hero2 = Hero("홍길동", "Mr.홍")
    println(hero1 == hero2)

    val str1 = "hello"
    val str2 = "hello"
    println(str1 == str2) // true

    val str3 = String("hello".toCharArray())
    println(str1 === str3) // false

    val str4 = "hel" + "lo"
    println(str1 == str4) // true

    val str5 = "hel" + getLo()
    println(str1 === str5) // false

    var greeting = "Hello"
    println(greeting.replace("H", "J"))
    println(greeting)

    var greeting1 = "Hello, World!"
    var greeting2 = greeting1
    greeting2.uppercase()
    println(greeting1)
    println(greeting2)

    var luckyNumber1 = 13
    var luckyNumber2 = luckyNumber1
    luckyNumber2 = 12
    println(luckyNumber1)
    println(luckyNumber2)
}

fun getLo() = "lo"

fun something(authState: AuthState) {
    when (authState) {
        AuthState.AUTHENTICATED -> println("Authenticated")
        AuthState.UNAUTHENTICATED -> println("Unauthenticated")
        AuthState.UNKNOWN -> println("Unknown")
    }
}
