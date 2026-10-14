package com.survivalcoding.day13

import java.io.File

fun main() {
    val file = File("save.txt")

    file.writeText("Hello World\n")
    file.appendText("Hello World\n")
    file.appendText("Hello World\n")

    println(file.readText())
}