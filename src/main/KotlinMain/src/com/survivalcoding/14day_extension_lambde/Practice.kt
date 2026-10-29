package com.survivalcoding.`14day_extension_lambde`

import kotlinx.coroutines.*
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

// 마법사들의 모임(메인 함수) 시작! runBlocking은 모든 마법이 끝날 때까지 프로그램을 끄지 않고 기다려 줍니다.
fun main() = runBlocking {

    // [주석 설명] 주석 처리된 옛날 방식들 (콜백이나 순차 실행 예시 등은 생략하고 아래 실행 부에 집중해 봐요!)

    // ==========================================
    // 1부: 순차 실행(차례대로 하나씩 하기) vs 병렬 실행(한꺼번에 같이 하기) 비교!
    // ==========================================

    // [방법 A] 순차 실행: task1(1초), task2(1.5초), task3(2초)을 차례대로 하나씩 함
    val time1 = measureTimeMillis {
        val result1 = task1() // 1초 기다림
        val result2 = task2() // 1.5초 기다림
        val result3 = task3() // 2초 기다림

        val totalResult = result1 + result2 + result3

        println("Total result: $totalResult")
    }
    // 걸린 시간 출력: 1초 + 1.5초 + 2초 = 총 4.5초(4500밀리초) 이상이 걸려요!
    println("Execution time: $time1 ms")


    // [방법 B] 병렬 실행 (async 사용): 세 가지 작업을 동시에 출발선에 놓고 뜀!
    val time2 = measureTimeMillis {
        // async라는 마법 주머니를 써서 세 일을 동시에 시작합니다.
        val result1 = async { task1() } // 동시에 출발!
        val result2 = async { task2() } // 동시에 출발!
        val result3 = async { task3() } // 동시에 출발!

        // await()는 "각각의 일이 끝날 때까지 기다려서 결과를 가져다오!" 라는 뜻이에요.
        // 가장 오래 걸리는 작업이 2초이므로, 전체는 딱 2초만 걸립니다!
        val totalResult = result1.await() + result2.await() + result3.await()

        println("Total result: $totalResult")
    }
    // 걸린 시간 출력: 가장 오래 걸린 2초만에 싹 끝나요! (시간이 훅 줄어듦)
    println("Execution time: $time2 ms")
}

// ==========================================
// 2부: 날씨를 알려주는 마법 함수들 (suspend = 잠시 멈춤 가능)
// ==========================================

// 옛날 방식: 콜백(연락망)을 이용해 날씨를 알려주는 방식
suspend fun Weathers.tomorrow(callback: (String) -> Unit) {
    delay(2000L.milliseconds) // 2초 동안 잠시 숨을 고르고 (다른 일에 양보 가능)
    callback("맑음")          // "맑음"이라고 연락을 줍니다.
}

// 현대적인 방식: 2초 동안 기다렸다가 "맑음"이라는 글자를 척척 반환해 줌
suspend fun Weathers.tomorrow(): String {
    delay(2000L.milliseconds) // 2초 대기
    return "맑음"
}

// ==========================================
// 3부: 숫자를 뱉어내는 간단한 마법 함수들
// ==========================================

suspend fun function1(): Int {
    delay(1000L.milliseconds) // 1초 기다림
    return 10                 // 10 반환
}

suspend fun function2(): Int {
    delay(1000L.milliseconds) // 1초 기다림
    return 20                 // 20 반환
}

// ==========================================
// 4부: 파일 저장 마법사 (Dispatcher.IO 사용)
// ==========================================
suspend fun saveFile(content: String) {
    // Dispatchers.IO는 "서로 파일을 읽고 쓰거나 인터넷 통신을 할 때 쓰는 전문 일꾼"을 불러오는 마법이에요!
    withContext(Dispatchers.IO) {
        // 컴퓨터 창고(하드디스크)의 "output.txt"라는 파일에 글자를 꾹꾹 적어 저장합니다.
        File("output.txt").writeText(content)
    }
}

// ==========================================
// 5부: 각각 다른 시간이 걸리는 세 가지 작업 (task1, 2, 3)
// ==========================================

suspend fun task1(): Int {
    delay(1000L.milliseconds) // 1초(1000ms) 동안 쉼
    return 1
}

suspend fun task2(): Int {
    delay(1500L.milliseconds) // 1.5초(1500ms) 동안 쉼
    return 2
}

suspend fun task3(): Int {
    delay(2000L.milliseconds) // 2초(2000ms) 동안 쉼
    return 3
}

// 날씨 서비스를 흉내 내기 위한 빈 객체 그릇
object Weathers