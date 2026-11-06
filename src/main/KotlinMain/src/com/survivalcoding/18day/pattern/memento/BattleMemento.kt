package com.survivalcoding.`18day`.pattern.memento

import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF Memento Pattern] 배틀 상태 스냅샷.
 *
 * 배틀 시점의 포켓몬 상태(HP, 차례, 배틀 활성 여부 등)를 캡슐화합니다.
 * 오리젠이터(Originator)가 내부 상태를 외부에 노출하지 않고 저장/복원할 수 있도록
 * 불변 스냅샷 객체를 제공합니다.
 *
 * SRP 준수: 상태 스냅샷 데이터만 보유, 로직 없음.
 */
data class BattleMemento(
    val myPokemon: Pokemon?,
    val opponentPokemon: Pokemon?,
    val isMyTurn: Boolean,
    val battleActive: Boolean
)

/**
 * [GoF Memento Pattern] 배틀 상태 관리자 (Caretaker).
 *
 * Memento 객체를 관리하고, 실행 취소(Undo)를 위해 히스토리를 보관합니다.
 * Originator의 내부 구조를 알지 못한 채 Memento를 보관/복원만 합니다.
 */
class BattleMementoCaretaker(private val maxHistory: Int = 10) {

    private val history = mutableListOf<BattleMemento>()

    fun save(memento: BattleMemento) {
        if (history.size >= maxHistory) {
            history.removeAt(0)
        }
        history.add(memento)
    }

    fun undo(): BattleMemento? {
        return if (history.isNotEmpty()) {
            history.removeAt(history.size - 1)
        } else {
            null
        }
    }

    fun peek(): BattleMemento? = history.lastOrNull()

    fun clear() {
        history.clear()
    }

    fun size(): Int = history.size
}
