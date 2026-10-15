package com.survivalcoding.day13

fun main() {
    val numString: String = "10.5"

    var num: Int

    try {
        num = numString.toInt()
    } catch (e: Exception) {
        println(e.message)
        num = 0
    }

    println(num)
}