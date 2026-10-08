package com.survivalcoding.generic_enum

class Pocket<E> {
    private var _data: E? = null

    fun put(data: E) {
        _data = data
    }

    fun get(): E? = _data
}

fun main() {
    val num: Int? = null

    val pocket = Pocket<Int>()
    pocket.put(100)

    val data = pocket.get()
    println(data)
}