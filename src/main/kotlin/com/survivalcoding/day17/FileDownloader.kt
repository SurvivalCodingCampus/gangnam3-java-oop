package com.survivalcoding.day17

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    while (true) {
        print("\n다운로드 파일명을 입력 (종료: q): ")
        val input = readln()

        if (input.equals("q", ignoreCase = true)) {
            println("프로그램을 종료합니다.")
            break
        }

        val result = DownloadManager.download(input)
        println("\n$result")
    }
}

object DownloadManager {

    suspend fun download(fileName: String): String = coroutineScope {
        var progress = 0

        val animatorJob = launch {
            Animator.start(fileName) { progress }
        }

        val downloadJob = async(Dispatchers.IO) {
            FileDownloader.downloadFile(fileName) {
                progress = it
            }
        }

        val result = downloadJob.await()

        animatorJob.cancelAndJoin()
        Animator.complete(fileName)

        result
    }
}

object FileDownloader {

    suspend fun downloadFile(
        name: String,
        onProgress: (Int) -> Unit
    ): String {
        for (progress in 1..100) {
            delay(15.milliseconds)
            onProgress(progress)
        }

        return "$name JSON"
    }
}

object Animator {
    suspend fun start(
        fileName: String,
        getProgress: () -> Int
    ) {
        while (true) {
            val progress = getProgress()

            print("\r$fileName 다운로드 중 $progress%")

            delay(60.milliseconds)
        }
    }

    fun complete(fileName: String) {
        println("\r$fileName 다운로드 완료! 100%")
    }
}