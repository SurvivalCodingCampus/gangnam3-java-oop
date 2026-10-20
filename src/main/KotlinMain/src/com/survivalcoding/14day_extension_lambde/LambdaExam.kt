package com.survivalcoding.`14day_extension_lambde`

fun main(args: Array<String>) {
    val total1: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
    val total2: (Int, Int) -> Int = { x: Int, y: Int -> x + y }

    println(total2(10, 20)  )
    println(total1(10, 20))

    val square: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
    println(square(10, 20))
    repeat(10, {x -> println(x * x)})

}
fun sum(a: Int, b: Int): Int {
    return a + b

}