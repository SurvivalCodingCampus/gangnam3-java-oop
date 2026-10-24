package com.survivalcoding.day17

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {
    val gguJob = launch { repeatSound("꾸우", 1) }
    val ggaJob = launch { repeatSound("까악", 2) }
    val jjaekJob = launch { repeatSound("짹짹", 3) }

    launch { cancelAfter(10, listOf(gguJob, ggaJob, jjaekJob)) }
}

suspend fun repeatSound(sound: String, intervalSeconds: Int) {
    while (true) {
        delay(intervalSeconds.seconds)
        println(sound)
    }
}

suspend fun cancelAfter(timeoutSeconds: Int, jobs: List<Job>){
    delay(timeoutSeconds.seconds)
    jobs.forEach {
        it.cancel()
    }
}