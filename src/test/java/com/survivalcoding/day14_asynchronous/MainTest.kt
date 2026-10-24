package com.survivalcoding.day14_asynchronous

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class MainTest {

    // 반환값 테스트
    @Test
    fun `kuu는 꾸우를 반환한다`() = runBlocking {
        assertEquals("꾸우", kuu())
    }

    @Test
    fun `caw는 까악을 반환한다`() = runBlocking {
        assertEquals("까악", caw())
    }

    @Test
    fun `tweet은 짹짹을 반환한다`() = runBlocking {
        assertEquals("짹짹", tweet())
    }

    // 지연 시간 테스트
    @Test
    fun `kuu는 약 1초가 걸린다`() = runBlocking {
        val time = measureTimeMillis { kuu() }

        assertTrue(time in 1000..1500)
    }

    @Test
    fun `caw는 약 2초가 걸린다`() = runBlocking {
        val time = measureTimeMillis { caw() }

        assertTrue(time in 2000..2500)
    }

    @Test
    fun `tweet은 약 3초가 걸린다`() = runBlocking {
        val time = measureTimeMillis { tweet() }

        assertTrue(time in 3000..3500)
    }

    // 동시 실행 테스트
    @Test
    fun `launch로 실행하면 동시에 실행된다`() = runBlocking {
        val sounds = mutableListOf<String>()

        val time = measureTimeMillis {
            val job1 = launch { sounds.add(kuu()) }
            val job2 = launch { sounds.add(caw()) }
            val job3 = launch { sounds.add(tweet()) }

            job1.join()
            job2.join()
            job3.join()
        }

        assertTrue(time in 3000..3500)
        assertEquals(listOf("꾸우", "까악", "짹짹"), sounds)
    }

    // 실행 취소 테스트
    @Test
    fun `cancelAndJoin으로 무한 반복을 멈춘다`() = runBlocking {
        var count = 0

        val job = launch {
            while (true) {
                kuu()
                count++
            }
        }

        delay(3500.milliseconds)
        job.cancelAndJoin()

        val countAfterCancel = count
        delay(2.seconds)

        assertTrue(job.isCancelled)
        assertFalse(job.isActive)
        assertEquals(3, countAfterCancel)
        assertEquals(countAfterCancel, count)
    }
}
