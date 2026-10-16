package com.survivalcoding.kotlin_exception

fun toIntOrDefault(numString: String) = try {
    numString.toInt()
} catch (e: NumberFormatException) {
    0
}

fun main() {
    val numString = "10.5"
    val num = toIntOrDefault(numString)

    println(num)
}
