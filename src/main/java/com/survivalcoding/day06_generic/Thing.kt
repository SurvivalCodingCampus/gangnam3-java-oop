package com.survivalcoding.day06_generic

interface Thing {

    var weight: Double

    companion object {
        const val MIN_WEIGHT = 0.0
        const val MAX_WEIGHT = 180.0
    }
}
