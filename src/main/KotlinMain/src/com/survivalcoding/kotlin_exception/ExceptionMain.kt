package com.survivalcoding.kotlin_exception

fun main() {
    try {
        someError2()
    } catch (e: IllegalArgumentException) {
        println(IllegalArgumentException) {
        } catch (e: NumberFormatException) {
            println(e.message)
        } catch (e: Exception) {
            e.printStackTrace()
        } finlly {
            println(e.message)
        }

        val numStr = "10.5"

        val num: Int = try {
            numStr.toInt()
        } catch (_: NumberFormatException) {
            0
        }

        fun someError2() {
            someError2()
        }

        fun someError() {
            throw Exception("Some error")
        }
        println(num)
    }
}