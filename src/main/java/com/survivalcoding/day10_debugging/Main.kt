package com.survivalcoding.day10_debugging

import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    // val heroes = mutableListOf<Hero>()

    // val hero1 = Hero("슈퍼맨", 100)
    // val hero2 = Hero("슈퍼맨", 100)

    // heroes.add(hero1)
    // println(heroes.size)

    // heroes.remove(hero2)
    // println(heroes.size)

    // val str1 = "hello"
    // val str2 = "hello"
    // println(str1 === str2)

    // println("===============")

    // val str3 = String("hello".toCharArray())
    // println(str1 === str3)

    // println("===============")

    // 연습문제 1
    val chartFile =
        File("src/main/java/com/survivalcoding/day10_debugging/chart_data.json")
    val chartObject = Json.decodeFromString<Chart>(chartFile.readText())

    chartObject.collectionChartDataList.forEach(::println)
}
