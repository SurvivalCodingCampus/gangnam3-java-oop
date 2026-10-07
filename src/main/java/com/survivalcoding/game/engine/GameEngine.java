package com.survivalcoding.game.engine;

import com.survivalcoding.game.input.InputHandler;
import com.survivalcoding.game.renderer.GameRenderer;
import javafx.animation.AnimationTimer;

/**
 * Core game engine with fixed timestep game loop.
 * Handles the main game loop: update -> render at 60 FPS target.
 */
public class GameEngine {
    
    // Target 60 FPS = 16.67ms per frame
    private static final double TARGET_FPS = 60.0;
    private static final double NANOS_PER_SECOND = 1_000_000_000.0;
    private static final double TARGET_FRAME_TIME = NANOS_PER_SECOND / TARGET_FPS;
    
    private final GameRenderer renderer;
    private final InputHandler inputHandler;
    private final GameState gameState;
    
    private long lastTime;
    private double accumulator = 0.0;
    private boolean running = false;
    private AnimationTimer gameLoop;
    
    // Frame timing stats
    private int frameCount = 0;
    private double fpsTimer = 0.0;
    private double currentFps = 0.0;
    
    public GameEngine(GameRenderer renderer, InputHandler inputHandler, GameState gameState) {
        this.renderer = renderer;
        this.inputHandler = inputHandler;
        this.gameState = gameState;
    }
    
    /**
     * Starts the game loop.
     */
    public void start() {
        if (running) return;
        running = true;
        lastTime = System.nanoTime();
        
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running) {
                    stop();
                    return;
                }
                
                double deltaTime = (now - lastTime) / NANOS_PER_SECOND;
                lastTime = now;
                
                // Cap delta time to prevent spiral of death
                if (deltaTime > 0.25) deltaTime = 0.25;
                
                accumulator += deltaTime;
                fpsTimer += deltaTime;
                frameCount++;
                
                // Fixed timestep updates
                while (accumulator >= TARGET_FRAME_TIME / NANOS_PER_SECOND) {
                    update(TARGET_FRAME_TIME / NANOS_PER_SECOND);
                    accumulator -= TARGET_FRAME_TIME / NANOS_PER_SECOND;
                }
                
                // Render with interpolation
                double alpha = accumulator / (TARGET_FRAME_TIME / NANOS_PER_SECOND);
                gameState.setFps(currentFps);
                if (renderer != null) {
                    renderer.render(gameState, alpha);
                }
                
                // Update FPS counter
                if (fpsTimer >= 1.0) {
                    currentFps = frameCount / fpsTimer;
                    frameCount = 0;
                    fpsTimer = 0.0;
                }
            }
        };
        
        gameLoop.start();
    }
    
    /**
     * Stops the game loop.
     */
    public void stop() {
        running = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
    
    /**
     * Updates game logic at fixed timestep.
     */
    private void update(double deltaTime) {
        // GameState consumes "just pressed" keys, so it must run before the transient states are cleared.
        gameState.update(deltaTime, inputHandler);

        inputHandler.update();
    }
    
    public double getCurrentFps() {
        return currentFps;
    }
    
    public boolean isRunning() {
        return running;
    }
}
