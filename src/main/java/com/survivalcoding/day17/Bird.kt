package com.survivalcoding.day17

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    launch {
        repeat(4) {
            delay(1000)
            println("꾸우")
        }
    }

    launch {
        repeat(4) {
            delay(2000)
            println("까악")
        }
    }

    launch {
        repeat(4) {
            delay(3000)
            println("짹짹")
        }
    }
}
