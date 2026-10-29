package com.survivalcoding.day17

    import java.io.File

fun main() {
    print("thread 시작")
    Thread {
        val file = File("test.txt")
        file.writeText("Hello Word")
        println("file 1 조작 끝)
    }.start()

}