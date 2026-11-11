package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.battle.DefaultTypeEffectivenessStrategy
import com.survivalcoding.`18day`.battle.PokemonBattleService
import com.survivalcoding.`18day`.model.Pokemon

/**
 * GoF Command Pattern 전용 테스트
 */
class CommandPatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [Command Pattern Test 시작] ==========")
        println()

        testAttackCommand()
        testResetBattleCommand()
        testCommandInvoker()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [Command Pattern Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    private fun testAttackCommand() {
        val battleService = PokemonBattleService(DefaultTypeEffectivenessStrategy())

        val attacker = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        val defender = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 78)

        val command = com.survivalcoding.`18day`.pattern.command.AttackCommand(attacker, defender, battleService)
        record("Command - 명령 이름",
            command.name == "공격")

        val result = command.execute()
        record("Command - 공격 실행 성공",
            result.success && result.message.contains("공격!") && result.message.contains("데미지"))

        // AttackCommand는 undo를 구현하지 않음
        record("Command - 실행 취소 미지원",
            !command.undo())
    }

    private fun testResetBattleCommand() {
        val context = com.survivalcoding.`18day`.pattern.state.BattleContext()
        val cmd = com.survivalcoding.`18day`.pattern.command.ResetBattleCommand(context)
        record("Command - ResetBattleCommand 이름",
            cmd.name == "배틀 초기화")

        val result = cmd.execute()
        record("Command - ResetBattleCommand 실행",
            result.success && result.message.contains("초기화"))
    }

    private fun testCommandInvoker() {
        val invoker = com.survivalcoding.`18day`.pattern.command.BattleCommandInvoker()

        val battleService = PokemonBattleService(DefaultTypeEffectivenessStrategy())
        val attacker = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        val defender = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 78)

        val cmd1 = com.survivalcoding.`18day`.pattern.command.AttackCommand(attacker, defender, battleService)
        val cmd2 = com.survivalcoding.`18day`.pattern.command.AttackCommand(defender, attacker, battleService)

        invoker.executeCommand(cmd1)
        invoker.executeCommand(cmd2)
        record("CommandInvoker - 히스토리 크기",
            invoker.historySize() == 2)

        invoker.clearHistory()
        record("CommandInvoker - 히스토리 클리어",
            invoker.historySize() == 0)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            CommandPatternTest().runAll()
        }
    }
}
