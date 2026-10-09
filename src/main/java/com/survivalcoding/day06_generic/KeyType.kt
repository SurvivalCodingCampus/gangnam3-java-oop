package com.survivalcoding.day06_generic

enum class KeyType(val limit: Int) {
    PADLOCK(limit = 1_024),
    BUTTON(limit = 10_000),
    DIAL(limit = 30_000),
    FINGER(limit = 1_000_000)
}