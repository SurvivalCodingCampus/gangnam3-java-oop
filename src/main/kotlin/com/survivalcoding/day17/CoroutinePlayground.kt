package com.survivalcoding.day17

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

/**
 * 🚀 코틀린 코루틴(Kotlin Coroutines) 핵심 학습 Playground
 *
 * 📌 [동시성(Concurrency) vs 병렬성(Parallelism) 개념 정리]
 * - [동시성 (Concurrency)]:
 *   여러 작업이 시분할(Interleaving)이나 중단/재개(Suspension/Resumption)를 통해
 *   논리적으로 '동시에 진행되는 것처럼' 처리되는 개념입니다.
 *   (단일 스레드/코어에서도 delay 등을 통해 스레드를 양보하며 동시성 달성 가능)
 *
 * - [병렬성 (Parallelism)]:
 *   여러 작업이 멀티 코어 CPU 환경의 여러 스레드에서 물리적으로 '정확히 같은 순간에'
 *   동시에 실행되는 개념입니다.
 *   (Dispatchers.Default, Dispatchers.IO 등의 스레드 풀을 통해 물리적 병렬 실행)
 *
 * --------------------------------------------------------------------
 *
 * 목차:
 * 1. 코루틴 기초 (동시성 - Concurrency)
 * 2. async & await 비동기 연산 (동시성 & 병렬성 비교)
 * 3. Job 취소 및 타임아웃 (동시성 제어 - Concurrency Control)
 * 4. Dispatchers 스레드 풀 전환 (병렬성 - Parallelism)
 * 5. 구조적 동시성 (구조적 동시성 - Structured Concurrency)
 * 6. 중단 함수와 고차 함수 연동 (동시성 - Concurrency)
 */

fun main(): Unit = runBlocking {
    log("🏁 CoroutinePlayground 시작")

    println("1. 코루틴 기초 (동시성 - Concurrency)")
    example1Basics()

    println("2. async & await 비동기 연산 (동시성 & 병렬성 비교)")
    example2AsyncAwait()

    println("3. Job 취소 및 타임아웃 (동시성 제어 - Concurrency Control)")
    example3JobAndCancellation()

    println("4. Dispatchers 스레드 풀 전환 (병렬성 - Parallelism)")
    example4Dispatchers()

    println("5. 구조적 동시성 (구조적 동시성 - Structured Concurrency)")
    example5StructuredConcurrency()

    println("6. 중단 함수와 고차 함수 연동 (동시성 - Concurrency)")
    example6SuspendAndHigherOrderFunction()

    log("🎉 CoroutinePlayground 모든 예제 종료")
}

// 💡 로그 출력 헬퍼 함수 (현재 스레드명을 함께 출력하여 동시성/병렬성 확인)
fun log(message: String) {
    println("[${Thread.currentThread().name}] $message")
}

// 1. 코루틴 기초: launch와 delay (동시성 - Concurrency)
//    - 설명: 동일한 단일 스레드(main) 상에서 여러 코루틴이 delay 중단 지점에서
//           스레드를 양보(interleaving)하며 번갈아 실행되는 전형적인 동시성 모델
suspend fun example1Basics() = coroutineScope {
    log("(동시성) 단일 스레드 상에서 자식 코루틴 2개 생성")

    // launch: 결과를 반환하지 않는 코루틴 빌더 (Job 반환)
    val job1 = launch {
        log("(동시성) 작업 1 시작 -> 500ms 일시 중단")
        // delay: 스레드를 블로킹(Thread.sleep)하지 않고 제어권을 양보함
        delay(500.milliseconds)
        log("(동시성) 작업 1 재개 및 완료")
    }

    val job2 = launch {
        log("(동시성) 작업 2 시작 -> 200ms 일시 중단")
        delay(200.milliseconds)
        log("(동시성) 작업 2 재개 및 완료 (작업 1보다 먼저 끝남)")
    }

    log("(동시성) 부모는 블로킹 없이 자식 코루틴들을 트리거함")
    job1.join()
    job2.join()
}

// 2. 비동기 연산과 결과 수신: async와 await (동시성 & 병렬성 비교)
//    - 2-A: I/O 대기 시간을 겹쳐서 단축하는 비동기 (동시성 - Concurrency)
//    - 2-B: 멀티코어 워커 스레드에서 실제로 동시에 계산하는 (병렬성 - Parallelism)
suspend fun example2AsyncAwait() = coroutineScope {
    log("--- 2-A. 네트워크/IO 대기 시간 중첩 (동시성 - Concurrency) ---")
    val ioElapsed = measureTimeMillis {
        val userDeferred = async { fetchUserData() }
        val orderDeferred = async { fetchOrderData() }

        log("(동시성) 두 I/O 작업을 비동기 요청 후 대기")
        val user = userDeferred.await()
        val order = orderDeferred.await()
        log("(동시성) 결과 수신 완료: $user, $order")
    }
    log("(동시성) 각 500ms 걸리는 두 작업이 중첩 실행되어 총 소요 시간: ${ioElapsed}ms")

    log("--- 2-B. 멀티 스레드 풀을 통한 CPU 연산 (병렬성 - Parallelism) ---")
    val cpuElapsed = measureTimeMillis {
        // Dispatchers.Default를 지정하여 서로 다른 CPU 코어/스레드에서 물리적으로 동시 실행
        val calc1 = async(Dispatchers.Default) {
            log("(병렬성) CPU 작업 1 실행 중")
            heavyCalculation(1)
        }
        val calc2 = async(Dispatchers.Default) {
            log("(병렬성) CPU 작업 2 실행 중")
            heavyCalculation(2)
        }

        val total = calc1.await() + calc2.await()
        log("(병렬성) 멀티코어 병렬 연산 결과 합계: $total")
    }
    log("(병렬성) 두 CPU 연산이 서로 다른 스레드에서 물리적으로 병렬 실행됨 (${cpuElapsed}ms)")
}

