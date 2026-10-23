package com.survivalcoding.day17

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

fun main(): Unit = runBlocking {
    val gguJob = launch {
        cry(birdSound = "꾸우", delayMillis = 1000)
    }

    val ggaJob = launch {
        cry(birdSound = "까악", delayMillis = 2000)
    }

    val jjaekJob = launch {
        cry(birdSound = "짹짹", delayMillis = 3000)
    }

    cancelAll(
        delayMillis = 10000,
        jobs = listOf(gguJob, ggaJob, jjaekJob)
    )
}

suspend fun cry(birdSound: String, delayMillis: Int) {
    while (true) {
        delay(delayMillis.milliseconds)
        println(birdSound)
    }
}

suspend fun cancelAll(delayMillis: Int, jobs: List<Job>){
    delay(delayMillis.milliseconds)
    jobs.forEach {
        it.cancel()
    }
}