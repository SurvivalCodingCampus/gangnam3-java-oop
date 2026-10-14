package com.survivalcoding.generic_enum_string

enum class KeyType {
    PADLOCK,
    BUTTON,
    DIAL,
    FINGER,
}

class StrongBox<E>(val keyType: KeyType) {
    private var _item: E? = null
    private var attemptCount: Int = 0

    fun put(item: E) {
        _item = item
    }

    fun get(): E? {
        attemptCount++

        val requiredCount = when (keyType) {
            KeyType.PADLOCK -> 1_024
            KeyType.BUTTON -> 10_000
            KeyType.DIAL -> 30_000
            KeyType.FINGER -> 1_000_000
        }

        return if (attemptCount < requiredCount) {
            null
        } else {
            _item
        }
    }

}