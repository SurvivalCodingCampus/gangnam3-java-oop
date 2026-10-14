package com.survivalcoding.kotlin_basic

fun main() {
    println("Hello World")

    val name: String? = "null"
    println(name?.length)

    val i : Int = 10
    val d : Double = i.toDouble()

    val hero = Hero("슈퍼맨")
}

class Hero(val name: String)