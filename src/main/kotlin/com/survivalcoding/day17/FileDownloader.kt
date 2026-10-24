package com.survivalcoding.day17

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {
    while (true) {
        // 입력 버퍼 버리기
        while (System.`in`.available() > 0) {
            System.`in`.read()
        }

        print("\n다운로드 파일명을 입력: ")
        val input = readln()

        if (input.equals("q", ignoreCase = true)) {
            println("프로그램을 종료합니다.")
            break
        }

        val result = DownloadManager.download(input)

        println(result)
    }
}

object DownloadManager {

    suspend fun download(fileName: String): String = coroutineScope {
        val downloadJob = async { FileDownloader.downloadFile(fileName) }
        launch { Animator.start("$fileName 파일 다운로드 중") }

        val result = downloadJob.await()
        Animator.end()
        println("\n$fileName 파일 다운로드 완료!")

        result
    }
}

object Animator {
    private var animatorJob: Job? = null
    private val dots = arrayOf(".", "..", "...")

    suspend fun start(message: String) = coroutineScope {
        animatorJob?.cancel()
        animatorJob = launch {
            var index = 0

            while (true) {
                print("\r$message${dots[index]}")

                index = (index + 1) % dots.size
                delay(0.5.seconds)
            }
        }
    }

    fun end() {
        animatorJob?.cancel()
        animatorJob = null
    }
}

object FileDownloader {

    suspend fun downloadFile(name: String): String {
        val rndSeconds = 3
        delay(rndSeconds.seconds)

        return "$name JSON"
    }
}