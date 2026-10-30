@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@file:Suppress("NonAsciiCharacters")

package day17

import com.survivalcoding.day17.cancelAfter
import com.survivalcoding.day17.ex1
import com.survivalcoding.day17.ex2
import com.survivalcoding.day17.repeatSound
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.time.Duration.Companion.seconds

class Test {
    private val output = ByteArrayOutputStream()
    private lateinit var originalOut: PrintStream

    @BeforeEach
    fun setUp() {
        originalOut = System.out // 기본 console 출력 저장
        System.setOut(PrintStream(output, true, Charsets.UTF_8)) // system.out을 output 으로 설정
    }

    @AfterEach
    fun tearDown() {
        System.out.close() // 버퍼를 비우고 닫음
        System.setOut(originalOut) // 기존 console 출력으로 변경
    }

    // 출력 스트림에 들어 있는 값을 확인하기 위해 리스트로 변환
    private fun sounds(): List<String> {
        val text = output.toString(Charsets.UTF_8)

        return if (text.isNotEmpty()) {
            text
                .trimEnd('\n')
                .lines() // 개행문자 기준 분리
        } else {
            emptyList()
        }
    }

    @Test
    fun `지정한 간격마다 소리를 내고 반복 횟수에 도달하면 종료한다`() = runTest {
        val job = launch { repeatSound("꾸우", intervalSeconds = 2, repeatTimes = 2) }


        advanceTimeBy(2.seconds) // 가상 시간을 지정한 만큼 진행시키는 함수

        // 2000ms에 예약된 작업은 advanceTimeBy(2000)만으로 실행되지 않음
        // 해당 시점의 작업을 실행하려면 runCurrent()를 호출해야 함
        assertThat(sounds()).isEmpty()
        runCurrent() // 현재 가상 시간에 예약된 작업 실행
        assertThat(sounds()).containsExactly("꾸우")
        assertThat(job.isActive).isTrue()

        advanceTimeBy(2.seconds)
        runCurrent()
        assertThat(sounds()).containsExactly("꾸우", "꾸우")
        assertThat(job.isCompleted).isTrue()
    }

    @Test
    fun `반복 횟수가 0이면 소리를 내지 않고 바로 종료한다`() = runTest {
        repeatSound("꾸우", intervalSeconds = 1, repeatTimes = 0)

        assertThat(sounds()).isEmpty()
        assertThat(currentTime).isZero()
    }

    @Test
    fun `무한 반복은 지정한 간격으로 출력하다 취소하면 멈춘다`() = runTest {
        val job = launch { repeatSound("짹짹", intervalSeconds = 3) }

        advanceTimeBy(3.seconds)
        assertThat(sounds()).isEmpty()
        runCurrent()
        assertThat(sounds()).containsExactly("짹짹")

        advanceTimeBy(3.seconds)
        runCurrent()
        assertThat(sounds()).containsExactly("짹짹", "짹짹")
        job.cancelAndJoin()
        advanceTimeBy(6.seconds)
        runCurrent()

        assertThat(job.isCancelled).isTrue()
        assertThat(sounds()).containsExactly("짹짹", "짹짹")
    }

    @Test
    fun `제한 시간이 지나면 전달한 모든 작업을 취소한다`() = runTest {
        val jobs = List(3) { Job() }
        launch { cancelAfter(10, jobs) }

        advanceTimeBy(10.seconds)
        assertThat(jobs.all { it.isActive }).isTrue()
        runCurrent()

        assertThat(jobs.all { it.isCancelled }).isTrue()
    }

    @Test
    fun `ex1은 세 종류의 소리를 각각 네 번 내고 12초에 완료한다`() = runTest {
        ex1()

        assertThat(
            sounds()
                .groupingBy { it }
                .eachCount())
                .isEqualTo(mapOf("꾸우" to 4, "까악" to 4, "짹짹" to 4)
         )
        assertThat(currentTime).isEqualTo(12_000L)
    }

    @Test
    fun `ex2는 10초에 모든 새소리를 멈춘다`() = runTest {
        ex2()

        assertThat(currentTime).isEqualTo(10_000L)

        val counts = sounds()
            .groupingBy { it }
            .eachCount()

        assertThat(counts.keys).containsExactlyInAnyOrder("꾸우", "까악", "짹짹")

        // 10초 경계의 출력과 취소 순서에 따라 마지막 소리는 출력될 수 있다
        assertThat(counts.getValue("꾸우")).isBetween(9, 10)
        assertThat(counts.getValue("까악")).isBetween(4, 5)
        assertThat(counts.getValue("짹짹")).isEqualTo(3)

        val completedSounds = sounds()

        advanceTimeBy(10.seconds)
        runCurrent()

        assertThat(sounds()).isEqualTo(completedSounds)
    }
}
