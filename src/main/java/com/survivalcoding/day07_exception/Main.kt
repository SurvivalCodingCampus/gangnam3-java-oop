package com.survivalcoding.day07_exception

fun main() {
    // try {
    //     someError2()
    // } catch (e: IllegalArgumentException) {
    //     println("IllegalArgumentException: ${e.message}")
    // } catch (e: NumberFormatException) {
    //     println(e.message)
    // } catch (e: Exception) {
    //     e.printStackTrace()
    // } finally {
    //     println("무조건 실행되는 코드")
    // }

    // 연습문제
    val numString = "10.5"

    val num: Int = try {
        numString.toInt()
    } catch (e: NumberFormatException) {
        println(e.message)
        0
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }

    val num2 = numString.toIntOrNull() ?: 0

    val num3 = numString.runCatching { toInt() }.getOrDefault(0)

    val num4 = numString.runCatching { toInt() }
        .onSuccess { it.toString() }
        .onFailure { it.message }

    println(num)
    println(num2)
    println(num3)
    println(num4)
}

// 직접 처리하지 않고 호출자(main)로 처리를 미룸
fun someError2() {
    someError()
}

fun someError() {
    throw Exception("에러가 발생했습니다")
}