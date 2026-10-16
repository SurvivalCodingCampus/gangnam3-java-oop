package com.survivalcoding.day14_extension_lambda

import com.survivalcoding.day01_class_instance.Attackable

fun main() {
    val list = listOf(1, 2, 3)
    list.forEach { println(it) }

    list.forEach(::println) // 메서드(함수) 주소 전달
    // action: (Int) -> Unit
    //         ::println   (Int) -> Unit

    list.forEach {
        println(it * it)
    }

    list.forEach {
        println(10)
    }

    list.sortedWith { a, b -> a - b }

    val total1: (Int, Int) -> Int = { a, b -> a + b }

    val total2 = { a: Int, b: Int -> a - b }

    println(total2(10, 20))

    val square: (Int) -> Int = { it * it }

    //action: (Int) -> Unit
    repeat(10, { it -> println(it)})

    repeat(times = 10, action = { println(it)})

    // 후행람다 사용 룰을 따름
    repeat(10) {
        println(it)
    }

    heal {
        println("attack")
    }

    runCatching {

    }
}

fun func1(a: Int) = a * 10

fun sum(a: Int, b: Int) = a + b

fun heal(attackable: Attackable) {
    // 뭔가 한다
}

//Java에서 SAM : Single Abstract Method

interface OnClickListener {
    fun onClick()
}

interface OnTouchListener {
    fun onTouch()
}