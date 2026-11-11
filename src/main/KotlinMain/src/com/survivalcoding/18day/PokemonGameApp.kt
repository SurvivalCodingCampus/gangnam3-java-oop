package com.survivalcoding.`18day`

import com.survivalcoding.`18day`.battle.DefaultTypeEffectivenessStrategy
import com.survivalcoding.`18day`.battle.PokemonBattleService
import com.survivalcoding.`18day`.battle.TypeEffectivenessStrategy
import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource
import com.survivalcoding.`18day`.Data_source.PokemonDataSource
import com.survivalcoding.`18day`.audio.SoundManager
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.game.GameController
import com.survivalcoding.`18day`.game.GameModel
import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.adapter.PokeApiPokemonAdapter
import com.survivalcoding.`18day`.pattern.command.BattleCommandResult
import com.survivalcoding.`18day`.pattern.decorator.*
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory.DataSourceKind
import com.survivalcoding.`18day`.pattern.facade.GameFacade
import com.survivalcoding.`18day`.pattern.observer.BattleEvent
import com.survivalcoding.`18day`.pattern.observer.BattleObserver
import com.survivalcoding.`18day`.pattern.proxy.CachingDataSourceProxy
import com.survivalcoding.`18day`.pattern.state.BattleContext
import com.survivalcoding.`18day`.pattern.state.*
import com.survivalcoding.`18day`.repostory.IPokemonBatchRepository
import com.survivalcoding.`18day`.repostory.IPokemonCacheManager
import com.survivalcoding.`18day`.repostory.IPokemonRepository
import com.survivalcoding.`18day`.repostory.ICloseableResource
import com.survivalcoding.`18day`.repostory.PokemonRepositoryImpl
import javafx.animation.*
import javafx.util.Duration
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import javafx.stage.StageStyle

/**
 * [DIP + GoF 패턴] 포켓몬 배틀 게임 JavaFX 애플리케이션 진입점.
 *
 * SOLID 원칙 준수:
 * - SRP: UI 표시 및 생명주기만 담당, 게임 로직은 GameController/GameFacade 위임
 * - DIP: 구체 클래스가 아닌 인터페이스(IPokemonRepository 등)에 의존
 * - OCP: TypeEffectivenessStrategy 주입 가능, 새 전략 추가 시 수정 불필요
 * - ISP: 분리된 인터페이스만 사용
 *
 * [GoF 디자인 패턴 적용]
 * - Factory: PokemonRepositoryFactory로 데이터 소스/저장소 생성 캡슐화
 * - State: BattleContext/상태 객체로 배틀 단계 관리 (UI에 상태명 표시)
 * - Observer: BattleObserver로 배틀 이벤트 수신 (UI, 로그, 사운드 분리)
 * - Command: GameController의 BattleCommandInvoker로 공격/초기화 캡슐화
 * - Decorator: SoundDecorator로 사운드 재생에 볼륨/루프/페이드 책임 동적 추가
 * - Facade: GameFacade로 복잡한 서브시스템 통합
 * - Proxy: CachingDataSourceProxy로 원격 데이터 소스 캐싱
 * - Adapter: PokeApiPokemonAdapter로 PokeAPI DTO↔도메인 모델 인터페이스 불일치 해소
 * - Memento: GameController의 BattleMementoCaretaker로 상태 스냅샷/복원
 *
 * 연결 구조:
 * PokemonGameApp → GameFacade → GameController → IPokemonRepository → PokemonRepositoryImpl →
 *   (CachingDataSourceProxy →) (FakePokemonDataSource | RemotePokemonDataSource) → PokeAPI
 *                                      → PokemonBattleService → TypeEffectivenessStrategy
 */
class PokemonGameApp : Application(), BattleObserver {

    private lateinit var primaryStage: Stage

    // [DIP] 인터페이스에 의존 (구체 클래스 아님)
    private val typeStrategy: TypeEffectivenessStrategy = DefaultTypeEffectivenessStrategy()
    private val battleService = PokemonBattleService(typeStrategy)

    // [GoF Factory] 팩토리로 저장소 생성 (구체 클래스 의존 제거)
    private val repository: IPokemonRepository =
        PokemonRepositoryFactory.createRepository(
            CachingDataSourceProxy.create(
                FakePokemonDataSource() as PokemonDataSource,
                maxCacheSize = 50
            )
        )

    private val controller = GameController(
        gameModel = GameModel(),
        pokemonRepository = repository,
        battleService = battleService,
        batchRepository = repository as? IPokemonBatchRepository,
        cacheManager = repository as? IPokemonCacheManager,
        closeableResource = repository as? ICloseableResource
    )

    // [GoF Facade] 퍼사드 (선택적 고수준 API)
    private val facade = GameFacade.create()

    // [GoF Observer] 이 컨트롤러 자신을 BattleObserver로 등록
    init {
        controller.registerObserver(this)
    }

