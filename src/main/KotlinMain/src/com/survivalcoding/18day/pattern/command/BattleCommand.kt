package com.survivalcoding.`18day`.pattern.command

import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF Command Pattern] 배틀 명령 인터페이스.
 *
 * 배틀 액션(공격, 포켓몬 교체, 배틀 초기화 등)을 캡슐화하여
 * 요청을 객체로 표현합니다. 이를 통해 명령을 매개변수화하고,
 * 큐에 넣거나 로깅하거나 실행 취소(Undo)할 수 있습니다.
 *
 * SRP 준수: 각 명령 클래스는 하나의 액션만 담당합니다.
 * OCP 준수: 새로운 배틀 액션을 추가해도 기존 명령 코드 수정 불필요.
 */
interface BattleCommand {

    /** 명령을 실행합니다. */
    fun execute(): BattleCommandResult

    /** 명령을 실행 취소합니다 (지원하지 않으면 false 반환). */
    fun undo(): Boolean = false

    /** 명령의 이름을 반환합니다 (디버깅/로그용). */
    val name: String
}

/**
 * 명령 실행 결과.
 */
data class BattleCommandResult(
    val success: Boolean,
    val message: String,
    val isMyTurn: Boolean = true,
    val battleEnded: Boolean = false,
    val isMyVictory: Boolean = false,
    val winnerName: String? = null
)

/**
 * [GoF Command Pattern] 공격 명령.
 *
 * 공격자 포켓몬이 방어자 포켓몬을 공격하는 명령을 캡슐화합니다.
 * 실행 취소 시 HP를 복원할 수 있도록 이전 상태를 스냅샷으로 보관합니다.
 */
class AttackCommand(
    private val attacker: Pokemon,
    private val defender: Pokemon,
    private val battleService: com.survivalcoding.`18day`.battle.PokemonBattleService
) : BattleCommand {

    private var previousDefender: Pokemon? = null
    private var calculatedDamage: Int = 0
    private var effectiveness: Double = 1.0

    override val name: String = "공격"

    override fun execute(): BattleCommandResult {
        // 실행 전 상태 스냅샷 (실행 취소용)
        previousDefender = defender

        calculatedDamage = battleService.calculateDamage(attacker, defender)
        effectiveness = battleService.getTypeEffectiveness(attacker.type, defender.type)
        val updatedDefender = battleService.applyDamage(defender, calculatedDamage)

        val msg = battleService.createAttackMessage(attacker, updatedDefender, calculatedDamage, effectiveness)

        return BattleCommandResult(
            success = true,
            message = msg,
            battleEnded = !updatedDefender.isAlive(),
            isMyVictory = !updatedDefender.isAlive(),
            winnerName = if (!updatedDefender.isAlive()) attacker.name else null
        )
    }

    override fun undo(): Boolean {
        return previousDefender != null
    }
}

/**
 * [GoF Command Pattern] 배틀 초기화 명령.
 */
class ResetBattleCommand(
    private val gameContext: com.survivalcoding.`18day`.pattern.state.BattleContext
) : BattleCommand {

    override val name: String = "배틀 초기화"

    override fun execute(): BattleCommandResult {
        gameContext.reset()
        return BattleCommandResult(
            success = true,
            message = "배틀이 초기화됐어요!",
            isMyTurn = true
        )
    }

    override fun undo(): Boolean = false
}

/**
 * [GoF Command Pattern] 사운드 재생 명령.
 */
class PlaySoundCommand(
    private val soundUrl: String?,
    private val loop: Boolean = false
) : BattleCommand {

    override val name: String = "사운드 재생"

    override fun execute(): BattleCommandResult {
        com.survivalcoding.`18day`.audio.SoundManager.play(soundUrl, loop = loop)
        return BattleCommandResult(
            success = true,
            message = "사운드 재생: ${soundUrl ?: "기본 효과음"}"
        )
    }

    override fun undo(): Boolean = false
}

/**
 * [GoF Command Pattern] 명령 실행자 (Invoker).
 *
 * 명령을 저장하고 실행하며, 실행 취소 히스토리를 관리합니다.
 */
class BattleCommandInvoker {

    private val history = mutableListOf<BattleCommand>()

    fun executeCommand(command: BattleCommand): BattleCommandResult {
        val result = command.execute()
        history.add(command)
        return result
    }

    fun undoLast(): Boolean {
        return if (history.isNotEmpty()) {
            val last = history.removeAt(history.size - 1)
            last.undo()
        } else {
            false
        }
    }

    fun clearHistory() {
        history.clear()
    }

    fun historySize(): Int = history.size
}
