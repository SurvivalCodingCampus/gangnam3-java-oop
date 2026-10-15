package com.survivalcoding.day13.exception

fun main() {
    val numStr = "10.5"

    val num: Int = try {
        numStr.toInt()
    } catch (_: NumberFormatException) {
        0
    }

    println(num)
}