package com.survivalcoding.day14

fun main() {
    var result = 4
    calc(6, 4) { x, y -> result += x + y } // result캡처, 클로저
    println(result) // 값 유지로 14
}

fun calc(a: Int, b: Int, operation: (Int, Int) -> Unit) {
    operation(a, b)
}