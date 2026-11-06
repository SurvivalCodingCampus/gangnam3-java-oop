package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.pattern.memento.BattleMemento
import com.survivalcoding.`18day`.pattern.memento.BattleMementoCaretaker
import com.survivalcoding.`18day`.model.Pokemon

/**
 * GoF Memento Pattern 전용 테스트
 */
class MementoPatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [Memento Pattern Test 시작] ==========")
        println()

        testSaveAndUndo()
        testMaxHistory()
        testClear()
        testPeek()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [Memento Pattern Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    private fun testSaveAndUndo() {
        val caretaker = BattleMementoCaretaker()

        val p1 = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        val p2 = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 78)

        val m = BattleMemento(myPokemon = p1, opponentPokemon = p2, isMyTurn = true, battleActive = true)
        caretaker.save(m)
        record("Memento - 저장 후 크기 1", caretaker.size() == 1)

        val undone = caretaker.undo()
        record("Memento - undo 반환",
            undone != null && undone!!.isMyTurn == true && undone.battleActive == true)

        val undone2 = caretaker.undo()
        record("Memento - 히스토리 소진 후 null", undone2 == null)
    }

    private fun testMaxHistory() {
        val caretaker = BattleMementoCaretaker(maxHistory = 3)

        val p = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)

        for (i in 1..5) {
            caretaker.save(BattleMemento(myPokemon = p.copy(hp = i), opponentPokemon = null, isMyTurn = true, battleActive = true))
        }
        record("Memento - maxHistory=3, 5개 저장 후 크기 3",
            caretaker.size() == 3)
    }

    private fun testClear() {
        val caretaker = BattleMementoCaretaker()
        val p = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        caretaker.save(BattleMemento(myPokemon = p, opponentPokemon = null, isMyTurn = true, battleActive = true))
        caretaker.clear()
        record("Memento - clear 후 크기 0", caretaker.size() == 0)
    }

    private fun testPeek() {
        val caretaker = BattleMementoCaretaker()
        val p = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)

        val m = BattleMemento(myPokemon = p, opponentPokemon = null, isMyTurn = true, battleActive = true)
        caretaker.save(m)
        record("Memento - peek 마지막 항목",
            caretaker.peek() != null && caretaker.peek()!!.myPokemon!!.name == "pikachu")
        record("Memento - peek 후 크기 변화 없음",
            caretaker.size() == 1)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MementoPatternTest().runAll()
        }
    }
}
