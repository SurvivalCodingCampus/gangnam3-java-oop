package com.survivalcoding.`18day`.game

import com.survivalcoding.`18day`.battle.PokemonBattleService
import com.survivalcoding.`18day`.battle.TypeEffectivenessStrategy
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.command.AttackCommand
import com.survivalcoding.`18day`.pattern.command.BattleCommandInvoker
import com.survivalcoding.`18day`.pattern.command.BattleCommandResult
import com.survivalcoding.`18day`.pattern.command.ResetBattleCommand
import com.survivalcoding.`18day`.pattern.memento.BattleMemento
import com.survivalcoding.`18day`.pattern.memento.BattleMementoCaretaker
import com.survivalcoding.`18day`.pattern.observer.BattleEvent
import com.survivalcoding.`18day`.pattern.observer.BattleObserver
import com.survivalcoding.`18day`.pattern.observer.BattleObservable
import com.survivalcoding.`18day`.pattern.state.BattleContext
import com.survivalcoding.`18day`.pattern.state.BattleEndedState
import com.survivalcoding.`18day`.pattern.state.MyTurnState
import com.survivalcoding.`18day`.pattern.state.OpponentTurnState
import com.survivalcoding.`18day`.pattern.state.WaitingState
import com.survivalcoding.`18day`.repostory.IPokemonBatchRepository
import com.survivalcoding.`18day`.repostory.IPokemonCacheManager
import com.survivalcoding.`18day`.repostory.IPokemonRepository
import com.survivalcoding.`18day`.repostory.ICloseableResource
import javafx.application.Platform

/**
 * [SRP] 게임 조정 로직 전담 컨트롤러.
 * GameModel 상태를 변경하고, Repository와 BattleService를 조율함.
 * UI에 대한 직접 의존 없음 (콜백 인터페이스로 통신).
 *
 * [GoF 패턴 적용]
 * - Observer: BattleObservable 구현 - ViewListener와 BattleObserver 모두 지원
 * - State: BattleContext로 배틀 상태 관리 (대기/내턴/상대턴/종료)
 * - Command: BattleCommandInvoker로 공격/초기화 명령 캡슐화 및 실행 취소 지원
 * - Memento: BattleMementoCaretaker로 배틀 상태 스냅샷/복원
 * - Facade: GameController 자체가 서브시스템에 대한 퍼사드 역할
 */
