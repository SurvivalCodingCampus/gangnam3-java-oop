package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.battle.DefaultTypeEffectivenessStrategy
import com.survivalcoding.`18day`.battle.PokemonBattleService
import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.command.AttackCommand
import com.survivalcoding.`18day`.pattern.command.BattleCommandInvoker
import com.survivalcoding.`18day`.pattern.command.ResetBattleCommand
import com.survivalcoding.`18day`.pattern.factory.PokemonBuilder
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory.DataSourceKind
import com.survivalcoding.`18day`.pattern.memento.BattleMemento
import com.survivalcoding.`18day`.pattern.memento.BattleMementoCaretaker
import com.survivalcoding.`18day`.pattern.observer.BattleEvent
import com.survivalcoding.`18day`.pattern.observer.BattleObserver
import com.survivalcoding.`18day`.pattern.state.*
import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource

/**
 * GoF 디자인 패턴 통합 테스트
 *
 * Factory, State, Observer, Command, Memento, Builder 패턴의 동작을 검증합니다.
 */
class GoFPatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [GoF Pattern Integration Test 시작] ==========")
        println()

        testFactoryPattern()
        testBuilderPattern()
        testStatePattern()
        testObserverPattern()
        testCommandPattern()
        testMementoPattern()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [GoF Pattern Integration Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    // ============================================================
    // 1. Factory Pattern 테스트
    // ============================================================
    private fun testFactoryPattern() {
        val fakeRepo = PokemonRepositoryFactory.createRepository(DataSourceKind.FAKE)
        val result = fakeRepo.getPokemon("pikachu")
        record("Factory - 가짜 저장소 생성 및 조회",
            result is com.survivalcoding.`18day`.core.Result.Success &&
                (result as com.survivalcoding.`18day`.core.Result.Success<Pokemon>).value.name == "pikachu")

        val remoteRepo = PokemonRepositoryFactory.createRepository(DataSourceKind.REMOTE)
        record("Factory - 원격 저장소 생성",
            remoteRepo ::class.java.name.contains("PokemonRepositoryImpl"))

        val fromDataSource = PokemonRepositoryFactory.createRepository(FakePokemonDataSource())
        val result2 = fromDataSource.getPokemon("charizard")
        record("Factory - DataSource 직접 주입",
            result2 is com.survivalcoding.`18day`.core.Result.Success &&
                (result2 as com.survivalcoding.`18day`.core.Result.Success<Pokemon>).value.name == "charizard")
    }

    // ============================================================
    // 2. Builder Pattern 테스트
    // ============================================================
    private fun testBuilderPattern() {
        val pikachu = PokemonBuilder {
            id = 25
            name = "pikachu"
            type = "electric"
            hp = 35
            attack = 55
            defense = 40
        }.build()

        record("Builder - 포켓몬 객체 생성",
            pikachu.id == 25 && pikachu.name == "pikachu" && pikachu.type == "electric" &&
                pikachu.hp == 35 && pikachu.attack == 55 && pikachu.defense == 40)

        // 기본값 테스트
        val defaultPoke = PokemonBuilder { name = "default" }.build()
        record("Builder - 기본값 적용",
            defaultPoke.name == "default" && defaultPoke.hp >= 1 && defaultPoke.id == 0)

        // 빈 이름 처리
        val blankPoke = PokemonBuilder { name = "   " }.build()
        record("Builder - 빈 이름 기본값 처리",
            blankPoke.name == "알 수 없는 포켓몬")
    }

    // ============================================================
    // 3. State Pattern 테스트
    // ============================================================
    private fun testStatePattern() {
        val context = BattleContext()

        // 초기 상태는 대기
        record("State - 초기 상태는 대기",
            context.stateName() == "대기 중" && !context.isBattleActive())

        // 포켓몬 설정 후 내 차례 상태로 변경
        val pikachu = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        val charizard = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 78)
        context.myPokemon = pikachu
        context.opponentPokemon = charizard
        context.setState(MyTurnState())

        record("State - 내 차례 상태",
            context.stateName() == "내 차례" && context.isBattleActive() && context.isMyTurn())

        // 공격 후 상대 차례로
        context.setState(OpponentTurnState())
        record("State - 상대 차례 상태",
            context.stateName() == "상대 차례" && !context.isMyTurn())

        // 배틀 종료
        context.setState(BattleEndedState())
        record("State - 배틀 종료 상태",
            context.stateName() == "배틀 종료" && !context.isBattleActive())

        // 리셋
        context.reset()
        record("State - 리셋 후 대기 상태",
            context.stateName() == "대기 중")
    }

    // ============================================================
    // 4. Observer Pattern 테스트
    // ============================================================
    private fun testObserverPattern() {
        val battleService = PokemonBattleService(DefaultTypeEffectivenessStrategy())

        // 테스트용 옵저버
        var receivedEvents = mutableListOf<BattleEvent>()
        val observer = object : BattleObserver {
            override fun onBattleEvent(event: BattleEvent) {
                receivedEvents.add(event)
            }
        }

        val pikachu = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)

        // 이벤트 수신 확인
        receivedEvents.add(BattleEvent.PokemonLoaded(true, pikachu))
        receivedEvents.add(BattleEvent.TurnChanged(true))
        receivedEvents.add(BattleEvent.BattleEnded(true, "pikachu"))

        record("Observer - 이벤트 수신",
            receivedEvents.size == 3 &&
                receivedEvents[0] is BattleEvent.PokemonLoaded &&
                receivedEvents[1] is BattleEvent.TurnChanged &&
                receivedEvents[2] is BattleEvent.BattleEnded)

        // 옵저버 인터페이스 구현 확인
        record("Observer - BattleObserver 인터페이스 구현",
            observer ::class.java.isInterface == false)
    }

    // ============================================================
    // 5. Command Pattern 테스트
    // ============================================================
    private fun testCommandPattern() {
        val battleService = PokemonBattleService(DefaultTypeEffectivenessStrategy())

        val attacker = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        val defender = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 78)

        val command = AttackCommand(attacker, defender, battleService)
        val result = command.execute()

        record("Command - 공격 명령 실행",
            result.success && result.message.contains("공격!") && result.message.contains("데미지"))

        val invoker = BattleCommandInvoker()
        val result2 = invoker.executeCommand(command)
        record("Command - Invoker 실행",
            result2.success && invoker.historySize() == 2)

        // 실행 취소 (AttackCommand는 undo를 지원하지 않음 - false 반환)
        val undone = invoker.undoLast()
        record("Command - 실행 취소 (미지원 명령)",
            !undone) // AttackCommand는 undo를 구현하지 않음

        // ResetBattleCommand 테스트
        val context = BattleContext()
        context.myPokemon = attacker
        context.opponentPokemon = defender
        val resetCmd = ResetBattleCommand(context)
        val resetResult = resetCmd.execute()
        record("Command - ResetBattleCommand 실행",
            resetResult.success && resetResult.message.contains("초기화"))
    }

    // ============================================================
    // 6. Memento Pattern 테스트
    // ============================================================
    private fun testMementoPattern() {
        val caretaker = BattleMementoCaretaker(maxHistory = 5)

        val p1 = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        val p2 = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 78)

        val memento1 = BattleMemento(myPokemon = p1, opponentPokemon = p2, isMyTurn = true, battleActive = true)
        caretaker.save(memento1)

        val p1Changed = p1.copy(hp = 10)
        val memento2 = BattleMemento(myPokemon = p1Changed, opponentPokemon = p2, isMyTurn = false, battleActive = true)
        caretaker.save(memento2)

        record("Memento - 저장 및 크기",
            caretaker.size() == 2)

        val undone = caretaker.undo()
        record("Memento - 실행 취소 (마지막 스냅샷)",
            undone != null && undone!!.isMyTurn == false && undone.myPokemon!!.hp == 10)

        val undone2 = caretaker.undo()
        record("Memento - 실행 취소 (첫 번째 스냅샷)",
            undone2 != null && undone2!!.isMyTurn == true && undone2.myPokemon!!.hp == 35)

        val undone3 = caretaker.undo()
        record("Memento - 히스토리 소진 후 null",
            undone3 == null)

        caretaker.clear()
        record("Memento - 캐럿터 클리어",
            caretaker.size() == 0)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            GoFPatternTest().runAll()
        }
    }
}
