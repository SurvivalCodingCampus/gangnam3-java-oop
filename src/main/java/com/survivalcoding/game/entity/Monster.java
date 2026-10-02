package com.survivalcoding.game.entity;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import com.survivalcoding.game.animation.Animation;
import com.survivalcoding.game.animation.ParticleEffect;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.Random;

/**
 * Monster entity with different types and AI behaviors.
 */
public class Monster extends GameEntity {
    
    public enum MonsterType {
        SLIME(30, 5, 80, 100, Color.web("#44AA44"), 10),
        POISON_SLIME(40, 8, 80, 120, Color.web("#8844AA"), 20),
        ZOMBIE(60, 12, 100, 80, Color.web("#556633"), 30),
        KING_SLIME(100, 15, 150, 60, Color.web("#FFD700"), 50);
        
        final int baseHp;
        final int baseDamage;
        final int detectionRange;
        final int speed;
        final Color color;
        final int expReward;
        
        public Color getColor() {
            return color;
        }
        
        MonsterType(int hp, int damage, int detection, int speed, Color color, int exp) {
            this.baseHp = hp;
            this.baseDamage = damage;
            this.detectionRange = detection;
            this.speed = speed;
            this.color = color;
            this.expReward = exp;
        }
    }
    
    private final MonsterType type;
    private final Random random = new Random();
    
    // AI
    private double aiTimer = 0;
    private double attackCooldown = 0;
    private double wanderTimer = 0;
    private double wanderAngle = 0;
    private boolean isAttacking = false;
    private double attackWindup = 0;
    private static final double ATTACK_COOLDOWN = 1.5;
    private static final double ATTACK_WINDUP_TIME = 0.5;
    private static final double ATTACK_RANGE = 50;
    
    // Visual
    private Animation idleAnimation;
    private Animation walkAnimation;
    private Animation attackAnimation;
    private Animation currentAnimation;
    private double scale = 1.0;
    private double scaleTarget = 1.0;
    private double jumpTimer = 0;
    private boolean isJumping = false;
    
    // Attack damage (from type)
    private int attackDamage;
    
    private static final double HEALTH_GROWTH_PER_WAVE = 0.15;
    private static final double DAMAGE_GROWTH_PER_WAVE = 0.10;
    
    public Monster(String name, int hp, int attackDamage, double x, double y, MonsterType type) {
        super(x, y, getWidthForType(type), getHeightForType(type), hp);
        this.type = type;
        this.speed = type.speed;
        this.attackDamage = type.baseDamage;
        this.frameCount = 4;
        this.frameDuration = 0.2;
        
        initializeAnimations();
    }
    
    public int getAttackDamage() {
        return attackDamage;
    }
    
    public void scaleForDifficulty(int wave) {
        int effectiveWave = Math.max(1, wave);
        double healthMultiplier = 1.0 + (effectiveWave - 1) * HEALTH_GROWTH_PER_WAVE;
        double damageMultiplier = 1.0 + (effectiveWave - 1) * DAMAGE_GROWTH_PER_WAVE;
        
        maxHp = Math.max(1, (int) Math.round(type.baseHp * healthMultiplier));
        hp = maxHp;
        attackDamage = Math.max(1, (int) Math.round(type.baseDamage * damageMultiplier));
    }
    
    private static double getWidthForType(MonsterType type) {
        return switch (type) {
            case SLIME -> 40;
            case POISON_SLIME -> 44;
            case ZOMBIE -> 48;
            case KING_SLIME -> 80;
        };
    }
    
    private static double getHeightForType(MonsterType type) {
        return switch (type) {
            case SLIME -> 32;
            case POISON_SLIME -> 36;
            case ZOMBIE -> 56;
            case KING_SLIME -> 64;
        };
    }
    
    private void initializeAnimations() {
        idleAnimation = new Animation("idle", 4, 0.3, true);
        walkAnimation = new Animation("walk", 4, 0.15, true);
        attackAnimation = new Animation("attack", 4, 0.1, false);
        currentAnimation = idleAnimation;
    }
    
