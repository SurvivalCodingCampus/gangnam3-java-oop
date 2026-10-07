package com.survivalcoding.game.renderer;

import com.survivalcoding.game.GameConfig;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.entity.GameEntity;
import com.survivalcoding.game.entity.Hero;
import com.survivalcoding.game.entity.Monster;
import com.survivalcoding.game.entity.Projectile;
import com.survivalcoding.game.animation.ParticleEffect;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Point2D;

import java.util.Random;

/**
 * Main game renderer handling all visual output.
 */
public class GameRenderer {
    
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Random random = new Random();
    
    // Background
    private final double backgroundOffsetX = 0;
    private final double backgroundOffsetY = 0;
    private final double[] starX = new double[100];
    private final double[] starY = new double[100];
    private final double[] starSpeed = new double[100];
    
    // Screen effects
    private double screenFlashTimer = 0;
    private Color screenFlashColor = Color.TRANSPARENT;
    
    // UI fonts
    private final Font titleFont = GameFonts.font(48, true);
    private final Font subtitleFont = GameFonts.font(24, false);
    private final Font uiFont = GameFonts.font(16, true);
    private final Font smallFont = GameFonts.font(14, false);
    private final Font tinyFont = GameFonts.font(11, false);
    private final Font skillFont = GameFonts.font(10, true);
    
    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        initializeBackground();
    }
    
    private void initializeBackground() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        for (int i = 0; i < starX.length; i++) {
            starX[i] = random.nextDouble() * width;
            starY[i] = random.nextDouble() * height;
            starSpeed[i] = 10 + random.nextDouble() * 40;
        }
    }
    
    /**
     * Main render method called every frame.
     */
    public void render(GameState gameState, double alpha) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        // Clear screen
        gc.setFill(Color.web("#1A1A2E"));
        gc.fillRect(0, 0, width, height);
        
        // Get camera position
        Point2D camera = gameState.getCameraPosition();
        double camX = camera.getX();
        double camY = camera.getY();
        
        switch (gameState.getCurrentMode()) {
            case MAIN_MENU -> renderMainMenu(gameState);
            case PLAYING -> renderGameplay(gameState, camX, camY, width, height);
            case PAUSED -> {
                renderGameplay(gameState, camX, camY, width, height);
                renderPauseOverlay();
            }
            case GAME_OVER -> renderGameOver(gameState);
            case VICTORY -> renderVictory(gameState);
        }
        
        // Screen flash effect
        if (screenFlashTimer > 0) {
            gc.setFill(screenFlashColor);
            gc.fillRect(0, 0, width, height);
            screenFlashTimer -= 1.0 / 60.0;
        }

        // Optional FPS overlay
        if (gameState.isShowFps()) {
            gc.setFill(Color.web("#FFFFFF", 0.85));
            gc.setFont(tinyFont);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText(String.format("FPS: %.1f", gameState.getFps()), 10, height - 10);
        }
    }
    
    private void renderGameplay(GameState gameState, double camX, double camY, 
                                double width, double height) {
        // Render background
        renderBackground(camX, camY, width, height);
        
        // Render world grid
        renderWorldGrid(gameState, camX, camY, width, height);
        
        // Render entities (back to front)
        // 1. Dead monsters (fade out)
        renderDeadEntities(gameState, camX, camY);
        
        // 2. Projectiles
        for (GameEntity entity : gameState.getProjectiles()) {
            if (entity.isAlive() && entity instanceof Projectile projectile) {
                projectile.render(gc, camX, camY);
            }
        }
        
        // 3. Monsters
        for (Monster monster : gameState.getMonsters()) {
            if (monster.isAlive()) {
                monster.render(gc, camX, camY);
            }
        }
        
        // 4. Hero (always on top)
        Hero hero = gameState.getHero();
        if (hero != null && hero.isAlive()) {
            hero.render(gc, camX, camY);
        }
        
        // 5. Particles (on top of everything)
        for (ParticleEffect particle : gameState.getParticles()) {
            particle.render(gc, camX, camY);
        }

        for (com.survivalcoding.game.animation.FloatingText text : gameState.getFloatingTexts()) {
            text.render(gc, camX, camY);
        }

        // 6. UI overlay
        renderGameUI(gameState, width, height);
        
        // 7. Minimap
        renderMinimap(gameState, camX, camY, width, height);
    }
    
    private void renderBackground(double camX, double camY, double width, double height) {
        // Parallax background
        double parallaxX = camX * 0.1;
        double parallaxY = camY * 0.1;
        
        // Stars
        gc.setFill(Color.web("#FFFFFF", 0.6));
        for (int i = 0; i < starX.length; i++) {
            double sx = (starX[i] - parallaxX) % width;
            double sy = (starY[i] - parallaxY) % height;
            if (sx < 0) sx += width;
            if (sy < 0) sy += height;
            gc.fillOval(sx, sy, 1, 1);
        }
        
        // Far mountains/terrain hint
        gc.setFill(Color.web("#0F0F1E"));
        for (int i = 0; i < 5; i++) {
            double mx = (i * 400 - parallaxX * 0.5) % (width + 400) - 200;
            double my = height - 100 - parallaxY * 0.5;
            gc.fillPolygon(
                new double[]{mx, mx + 200, mx + 400},
                new double[]{height, my, height},
                3
            );
        }
    }
    
    private void renderWorldGrid(GameState gameState, double camX, double camY, double width, double height) {
        gc.setStroke(Color.web("#333355", 0.3));
        gc.setLineWidth(1);
        
        int gridSize = 100;
        int startX = (int)(camX / gridSize) * gridSize;
        int startY = (int)(camY / gridSize) * gridSize;
        
        for (double x = startX; x < camX + width + gridSize; x += gridSize) {
            gc.strokeLine(x - camX, 0, x - camX, height);
        }
        for (double y = startY; y < camY + height + gridSize; y += gridSize) {
            gc.strokeLine(0, y - camY, width, y - camY);
        }
        
        // World boundary
        gc.setStroke(Color.web("#555577", 0.5));
        gc.setLineWidth(2);
        gc.strokeRect(-camX, -camY, gameState.getWorldWidth(), gameState.getWorldHeight());
    }
    
    private void renderDeadEntities(GameState gameState, double camX, double camY) {
        // Dead entities fade out - handled in entity render
    }
    
    private void renderGameUI(GameState gameState, double width, double height) {
        Hero hero = gameState.getHero();
        if (hero == null) return;
        
        // Top HUD
        renderTopHUD(gameState, width, height);
        
        // Bottom HUD - Skills/Cooldowns
        renderBottomHUD(hero, width, height);
        
        // Side HUD - Stats
        renderSideHUD(hero, width, height);
        
        // Wave indicator
        renderWaveIndicator(gameState, width, height);
    }
    
    private void renderTopHUD(GameState gameState, double width, double height) {
        Hero hero = gameState.getHero();
        
        // Background panel
        gc.setFill(Color.web("#000000", 0.7));
        gc.fillRect(10, 10, 300, 80);
        gc.setStroke(Color.web("#444466"));
        gc.setLineWidth(1);
        gc.strokeRect(10, 10, 300, 80);
        
        // HP Bar
        gc.setFill(Color.web("#333333"));
        gc.fillRect(20, 20, 280, 20);
        
        double hpRatio = (double) hero.getHp() / hero.getMaxHp();
        Color hpColor = hpRatio > 0.5 ? Color.web("#00CC44") : 
                        hpRatio > 0.25 ? Color.web("#FFCC00") : Color.web("#FF3333");
        gc.setFill(hpColor);
        gc.fillRect(20, 20, 280 * hpRatio, 20);
        
        gc.setFill(Color.WHITE);
        gc.setFont(uiFont);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("HP: " + hero.getHp() + " / " + hero.getMaxHp(), 30, 35);
        
        // MP Bar
        gc.setFill(Color.web("#333333"));
        gc.fillRect(20, 45, 280, 15);
        
        double mpRatio = (double) hero.getMp() / hero.getMaxMp();
        gc.setFill(Color.web("#0088FF"));
        gc.fillRect(20, 45, 280 * mpRatio, 15);
        
        gc.setFill(Color.WHITE);
        gc.setFont(smallFont);
        gc.fillText("MP: " + hero.getMp() + " / " + hero.getMaxMp(), 30, 56);
        
        // Level & Exp
        gc.setFill(Color.WHITE);
        gc.setFont(smallFont);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("Lv." + hero.getLevel() + "  EXP: " + hero.getExp() + "/" + hero.getExpToNextLevel(), 290, 56);
        
        // Score & Wave
        gc.setFont(uiFont);
        gc.fillText("점수: " + gameState.getScore(), width - 20, 35);
        gc.fillText("처치: " + gameState.getMonstersKilled(), width - 20, 95);
        boolean muted = com.survivalcoding.game.audio.SoundManager.get().isMuted();
        int volumePercent = (int) Math.round(com.survivalcoding.game.audio.SoundManager.get().getVolume() * 100);
        gc.fillText("사운드: " + (muted ? "끔" : "켜짐") + " " + volumePercent + "%", width - 20, 115);
        gc.fillText("최고: " + gameState.getBestScore(), width - 20, 55);
        gc.fillText("웨이브: " + gameState.getWave() + "/" + GameConfig.FINAL_WAVE, width - 20, 75);
    }
    
    private void renderBottomHUD(Hero hero, double width, double height) {
        int slotSize = 60;
        int spacing = 10;
        int startX = (int)((width - (slotSize * 4 + spacing * 3)) / 2);
        int y = (int)(height - slotSize - 20);
        
        // Skill slots
        String[] skills = {"공격 [SPACE]", "마법 [M]", "대시 [SHIFT]", "아이템 [E]"};
        double[] cooldowns = {
            hero.getAttackCooldown() / GameConfig.HERO_ATTACK_COOLDOWN_SECONDS,
            hero.getMagicCooldown() / GameConfig.HERO_MAGIC_COOLDOWN_SECONDS,
            hero.getDashCooldown() / GameConfig.HERO_DASH_COOLDOWN_SECONDS,
            0 // item cooldown not tracked simply
        };
        Color[] colors = {
            Color.web("#FF6666"),
            Color.web("#FF8800"),
            Color.web("#00AAFF"),
            Color.web("#00CC66")
        };
        
        for (int i = 0; i < 4; i++) {
            int x = startX + i * (slotSize + spacing);
            
            // Background
            gc.setFill(Color.web("#000000", 0.7));
            gc.fillRoundRect(x, y, slotSize, slotSize, 8, 8);
            
            // Cooldown overlay
            if (cooldowns[i] > 0) {
                gc.setFill(Color.web("#000000", 0.8));
                gc.fillRoundRect(x, y + slotSize * (1 - cooldowns[i]), slotSize, slotSize * cooldowns[i], 8, 8);
            }
            
            // Border
            gc.setStroke(colors[i]);
            gc.setLineWidth(2);
            gc.strokeRoundRect(x, y, slotSize, slotSize, 8, 8);
            
            // Icon (simple shapes)
            gc.setFill(colors[i]);
            drawSkillIcon(gc, x + slotSize/2, y + slotSize/2, i);
            
            // Key label
            gc.setFill(Color.WHITE);
            gc.setFont(skillFont);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(skills[i].split(" ")[0], x + slotSize/2, y + slotSize + 15);
        }
    }
    
    private void drawSkillIcon(GraphicsContext gc, double cx, double cy, int skillIndex) {
        switch (skillIndex) {
            case 0 -> { // Attack - sword
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(3);
                gc.strokeLine(cx - 15, cy + 15, cx + 15, cy - 15);
                gc.strokeLine(cx - 10, cy + 10, cx + 10, cy - 10);
            }
            case 1 -> { // Magic - fireball
                gc.setFill(Color.web("#FF8800"));
                gc.fillOval(cx - 12, cy - 12, 24, 24);
                gc.setFill(Color.web("#FFFF00"));
                gc.fillOval(cx - 6, cy - 6, 12, 12);
            }
            case 2 -> { // Dash - arrow
                gc.setFill(Color.WHITE);
                gc.fillPolygon(
                    new double[]{cx, cx - 12, cx - 12, cx + 12, cx + 12},
                    new double[]{cy - 15, cy, cy - 5, cy, cy - 5},
                    5
                );
            }
            case 3 -> { // Item - potion
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(2);
                gc.strokeRect(cx - 8, cy - 15, 16, 25);
                gc.strokeLine(cx - 10, cy - 10, cx + 10, cy - 10);
                gc.setFill(Color.web("#00CC66"));
                gc.fillRect(cx - 6, cy - 13, 12, 20);
            }
        }
    }
    
    private void renderSideHUD(Hero hero, double width, double height) {
        // Right side - stats
        int x = (int)(width - 200);
        int y = 100;
        int w = 190;
        int h = 200;
        
        gc.setFill(Color.web("#000000", 0.6));
        gc.fillRoundRect(x, y, w, h, 10, 10);
        gc.setStroke(Color.web("#444466"));
        gc.setLineWidth(1);
        gc.strokeRoundRect(x, y, w, h, 10, 10);
        
        gc.setFill(Color.WHITE);
        gc.setFont(uiFont);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("능력치", x + 15, y + 25);
        
        gc.setFont(smallFont);
        int lineY = y + 50;
        gc.fillText("공격력: " + hero.getAttackDamage(), x + 15, lineY); lineY += 22;
        gc.fillText("마법력: " + hero.getMagicDamage(), x + 15, lineY); lineY += 22;
        gc.fillText("이동속도: " + (int)hero.getSpeed(), x + 15, lineY); lineY += 22;
        gc.fillText("최대 HP: " + hero.getMaxHp(), x + 15, lineY); lineY += 22;
        gc.fillText("최대 MP: " + hero.getMaxMp(), x + 15, lineY); lineY += 22;
        
        // Controls hint
        gc.setFont(tinyFont);
        gc.setFill(Color.web("#AAAAAA"));
        lineY += 10;
        gc.fillText("이동: WASD / 방향키", x + 15, lineY); lineY += 18;
        gc.fillText("공격: SPACE", x + 15, lineY); lineY += 18;
        gc.fillText("마법: M (MP 15)", x + 15, lineY); lineY += 18;
        gc.fillText("대시: SHIFT", x + 15, lineY); lineY += 18;
        gc.fillText("회복: E", x + 15, lineY); lineY += 18;
        gc.fillText("FPS 표시: F3", x + 15, lineY); lineY += 18;
        gc.fillText("음소거: F4", x + 15, lineY); lineY += 18;
        gc.fillText("전체화면: F11", x + 15, lineY); lineY += 18;
        gc.fillText("일시정지: ESC", x + 15, lineY);
    }
    
    private void renderWaveIndicator(GameState gameState, double width, double height) {
        if (gameState.getWave() > 1) {
            gc.setFill(Color.web("#FFD700", 0.9));
            gc.setFont(GameFonts.font(36, true));
            gc.setTextAlign(TextAlignment.CENTER);
            String text = "WAVE " + gameState.getWave();
            gc.fillText(text, width/2, 100);
            
            // Subtitle
            gc.setFill(Color.WHITE);
            gc.setFont(GameFonts.font(18, false));
            gc.fillText("몬스터가 몰려옵니다!", width/2, 130);
        }
    }
    
    private void renderMinimap(GameState gameState, double camX, double camY, 
                               double width, double height) {
        int mapSize = 150;
        int mapX = (int)(width - mapSize - 20);
        int mapY = 20;
        
        // Background
        gc.setFill(Color.web("#000000", 0.7));
        gc.fillRoundRect(mapX, mapY, mapSize, mapSize, 5, 5);
        gc.setStroke(Color.web("#444466"));
        gc.setLineWidth(1);
        gc.strokeRoundRect(mapX, mapY, mapSize, mapSize, 5, 5);
        
        // World boundary
        double worldW = gameState.getWorldWidth();
        double worldH = gameState.getWorldHeight();
        double scaleX = mapSize / worldW;
        double scaleY = mapSize / worldH;
        
        // Hero position
        Hero hero = gameState.getHero();
        if (hero != null) {
            int hx = mapX + (int)(hero.getX() * scaleX);
            int hy = mapY + (int)(hero.getY() * scaleY);
            gc.setFill(Color.web("#00FFFF"));
            gc.fillOval(hx - 3, hy - 3, 6, 6);
        }
        
        // Monsters
        gc.setFill(Color.web("#FF4444"));
        for (Monster monster : gameState.getMonsters()) {
            if (monster.isAlive()) {
                int mx = mapX + (int)(monster.getX() * scaleX);
                int my = mapY + (int)(monster.getY() * scaleY);
                gc.fillOval(mx - 2, my - 2, 4, 4);
            }
        }
        
        // Camera view indicator
        gc.setStroke(Color.web("#00FFFF", 0.5));
        gc.setLineWidth(1);
        double viewW = width * scaleX;
        double viewH = height * scaleY;
        gc.strokeRect(mapX + camX * scaleX, mapY + camY * scaleY, viewW, viewH);
    }
    
    private void renderMainMenu(GameState gameState) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        // Dark background
        gc.setFill(Color.web("#0A0A1A"));
        gc.fillRect(0, 0, width, height);
        
        // Animated background
        renderMenuBackground(width, height);
        
        // Title
        gc.setFill(Color.web("#FFD700"));
        gc.setFont(titleFont);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("몬스터 아레나 배틀", width/2, height/2 - 100);
        
        // Subtitle
        gc.setFill(Color.web("#AAAAAA"));
        gc.setFont(subtitleFont);
        gc.fillText("용사가 되어 몬스터를 물리치세요!", width/2, height/2 - 50);
        
        // Start prompt
        double pulse = 0.7 + 0.3 * Math.sin(System.currentTimeMillis() / 500.0);
        gc.setFill(Color.web("#FFFFFF", pulse));
        gc.setFont(GameFonts.font(20, true));
        gc.fillText("ENTER 키를 눌러 시작", width/2, height/2 + 50);
        
        // Controls preview
        gc.setFill(Color.web("#888888"));
        gc.setFont(smallFont);
        gc.fillText("이동: WASD / 방향키    공격: SPACE    마법: M    대시: SHIFT    회복: E", width/2, height/2 + 100);
        gc.fillText("웨이브 10을 클리어하면 승리!", width/2, height/2 + 130);
        
        gc.setFont(uiFont);
        gc.fillText("최고 점수: " + gameState.getBestScore(), width/2, height - 50);
    }
    
    private void renderMenuBackground(double width, double height) {
        // Floating particles
        long time = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            double x = (starX[i] + time * starSpeed[i] / 1000) % width;
            double y = starY[i];
            double size = 1 + Math.sin(time / 500.0 + i) * 0.5;
            gc.setFill(Color.web("#FFD700", 0.3 + 0.2 * Math.sin(time / 300.0 + i)));
            gc.fillOval(x, y, size, size);
        }
    }
    
    private void renderPauseOverlay() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        // Semi-transparent overlay
        gc.setFill(Color.web("#000000", 0.7));
        gc.fillRect(0, 0, width, height);
        
        // Pause text
        gc.setFill(Color.WHITE);
        gc.setFont(titleFont);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("일시 정지", width/2, height/2 - 30);
        
        gc.setFill(Color.web("#AAAAAA"));
        gc.setFont(subtitleFont);
        gc.fillText("ESC 또는 ENTER로 계속", width/2, height/2 + 30);
    }
    
    private void renderGameOver(GameState gameState) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        // Dark background
        gc.setFill(Color.web("#1A0000"));
        gc.fillRect(0, 0, width, height);
        
        // Game Over text
        gc.setFill(Color.web("#FF3333"));
        gc.setFont(titleFont);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", width/2, height/2 - 80);
        
        // Stats
        gc.setFill(Color.WHITE);
        gc.setFont(subtitleFont);
        gc.fillText("최종 점수: " + gameState.getScore(), width/2, height/2 - 20);
        gc.fillText("도달 웨이브: " + gameState.getWave(), width/2, height/2 + 20);
        gc.fillText("처치한 몬스터: " + gameState.getMonstersKilled(), width/2, height/2 + 60);
        
        // Restart prompt
        double pulse = 0.7 + 0.3 * Math.sin(System.currentTimeMillis() / 500.0);
        gc.setFill(Color.web("#FFFFFF", pulse));
        gc.setFont(GameFonts.font(18, true));
        gc.fillText("ENTER로 메인 메뉴 돌아가기", width/2, height/2 + 120);
    }
    
    private void renderVictory(GameState gameState) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        // Victory background
        gc.setFill(Color.web("#0A1A0A"));
        gc.fillRect(0, 0, width, height);
        
        // Celebration particles
        long time = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {
            double x = (starX[i] + time * starSpeed[i] / 500) % width;
            double y = height - (time * 50 + i * 30) % (height + 100);
            gc.setFill(Color.web("#FFD700", 0.5 + 0.3 * Math.sin(time / 200.0 + i)));
            gc.fillOval(x, y, 4, 4);
        }
        
        // Victory text
        gc.setFill(Color.web("#FFD700"));
        gc.setFont(GameFonts.font(64, true));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("VICTORY!", width/2, height/2 - 80);
        
        gc.setFill(Color.WHITE);
        gc.setFont(subtitleFont);
        gc.fillText("모든 웨이브를 클리어했습니다!", width/2, height/2 - 30);
        
        // Stats
        gc.setFont(uiFont);
        gc.fillText("최종 점수: " + gameState.getScore(), width/2, height/2 + 20);
        gc.fillText("처치한 몬스터: " + gameState.getMonstersKilled(), width/2, height/2 + 50);
        gc.fillText("소요 시간: " + formatTime(gameState.getGameTime()), width/2, height/2 + 80);
        
        Hero hero = gameState.getHero();
        if (hero != null) {
            gc.fillText("최종 레벨: " + hero.getLevel(), width/2, height/2 + 110);
        }
        
        // Restart prompt
        double pulse = 0.7 + 0.3 * Math.sin(System.currentTimeMillis() / 500.0);
        gc.setFill(Color.web("#FFFFFF", pulse));
        gc.setFont(GameFonts.font(18, true));
        gc.fillText("ENTER로 메인 메뉴 돌아가기", width/2, height/2 + 160);
    }
    
    private String formatTime(double seconds) {
        int mins = (int)(seconds / 60);
        int secs = (int)(seconds % 60);
        return String.format("%02d:%02d", mins, secs);
    }
    
    public void flashScreen(Color color, double duration) {
        screenFlashColor = color;
        screenFlashTimer = duration;
    }
    
    public void setCanvasSize(double width, double height) {
        canvas.setWidth(width);
        canvas.setHeight(height);
    }
}
