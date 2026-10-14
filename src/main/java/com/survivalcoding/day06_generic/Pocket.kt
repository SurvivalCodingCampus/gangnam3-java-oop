package com.survivalcoding.day06_generic

class Pocket<E> {

    private var _data: E? = null

    fun put(data: E) {
        _data = data
    }

    fun get(): E? = _data
}