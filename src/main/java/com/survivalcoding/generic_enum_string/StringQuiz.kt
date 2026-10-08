package com.survivalcoding.generic_enum_string

fun main() {
    val str1 = "hello"  // 컴파일 타임
    val str2 = "hello"  // 컴파일 타임
    println(str1 === str2)

    val str3 = String("hello".toCharArray())    // 런타임 생성
    println(str1 === str3)
    println(str1)
    println(str3)

    val str4 = "hel" + "lo"
    println(str1 === str4)

    val str5 = "hel" + getLo()
    println(str1 === str5)

    var greeting = "Hello"

    println(greeting.replace("H", "J"))
    println(greeting)

    var luckyNumber1 = 13;
    var luckyNumber2 = luckyNumber1;
    luckyNumber2 = 12;

    println(luckyNumber1)
    println(luckyNumber2)

}

fun getLo(): String {
    return "lo"
}