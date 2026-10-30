package com.survivalcoding.day14_asynchronous

import kotlinx.coroutines.*
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    // println("시작")

    // launch {
    //     Weathers.tomorrow {
    //         println("내일 날씨는: $it")
    //     }
    // }

    // println("끝")

    // val time = measureTimeMillis {
    //     val one = function1()
    //     val two = function2()

    //     println("The answer is ${one + two}")
    // }

    // println("Completed in $time ms")

    // val content = "Hello, World!"
    // saveFile(content)

    // println("File saved")

    // launch(Dispatchers.IO) {
    //     delay(3000L.milliseconds)
    //     println("코루틴 1: ${Thread.currentThread().name}")
    //     withContext(Dispatchers.Default) {
    //         println("코루틴 2: ${Thread.currentThread().name}")
    //     }
    // }

    val time1 = measureTimeMillis {
        val result1 = task1()
        val result2 = task2()
        val result3 = task3()

        val totalResult = result1 + result2 + result3

        println("Total result: $totalResult")
    }

    println("Execution time: $time1 ms")

    val time2 = measureTimeMillis {
        val result1 = async { task1() }
        val result2 = async { task2() }
        val result3 = async { task3() }

        val totalResult = result1.await() + result2.await() + result3.await()

        println("Total result: $totalResult")
    }

    println("Execution time: $time2 ms")
}

suspend fun Weathers.tomorrow(callback: (String) -> Unit) {
    delay(2000L.milliseconds)
    callback("맑음")
}

suspend fun Weathers.tomorrow(): String {
    delay(2000L.milliseconds)
    return "맑음"
}

suspend fun function1(): Int {
    delay(1000L.milliseconds)
    return 10
}

suspend fun function2(): Int {
    delay(1000L.milliseconds)
    return 20
}

suspend fun saveFile(content: String) {
    withContext(Dispatchers.IO) {
        // 파일 저장 로직
        File("output.txt").writeText(content)
    }
}

suspend fun task1(): Int {
    delay(1000L.milliseconds)
    return 1
}

suspend fun task2(): Int {
    delay(1500L.milliseconds)
    return 2
}

suspend fun task3(): Int {
    delay(2000L.milliseconds)
    return 3
}