    @Override
    public void update(double deltaTime, InputHandler input, GameState gameState) {
        Hero hero = gameState.getHero();
        if (hero == null || !hero.isAlive()) return;
        
        // Update cooldowns
        if (attackCooldown > 0) attackCooldown -= deltaTime;
        if (attackWindup > 0) attackWindup -= deltaTime;
        
        // Update animations
        currentAnimation.update(deltaTime);
        currentFrame = currentAnimation.getCurrentFrame();
        updateAnimation(deltaTime);
        
        // Scale animation (breathing effect)
        scale += (scaleTarget - scale) * deltaTime * 5;
        if (Math.abs(scale - scaleTarget) < 0.01) {
            scaleTarget = 1.0 + (random.nextDouble() - 0.5) * 0.1;
        }
        
        // Jump animation for slimes
        if (type == MonsterType.SLIME || type == MonsterType.POISON_SLIME || type == MonsterType.KING_SLIME) {
            updateJump(deltaTime);
        }
        
        // AI Behavior
        double distanceToHero = distanceTo(hero);
        
        if (distanceToHero <= type.detectionRange) {
            // Chase hero
            chaseHero(hero, deltaTime, gameState);
        } else {
            // Wander
            wander(deltaTime, gameState);
        }
        
        // Attack if in range
        if (distanceToHero <= ATTACK_RANGE + width / 2 && attackCooldown <= 0) {
            startAttack(hero, gameState);
        }
        
        // Execute attack after windup
        if (isAttacking && attackWindup <= 0) {
            executeAttack(hero, gameState);
        }
        
        updatePosition(deltaTime, gameState);
    }
    
    private void updateJump(double deltaTime) {
        if (!isJumping && random.nextDouble() < 0.02) {
            isJumping = true;
            jumpTimer = 0.5;
            velocityY = -200;
        }
        
        if (isJumping) {
            jumpTimer -= deltaTime;
            if (jumpTimer <= 0) {
                isJumping = false;
            }
        }
    }
    
    private void chaseHero(Hero hero, double deltaTime, GameState gameState) {
        double angle = angleTo(hero);
        
        // Move towards hero
        velocityX = Math.cos(angle) * speed;
        velocityY = Math.sin(angle) * speed;
        
        // Face hero
        facingRight = hero.getX() > x;
        
        // Update animation
        currentAnimation = walkAnimation;
    }
    
    private void wander(double deltaTime, GameState gameState) {
        wanderTimer -= deltaTime;
        
        if (wanderTimer <= 0) {
            wanderTimer = 2.0 + random.nextDouble() * 3.0;
            wanderAngle = random.nextDouble() * Math.PI * 2;
        }
        
        velocityX = Math.cos(wanderAngle) * speed * 0.3;
        velocityY = Math.sin(wanderAngle) * speed * 0.3;
        
        facingRight = velocityX > 0;
        currentAnimation = walkAnimation;
    }
    
    private void startAttack(Hero hero, GameState gameState) {
        isAttacking = true;
        attackWindup = ATTACK_WINDUP_TIME;
        attackCooldown = ATTACK_COOLDOWN;
        currentAnimation = attackAnimation;
        currentAnimation.reset();
        
        // Face hero
        facingRight = hero.getX() > x;
        
        // Warning particles
        for (int i = 0; i < 5; i++) {
            double angle = angleTo(hero);
            double px = x + Math.cos(angle) * (width / 2 + 10);
            double py = y + Math.sin(angle) * (width / 2 + 10);
            gameState.addParticle(new ParticleEffect(
                px, py,
                ParticleEffect.ParticleType.WARNING,
                Color.YELLOW, 8
            ));
        }
    }
    
    private void executeAttack(Hero hero, GameState gameState) {
        isAttacking = false;
        
        double distance = distanceTo(hero);
        if (distance <= ATTACK_RANGE + width / 2 + hero.getWidth() / 2) {
            hero.takeDamage(attackDamage);
            gameState.shakeCamera(0.4);
            
            // Knockback hero
            double angle = angleTo(hero);
            hero.applyKnockback(Math.cos(angle) * 400, Math.sin(angle) * 400);
            
            // Special effects based on type
            switch (type) {
                case POISON_SLIME -> {
                    // Poison effect (could add DoT)
                    gameState.addParticle(new ParticleEffect(
                        hero.getX(), hero.getY(),
                        ParticleEffect.ParticleType.POISON,
                        Color.GREEN, 12
                    ));
                }
                case ZOMBIE -> {
                    // Zombie heals slightly on hit
                    heal(2);
                }
                case KING_SLIME -> {
                    // King slime spawns mini slimes
                    if (random.nextDouble() < 0.3) {
                        spawnMiniSlime(gameState);
                    }
                }
            }
        }
        
        // Attack particles
        double angle = angleTo(hero);
        double ax = x + Math.cos(angle) * (width / 2 + 20);
        double ay = y + Math.sin(angle) * (width / 2 + 20);
        gameState.addParticle(new ParticleEffect(
            ax, ay,
            ParticleEffect.ParticleType.MONSTER_ATTACK,
            type.color, 15
        ));
    }
    