suspend fun fetchUserData(): String {
    delay(500.milliseconds) // 0.5초 네트워크 대기 시뮬레이션
    return "User(홍길동)"
}

suspend fun fetchOrderData(): String {
    delay(500.milliseconds) // 0.5초 네트워크 대기 시뮬레이션
    return "Order(맥북)"
}

fun heavyCalculation(id: Int): Long {
    var sum = 0L
    for (i in 1..10_00) {
        sum += (i % 7)
    }
    return sum
}

// 3. 코루틴 제어와 취소: cancel, withTimeoutOrNull (동시성 제어 - Concurrency Control)
//    - 설명: 진행 중인 비동기 동시성 작업의 생명주기 관리 및 협조적 취소 처리
suspend fun example3JobAndCancellation() = coroutineScope {
    log("--- 3-1. Job 취소 (동시성 제어 - Cancellation) ---")
    val countingJob: Job = launch {
        var count = 1
        while (true) {
            log("(동시성 제어) 카운팅 중: $count")
            delay(200.milliseconds) // 중단 지점(suspension point)에서 취소 여부를 자동 감지
            count++
        }
    }

    delay(500.milliseconds)
    log("(동시성 제어) countingJob 취소 요청!")
    countingJob.cancel()
    countingJob.join()
    log("(동시성 제어) countingJob 안전하게 종료됨")

    log("--- 3-2. 타임아웃 (동시성 제어 - Timeout) ---")
    val timeoutResult = withTimeoutOrNull(300.milliseconds) {
        log("(동시성 제어) 1000ms 걸리는 작업 시작")
        delay(1000.milliseconds)
        "작업 성공"
    }
    log("(동시성 제어) 타임아웃(300ms) 경과로 null 반환: $timeoutResult")
}

// 4. Dispatchers와 withContext 스레드 풀 전환 (병렬성 - Parallelism)
//    - 설명: JVM 백그라운드 스레드 풀(Default, IO)의 여러 스레드에 작업을
//           물리적으로 분산하여 동시에 병렬 실행
suspend fun example4Dispatchers() = coroutineScope {
    log("(병렬성) 메인 스레드에서 시작")

    // Dispatchers.Default: CPU 코어 수에 비례하는 워커 스레드 풀에서 병렬 실행
    val cpuResult = withContext(Dispatchers.Default) {
        log("(병렬성) CPU 연산 스레드로 전환되어 실행")
        (1..1_000).filter { it % 2 == 0 }.sumOf { it.toLong() }
    }
    log("(병렬성) CPU 연산 결과 수신: $cpuResult")

    // Dispatchers.IO: 블로킹 I/O 전용 스레드 풀(최대 64개 또는 코어 수)에서 실행
    val ioResult = withContext(Dispatchers.IO) {
        log("(병렬성/IO) I/O 작업자 스레드로 전환되어 실행")
        delay(200.milliseconds)
        "DB 데이터 조회 완료"
    }
    log("(병렬성/IO) I/O 결과 수신: $ioResult")

    log("(병렬성) withContext 종료 후 원래 컨텍스트 스레드로 복귀")
}

// 5. 구조적 동시성: coroutineScope (구조적 동시성 - Structured Concurrency)
//    - 설명: 부모 코루틴은 모든 자식 코루틴이 완료될 때까지 종료되지 않으며,
//           자식의 예외나 취소가 부모/형제에게 전파되어 비동기 작업 누수를 원천 차단
suspend fun example5StructuredConcurrency() = coroutineScope {
    log("(구조적 동시성) 부모 스코프 시작")

    coroutineScope {
        launch {
            delay(300.milliseconds)
            log("(구조적 동시성) 자식 작업 A 완료")
        }

        launch {
            delay(150.milliseconds)
            log("(구조적 동시성) 자식 작업 B 완료")
        }
    }

    log("(구조적 동시성) 자식 A, B가 모두 끝나야만 이 줄이 실행됨 (비동기 누수 방지)")
}

// 6. 중단 함수와 고차 함수 연동 (동시성 - Concurrency)
//    - 설명: 비동기 작업 완료 시점에 람다 콜백을 전달하여 결과를 처리하는 패턴
suspend fun example6SuspendAndHigherOrderFunction() {
    log("(동시성) 중단 함수 호출 및 람다 전달")

    val result = func(delayMillis = 300L) { a, b ->
        val sum = a + b
        log("(동시성) 람다 콜백 실행: $a + $b = $sum")
        sum
    }

    log("(동시성) 최종 반환값: $result")
}

/**
 * 중단 함수 내에서 비동기 지연 후 고차 함수(콜백)를 실행하는 예제
 */
suspend fun func(delayMillis: Long, callback: (Int, Int) -> Int): Int {
    delay(delayMillis.milliseconds)
    return callback(10, 20)
}
