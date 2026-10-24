package com.survivalcoding.day14_asynchronous

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class MainTest {

    // 반환값 테스트
    @Test
    fun `kuu는 꾸우를 반환한다`() = runTest {
        assertEquals("꾸우", kuu())
    }

    @Test
    fun `caw는 까악을 반환한다`() = runTest {
        assertEquals("까악", caw())
    }

    @Test
    fun `tweet은 짹짹을 반환한다`() = runTest {
        assertEquals("짹짹", tweet())
    }

    // 지연 시간 테스트
    @Test
    fun `kuu는 1초가 걸린다`() = runTest {
        kuu()

        assertEquals(1000, currentTime)
    }

    @Test
    fun `caw는 2초가 걸린다`() = runTest {
        caw()

        assertEquals(2000, currentTime)
    }

    @Test
    fun `tweet은 3초가 걸린다`() = runTest {
        tweet()

        assertEquals(3000, currentTime)
    }

    // 동시 실행 테스트
    @Test
    fun `launch로 실행하면 동시에 실행된다`() = runTest {
        val sounds = mutableListOf<String>()

        val job1 = launch { sounds.add(kuu()) }
        val job2 = launch { sounds.add(caw()) }
        val job3 = launch { sounds.add(tweet()) }

        job1.join()
        job2.join()
        job3.join()

        assertEquals(3000, currentTime)
        assertEquals(listOf("꾸우", "까악", "짹짹"), sounds)
    }

    // 실행 취소 테스트
    @Test
    fun `cancelAndJoin으로 무한 반복을 멈춘다`() = runTest {
        var count = 0

        val job = launch {
            while (true) {
                kuu()
                count++
            }
        }

        advanceTimeBy(3500.milliseconds)
        job.cancelAndJoin()

        val countAfterCancel = count
        advanceTimeBy(2.seconds)

        assertTrue(job.isCancelled)
        assertFalse(job.isActive)
        assertEquals(3, countAfterCancel)
        assertEquals(countAfterCancel, count)
    }
}