    private void spawnMiniSlime(GameState gameState) {
        double angle = random.nextDouble() * Math.PI * 2;
        double spawnX = x + Math.cos(angle) * 50;
        double spawnY = y + Math.sin(angle) * 50;
        
        Monster mini = new Monster("미니 슬라임", 15, 3, spawnX, spawnY, MonsterType.SLIME);
        mini.setSpeed(120);
        gameState.addMonster(mini);
    }
    
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        scaleTarget = 0.7;
        
        if (isAlive()) {
            currentAnimation = idleAnimation; // Will be overridden by chase
        }
    }
    
    @Override
    protected void onDeath() {
        // Drop experience orb
        // Handled in gameState
    }
    
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double renderX = x - cameraX;
        double renderY = y - cameraY;
        
        // Don't render if off screen
        if (renderX < -width || renderX > gc.getCanvas().getWidth() + width ||
            renderY < -height || renderY > gc.getCanvas().getHeight() + height) {
            return;
        }
        
        gc.save();
        
        // Hit flash
        if (isHitFlashing()) {
            gc.setGlobalAlpha(0.5 + 0.5 * Math.sin(hitFlashTimer * 100));
        }
        
        // Flip
        if (!facingRight) {
            gc.translate(renderX + width / 2, renderY);
            gc.scale(-1, 1);
            gc.translate(-(renderX + width / 2), -renderY);
        }
        
        // Apply scale
        gc.translate(renderX + width / 2, renderY + height / 2);
        gc.scale(scale, scale);
        gc.translate(-(renderX + width / 2), -(renderY + height / 2));
        
        // Draw monster
        drawMonster(gc, renderX, renderY);
        
        // Draw health bar
        drawHealthBar(gc, renderX, renderY - 15);
        
        // Attack windup indicator
        if (attackWindup > 0) {
            drawAttackIndicator(gc, renderX, renderY);
        }
        
        gc.restore();
    }
    
    private void drawMonster(GraphicsContext gc, double rx, double ry) {
        double cx = rx + width / 2;
        double cy = ry + height / 2;
        
        switch (type) {
            case SLIME -> drawSlime(gc, cx, cy, Color.web("#44AA44"));
            case POISON_SLIME -> drawSlime(gc, cx, cy, Color.web("#8844AA"));
            case ZOMBIE -> drawZombie(gc, cx, cy);
            case KING_SLIME -> drawKingSlime(gc, cx, cy);
        }
    }
    
    private void drawSlime(GraphicsContext gc, double cx, double cy, Color baseColor) {
        // Main body (blob)
        gc.setFill(baseColor);
        
        // Wobble effect
        double wobble = Math.sin(animationTimer * 8) * 3;
        
        gc.fillOval(cx - width / 2 + wobble, cy - height / 4, width - wobble * 2, height / 2);
        gc.fillOval(cx - width / 2, cy, width, height / 2);
        
        // Highlight
        gc.setFill(baseColor.brighter());
        gc.fillOval(cx - width / 4, cy - height / 4, width / 4, height / 4);
        
        // Eyes
        gc.setFill(Color.WHITE);
        double eyeY = cy - height / 6;
        gc.fillOval(cx - 10, eyeY, 10, 10);
        gc.fillOval(cx + 2, eyeY, 10, 10);
        
        gc.setFill(Color.BLACK);
        gc.fillOval(cx - 7, eyeY + 2, 5, 5);
        gc.fillOval(cx + 5, eyeY + 2, 5, 5);
        
        // Mouth
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeArc(cx - 8, cy - 2, 16, 10, 0, 180, ArcType.OPEN);
        
        // Poison bubbles for poison slime
        if (type == MonsterType.POISON_SLIME) {
            for (int i = 0; i < 3; i++) {
                double bubbleX = cx - 10 + i * 10 + Math.sin(animationTimer * 5 + i) * 3;
                double bubbleY = cy - height / 4 + Math.cos(animationTimer * 3 + i) * 5;
                gc.setFill(Color.web("#00FF00", 0.6));
                gc.fillOval(bubbleX - 3, bubbleY - 3, 6, 6);
            }
        }
    }
    
    private void drawZombie(GraphicsContext gc, double cx, double cy) {
        // Body
        gc.setFill(Color.web("#556633"));
        gc.fillRect(cx - width / 2, cy - height / 2 + 10, width, height - 10);
        
        // Head
        gc.setFill(Color.web("#667744"));
        gc.fillOval(cx - 18, cy - height / 2 - 8, 36, 36);
        
        // Eyes (glowing)
        gc.setFill(Color.web("#00FF00"));
        gc.fillOval(cx - 12, cy - height / 2, 8, 8);
        gc.fillOval(cx + 4, cy - height / 2, 8, 8);
        
        gc.setFill(Color.BLACK);
        gc.fillOval(cx - 9, cy - height / 2 + 2, 3, 3);
        gc.fillOval(cx + 7, cy - height / 2 + 2, 3, 3);
        
        // Arms
        gc.setFill(Color.web("#556633"));
        double armSwing = Math.sin(animationTimer * 10) * 15;
        gc.fillRect(cx - width / 2 - 15, cy - 10 + armSwing, 15, 30);
        gc.fillRect(cx + width / 2, cy - 10 - armSwing, 15, 30);
        
        // Legs
        double legSwing = Math.sin(animationTimer * 8) * 8;
        gc.fillRect(cx - 12, cy + height / 2 - 20, 12, 20 + legSwing);
        gc.fillRect(cx, cy + height / 2 - 20, 12, 20 - legSwing);
        
        // Tattered clothes
        gc.setFill(Color.web("#334422"));
        gc.fillRect(cx - width / 2, cy + 10, width, 20);
    }
    
    private void drawKingSlime(GraphicsContext gc, double cx, double cy) {
        // Crown
        gc.setFill(Color.GOLD);
        gc.fillPolygon(
            new double[]{cx - 30, cx - 15, cx, cx + 15, cx + 30, cx + 30, cx - 30},
            new double[]{cy - height / 2, cy - height / 2 - 20, cy - height / 2, cy - height / 2 - 20, cy - height / 2, cy - height / 2 + 10, cy - height / 2 + 10},
            7
        );
        
        // Jewels on crown
        gc.setFill(Color.RED);
        gc.fillOval(cx - 5, cy - height / 2 - 5, 10, 10);
        gc.setFill(Color.BLUE);
        gc.fillOval(cx - 20, cy - height / 2 + 2, 6, 6);
        gc.fillOval(cx + 14, cy - height / 2 + 2, 6, 6);
        
        // Main body (large slime)
        gc.setFill(Color.web("#FFD700"));
        double wobble = Math.sin(animationTimer * 5) * 5;
        gc.fillOval(cx - width / 2 + wobble, cy - height / 4, width - wobble * 2, height / 2);
        gc.fillOval(cx - width / 2, cy, width, height / 2);
        
        // Highlight
        gc.setFill(Color.web("#FFFF99"));
        gc.fillOval(cx - width / 4, cy - height / 4, width / 3, height / 3);
        
        // Eyes (larger)
        gc.setFill(Color.WHITE);
        gc.fillOval(cx - 18, cy - height / 6, 14, 14);
        gc.fillOval(cx + 4, cy - height / 6, 14, 14);
        
        gc.setFill(Color.BLACK);
        gc.fillOval(cx - 13, cy - height / 6 + 3, 6, 6);
        gc.fillOval(cx + 9, cy - height / 6 + 3, 6, 6);
        
        // Mouth
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeArc(cx - 15, cy, 30, 15, 0, 180, ArcType.OPEN);
    }
    
    private void drawHealthBar(GraphicsContext gc, double rx, double ry) {
        double barWidth = width;
        double barHeight = 5;
        
        gc.setFill(Color.web("#333333"));
        gc.fillRect(rx, ry, barWidth, barHeight);
        
        double hpRatio = (double) hp / maxHp;
        gc.setFill(hpRatio > 0.5 ? Color.GREEN : (hpRatio > 0.25 ? Color.YELLOW : Color.RED));
        gc.fillRect(rx, ry, barWidth * hpRatio, barHeight);
        
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.strokeRect(rx, ry, barWidth, barHeight);
    }
    
    private void drawAttackIndicator(GraphicsContext gc, double rx, double ry) {
        gc.setFill(Color.web("#FF0000", 0.5 + 0.5 * Math.sin(attackWindup * 20)));
        gc.fillOval(rx + width / 2 - 10, ry - 20, 20, 20);
        
        gc.setFill(Color.WHITE);
        gc.setFont(new javafx.scene.text.Font(12));
        gc.fillText("!", rx + width / 2 - 3, ry - 5);
    }
    
    // Getters
    public MonsterType getType() { return type; }
    public int getExpReward() { return type.expReward; }
    public boolean isAttacking() { return isAttacking; }
    public double getAttackWindup() { return attackWindup; }
}