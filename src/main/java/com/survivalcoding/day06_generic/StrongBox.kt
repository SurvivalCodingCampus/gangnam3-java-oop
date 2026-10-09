package com.survivalcoding.day06_generic

class StrongBox<T : KeyType>(private var data: T?) {

    var count: Int = 0
        private set

    fun put(data: T?) {
        count = 0
        this.data = data
    }

    fun get(): T? {
        data?.let {
            count++
            return if (count >= it.limit) data else null
        } ?: return null
    }
}