package com.survivalcoding.game.entity;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for all game entities.
 */
public abstract class GameEntity {
    
    protected double x, y;
    protected double velocityX, velocityY;
    protected double width, height;
    protected int hp, maxHp;
    protected boolean alive = true;
    protected double speed = 200; // pixels per second
    
    // Animation
    protected double animationTimer = 0;
    protected int currentFrame = 0;
    protected int frameCount = 1;
    protected double frameDuration = 0.1;
    protected boolean facingRight = true;
    
    // Visual effects
    protected double hitFlashTimer = 0;
    protected static final double HIT_FLASH_DURATION = 0.1;

    private final List<StatusEffect> statusEffects = new ArrayList<>();
    
    public GameEntity(double x, double y, double width, double height, int hp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.maxHp = hp;
    }
    
    /**
     * Update entity logic. Override in subclasses.
     */
    public abstract void update(double deltaTime, InputHandler input, GameState gameState);
    
    /**
     * Take damage and trigger hit flash.
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
        hitFlashTimer = HIT_FLASH_DURATION;
        
        if (hp <= 0) {
            alive = false;
            onDeath();
        }
    }
    
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.removeIf(existing -> existing.getType() == effect.getType());
        statusEffects.add(effect);
    }

    public void updateStatusEffects(double deltaTime, GameState gameState) {
        Iterator<StatusEffect> iterator = statusEffects.iterator();
        while (iterator.hasNext()) {
            StatusEffect effect = iterator.next();
            effect.update(deltaTime, this, gameState);
            if (effect.isExpired()) {
                iterator.remove();
            }
        }
    }

    public boolean hasStatus(StatusEffect.Type type) {
        return statusEffects.stream().anyMatch(effect -> effect.getType() == type && !effect.isExpired());
    }

    public boolean isStunned() {
        return hasStatus(StatusEffect.Type.STUN);
    }

    /**
     * Heal entity.
     */
    public void heal(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }
    
    /**
     * Called when entity dies. Override for custom behavior.
     */
    protected void onDeath() {
        // Override in subclasses
    }
    
    /**
     * Apply knockback.
     */
    public void applyKnockback(double forceX, double forceY) {
        velocityX += forceX;
        velocityY += forceY;
    }
    
    /**
     * Update position based on velocity.
     */
    protected void updatePosition(double deltaTime, GameState gameState) {
        // Apply velocity
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        
        // Apply friction
        velocityX *= 0.9;
        velocityY *= 0.9;
        
        // Clamp to world bounds
        double worldWidth = gameState.getWorldWidth();
        double worldHeight = gameState.getWorldHeight();
        
        x = Math.max(width / 2, Math.min(x, worldWidth - width / 2));
        y = Math.max(height / 2, Math.min(y, worldHeight - height / 2));
    }
    
    /**
     * Update animation frame.
     */
    protected void updateAnimation(double deltaTime) {
        animationTimer += deltaTime;
        if (animationTimer >= frameDuration) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % frameCount;
        }
        
        if (hitFlashTimer > 0) {
            hitFlashTimer -= deltaTime;
        }
    }
    
    /**
     * Get collision bounds.
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }
    
    /**
     * Check collision with another entity.
     */
    public boolean collidesWith(GameEntity other) {
        return getBounds().intersects(other.getBounds());
    }
    
    /**
     * Get distance to another entity.
     */
    public double distanceTo(GameEntity other) {
        double dx = other.x - x;
        double dy = other.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Get angle to another entity in radians.
     */
    public double angleTo(GameEntity other) {
        return Math.atan2(other.y - y, other.x - x);
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public void setPosition(double x, double y) { this.x = x; this.y = y; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public void setVelocity(double vx, double vy) { this.velocityX = vx; this.velocityY = vy; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean facingRight) { this.facingRight = facingRight; }
    public int getCurrentFrame() { return currentFrame; }
    public int getFrameCount() { return frameCount; }
    public boolean isHitFlashing() { return hitFlashTimer > 0; }
    public Point2D getCenter() { return new Point2D(x, y); }
}