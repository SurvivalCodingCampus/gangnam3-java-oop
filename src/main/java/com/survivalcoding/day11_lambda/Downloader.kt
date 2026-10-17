package com.survivalcoding.day11_lambda

class Downloader {

    fun download(onProgress: (Int) -> Unit) {
        println("다운로드 시작")
        for (progress in 0..100 step 20) {
            Thread.sleep(500L)
            onProgress(progress)
        }
    }
}