    // UI 컴포넌트
    private lateinit var splashStage: Stage
    private lateinit var myPokemonImageView: ImageView
    private lateinit var opponentPokemonImageView: ImageView
    private lateinit var myNameLabel: Label
    private lateinit var myTypeLabel: Label
    private lateinit var myHpLabel: Label
    private lateinit var myStatsLabel: Label
    private lateinit var myAbilityLabel: Label
    private lateinit var myHpProgress: ProgressBar
    private lateinit var opponentNameLabel: Label
    private lateinit var opponentTypeLabel: Label
    private lateinit var opponentHpLabel: Label
    private lateinit var opponentStatsLabel: Label
    private lateinit var opponentAbilityLabel: Label
    private lateinit var opponentHpProgress: ProgressBar
    private lateinit var battleLog: TextArea
    private lateinit var statusLabel: Label
    private lateinit var soundStatusLabel: Label
    private lateinit var dataSourceLabel: Label
    private lateinit var turnLabel: Label
    private lateinit var stateLabel: Label // [GoF State] 현재 배틀 상태 표시
    private lateinit var battlePanel: VBox // 배틀 패널 (초기에는 숨김)

    // [SRP] 원색 컬러 팔레트
    private val COLOR_BG = Color.web("#FFEB3B")
    private val COLOR_RED = Color.web("#F44336")
    private val COLOR_BLUE = Color.web("#2196F3")
    private val COLOR_GREEN = Color.web("#4CAF50")
    private val COLOR_ORANGE = Color.web("#FF9800")

    // [한글 표시] 포켓몬 영어 이름 → 한글 이름 매핑
    private val pokemonKoreanNames = mapOf(
        "pikachu" to "피카츄",
        "charizard" to "리자몽",
        "bulbasaur" to "이상해씨",
        "squirtle" to "꼬부기",
        "jigglypuff" to "푸푸린",
        "meowth" to "야옹이",
        "psyduck" to "마시땅",
        "snorlax" to "잠만보"
    )

    private val pokemonNames = listOf("pikachu", "charizard", "bulbasaur", "squirtle", "jigglypuff", "meowth", "psyduck", "snorlax")


    private var victoryScaleTransition: ScaleTransition? = null

    // [GoF Decorator] 사운드 데코레이터 체인
    private lateinit var soundPlayer: ISoundPlayer

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
        primaryStage.title = "🎮 포켓몬 배틀 게임! 🎮"

        // [GoF Decorator] 데코레이터 체인 구성: 볼륨 0.6 → 페이드인 → 루프 강제 안 함
        soundPlayer = VolumeDecorator(
            FadeInDecorator(
                SoundManagerAdapter(),
                targetVolume = 0.7,
                startVolume = 0.2
            ),
            volume = 0.6
        )

        controller.setViewListener(object : GameController.ViewListener {
            override fun onPokemonLoaded(isMy: Boolean, pokemon: Pokemon) {
                updatePokemonUI(isMy, pokemon)
            }

            override fun onLoadError(message: String) {
                statusLabel.text = message
            }

            override fun onBattleReady() {
                // [게임 흐름] 양팀 포켓몬 선택 완료 → 배틀 화면 표시
                battlePanel.setVisible(true)
                battlePanel.setManaged(true)
                updateTurnUI()
                updateStateUI()
                statusLabel.text = "⚔ 배틀 시작! 내 차례예요!"
                soundPlayer.play(controller.getMyPokemon()?.getAvailableSoundUrl(), loop = false)
            }

            override fun onTurnChanged(isMyTurn: Boolean) {
                updateTurnUI()
                updateStateUI()
            }

            override fun onAttackExecuted(message: String, isMyTurn: Boolean) {
                addLog(if (isMyTurn) "🔵 $message" else "🔴 $message")
                statusLabel.text = message
            }

            override fun onPokemonUpdated(isMy: Boolean, pokemon: Pokemon) {
                updatePokemonUI(isMy, pokemon)
            }

            override fun onBattleEnded(isMyVictory: Boolean, winnerName: String) {
                handleBattleEnded(isMyVictory, winnerName)
            }

            override fun onStatusChanged(message: String) {
                statusLabel.text = message
            }

            override fun onBattleReset() {
                myPokemonImageView.image = null
                opponentPokemonImageView.image = null
                myNameLabel.text = "-"
                myTypeLabel.text = "-"
                myHpLabel.text = "-"
                myStatsLabel.text = "-"
                myAbilityLabel.text = "-"
                myHpProgress.progress = 0.0
                opponentNameLabel.text = "-"
                opponentTypeLabel.text = "-"
                opponentHpLabel.text = "-"
                opponentStatsLabel.text = "-"
                opponentAbilityLabel.text = "-"
                opponentHpProgress.progress = 0.0
                turnLabel.text = ""
                stateLabel.text = ""
                battleLog.clear()
                soundStatusLabel.text = ""
                SoundManager.stop()
            }
        })

