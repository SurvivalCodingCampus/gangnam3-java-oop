package com.survivalcoding.day12.generic

enum class KeyType {
    PADLOCK,
    BUTTON,
    DIAL,
    FINGER,
}

class StrongBox<T: Any>(
    data: T,
    key: KeyType,
) {
    internal val _requiredCount: Int = when (key) {
        KeyType.PADLOCK -> 1_024
        KeyType.BUTTON -> 10_000
        KeyType.DIAL -> 30_000
        KeyType.FINGER -> 1_000_000
    }

    internal var _count: Int = 0
    private val _data: T = data

    fun get(): T? = if (_count++ < _requiredCount) null else _data
}