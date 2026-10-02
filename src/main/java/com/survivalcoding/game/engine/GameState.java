package com.survivalcoding.game.engine;

import com.survivalcoding.game.entity.GameEntity;
import com.survivalcoding.game.entity.Hero;
import com.survivalcoding.game.entity.Monster;
import com.survivalcoding.game.animation.ParticleEffect;
import com.survivalcoding.game.audio.SoundManager;
import com.survivalcoding.game.input.InputHandler;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Holds all game state: entities, camera, game mode, etc.
 */
public class GameState {
    
    public enum GameMode {
        MAIN_MENU,
        PLAYING,
        PAUSED,
        GAME_OVER,
        VICTORY
    }
    
    private GameMode currentMode = GameMode.MAIN_MENU;
    
    // Entities
    private Hero hero;
    private final List<Monster> monsters = new ArrayList<>();
    private final List<GameEntity> entities = new ArrayList<>();
    private final List<GameEntity> projectiles = new ArrayList<>();
    private final List<ParticleEffect> particles = new ArrayList<>();
    
    // Camera
    private Point2D cameraPosition = new Point2D(0, 0);
    private double cameraShake = 0;
    private double cameraShakeTimer = 0;
    
    // Game stats
    private int score = 0;
    private int wave = 1;
    private int monstersKilled = 0;
    private double gameTime = 0;
    
    // UI state
    private boolean showFps = false;
    private double fps = 0.0;
    
    // Screen dimensions
    private double screenWidth = 1280;
    private double screenHeight = 720;
    
    // World bounds
    private final double worldWidth = 3000;
    private final double worldHeight = 2000;
    
    // Random for effects
    private final Random random = new Random();
    
    public GameState() {
        initializeHero();
    }
    
    private void initializeHero() {
        hero = new Hero("준석이", 100, 100);
        hero.setPosition(screenWidth / 2, screenHeight / 2);
        entities.add(hero);
    }
    
    public void update(double deltaTime, InputHandler input) {
        gameTime += deltaTime;
        
        // Handle global toggles
        if (input.isFpsTogglePressed()) {
            showFps = !showFps;
        }
        
        if (input.isMuteTogglePressed()) {
            SoundManager.get().toggleMute();
        }
        
        // Handle camera shake
        if (cameraShakeTimer > 0) {
            cameraShakeTimer -= deltaTime;
            cameraShake = Math.max(0, cameraShake - deltaTime * 10);
        }
        
        switch (currentMode) {
            case PLAYING -> updatePlaying(deltaTime, input);
            case MAIN_MENU -> updateMenu(input);
            case PAUSED -> updatePaused(input);
            case GAME_OVER, VICTORY -> updateGameOver(input);
        }
        
        // Update camera to follow hero
        updateCamera(deltaTime);
        
        // Clean up dead entities
        cleanupEntities();
    }
    
