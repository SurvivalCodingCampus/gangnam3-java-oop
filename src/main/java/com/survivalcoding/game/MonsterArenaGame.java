package com.survivalcoding.game;

import com.survivalcoding.game.engine.GameEngine;
import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import com.survivalcoding.game.renderer.GameFonts;
import com.survivalcoding.game.renderer.GameRenderer;
import com.survivalcoding.game.battle.BattleSystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main game application - Monster Arena Battle.
 * A real-time action RPG with keyboard controls, animations, and particle effects.
 */
public class MonsterArenaGame extends Application {
    
    // Game constants
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final String GAME_TITLE = "몬스터 아레나 배틀";
    
    // Core systems
    private GameEngine gameEngine;
    private GameState gameState;
    private InputHandler inputHandler;
    private GameRenderer gameRenderer;
    private BattleSystem battleSystem;
    
    // JavaFX components
    private Canvas canvas;
    private Stage primaryStage;
    private Scene scene;
    
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
GameFonts.load();

        // Initialize core systems
        initializeSystems();
        
        // Setup JavaFX
        setupJavaFX();
        
        // Start game
        startGame();
        
        // Show window
        primaryStage.show();
        
        // Request focus for input
        canvas.requestFocus();
    }
    
    private void initializeSystems() {
        // Create canvas for rendering
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Initialize systems in dependency order
        gameState = new GameState();
        gameState.setScreenSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        inputHandler = new InputHandler();
        gameRenderer = new GameRenderer(canvas);
        battleSystem = gameState.getBattleSystem();
        gameEngine = new GameEngine(gameRenderer, inputHandler, gameState);
        
        // Set canvas reference in renderer for size access
        gameRenderer.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    }
    
    private void setupJavaFX() {
        // Create scene
        StackPane root = new StackPane(canvas);
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Initialize input handler with scene
        inputHandler.initialize(scene);
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F11) {
                toggleFullscreen();
                event.consume();
            }
        });
        
        // Stage setup
        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setOnCloseRequest(e -> shutdown());
        
        // Center on screen
        primaryStage.centerOnScreen();
    }
    
    private void toggleFullscreen() {
        primaryStage.setFullScreen(!primaryStage.isFullScreen());
    }
    
    private void startGame() {
        // Start game engine (fixed timestep update loop)
        gameEngine.start();
    }
    
    private void shutdown() {
        // Stop loops
        if (gameEngine != null) {
            gameEngine.stop();
        }

        if (gameState != null) {
            gameState.saveBestScore();
        }

        // Exit
        Platform.exit();
        System.exit(0);
    }
    
    /**
     * Main entry point.
     */
    static void main(String[] args) {
        // JavaFX requires this to be called on the FX Application Thread
        launch(args);
    }
    
    // Getters for testing/debugging
    public GameEngine getGameEngine() { return gameEngine; }
    public GameState getGameState() { return gameState; }
    public InputHandler getInputHandler() { return inputHandler; }
    public GameRenderer getGameRenderer() { return gameRenderer; }
    public BattleSystem getBattleSystem() { return battleSystem; }
}