class GameController(
    private val gameModel: com.survivalcoding.`18day`.game.GameModel,
    private val pokemonRepository: IPokemonRepository,
    private val battleService: PokemonBattleService,
    private val batchRepository: IPokemonBatchRepository? = null,
    private val cacheManager: IPokemonCacheManager? = null,
    private val closeableResource: ICloseableResource? = null
) : BattleObservable {

    // --- 기존 ViewListener (UI 콜백) ---
    interface ViewListener {
        fun onPokemonLoaded(isMy: Boolean, pokemon: Pokemon)
        fun onLoadError(message: String)
        fun onBattleReady()
        fun onTurnChanged(isMyTurn: Boolean)
        fun onAttackExecuted(message: String, isMyTurn: Boolean)
        fun onPokemonUpdated(isMy: Boolean, pokemon: Pokemon)
        fun onBattleEnded(isMyVictory: Boolean, winnerName: String)
        fun onStatusChanged(message: String)
        fun onBattleReset()
    }

    private var listener: ViewListener? = null

    fun setViewListener(listener: ViewListener) {
        this.listener = listener
    }

    // [SRP] 게임 상태는 GameController/GameModel이 소유, UI는 조회만 담당
    val isBattleActive: Boolean
        get() = gameModel.battleActive

    val isMyTurn: Boolean
        get() = gameModel.isMyTurn

    // --- GoF Observer: BattleObserver 등록 관리 ---
    private val battleObservers = mutableListOf<BattleObserver>()

    override fun registerObserver(observer: BattleObserver) {
        if (observer !in battleObservers) {
            battleObservers.add(observer)
        }
    }

    override fun unregisterObserver(observer: BattleObserver) {
        battleObservers.remove(observer)
    }

    override fun notifyObservers(event: BattleEvent) {
        battleObservers.toList().forEach { it.onBattleEvent(event) }
    }

    // --- GoF State: BattleContext ---
    private val battleContext = BattleContext().apply {
        myPokemon = gameModel.myPokemon
        opponentPokemon = gameModel.opponentPokemon
    }

    // --- GoF Command: Invoker ---
    private val commandInvoker = BattleCommandInvoker()

    // --- GoF Memento: Caretaker ---
    private val mementoCaretaker = BattleMementoCaretaker()

    fun selectMyPokemon(name: String) {
        listener?.onStatusChanged("내 포켓몬을 불러오는 중...")
        loadPokemon(name, isMy = true)
    }

    fun selectOpponentPokemon(name: String) {
        listener?.onStatusChanged("상대 포켓몬을 불러오는 중...")
        loadPokemon(name, isMy = false)
    }

    private fun loadPokemon(name: String, isMy: Boolean) {
        val result = pokemonRepository.getPokemon(name)
        when (result) {
            is Result.Success -> {
                val pokemon = result.value
                if (isMy) {
                    gameModel.myPokemon = pokemon
                    battleContext.myPokemon = pokemon
                } else {
                    gameModel.opponentPokemon = pokemon
                    battleContext.opponentPokemon = pokemon
                }
                listener?.onPokemonLoaded(isMy, pokemon)
                notifyPokemonLoaded(isMy, pokemon)
                checkBattleReady()
            }
            is Result.Error -> {
                listener?.onLoadError("포켓몬을 찾을 수 없어요: ${result.error}")
                notifyLoadError("포켓몬을 찾을 수 없어요: ${result.error}")
            }
            else -> {
                listener?.onLoadError("알 수 없는 결과 타입입니다.")
                notifyLoadError("알 수 없는 결과 타입입니다.")
            }
        }
    }

    private fun checkBattleReady() {
        if (gameModel.isBattleReady()) {
            gameModel.battleActive = true
            gameModel.isMyTurn = true
            battleContext.setState(com.survivalcoding.`18day`.pattern.state.MyTurnState())
            saveMemento()
            listener?.onBattleReady()
            listener?.onTurnChanged(true)
            listener?.onStatusChanged("⚔ 배틀 시작! 내 차례예요!")
            notifyBattleReady()
            notifyTurnChanged(true)
            notifyStatusChanged("⚔ 배틀 시작! 내 차례예요!")
        }
    }

    fun performAttack() {
        if (!gameModel.battleActive) {
            listener?.onStatusChanged("⚠️ 먼저 양쪽 포켓몬을 선택해요!")
            notifyStatusChanged("⚠️ 먼저 양쪽 포켓몬을 선택해요!")
            return
        }

        if (gameModel.isMyTurn) {
            val attacker = gameModel.myPokemon!!
            val defender = gameModel.opponentPokemon!!

            // [GoF Command] 공격 명령을 캡슐화하여 실행
            val command = AttackCommand(attacker, defender, battleService)
            val result: BattleCommandResult = commandInvoker.executeCommand(command)

            // 데미지 적용된 방어자 상태로 게임 모델 업데이트
            val damage = battleService.calculateDamage(attacker, defender)
            val updatedDefender = battleService.applyDamage(defender, damage)
            gameModel.opponentPokemon = updatedDefender
            battleContext.opponentPokemon = updatedDefender

            saveMemento()

            listener?.onAttackExecuted(result.message, true)
            listener?.onStatusChanged(result.message)
            notifyAttackExecuted(result.message, true, attacker, updatedDefender, damage)
            notifyStatusChanged(result.message)
            listener?.onPokemonUpdated(false, updatedDefender)
            notifyPokemonUpdated(false, updatedDefender)

            if (result.battleEnded) {
                val winnerName = attacker.name
                gameModel.battleActive = false
                battleContext.setState(com.survivalcoding.`18day`.pattern.state.BattleEndedState())
                listener?.onBattleEnded(true, winnerName)
                notifyBattleEnded(true, winnerName)
            } else {
                gameModel.isMyTurn = false
                battleContext.setState(com.survivalcoding.`18day`.pattern.state.OpponentTurnState())
                listener?.onTurnChanged(false)
                listener?.onStatusChanged("상대 차례예요...")
                notifyTurnChanged(false)
                notifyStatusChanged("상대 차례예요...")

                // 상대 공격 지연 후 실행 (JavaFX 스레드에서 UI 업데이트)
                java.util.Timer().schedule(object : java.util.TimerTask() {
                    override fun run() {
                        Platform.runLater {
                            opponentAttack()
                        }
                    }
                }, 1500)
            }
        }
    }

    private fun opponentAttack() {
        if (!gameModel.battleActive) return

        val attacker = gameModel.opponentPokemon!!
        val defender = gameModel.myPokemon!!

        // [GoF Command] 공격 명령을 캡슐화하여 실행
        val command = AttackCommand(attacker, defender, battleService)
        val result: BattleCommandResult = commandInvoker.executeCommand(command)

        // 데미지 적용된 방어자 상태로 게임 모델 업데이트
        val damage = battleService.calculateDamage(attacker, defender)
        val updatedDefender = battleService.applyDamage(defender, damage)
        gameModel.myPokemon = updatedDefender
        battleContext.myPokemon = updatedDefender

        saveMemento()

        listener?.onAttackExecuted(result.message, false)
        listener?.onStatusChanged(result.message)
        notifyAttackExecuted(result.message, false, attacker, updatedDefender, damage)
        notifyStatusChanged(result.message)
        listener?.onPokemonUpdated(true, updatedDefender)
        notifyPokemonUpdated(true, updatedDefender)

        if (result.battleEnded) {
            val winnerName = attacker.name
            gameModel.battleActive = false
            battleContext.setState(com.survivalcoding.`18day`.pattern.state.BattleEndedState())
            listener?.onBattleEnded(false, winnerName)
            notifyBattleEnded(false, winnerName)
        } else {
            gameModel.isMyTurn = true
            battleContext.setState(com.survivalcoding.`18day`.pattern.state.MyTurnState())
            listener?.onTurnChanged(true)
            listener?.onStatusChanged("⚔ 내 차례예요! 공격을 눌러요.")
            notifyTurnChanged(true)
            notifyStatusChanged("⚔ 내 차례예요! 공격을 눌러요.")
        }
    }

    private fun saveMemento() {
        mementoCaretaker.save(
            BattleMemento(
                myPokemon = gameModel.myPokemon,
                opponentPokemon = gameModel.opponentPokemon,
                isMyTurn = gameModel.isMyTurn,
                battleActive = gameModel.battleActive
            )
        )
    }

    /**
     * [GoF Memento] 마지막 배틀 상태를 복원합니다 (실행 취소).
     * @return 복원 성공 여부
     */
    fun undoLastBattleState(): Boolean {
        val memento = mementoCaretaker.undo() ?: return false
        gameModel.myPokemon = memento.myPokemon
        gameModel.opponentPokemon = memento.opponentPokemon
        gameModel.isMyTurn = memento.isMyTurn
        gameModel.battleActive = memento.battleActive
        battleContext.myPokemon = memento.myPokemon
        battleContext.opponentPokemon = memento.opponentPokemon
        val emptyPokemon = Pokemon(id = 0, name = "", type = "unknown", height = 0, weight = 0,
            baseExperience = 0, hp = 0, attack = 0, defense = 0)
        listener?.onPokemonUpdated(true, memento.myPokemon ?: emptyPokemon)
        listener?.onPokemonUpdated(false, memento.opponentPokemon ?: emptyPokemon)
        listener?.onTurnChanged(memento.isMyTurn)
        listener?.onStatusChanged("배틀 상태를 복원했어요!")
        notifyTurnChanged(memento.isMyTurn)
        notifyStatusChanged("배틀 상태를 복원했어요!")
        return true
    }

    private fun endBattle(isMyVictory: Boolean, winnerName: String) {
        gameModel.battleActive = false
        listener?.onBattleEnded(isMyVictory, winnerName)
        notifyBattleEnded(isMyVictory, winnerName)
    }

    fun resetBattle() {
        val command = ResetBattleCommand(battleContext)
        val result: BattleCommandResult = commandInvoker.executeCommand(command)
        gameModel.reset()
        battleContext.reset()
        mementoCaretaker.clear()
        listener?.onBattleReset()
        listener?.onStatusChanged("배틀이 초기화됐어요!")
        notifyBattleReset()
        notifyStatusChanged("배틀이 초기화됐어요!")
    }

    fun getCachedPokemonNames(): List<String> {
        return cacheManager?.getCachedPokemonNames() ?: emptyList()
    }

    fun clearCache() {
        cacheManager?.clearCache()
    }

    fun cacheSize(): Int {
        return cacheManager?.cacheSize() ?: 0
    }

    fun getPokemonBatch(names: List<String>): Map<String, Result<Pokemon, NetworkError>> {
        return batchRepository?.getPokemonBatch(names) ?: emptyMap()
    }

    // [SRP] gameModel 직접 접근 대신 컨트롤러 게터 사용
    fun getMyPokemon(): Pokemon? = gameModel.myPokemon
    fun getOpponentPokemon(): Pokemon? = gameModel.opponentPokemon

    fun close() {
        closeableResource?.close()
    }

    // --- GoF Observer 알림 메서드들 ---

    private fun notifyPokemonLoaded(isMy: Boolean, pokemon: Pokemon) {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.PokemonLoaded(isMy, pokemon)) }
    }

    private fun notifyLoadError(message: String) {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.LoadError(message)) }
    }

    private fun notifyBattleReady() {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.BattleReady()) }
    }

    private fun notifyTurnChanged(isMyTurn: Boolean) {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.TurnChanged(isMyTurn)) }
    }

    private fun notifyAttackExecuted(message: String, isMyTurn: Boolean, attacker: Pokemon, defender: Pokemon, damage: Int) {
        battleObservers.toList().forEach {
            it.onBattleEvent(BattleEvent.AttackExecuted(message, isMyTurn, attacker, defender, damage))
        }
    }

    private fun notifyPokemonUpdated(isMy: Boolean, pokemon: Pokemon) {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.PokemonUpdated(isMy, pokemon)) }
    }

    private fun notifyBattleEnded(isMyVictory: Boolean, winnerName: String) {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.BattleEnded(isMyVictory, winnerName)) }
    }

    private fun notifyStatusChanged(message: String) {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.StatusChanged(message)) }
    }

    private fun notifyBattleReset() {
        battleObservers.toList().forEach { it.onBattleEvent(BattleEvent.BattleReset()) }
    }
}
