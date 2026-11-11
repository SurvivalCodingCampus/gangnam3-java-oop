package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.pattern.state.*

/**
 * GoF State Pattern 전용 테스트
 */
class StatePatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [State Pattern Test 시작] ==========")
        println()

        testWaitingState()
        testMyTurnState()
        testOpponentTurnState()
        testBattleEndedState()
        testStateTransitions()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [State Pattern Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    private fun testWaitingState() {
        val context = BattleContext()
        record("State - 대기: 배틀 비활성",
            !context.isBattleActive())
        record("State - 대기: 내 차례 아님",
            !context.isMyTurn())
        record("State - 대기: 상태명",
            context.stateName() == "대기 중")
    }

    private fun testMyTurnState() {
        val context = BattleContext()
        context.setState(MyTurnState())
        record("State - 내 차례: 배틀 활성",
            context.isBattleActive())
        record("State - 내 차례: 내 차례임",
            context.isMyTurn())
        record("State - 내 차례: 상태명",
            context.stateName() == "내 차례")
    }

    private fun testOpponentTurnState() {
        val context = BattleContext()
        context.setState(OpponentTurnState())
        record("State - 상대 차례: 배틀 활성",
            context.isBattleActive())
        record("State - 상대 차례: 내 차례 아님",
            !context.isMyTurn())
        record("State - 상대 차례: 상태명",
            context.stateName() == "상대 차례")
    }

    private fun testBattleEndedState() {
        val context = BattleContext()
        context.setState(BattleEndedState())
        record("State - 배틀 종료: 배틀 비활성",
            !context.isBattleActive())
        record("State - 배틀 종료: 내 차례 아님",
            !context.isMyTurn())
        record("State - 배틀 종료: 상태명",
            context.stateName() == "배틀 종료")
    }

    private fun testStateTransitions() {
        val context = BattleContext()

        // 대기 → 내 차례 → 상대 차례 → 배틀 종료 → 대기(리셋)
        context.setState(MyTurnState())
        val t1 = context.stateName() == "내 차례"

        context.setState(OpponentTurnState())
        val t2 = context.stateName() == "상대 차례"

        context.setState(BattleEndedState())
        val t3 = context.stateName() == "배틀 종료"

        context.reset()
        val t4 = context.stateName() == "대기 중"

        record("State - 상태 전이",
            t1 && t2 && t3 && t4)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StatePatternTest().runAll()
        }
    }
}