        showSplashScreen()
    }

    // [GoF Observer] BattleObserver 구현 - 컨트롤러의 BattleObservable로부터 이벤트 수신
    override fun onBattleEvent(event: BattleEvent) {
        when (event) {
            is BattleEvent.PokemonLoaded -> updatePokemonUI(event.isMy, event.pokemon)
            is BattleEvent.LoadError -> statusLabel.text = event.message
            is BattleEvent.BattleReady -> {
                // [게임 흐름] 양팀 포켓몬 선택 완료 → 배틀 화면 표시
                battlePanel.setVisible(true)
                battlePanel.setManaged(true)
                updateTurnUI()
                updateStateUI()
                statusLabel.text = "⚔ 배틀 시작! 내 차례예요!"
                soundPlayer.play(controller.getMyPokemon()?.getAvailableSoundUrl(), loop = false)
            }
            is BattleEvent.TurnChanged -> {
                updateTurnUI()
                updateStateUI()
            }
            is BattleEvent.AttackExecuted -> {
                addLog(if (event.isMyTurn) "🔵 ${event.message}" else "🔴 ${event.message}")
                statusLabel.text = event.message
            }
            is BattleEvent.PokemonUpdated -> updatePokemonUI(event.isMy, event.pokemon)
            is BattleEvent.BattleEnded -> handleBattleEnded(event.isMyVictory, event.winnerName)
            is BattleEvent.StatusChanged -> statusLabel.text = event.message
            is BattleEvent.BattleReset -> handleBattleReset()
        }
    }

    private fun showSplashScreen() {
        splashStage = Stage(StageStyle.UNDECORATED)
        splashStage.title = "포켓몬 게임 로딩 중..."

        val layout = StackPane().apply {
            alignment = Pos.CENTER
            prefWidth = 400.0
            prefHeight = 300.0
            style = "-fx-background-color: linear-gradient(to bottom, #2196F3, #1976D2); -fx-background-radius: 25;"
        }

        val loadingImageView = ImageView().apply {
            setPreserveRatio(true)
            setFitWidth(90.0)
            setFitHeight(90.0)
        }
        var imageLoaded = false
        try {
            val icoPath = "C:/Users/USER/OneDrive/바탕 화면/pikachu.ico"
            val image = Image("file:///$icoPath", 130.0, 130.0, true, true)
            loadingImageView.image = image
            imageLoaded = true
        } catch (_: Exception) {
        }

        if (imageLoaded) {
            layout.children.add(loadingImageView)
        } else {
            val ball = Circle(60.0).apply {
                fill = Color.web("#FFEB3B")
                stroke = Color.web("#F44336")
                strokeWidth = 5.0
                effect = DropShadow(15.0, Color.rgb(0, 0, 0, 0.5))
            }
            val inner = Circle(25.0).apply {
                fill = Color.web("#FFFFFF")
                effect = DropShadow(8.0, Color.rgb(0, 0, 0, 0.4))
            }
            layout.children.addAll(ball, inner)
        }

        val titleLabel = Label("⚡ 포켓몬 배틀 게임 ⚡").apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.WHITE
            setTranslateY(-60.0)
            effect = DropShadow()
        }

        val countdownLabel = Label("3").apply {
            font = Font.font("Arial", FontWeight.BOLD, 120.0)
            textFill = Color.web("#F44336")
            setTranslateY(25.0)
            effect = DropShadow()
        }

        val statusLabel = Label("게임 시작 준비 중...").apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.web("#FFEB3B")
            setTranslateY(90.0)
            effect = DropShadow()
        }

        layout.children.addAll(titleLabel, countdownLabel, statusLabel)

        splashStage.scene = Scene(layout, 400.0, 300.0)
        splashStage.show()

        var count = 3
        val timeline = Timeline()
        timeline.keyFrames.add(
            KeyFrame(Duration.seconds(1.0), { _ ->
                count--
                when {
                    count > 0 -> {
                        countdownLabel.text = count.toString()
                    }
                    count == 0 -> {
                        countdownLabel.text = "게임 시작!"
                    }
                    else -> {
                        timeline.stop()
                        Platform.runLater {
                            splashStage.close()
                            initializeMainGame()
                        }
                    }
                }
            }, *emptyArray<KeyValue>())
        )
        timeline.setCycleCount(4)
        timeline.play()
    }

    private fun initializeMainGame() {
        val root = VBox(4.0).apply {
            alignment = Pos.CENTER
            padding = Insets(5.0)
            style = "-fx-background-color: #FFEB3B;"
        }

        // 배틀 패널 생성 후 초기에는 숨김 (포켓몬 선택만 보이게 함)
        battlePanel = createBattlePanel()
        battlePanel.setVisible(false)
        battlePanel.setManaged(false)

        root.children.addAll(
            createTitle(),
            createControlPanel(),
            createPokemonSelectionPanel(),
            battlePanel
        )

        primaryStage.scene = Scene(root, 1200.0, 900.0)
        primaryStage.setFullScreen(true) // 게임 시작 시 전체화면
        primaryStage.show()
    }

    override fun stop() {
        controller.close()
        SoundManager.stop()
        super.stop()
        System.exit(0) // 게임 종료 시 커맨드 창(서버 콘솔)도 함께 닫힘
    }

    // ============================================================
    // UI 컴포넌트 생성 (SRP: UI 표시만 담당)
    // ============================================================

    private fun createTitle(): Label {
        return Label("⚡ 포켓몬 배틀 게임! ⚡").apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.web("#F44336")
            effect = DropShadow(10.0, Color.rgb(0, 0, 0, 0.4))
        }
    }

    private fun createControlPanel(): HBox {
        val label = Label("⚙️ 게임 설정").apply {
            font = Font.font("Arial", FontWeight.BOLD, 20.0)
            textFill = Color.web("#1976D2")
        }

        val fakeBtn = Button("📦 가짜 데이터").apply {
            setPrefSize(90.0, 30.0)
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            style = kidButtonStyle(COLOR_GREEN)
            setOnAction {
                switchDataSource("가짜 데이터", FakePokemonDataSource(), Color.DARKGREEN, "가짜 데이터를 쓸게요!", "📦 가짜 데이터 선택!")
            }
        }

        val remoteBtn = Button("🌐 원격 PokeAPI").apply {
            setPrefSize(90.0, 30.0)
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            style = kidButtonStyle(COLOR_BLUE)
            setOnAction {
                statusLabel.text = "원격 PokeAPI는 추후 지원 예정입니다."
                addLog("🌐 원격 PokeAPI (준비 중)")
            }
        }

        val refreshBtn = Button("🔄 새로고침").apply {
            setPrefSize(85.0, 30.0)
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            style = kidButtonStyle(COLOR_ORANGE)
            setOnAction {
                controller.resetBattle()
                statusLabel.text = "게임이 초기화됐어요!"
                addLog("🔄 새로고침!")
            }
        }

        val undoBtn = Button("↩️ 실행취소").apply {
            setPrefSize(85.0, 30.0)
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            style = kidButtonStyle(COLOR_BLUE)
            setOnAction {
                if (controller.undoLastBattleState()) {
                    statusLabel.text = "배틀 상태를 복원했어요!"
                    addLog("↩️ 실행 취소!")
                } else {
                    statusLabel.text = "복원할 상태가 없어요."
                    addLog("↩️ 복원할 상태 없음")
                }
            }
        }

        dataSourceLabel = Label().apply {
            font = Font.font("Arial", FontWeight.BOLD, 20.0)
        }

        stateLabel = Label().apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.GRAY
        }

        val leftBox = HBox(10.0, fakeBtn, remoteBtn).apply {
            alignment = Pos.CENTER
            padding = Insets(8.0)
            style = "-fx-background-color: #E3F2FD; -fx-background-radius: 12; -fx-border-color: #2196F3; -fx-border-width: 3; -fx-border-radius: 12;"
        }

        val rightBox = VBox(6.0, dataSourceLabel, stateLabel, refreshBtn, undoBtn).apply {
            alignment = Pos.CENTER
            padding = Insets(5.0)
        }

        return HBox(8.0, leftBox, rightBox).apply {
            alignment = Pos.CENTER
            padding = Insets(8.0)
            style = "-fx-background-color: #FFF176; -fx-background-radius: 12; -fx-border-color: #F44336; -fx-border-width: 3; -fx-border-radius: 12;"
        }
    }

    private fun switchDataSource(
        label: String,
        dataSource: com.survivalcoding.`18day`.Data_source.PokemonDataSource,
        textFill: Color,
        statusMsg: String,
        logMsg: String
    ) {
        // [GoF Factory] 팩토리로 새 저장소 생성
        val newRepo = PokemonRepositoryFactory.createRepository(dataSource)
        // 컨트롤러는 이미 생성됨 - 새 repository로 교체하려면 새 controller 필요
        // 여기서는 UI 라벨만 업데이트 (실제 교체는 추후 확장 가능)
        dataSourceLabel.text = "데이터 소스: $label"
        dataSourceLabel.textFill = textFill
        statusLabel.text = statusMsg
        addLog(logMsg)
    }

    private fun createPokemonSelectionPanel(): VBox {
        val label = Label("🎯 포켓몬을 선택해요!").apply {
            font = Font.font("Arial", FontWeight.BOLD, 20.0)
            textFill = Color.web("#1976D2")
        }

        val myButtonBox = createPokemonButtonBox(COLOR_BLUE) { name ->
            controller.selectMyPokemon(name)
        }
        val opponentButtonBox = createPokemonButtonBox(COLOR_RED) { name ->
            controller.selectOpponentPokemon(name)
        }

        val myBox = VBox(6.0, Label("💙 내 포켓몬").apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.web("#1976D2")
        }, myButtonBox).apply {
            alignment = Pos.CENTER
            padding = Insets(5.0)
            style = "-fx-background-color: #BBDEFB; -fx-background-radius: 12; -fx-border-color: #2196F3; -fx-border-width: 3; -fx-border-radius: 12;"
            maxWidth = Double.MAX_VALUE
        }

        val opponentBox = VBox(6.0, Label("❤️ 상대 포켓몬").apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.web("#F44336")
        }, opponentButtonBox).apply {
            alignment = Pos.CENTER
            padding = Insets(5.0)
            style = "-fx-background-color: #FFCDD2; -fx-background-radius: 12; -fx-border-color: #F44336; -fx-border-width: 3; -fx-border-radius: 12;"
            maxWidth = Double.MAX_VALUE
        }

        val selectionHBox = HBox(12.0, myBox, opponentBox).apply {
            alignment = Pos.CENTER
            padding = Insets(5.0)
        }

        return VBox(5.0, label, selectionHBox).apply {
            alignment = Pos.CENTER
            padding = Insets(5.0)
            style = "-fx-background-color: #FFF9C4; -fx-background-radius: 12; -fx-border-color: #FFEB3B; -fx-border-width: 3; -fx-border-radius: 12;"
            maxWidth = Double.MAX_VALUE
        }
    }

    private fun createPokemonButtonBox(buttonColor: Color, onSelect: (String) -> Unit): FlowPane {
        return FlowPane(4.0, 10.0).apply {
            alignment = Pos.CENTER
            padding = Insets(8.0)
            style = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: ${toHex(buttonColor)}; -fx-border-width: 3; -fx-border-radius: 12;"
            hgap = 15.0
            vgap = 15.0
            maxWidth = Double.MAX_VALUE

            for (name in pokemonNames) {
                // [캐릭터 표시] 포켓몬 이미지를 버튼 graphic으로 설정 (텍스트 제거)
                val pokemonImageView = ImageView().apply {
                    setPreserveRatio(true)
                    setFitWidth(60.0)
                    setFitHeight(60.0)
                }

                // 이미지 로드 시도
                try {
                    val result = repository.getPokemon(name)
                    if (result is Result.Success && result.value.hasImage()) {
                        val image = Image(result.value.imageUrl!!, 60.0, 60.0, true, true)
                        pokemonImageView.image = image
                    }
                } catch (_: Exception) {
                    // 이미지 로드 실패 시 기본 이미지 없음
                }

                val btn = Button().apply {
                    graphic = pokemonImageView
                    setPrefSize(80.0, 90.0)
                    setMinSize(80.0, 90.0)
                    style = "-fx-background-color: transparent; -fx-border-color: transparent; -fx-background-radius: 12; -fx-border-radius: 12;"
                    setOnMouseEntered { e ->
                        style = "-fx-background-color: ${toHex(Color.rgb(255, 255, 255, 0.3))}; -fx-border-color: ${toHex(buttonColor)}; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12;"
                    }
                    setOnMouseExited { e ->
                        style = "-fx-background-color: transparent; -fx-border-color: transparent; -fx-background-radius: 12; -fx-border-radius: 12;"
                    }
                    setOnAction {
                        onSelect(name)
                    }
                }

                // 이미지 아래에 포켓몬 이름 라벨 (작게)
                val koreanName = pokemonKoreanNames[name] ?: name.replaceFirstChar { it.uppercase() }
                val nameLabel = Label(koreanName).apply {
                    font = Font.font("Arial", FontWeight.BOLD, 12.0)
                    alignment = Pos.CENTER
                    maxWidth = 80.0
                    setWrapText(true)
                    textFill = Color.web("#1976D2")
                }

                val pokemonBox = VBox(4.0, btn, nameLabel).apply {
                    alignment = Pos.CENTER
                }

                children.add(pokemonBox)
            }
        }
    }

    private fun kidButtonStyle(color: Color): String {
        return "-fx-background-color: ${toHex(color)}; -fx-text-fill: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 3);"
    }

    private fun toHex(color: Color): String {
        return String.format("#%02X%02X%02X",
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt())
    }

    private fun createBattlePanel(): VBox {
        val label = Label("⚔️ 배틀 화면!").apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.web("#F44336")
        }

        val battleField = HBox(12.0).apply {
            alignment = Pos.CENTER

            val myInfo = createPokemonInfoPanel(
                title = "💙 내 포켓몬",
                titleColor = Color.web("#1976D2"),
                bgColor = "#BBDEFB",
                borderColor = "#2196F3",
                progressColor = "#4CAF50"
            ) { iv, nl, tl, hl, sl, al, pb ->
                myPokemonImageView = iv
                myNameLabel = nl
                myTypeLabel = tl
                myHpLabel = hl
                myStatsLabel = sl
                myAbilityLabel = al
                myHpProgress = pb
            }

            val vsLabel = Label("VS").apply {
                font = Font.font("Arial", FontWeight.BOLD, 70.0)
                textFill = Color.web("#F44336")
                effect = DropShadow()
            }

            val opponentInfo = createPokemonInfoPanel(
                title = "❤️ 상대 포켓몬",
                titleColor = Color.web("#F44336"),
                bgColor = "#FFCDD2",
                borderColor = "#F44336",
                progressColor = "#F44336"
            ) { iv, nl, tl, hl, sl, al, pb ->
                opponentPokemonImageView = iv
                opponentNameLabel = nl
                opponentTypeLabel = tl
                opponentHpLabel = hl
                opponentStatsLabel = sl
                opponentAbilityLabel = al
                opponentHpProgress = pb
            }

            children.addAll(myInfo, vsLabel, opponentInfo)
        }

        turnLabel = Label().apply {
            font = Font.font("Arial", FontWeight.BOLD, 18.0)
            textFill = Color.web("#FFEB3B")
            alignment = Pos.CENTER
            prefWidth = 550.0
            effect = DropShadow()
        }

        val logBox = VBox(5.0).apply {
            alignment = Pos.CENTER_LEFT
            val battleLogLabel = Label("📝 배틀 로그").apply {
                font = Font.font("Arial", FontWeight.BOLD, 18.0)
                textFill = Color.web("#1976D2")
            }
            battleLog = TextArea().apply {
                prefHeight = 50.0
                prefWidth = 550.0
                setEditable(false)
                setWrapText(true)
                style = "-fx-control-inner-background: #FFFFFF; -fx-border-color: #FFEB3B; -fx-border-radius: 8; -fx-background-radius: 8;"
                font = Font.font("Arial", FontWeight.BOLD, 18.0)
            }
            children.addAll(battleLogLabel, battleLog)
        }

        val buttonBox = HBox(8.0).apply {
            alignment = Pos.CENTER
            padding = Insets(6.0)
            style = "-fx-background-color: #E0E0E0; -fx-background-radius: 18; -fx-border-color: #9E9E9E; -fx-border-width: 3; -fx-border-radius: 18;"

            val soundBtn = Button("𔊼 사운드").apply {
                setPrefSize(150.0, 60.0)
                setMinSize(150.0, 60.0)
                font = Font.font("Arial", FontWeight.BOLD, 20.0)
                style = kidButtonStyle(COLOR_BLUE) +
                        "-fx-effect: dropshadow(three-pass-box, rgba(33,150,243,0.5), 8, 0, 0, 5);"
                setOnMouseEntered { e ->
                    style = "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-background-radius: 15; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(25,118,210,0.6), 12, 0, 0, 6);"
                }
                setOnMouseExited { e ->
                    style = "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 12; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(33,150,243,0.5), 8, 0, 0, 5);"
                }
                setOnAction {
                    playCurrentPokemonSound()
                }
            }

            val attackBtn = Button("⚔ 공격!").apply {
                setPrefSize(180.0, 70.0)
                setMinSize(180.0, 70.0)
                font = Font.font("Arial", FontWeight.BOLD, 18.0)
                style = "-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(244,67,54,0.6), 10, 0, 0, 6);"
                setOnMouseEntered { e ->
                    style = "-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-background-radius: 20; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(244,67,54,0.7), 14, 0, 0, 8);"
                }
                setOnMouseExited { e ->
                    style = "-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 20; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(244,67,54,0.6), 10, 0, 0, 6);"
                }
                setOnAction {
                    controller.performAttack()
                }
            }

            val resetBtn = Button("🔄 초기화").apply {
                setPrefSize(140.0, 65.0)
                setMinSize(140.0, 65.0)
                font = Font.font("Arial", FontWeight.BOLD, 20.0)
                style = kidButtonStyle(COLOR_ORANGE) +
                        "-fx-effect: dropshadow(three-pass-box, rgba(255,152,0,0.5), 8, 0, 0, 5);"
                setOnMouseEntered { e ->
                    style = "-fx-background-color: #FB8C00; -fx-text-fill: white; -fx-background-radius: 15; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(251,140,0,0.6), 12, 0, 0, 6);"
                }
                setOnMouseExited { e ->
                    style = "-fx-background-color: #FF9800; -fx-text-fill: white; -fx-background-radius: 12; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(255,152,0,0.5), 8, 0, 0, 5);"
                }
                setOnAction {
                    controller.resetBattle()
                }
            }

            children.addAll(soundBtn, attackBtn, resetBtn)
        }

        statusLabel = Label().apply {
            font = Font.font("Arial", FontWeight.BOLD, 20.0)
            textFill = Color.web("#F44336")
            alignment = Pos.CENTER
            prefWidth = 550.0
            effect = DropShadow()
        }

        soundStatusLabel = Label().apply {
            font = Font.font("Arial", FontWeight.BOLD, 20.0)
            textFill = Color.GRAY
            alignment = Pos.CENTER
            prefWidth = 550.0
        }

        return VBox(5.0, label, battleField, turnLabel, logBox, buttonBox, statusLabel, soundStatusLabel).apply {
            padding = Insets(6.0)
            style = "-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #FFEB3B; -fx-border-width: 4; -fx-border-radius: 20;"
            prefWidth = 550.0
            effect = DropShadow(12.0, Color.rgb(0, 0, 0, 0.3))
        }
    }

    private fun createPokemonInfoPanel(
        title: String,
        titleColor: Color,
        bgColor: String,
        borderColor: String,
        progressColor: String,
        builder: (ImageView, Label, Label, Label, Label, Label, ProgressBar) -> Unit
    ): VBox {
        return VBox(4.0).apply {
            alignment = Pos.CENTER
            prefWidth = 320.0

            val imageView = ImageView().apply {
                setPreserveRatio(true)
                setFitWidth(160.0)
                setFitHeight(160.0)
            }

            val pane = StackPane().apply {
                alignment = Pos.CENTER
                prefWidth = 200.0
                prefHeight = 280.0
                style = "-fx-background-color: $bgColor; -fx-border-color: $borderColor; -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 2;"
                children.add(imageView)
            }

            val nameLabel = Label().apply {
                font = Font.font("Arial", FontWeight.BOLD, 18.0)
                textFill = titleColor
                alignment = Pos.CENTER
                effect = DropShadow()
            }
            val typeLabel = Label().apply {
                font = Font.font("Arial", FontWeight.BOLD, 18.0)
                alignment = Pos.CENTER
            }
            val hpLabel = Label().apply {
                font = Font.font("Arial", FontWeight.BOLD, 20.0)
                alignment = Pos.CENTER
                textFill = Color.web("#D32F2F")
            }
            val statsLabel = Label().apply {
                font = Font.font("Arial", 18.0)
                alignment = Pos.CENTER
                textFill = Color.DARKGRAY
            }
            val abilityLabel = Label().apply {
                font = Font.font("Arial", FontWeight.BOLD, 18.0)
                alignment = Pos.CENTER
                textFill = Color.GRAY
            }
            val progress = ProgressBar(1.0).apply {
                prefWidth = 200.0
                style = "-fx-accent: $progressColor;"
            }

            builder(imageView, nameLabel, typeLabel, hpLabel, statsLabel, abilityLabel, progress)

            children.addAll(
                Label(title).apply { font = Font.font("Arial", FontWeight.BOLD, 20.0) },
                pane,
                nameLabel, typeLabel, hpLabel, statsLabel, abilityLabel, progress
            )
        }
    }

    // ============================================================
    // UI 업데이트 및 헬퍼 (SRP: UI 표시만 담당)
    // ============================================================

    private fun updatePokemonUI(isMy: Boolean, pokemon: Pokemon) {
        if (isMy) {
            myNameLabel.text = "⭐ ${pokemon.name.replaceFirstChar { it.uppercase() }}"
            myTypeLabel.text = "타입: ${pokemon.type.uppercase()}"
            myHpLabel.text = "체력: ${pokemon.hp} ❤️"
            myStatsLabel.text = "공격력:${pokemon.attack}  방어력:${pokemon.defense}  속도:${pokemon.speed}"
            myAbilityLabel.text = "특성: ${pokemon.abilities.takeIf { it.isNotBlank() } ?: "없음"}"
            myHpProgress.progress = if (pokemon.hp > 0) 1.0 else 0.0
            myHpProgress.style = if (pokemon.isAlive()) "-fx-accent: #4CAF50;" else "-fx-accent: #F44336;"
            setPokemonImage(myPokemonImageView, pokemon)
        } else {
            opponentNameLabel.text = "⭐ ${pokemon.name.replaceFirstChar { it.uppercase() }}"
            opponentTypeLabel.text = "타입: ${pokemon.type.uppercase()}"
            opponentHpLabel.text = "체력: ${pokemon.hp} ❤️"
            opponentStatsLabel.text = "공격력:${pokemon.attack}  방어력:${pokemon.defense}  속도:${pokemon.speed}"
            opponentAbilityLabel.text = "특성: ${pokemon.abilities.takeIf { it.isNotBlank() } ?: "없음"}"
            opponentHpProgress.progress = if (pokemon.hp > 0) 1.0 else 0.0
            opponentHpProgress.style = if (pokemon.isAlive()) "-fx-accent: #F44336;" else "-fx-accent: #4CAF50;"
            setPokemonImage(opponentPokemonImageView, pokemon)
        }
    }

    private fun setPokemonImage(imageView: ImageView, pokemon: Pokemon) {
        if (pokemon.hasImage()) {
            try {
                val image = Image(pokemon.imageUrl!!, 180.0, 180.0, true, true)
                imageView.image = image
            } catch (e: Exception) {
                imageView.image = null
            }
        } else {
            imageView.image = null
        }
    }

    private fun updateTurnUI() {
        turnLabel.text = if (controller.isMyTurn) "🎯 내 차례예요! 👉 공격을 눌러요!" else "🤖 상대 차례예요... ⏳ 기다려요!"
        turnLabel.textFill = if (controller.isMyTurn) COLOR_GREEN else COLOR_RED
    }

    // [GoF State] 현재 배틀 상태를 UI에 표시 (상태는 GameController 소유)
    private fun updateStateUI() {
        stateLabel.text = if (controller.isBattleActive) {
            if (controller.isMyTurn) "📊 상태: 내 차례예요" else "📊 상태: 상대 차례예요"
        } else {
            "📊 상태: 배틀 대기 중"
        }
    }

    private fun playCurrentPokemonSound() {
        val target = if (controller.isMyTurn && controller.getMyPokemon() != null) controller.getMyPokemon()
        else if (!controller.isMyTurn && controller.getOpponentPokemon() != null) controller.getOpponentPokemon()
        else null

        if (target != null) {
            val soundUrl = target.getAvailableSoundUrl()
            if (soundUrl != null) {
                soundPlayer.play(soundUrl, loop = false)
                soundStatusLabel.text = "𔊼 사운드 재생 중: ${target.name}"
            } else {
                soundPlayer.play(null, loop = false)
                soundStatusLabel.text = "𔊼 기본 효과음 (사운드 파일 없음)"
            }
        } else {
            soundStatusLabel.text = "⚠️ 먼저 포켓몬을 선택해 주세요."
        }
    }

    private fun addLog(message: String) {
        battleLog.appendText("$message\n")
        battleLog.selectPositionCaret(battleLog.text.length)
        battleLog.deselect()
    }

    // ============================================================
    // 애니메이션 (SRP: 애니메이션만 담당)
    // ============================================================

    private fun handleBattleReset() {
        myPokemonImageView.image = null
        opponentPokemonImageView.image = null
        myNameLabel.text = "-"
        myTypeLabel.text = "-"
        myHpLabel.text = "-"
        myStatsLabel.text = "-"
        myAbilityLabel.text = "-"
        myHpProgress.progress = 0.0
        opponentNameLabel.text = "-"
        opponentTypeLabel.text = "-"
        opponentHpLabel.text = "-"
        opponentStatsLabel.text = "-"
        opponentAbilityLabel.text = "-"
        opponentHpProgress.progress = 0.0
        turnLabel.text = ""
        stateLabel.text = ""
        battleLog.clear()
        soundStatusLabel.text = ""
        SoundManager.stop()
    }

    private fun handleBattleEnded(isMyVictory: Boolean, winnerName: String) {
        // battleActive는 GameController(GameModel)이 이미 false로 설정함
        if (isMyVictory) {
            statusLabel.text = "🏆 ${winnerName}의 승리!"
            statusLabel.textFill = COLOR_GREEN
            victoryAnimation(myPokemonImageView)
        } else {
            statusLabel.text = "💀 아쉽게 졌어요! ${winnerName}이(가) 이겼어요."
            statusLabel.textFill = COLOR_RED
            victoryAnimation(opponentPokemonImageView)
        }
        updateStateUI()
        SoundManager.stop()
        val winner = if (isMyVictory) controller.getMyPokemon() else controller.getOpponentPokemon()
        soundPlayer.play(winner?.getAvailableSoundUrl(), loop = false)

        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "배틀 종료!"
        alert.headerText = if (isMyVictory) "🏆 승리!" else "💀 패배!"
        alert.contentText = if (isMyVictory) {
            "🎉 ${winnerName}이(가) 이겼어요! 축하해요!"
        } else {
            "😢 상대 포켓몬 ${winnerName}에게 졌어요. 💔 다음에 이겨요! 💪"
        }

        // [게임 흐름] 배틀 종료 후 다음 게임으로 자동 진행
        alert.setOnHidden { _ ->
            proceedToNextGame()
        }

        alert.showAndWait()
    }

    /**
     * [게임 흐름] 다음 게임으로 진행: 배틀 화면 숨김, 포켓몬 선택만 표시, 배틀 상태 초기화
     */
    private fun proceedToNextGame() {
        // 배틀 화면 숨김
        battlePanel.setVisible(false)
        battlePanel.setManaged(false)

        // 배틀 상태 초기화
        controller.resetBattle()

        // UI 초기화
        handleBattleReset()

        statusLabel.text = "🎮 다음 게임을 시작해요! 포켓몬을 선택해 주세요."
        addLog("🔄 다음 게임 시작!")
    }

    private fun attackAnimation(attackerImageView: ImageView, defenderImageView: ImageView) {
        val attackerOriginalX = attackerImageView.translateX
        val attackerOriginalY = attackerImageView.translateY
        val defenderOriginalX = defenderImageView.translateX
        val defenderOriginalY = defenderImageView.translateY

        // 1단계: 양쪽 캐릭터가 중앙(VS 위쪽으로) 돌진 (충돌 지점)
        val rushAttacker = TranslateTransition(Duration.millis(250.0), attackerImageView)
        rushAttacker.fromX = 0.0
        rushAttacker.toX = 70.0   // 오른쪽으로 중앙으로
        rushAttacker.fromY = 0.0
        rushAttacker.toY = -70.0  // 위로(VS 위쪽으로)

        val rushDefender = TranslateTransition(Duration.millis(250.0), defenderImageView)
        rushDefender.fromX = 0.0
        rushDefender.toX = -70.0  // 왼쪽으로 중앙으로
        rushDefender.fromY = 0.0
        rushDefender.toY = -70.0  // 위로(VS 위쪽으로)

        // 2단계: 중앙에서 충돌(흔들림 + 스케일업 = 부딧치는 효과)
        val shakeAttacker = TranslateTransition(Duration.millis(60.0), attackerImageView)
        shakeAttacker.fromX = 70.0
        shakeAttacker.toX = 80.0
        shakeAttacker.setCycleCount(3)
        shakeAttacker.setAutoReverse(true)

        val shakeDefender = TranslateTransition(Duration.millis(60.0), defenderImageView)
        shakeDefender.fromX = -70.0
        shakeDefender.toX = -80.0
        shakeDefender.setCycleCount(3)
        shakeDefender.setAutoReverse(true)

        val collisionScaleAttacker = ScaleTransition(Duration.millis(120.0), attackerImageView)
        collisionScaleAttacker.fromX = 1.0
        collisionScaleAttacker.toX = 1.4
        collisionScaleAttacker.fromY = 1.0
        collisionScaleAttacker.toY = 1.4
        collisionScaleAttacker.setCycleCount(2)
        collisionScaleAttacker.setAutoReverse(true)

        val collisionScaleDefender = ScaleTransition(Duration.millis(120.0), defenderImageView)
        collisionScaleDefender.fromX = 1.0
        collisionScaleDefender.toX = 1.4
        collisionScaleDefender.fromY = 1.0
        collisionScaleDefender.toY = 1.4
        collisionScaleDefender.setCycleCount(2)
        collisionScaleDefender.setAutoReverse(true)

        // 3단계: 원래 자리로 튕겨나감
        val bounceBackAttacker = TranslateTransition(Duration.millis(250.0), attackerImageView)
        bounceBackAttacker.fromX = 70.0
        bounceBackAttacker.toX = 0.0
        bounceBackAttacker.fromY = -70.0
        bounceBackAttacker.toY = 0.0

        val bounceBackDefender = TranslateTransition(Duration.millis(250.0), defenderImageView)
        bounceBackDefender.fromX = -70.0
        bounceBackDefender.toX = 0.0
        bounceBackDefender.fromY = -70.0
        bounceBackDefender.toY = 0.0

        // 애니메이션 체인 연결: 돌진 → 충돌(흔들림+스케일) → 튕겨나감 → 원위치
        rushAttacker.onFinished = {
            shakeAttacker.play()
            collisionScaleAttacker.play()
        }
        rushDefender.onFinished = {
            shakeDefender.play()
            collisionScaleDefender.play()
        }

        collisionScaleAttacker.onFinished = { bounceBackAttacker.play() }
        collisionScaleDefender.onFinished = { bounceBackDefender.play() }

        bounceBackAttacker.onFinished = {
            attackerImageView.translateX = attackerOriginalX
            attackerImageView.translateY = attackerOriginalY
        }
        bounceBackDefender.onFinished = {
            defenderImageView.translateX = defenderOriginalX
            defenderImageView.translateY = defenderOriginalY
        }

        rushAttacker.play()
        rushDefender.play()
    }

    private fun victoryAnimation(winnerImageView: ImageView) {
        val scale = ScaleTransition(Duration.millis(400.0), winnerImageView)
        scale.fromX = 1.0
        scale.toX = 1.5
        scale.fromY = 1.0
        scale.toY = 1.5
        scale.setCycleCount(Animation.INDEFINITE)
        scale.setAutoReverse(true)
        victoryScaleTransition = scale
        scale.play()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(PokemonGameApp::class.java, *args)
        }
    }
}

/**
 * 데이터 소스 유형 열거형
 */
enum class DataSourceType(val label: String) {
    FAKE("가짜 데이터"),
    REMOTE("원격 PokeAPI");
}
