package com.survivalcoding.day17

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
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
        val progress = AtomicInteger(0)

        val animatorJob = launch {
            Animator.start(fileName) { progress.get() }
        }

        val result = withContext(Dispatchers.IO) {
            FileDownloader.downloadFile(fileName) {
                progress.set(it)
            }
        }

        animatorJob.cancel()
        Animator.complete(fileName)

        result
    }
}

object FileDownloader {

    suspend fun downloadFile(
        name: String,
        onProgressChanged: (Int) -> Unit
    ): String {
        for (progress in 1..100) {
            delay(15.milliseconds)
            onProgressChanged(progress)
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