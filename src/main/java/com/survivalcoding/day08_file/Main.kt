package com.survivalcoding.day08_file

import java.io.File
import java.io.FileNotFoundException

fun main() {
    // 파일 지정
    val source = File("save.txt")

    // 내용 쓰기
    source.writeText("Hello, world!")

    // 내용 읽기
    val text = source.readText()
    println(text)

    // 연습문제
    val destination = File("destination.txt")
    val fileOperations = DefaultFileOperations()

    try {
        fileOperations.copyFile(File("unknown.txt"), destination)
    } catch (e: FileNotFoundException) {
        println(e.message)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}