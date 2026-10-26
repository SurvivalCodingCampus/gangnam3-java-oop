package com.survivalcoding.day17_async

import kotlinx.coroutines.delay
import java.io.File

fun main() {
    println(Thread.currentThread())

    println("thread 시작")    // 1

    Thread {
        println(Thread.currentThread())
        val file = File("test1.txt")
        file.writeText("Hello World")
        println("File 1 조작 끝")    // 3
    }.start()

    Thread {
        println(Thread.currentThread())
        val file = File("test2.txt")
        file.writeText("헬로")
        println("File 2 조작 끝")    // 3
    }.start()

    println("thread 끝")     // 2
}