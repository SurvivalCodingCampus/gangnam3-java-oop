package com.survivalcoding.day14_asynchronous

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {
    println("========== 연습 문제 1 시작 ==========")

    val job1 = launch {
        repeat(4) {
            println(kuu())
        }
    }

    val job2 = launch {
        repeat(4) {
            println(caw())
        }
    }

    val job3 = launch {
        repeat(4) {
            println(tweet())
        }
    }

    job1.join()
    job2.join()
    job3.join()

    println("========== 연습 문제 1 끝 ==========")

    println("========== 연습 문제 2 시작 ==========")

    val job4 = launch {
        while (true) {
            println(kuu())
        }
    }

    val job5 = launch {
        while (true) {
            println(caw())
        }
    }

    val job6 = launch {
        while (true) {
            println(tweet())
        }
    }

    delay(10.seconds)

    job4.cancelAndJoin()
    job5.cancelAndJoin()
    job6.cancelAndJoin()

    println("========== 연습 문제 2 끝 ==========")
}

suspend fun kuu(): String {
    delay(1.seconds)
    return "꾸우"
}

suspend fun caw(): String {
    delay(2.seconds)
    return "까악"
}

suspend fun tweet(): String {
    delay(3.seconds)
    return "짹짹"
}