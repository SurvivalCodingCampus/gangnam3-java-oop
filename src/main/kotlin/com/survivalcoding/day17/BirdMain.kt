package com.survivalcoding.day17

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

fun main(): Unit = runBlocking {
    val gguJob = launch {
        repeatSound(
            sound = "꾸우",
            intervalMs = 1000
        )
    }

    val ggaJob = launch {
        repeatSound(
            sound = "까악",
            intervalMs = 2000
        )
    }

    val jjaekJob = launch {
        repeatSound(
            sound = "짹짹",
            intervalMs = 3000
        )
    }

    launch {
        cancelAfter(
            delayMs = 10000,
            jobs = listOf(gguJob, ggaJob, jjaekJob)
        )
    }
}

suspend fun repeatSound(sound: String, intervalMs: Int) {
    while (true) {
        delay(intervalMs.milliseconds)
        println(sound)
    }
}

suspend fun cancelAfter(delayMs: Int, jobs: List<Job>){
    delay(delayMs.milliseconds)
    jobs.forEach {
        it.cancel()
    }
}