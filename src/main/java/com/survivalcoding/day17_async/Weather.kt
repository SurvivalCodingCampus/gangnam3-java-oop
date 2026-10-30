package com.survivalcoding.day17_async

import kotlinx.coroutines.*
import java.io.File

// 메인 스레드
fun main(): Unit = runBlocking {

    println("main start")

    launch {
        val data = Weathers.tomorrow()
        println(data)
    }

    launch {
        delay(1000)
        println("world")
    }

    delay(1000)

    println("main end")

    launch {
        println(Thread.currentThread())
        saveFile("dddd")
    }
}

suspend fun saveFile(content: String) { // 이미 코루틴 스코프에서 실행이 된 거임
    withContext(Dispatchers.IO) {
        println(Thread.currentThread())
        File("output.txt").writeText(content)
    }

    println(Thread.currentThread())
}

// 오래 걸리는 함수 suspend
suspend fun Weathers.tomorrow(): String {
//    Thread.sleep(2000) // 2초간 처리        스레드 블록 (다 멈춰) 절대로 쓰면 안되는거야!!!!!!!!
    delay(2000)                         // 스레드 블록을 안 함
    return "맑음"
}


/** 날씨 서비스 */
object Weathers
