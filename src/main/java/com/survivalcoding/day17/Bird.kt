package com.survivalcoding.day17

import kotlinx.coroutines.*

fun main(): Unit = runBlocking {

    launch(Dispatchers.IO) {
        repeat(4) {
            withContext(Dispatchers.Default) {
                println("꾸우: ${Thread.currentThread().name}")
            }
            delay(1000) // 울고 딜레이가 더 논리적
        }
    }

    launch(Dispatchers.IO) {
        repeat(4) {
            delay(2000)
            withContext(Dispatchers.Default) {
                println("까악: ${Thread.currentThread().name}")
            }
        }
    }

    launch(Dispatchers.IO) {
        repeat(4) {
            delay(3000)
            withContext(Dispatchers.Default) {
                println("짹짹: ${Thread.currentThread().name}")
            }
        }
    }
}
