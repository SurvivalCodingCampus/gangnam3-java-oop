package com.survivalcoding.day13.ExceptionEx

fun main() {
    val numStr = "10.5"

    val num: Int = try {
        numStr.toInt()
    } catch (e: NumberFormatException) {
        0
    }

    println(num)
}