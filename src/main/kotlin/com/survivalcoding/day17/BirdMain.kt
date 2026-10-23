package com.survivalcoding.day17

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

fun main(): Unit = runBlocking {
    launch {
        cry(birdSound = "꾸우", milliseconds = 1000, repeatTimes = 4)
    }

    launch {
        cry(birdSound = "까악", milliseconds = 2000, repeatTimes = 4)
    }

    launch {
        cry(birdSound = "짹짹", milliseconds = 3000, repeatTimes = 4)
    }
}

suspend fun cry(birdSound: String, milliseconds: Int, repeatTimes: Int) {
    repeat (repeatTimes) {
        delay(milliseconds.milliseconds)
        println(birdSound)
    }
}