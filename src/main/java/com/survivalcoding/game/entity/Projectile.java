package com.survivalcoding.game.entity;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import com.survivalcoding.game.animation.ParticleEffect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Projectile entity for magic attacks and ranged weapons.
 */
public class Projectile extends GameEntity {
    
    public enum Type {
        FIREBALL(Color.ORANGE, 16, 16, 500, 30),
        ICE_SHARD(Color.CYAN, 12, 12, 600, 25),
        LIGHTNING_BOLT(Color.YELLOW, 20, 20, 800, 40),
        POISON_DART(Color.GREEN, 10, 10, 400, 15);
        
        final Color color;
        final int width;
        final int height;
        final int speed;
        final int damage;
        
        Type(Color color, int width, int height, int speed, int damage) {
            this.color = color;
            this.width = width;
            this.height = height;
            this.speed = speed;
            this.damage = damage;
        }
    }
    
    private final Type type;
    private double lifeTime = 0;
    private double maxLifeTime = 3.0;
    private double rotation = 0;
    private double rotationSpeed = 0;
    private boolean hasHit = false;
    private final Random random = new Random();
    private int attackDamage;
    
    public Projectile(double x, double y, double velX, double velY, 
                      int damage, int width, int height, Color color, Type type) {
        super(x, y, width, height, 1); // HP not used for projectiles
        this.type = type;
        this.velocityX = velX;
        this.velocityY = velY;
        this.attackDamage = damage > 0 ? damage : type.damage;
        
        // Set rotation based on velocity
        rotation = Math.atan2(velY, velX);
        rotationSpeed = (random.nextDouble() - 0.5) * 10;
    }
    
    @Override
    public void update(double deltaTime, InputHandler input, GameState gameState) {
        if (!alive || hasHit) return;
        
        lifeTime += deltaTime;
        if (lifeTime >= maxLifeTime) {
            alive = false;
            return;
        }
        
        rotation += rotationSpeed * deltaTime;
        
        // Update position
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        
        // Trail particles
        if (type == Type.FIREBALL && random.nextDouble() < 0.3) {
            gameState.addParticle(new ParticleEffect(
                x, y,
                ParticleEffect.ParticleType.MAGIC,
                type.color, 3
            ));
        }
        
        // Check collision with monsters
        for (Monster monster : gameState.getMonsters()) {
            if (monster.isAlive() && collidesWith(monster)) {
                onHit(monster, gameState);
                break;
            }
        }
        
        // Check world bounds
        if (x < 0 || x > gameState.getWorldWidth() || 
            y < 0 || y > gameState.getWorldHeight()) {
            alive = false;
        }
    }
    
    private void onHit(Monster monster, GameState gameState) {
        hasHit = true;
        alive = false;
        
        monster.takeDamage(attackDamage);
        gameState.addScore(15);
        
        // Hit particles
        for (int i = 0; i < 10; i++) {
            gameState.addParticle(new ParticleEffect(
                x, y,
                ParticleEffect.ParticleType.HIT,
                type.color, 8
            ));
        }
        
        // Screen shake
        gameState.shakeCamera(0.2);
        
        if (!monster.isAlive()) {
            gameState.incrementMonstersKilled();
            gameState.addScore(50);
            
            // Death particles
            for (int i = 0; i < 15; i++) {
                gameState.addParticle(new ParticleEffect(
                    monster.getX(), monster.getY(),
                    ParticleEffect.ParticleType.DEATH,
                    monster.getType().color, 20
                ));
            }
        }
    }
    
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (!alive) return;
        
        double renderX = x - cameraX;
        double renderY = y - cameraY;
        
        if (renderX < -width || renderX > gc.getCanvas().getWidth() + width ||
            renderY < -height || renderY > gc.getCanvas().getHeight() + height) {
            return;
        }
        
        gc.save();
        gc.translate(renderX + width / 2, renderY + height / 2);
        gc.rotate(Math.toDegrees(rotation));
        
        drawProjectile(gc);
        
        gc.restore();
    }
    
    private void drawProjectile(GraphicsContext gc) {
        gc.setFill(type.color);
        
        switch (type) {
            case FIREBALL -> {
                // Main fireball
                gc.fillOval(-width / 2, -height / 2, width, height);
                
                // Inner glow
                gc.setFill(Color.web("#FFFF00"));
                gc.fillOval(-width / 4, -height / 4, width / 2, height / 2);
                
                // Core
                gc.setFill(Color.web("#FF8800"));
                gc.fillOval(-width / 6, -height / 6, width / 3, height / 3);
                
                // Trail effect
                gc.setFill(Color.web("#FF4400", 0.5));
                for (int i = 1; i <= 3; i++) {
                    double trailX = -width / 2 - i * 5;
                    gc.fillOval(trailX - 2, -3, 6, 6);
                }
            }
            case ICE_SHARD -> {
                // Diamond shape
                gc.fillPolygon(
                    new double[]{0, -width/2, 0, width/2},
                    new double[]{-height/2, 0, height/2, 0},
                    4
                );
                
                // Inner highlight
                gc.setFill(Color.web("#88FFFF"));
                gc.fillPolygon(
                    new double[]{0, -width/4, 0, width/4},
                    new double[]{-height/4, 0, height/4, 0},
                    4
                );
            }
            case LIGHTNING_BOLT -> {
                // Zigzag bolt
                gc.setStroke(type.color);
                gc.setLineWidth(4);
                gc.strokeLine(-width/2, -height/2, 0, 0);
                gc.strokeLine(0, 0, width/2, height/2);
                
                // Glow
                gc.setStroke(Color.web("#FFFF88"));
                gc.setLineWidth(2);
                gc.strokeLine(-width/2, -height/2, 0, 0);
                gc.strokeLine(0, 0, width/2, height/2);
            }
            case POISON_DART -> {
                // Dart shape
                gc.fillRect(-width/2, -height/4, width * 0.7, height/2);
                // Tip
                gc.fillPolygon(
                    new double[]{width/2 * 0.7, width/2, width/2 * 0.7},
                    new double[]{-height/4, 0, height/4},
                    3
                );
                
                // Feathers
                gc.setFill(Color.web("#00AA00"));
                gc.fillRect(-width/2, -height/3, width/4, height/1.5);
            }
        }
    }
}