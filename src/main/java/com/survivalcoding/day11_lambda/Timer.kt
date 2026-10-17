package com.survivalcoding.day11_lambda

class Timer {

    fun start(callback: OnCompleteCallback) {
        println("타이머 시작")
        Thread.sleep(1000L)
        callback.onComplete()
    }

    fun start(callback: () -> Unit) {
        println("타이머 시작")
        Thread.sleep(1000L)
        callback()
    }
}