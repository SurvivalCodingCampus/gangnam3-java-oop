package com.survivalcoding.day17

import kotlinx.coroutines.*

fun main(): Unit = runBlocking {
    val job1: Job = launch(Dispatchers.IO) {
        while (true) {
            delay(1000)
            withContext(Dispatchers.Default) {
                println("꾸우: ${Thread.currentThread().name}")
            }
        }
    }

    val job2: Job = launch(Dispatchers.IO) {
        while (true) {
            delay(2000)
            withContext(Dispatchers.Default) {
                println("까악: ${Thread.currentThread().name}")
            }
        }
    }
    val job3: Job = launch(Dispatchers.IO) {
        while (true) {
            delay(3000)
            withContext(Dispatchers.Default) {
                println("짹짹: ${Thread.currentThread().name}")
            }
        }
    }

    launch {
        delay(10000)
        job1.cancel()
        job2.cancel()
        job3.cancel()
    }
}
