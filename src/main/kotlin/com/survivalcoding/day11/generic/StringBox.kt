package com.survivalcoding.day11.generic

enum class KeyType {
    PADLOCK,
    BUTTON,
    DIAL,
    FINGER,
}

class StrongBox<E>(
    data: E,
    key: KeyType,
) {
    internal val _requiredCount: Int = when (key) {
        KeyType.PADLOCK -> 1_024
        KeyType.BUTTON -> 10_000
        KeyType.DIAL -> 30_000
        KeyType.FINGER -> 1_000_000
    }

    internal var _count: Int = 0
    private val _data: E = data

    fun get(): E? {
        if (_count < _requiredCount) {
            _count++
            return null
        }

        return _data
    }
}