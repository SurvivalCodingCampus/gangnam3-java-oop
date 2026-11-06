package com.survivalcoding.`18day`.pattern.facade

import com.survivalcoding.`18day`.battle.DefaultTypeEffectivenessStrategy
import com.survivalcoding.`18day`.battle.PokemonBattleService
import com.survivalcoding.`18day`.battle.TypeEffectivenessStrategy
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.game.GameController
import com.survivalcoding.`18day`.game.GameModel
import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.command.AttackCommand
import com.survivalcoding.`18day`.pattern.command.BattleCommandInvoker
import com.survivalcoding.`18day`.pattern.command.ResetBattleCommand
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory
import com.survivalcoding.`18day`.pattern.observer.BattleObservable
import com.survivalcoding.`18day`.pattern.observer.BattleObserver
import com.survivalcoding.`18day`.pattern.observer.BattleEvent
import com.survivalcoding.`18day`.pattern.state.BattleContext
import com.survivalcoding.`18day`.repostory.IPokemonBatchRepository
import com.survivalcoding.`18day`.repostory.IPokemonCacheManager
import com.survivalcoding.`18day`.repostory.IPokemonRepository
import com.survivalcoding.`18day`.repostory.ICloseableResource

/**
 * [GoF Facade Pattern] 게임 퍼사드.
 *
 * GameController, BattleContext, BattleCommandInvoker, IPokemonRepository 등
 * 복잡한 서브시스템을 하나의 간단한 인터페이스로 통합합니다.
 *
 * 클라이언트(PokemonGameApp)는 서브시스템의 내부 구조를 몰라도
 * 퍼사드 메서드만 호출하면 게임을 제어할 수 있습니다.
 *
 * SRP 준수: 게임 orchestration만을 담당합니다.
 * DIP 준수: 인터페이스(IPokemonRepository, BattleObservable)에 의존합니다.
 */
class GameFacade {

    private val gameModel = GameModel()
    private val battleContext = BattleContext()
    private val typeStrategy: TypeEffectivenessStrategy = DefaultTypeEffectivenessStrategy()
    private val battleService = PokemonBattleService(typeStrategy)
    private val repository: IPokemonRepository = PokemonRepositoryFactory.createFake()
    private val controller = GameController(
        gameModel = gameModel,
        pokemonRepository = repository,
        battleService = battleService,
        batchRepository = repository as? IPokemonBatchRepository,
        cacheManager = repository as? IPokemonCacheManager,
        closeableResource = repository as? ICloseableResource
    )
    private val commandInvoker = BattleCommandInvoker()

    fun selectMyPokemon(name: String) {
        controller.selectMyPokemon(name)
    }

    fun selectOpponentPokemon(name: String) {
        controller.selectOpponentPokemon(name)
    }

    fun performAttack() {
        if (!gameModel.battleActive) {
            return
        }

        if (gameModel.isMyTurn) {
            val attacker = gameModel.myPokemon!!
            val defender = gameModel.opponentPokemon!!
            val command = AttackCommand(attacker, defender, battleService)
            val result = commandInvoker.executeCommand(command)

            if (result.battleEnded) {
                gameModel.battleActive = false
            } else {
                gameModel.isMyTurn = false
            }
        }
    }

    fun resetBattle() {
        val command = ResetBattleCommand(battleContext)
        val result = commandInvoker.executeCommand(command)
        gameModel.reset()
        battleContext.reset()
    }

    fun getMyPokemon(): Pokemon? = gameModel.myPokemon
    fun getOpponentPokemon(): Pokemon? = gameModel.opponentPokemon
    fun isBattleActive(): Boolean = gameModel.battleActive
    fun isMyTurn(): Boolean = gameModel.isMyTurn
    fun getStateName(): String = battleContext.stateName()

    fun getCachedPokemonNames(): List<String> {
        return controller.getCachedPokemonNames().toList()
    }

    fun clearCache() {
        controller.clearCache()
    }

    fun cacheSize(): Int = controller.cacheSize()

    fun getPokemonBatch(names: List<String>): Map<String, Result<Pokemon, NetworkError>> {
        return controller.getPokemonBatch(names)
    }

    fun close() {
        controller.close()
        commandInvoker.clearHistory()
    }

    companion object {
        @JvmStatic
        fun create(): GameFacade = GameFacade()
    }
}
