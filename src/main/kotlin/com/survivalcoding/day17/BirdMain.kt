package com.survivalcoding.day17

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {

    ex2()
}

suspend fun ex1() = coroutineScope {

    launch { repeatSound("꾸우", intervalSeconds = 1, repeatTimes = 4) }
    launch { repeatSound("까악", intervalSeconds = 2, repeatTimes = 4) }
    launch { repeatSound("짹짹", intervalSeconds = 3, repeatTimes = 4) }
}

suspend fun ex2() = coroutineScope {

    val gguJob = launch { repeatSound("꾸우", intervalSeconds = 1) }
    val ggaJob = launch { repeatSound("까악", intervalSeconds = 2) }
    val jjaekJob = launch { repeatSound("짹짹", intervalSeconds = 3) }

    cancelAfter(10, listOf(gguJob, ggaJob, jjaekJob))
}

suspend fun repeatSound(sound: String, intervalSeconds: Int, repeatTimes: Int) {

    repeat (repeatTimes) {
        delay(intervalSeconds.seconds)
        println(sound)
    }
}

suspend fun repeatSound(sound: String, intervalSeconds: Int) {

    while (true) {
        delay(intervalSeconds.seconds)
        println(sound)
    }
}

suspend fun cancelAfter(timeoutSeconds: Int, jobs: List<Job>) {

    delay(timeoutSeconds.seconds)

    jobs.forEach {
        it.cancel()
    }
}