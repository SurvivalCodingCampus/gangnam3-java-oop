package com.survivalcoding.day17

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val job1: Job = launch {
        while (true) {
            delay(1000)
            println("꾸우")
        }
    }

    val job2: Job = launch {
        while (true) {
            delay(2000)
            println("까악")
        }
    }

    val job3: Job = launch {
        while (true) {
            delay(3000)
            println("짹짹")
        }
    }

    launch {
        delay(10000)
        job1.cancel()
        job2.cancel()
        job3.cancel()
    }
}