    private void updatePlaying(double deltaTime, InputHandler input) {
        // Toggle pause when requested (ESC). Early return to freeze gameplay updates.
        if (input.isPausePressed()) {
            currentMode = GameMode.PAUSED;
            return;
        }
        
        // Update hero
        if (hero != null && hero.isAlive()) {
            hero.update(deltaTime, input, this);
        }
        
        // Update monsters
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                monster.update(deltaTime, input, this);
            }
        }
        
        // Update projectiles
        for (GameEntity projectile : projectiles) {
            projectile.update(deltaTime, input, this);
        }
        
        // Update particles
        for (int i = particles.size() - 1; i >= 0; i--) {
            ParticleEffect particle = particles.get(i);
            particle.update(deltaTime);
            if (particle.isDead()) {
                particles.remove(i);
            }
        }
        
        // Check win condition
        if (wave > 10 && monsters.isEmpty()) {
            currentMode = GameMode.VICTORY;
            return;
        }
        
        // Spawn monsters if needed
        if (monsters.isEmpty() && currentMode == GameMode.PLAYING) {
            spawnWave();
        }
        
        // Check lose condition
        if (hero != null && !hero.isAlive()) {
            currentMode = GameMode.GAME_OVER;
        }
    }
    
    private void updateMenu(InputHandler input) {
        if (input.isActionPressed()) {
            SoundManager.get().play(SoundManager.Effect.MENU);
            currentMode = GameMode.PLAYING;
            resetGame();
        }
    }
    
    private void updatePaused(InputHandler input) {
        // Resume on ESC (pause toggle) or ENTER (action)
        if (input.isPausePressed() || input.isActionPressed()) {
            currentMode = GameMode.PLAYING;
        }
    }
    
    private void updateGameOver(InputHandler input) {
        if (input.isActionPressed()) {
            currentMode = GameMode.MAIN_MENU;
            resetGame();
        }
    }
    
    private void updateCamera(double deltaTime) {
        if (hero == null) return;
        
        // Smooth camera follow
        double targetX = hero.getX() - screenWidth / 2;
        double targetY = hero.getY() - screenHeight / 2;
        
        // Clamp to world bounds
        targetX = Math.max(0, Math.min(targetX, worldWidth - screenWidth));
        targetY = Math.max(0, Math.min(targetY, worldHeight - screenHeight));
        
        // Smooth interpolation
        cameraPosition = cameraPosition.interpolate(new Point2D(targetX, targetY), 0.1);
        
        // Apply camera shake
        if (cameraShake > 0) {
            double shakeX = (random.nextDouble() - 0.5) * cameraShake * 20;
            double shakeY = (random.nextDouble() - 0.5) * cameraShake * 20;
            cameraPosition = cameraPosition.add(shakeX, shakeY);
        }
    }
    
    private void spawnWave() {
        wave++;
        int monsterCount = Math.min(3 + wave, 15);
        
        for (int i = 0; i < monsterCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = 400 + random.nextDouble() * 300;
            double spawnX = hero.getX() + Math.cos(angle) * distance;
            double spawnY = hero.getY() + Math.sin(angle) * distance;
            
            // Clamp to world bounds
            spawnX = Math.max(50, Math.min(spawnX, worldWidth - 50));
            spawnY = Math.max(50, Math.min(spawnY, worldHeight - 50));
            
            Monster monster = createRandomMonster(spawnX, spawnY);
            monster.scaleForDifficulty(wave);
            monsters.add(monster);
            entities.add(monster);
        }
    }
    
    private Monster createRandomMonster(double x, double y) {
        int type = random.nextInt(4);
        return switch (type) {
            case 0 -> new Monster("슬라임", 30, 5, x, y, Monster.MonsterType.SLIME);
            case 1 -> new Monster("포이즌 슬라임", 40, 8, x, y, Monster.MonsterType.POISON_SLIME);
            case 2 -> new Monster("좀비", 60, 12, x, y, Monster.MonsterType.ZOMBIE);
            default -> new Monster("킹 슬라임", 100, 15, x, y, Monster.MonsterType.KING_SLIME);
        };
    }
    
    private void cleanupEntities() {
        // Remove dead monsters
        monsters.removeIf(m -> !m.isAlive());
        entities.removeIf(e -> !e.isAlive());
        projectiles.removeIf(p -> !p.isAlive());
    }
    
    public void resetGame() {
        monsters.clear();
        projectiles.clear();
        particles.clear();
        entities.clear();
        
        score = 0;
        wave = 1;
        monstersKilled = 0;
        gameTime = 0;
        
        initializeHero();
        currentMode = GameMode.PLAYING;
    }
    
    public void addParticle(ParticleEffect particle) {
        particles.add(particle);
    }
    
    public void addProjectile(GameEntity projectile) {
        projectiles.add(projectile);
        entities.add(projectile);
    }
    
    public void addMonster(Monster monster) {
        monsters.add(monster);
        entities.add(monster);
    }
    
    public void addScore(int points) {
        score += points;
    }
    
    public void incrementMonstersKilled() {
        monstersKilled++;
    }
    
    public void shakeCamera(double intensity) {
        cameraShake = intensity;
        cameraShakeTimer = 0.3;
    }
    
    // Getters
    public GameMode getCurrentMode() { return currentMode; }
    public Hero getHero() { return hero; }
    public List<Monster> getMonsters() { return new ArrayList<>(monsters); }
    public List<GameEntity> getEntities() { return new ArrayList<>(entities); }
    public boolean isShowFps() { return showFps; }
    public void setShowFps(boolean show) { this.showFps = show; }
    public double getFps() { return fps; }
    public void setFps(double fps) { this.fps = fps; }
    public List<GameEntity> getProjectiles() { return new ArrayList<>(projectiles); }
    public List<ParticleEffect> getParticles() { return new ArrayList<>(particles); }
    public Point2D getCameraPosition() { return cameraPosition; }
    public int getScore() { return score; }
    public int getWave() { return wave; }
    public int getMonstersKilled() { return monstersKilled; }
    public double getGameTime() { return gameTime; }
    public double getScreenWidth() { return screenWidth; }
    public double getScreenHeight() { return screenHeight; }
    public double getWorldWidth() { return worldWidth; }
    public double getWorldHeight() { return worldHeight; }
    
    public void setScreenSize(double width, double height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }
}
