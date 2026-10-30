package com.survivalcoding.`14day_extension_lambde`

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

// "========== 연습 문제 1  =========="
fun main() = runBlocking {
    // runBlocking은 코루틴이 모두 끝날 때까지 메인 프로그램을 잠시 멈춰주는 역할을 합니다.

    // 첫 번째 새: 1초마다 "꾸우" 소리를 4번 냅니다.
    val job1 = launch {
        repeat(4) { it -> // 4번 반복하는 반복문
            delay(1000L)  // 1초(1000밀리초) 동안 일시 정지 (스레드를 차단하지 않고 양보함)
            println("첫 번째 새: 꾸우 (${it + 1}번째)")
        }
    }

    // 두 번째 새: 2초마다 "까악" 소리를 4번 냅니다.
    val job2 = launch {
        repeat(4) { it ->
            delay(2000L)  // 2초 동안 일시 정지
            println("두 번째 새: 까악 (${it + 1}번째)")
        }
    }

    // 세 번째 새: 3초마다 "짹짹" 소리를 4번 냅니다.
    val job3 = launch {
        repeat(4) { it ->
            delay(3000L)  // 3초 동안 일시 정지
            println("마지막 새: 짹짹 (${it + 1}번째)")
        }
    }

    // 세 마리의 새(코루틴)가 모두 일을 마칠 때까지 기다려 줍니다.
    job1.join()
    job2.join()
    job3.join()

    println("모든 새들이 노래를 마쳤습니다!")
}


// "========== 연습 문제 2  =========="
fun main() = runBlocking {
    // 첫 번째 새: 1초마다 무한히 "꾸우" 소리를 냅니다.
    val job1 = launch {
        var count = 1
        while (true) { // 무한 반복
            delay(1000L)
            println("첫 번째 새: 꾸우 ($count)")
            count++
        }
    }

    // 두 번째 새: 2초마다 무한히 "까악" 소리를 냅니다.
    val job2 = launch {
        var count = 1
        while (true) {
            delay(2000L)
            println("두 번째 새: 까악 ($count)")
            count++
        }
    }

    // 세 번째 새: 3초마다 무한히 "짹짹" 소리를 냅니다.
    val job3 = launch {
        var count = 1
        while (true) {
            delay(3000L)
            println("마지막 새: 짹짹 ($count)")
            count++
        }
    }

    // 메인 프로그램에서 10초(10000밀리초) 동안 기다립니다.
    delay(10000L)

    // 10초가 지나면 실행 중인 모든 새(코루틴)들에게 취소 명령을 내립니다![cite: 16]
    job1.cancel()
    job2.cancel()
    job3.cancel()

    println("10초가 지나서 모든 새들이 잠에 들었습니다!")
}