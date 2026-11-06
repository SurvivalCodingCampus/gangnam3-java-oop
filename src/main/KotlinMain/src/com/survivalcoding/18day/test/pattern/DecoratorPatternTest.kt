package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.pattern.decorator.*
import com.survivalcoding.`18day`.audio.SoundManager

/**
 * GoF Decorator Pattern 테스트
 *
 * SoundDecorator 체인의 동작을 검증합니다.
 * (실제 사운드 재생은 환경에 의존하므로, 데코레이터 래핑 및 위임 구조만 검증)
 */
class DecoratorPatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [Decorator Pattern Test 시작] ==========")
        println()

        testDecoratorChain()
        testVolumeDecorator()
        testLoopDecorator()
        testFadeInDecorator()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [Decorator Pattern Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    private fun testDecoratorChain() {
        val base = SoundManagerAdapter()
        val decorated = VolumeDecorator(FadeInDecorator(base), volume = 0.5)

        // 데코레이터 체인이 정상적으로 생성되고 메서드 위임이 동작하는지 확인
        // (실제 사운드 재생은 환경에 따라 실패할 수 있으므로 예외 없이 실행되는지만 확인)
        try {
            decorated.play(null, loop = false) // null → 기본 비프음
            decorated.stop()
            decorated.setVolume(0.8)
            record("Decorator - 체인 생성 및 메서드 위임", true)
        } catch (e: Exception) {
            record("Decorator - 체인 생성 및 메서드 위임", false, e.message ?: "")
        }
    }

    private fun testVolumeDecorator() {
        val base = SoundManagerAdapter()
        val volDecorator = VolumeDecorator(base, volume = 0.3)

        // 볼륨 데코레이터는 재생 전에 볼륨을 설정해야 함
        try {
            volDecorator.play("sounds/nonexistent.wav", loop = false)
            record("Decorator - VolumeDecorator 재생", true)
        } catch (e: Exception) {
            record("Decorator - VolumeDecorator 재생", false, e.message ?: "")
        }
    }

    private fun testLoopDecorator() {
        val base = SoundManagerAdapter()
        val loopDecorator = LoopDecorator(base, forceLoop = true)

        try {
            loopDecorator.play("sounds/nonexistent.wav", loop = false) // loop 강제 true
            record("Decorator - LoopDecorator 재생", true)
        } catch (e: Exception) {
            record("Decorator - LoopDecorator 재생", false, e.message ?: "")
        }
    }

    private fun testFadeInDecorator() {
        val base = SoundManagerAdapter()
        val fadeDecorator = FadeInDecorator(base, targetVolume = 0.7, startVolume = 0.2)

        try {
            fadeDecorator.play("sounds/nonexistent.wav", loop = false)
            record("Decorator - FadeInDecorator 재생", true)
        } catch (e: Exception) {
            record("Decorator - FadeInDecorator 재생", false, e.message ?: "")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DecoratorPatternTest().runAll()
        }
    }
}
