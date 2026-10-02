package com.survivalcoding.game.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Particle effect system for visual feedback.
 */
public class ParticleEffect {
    
    public enum ParticleType {
        HIT,           // Hit impact
        DEATH,         // Death explosion
        HEAL,          // Healing
        MAGIC,         // Magic casting
        SWORD_SWING,   // Sword trail
        MONSTER_ATTACK,// Monster attack
        WARNING,       // Attack warning
        POISON,        // Poison effect
        LEVEL_UP,      // Level up
        DASH,          // Dash trail
        EXP_ORB        // Experience orb
    }
    
    private double x, y;
    private double velocityX, velocityY;
    private double life, maxLife;
    private final Color color;
    private final ParticleType type;
    private final int particleCount;
    private final Random random = new Random();
    
    // Individual particles
    private final Particle[] particles;
    
    private static class Particle {
        double x, y;
        double vx, vy;
        double size;
        double rotation;
        double rotationSpeed;
        Color color;
        double life;
    }
    
    public ParticleEffect(double x, double y, ParticleType type, Color color, int count) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.color = color;
        this.particleCount = count;
        this.particles = new Particle[count];
        this.maxLife = getLifeForType(type);
        this.life = maxLife;
        
        initializeParticles();
    }
    
    private double getLifeForType(ParticleType type) {
        return switch (type) {
            case HIT -> 0.3;
            case DEATH -> 1.0;
            case HEAL -> 0.8;
            case MAGIC -> 0.6;
            case SWORD_SWING -> 0.2;
            case MONSTER_ATTACK -> 0.4;
            case WARNING -> 0.5;
            case POISON -> 1.5;
            case LEVEL_UP -> 2.0;
            case DASH -> 0.3;
            case EXP_ORB -> 5.0;
        };
    }
    
    private void initializeParticles() {
        for (int i = 0; i < particleCount; i++) {
            Particle p = new Particle();
            p.x = x;
            p.y = y;
            p.color = color;
            
            switch (type) {
                case HIT -> {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double speed = 100 + random.nextDouble() * 200;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 3 + random.nextDouble() * 5;
                    p.life = 0.3;
                }
                case DEATH -> {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double speed = 50 + random.nextDouble() * 300;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 4 + random.nextDouble() * 8;
                    p.life = 0.5 + random.nextDouble() * 0.5;
                    p.rotationSpeed = (random.nextDouble() - 0.5) * 10;
                }
                case HEAL -> {
                    double angle = -Math.PI / 2 + (random.nextDouble() - 0.5) * 0.5;
                    double speed = 50 + random.nextDouble() * 100;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 4 + random.nextDouble() * 4;
                    p.life = 0.5 + random.nextDouble() * 0.3;
                }
                case MAGIC -> {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double speed = 80 + random.nextDouble() * 150;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 3 + random.nextDouble() * 6;
                    p.life = 0.4 + random.nextDouble() * 0.4;
                    p.rotationSpeed = (random.nextDouble() - 0.5) * 5;
                }
                case SWORD_SWING -> {
                    double angle = random.nextDouble() * Math.PI * 0.5 - Math.PI / 4;
                    double speed = 200 + random.nextDouble() * 300;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 2 + random.nextDouble() * 4;
                    p.life = 0.15;
                }
                case MONSTER_ATTACK -> {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double speed = 100 + random.nextDouble() * 200;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 4 + random.nextDouble() * 6;
                    p.life = 0.3;
                }
                case WARNING -> {
                    double angle = random.nextDouble() * Math.PI * 2;
                    p.vx = Math.cos(angle) * 50;
                    p.vy = Math.sin(angle) * 50;
                    p.size = 6 + random.nextDouble() * 4;
                    p.life = 0.5;
                    p.rotationSpeed = 5;
                }
                case POISON -> {
                    p.vx = (random.nextDouble() - 0.5) * 30;
                    p.vy = -20 - random.nextDouble() * 50;
                    p.size = 3 + random.nextDouble() * 3;
                    p.life = 1.0 + random.nextDouble() * 1.0;
                }
                case LEVEL_UP -> {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double speed = 100 + random.nextDouble() * 200;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 5 + random.nextDouble() * 5;
                    p.life = 1.0 + random.nextDouble() * 1.0;
                    p.rotationSpeed = (random.nextDouble() - 0.5) * 8;
                }
                case DASH -> {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double speed = 50 + random.nextDouble() * 100;
                    p.vx = Math.cos(angle) * speed;
                    p.vy = Math.sin(angle) * speed;
                    p.size = 3 + random.nextDouble() * 4;
                    p.life = 0.2;
                }
                case EXP_ORB -> {
                    p.vx = (random.nextDouble() - 0.5) * 20;
                    p.vy = -30 - random.nextDouble() * 50;
                    p.size = 6 + random.nextDouble() * 4;
                    p.life = 5.0;
                    p.rotationSpeed = 2;
                }
            }
            
            p.rotation = random.nextDouble() * Math.PI * 2;
            particles[i] = p;
        }
    }
    
    public void update(double deltaTime) {
        life -= deltaTime;
        
        for (Particle p : particles) {
            if (p.life > 0) {
                p.life -= deltaTime;
                if (p.life <= 0) continue;
                
                p.x += p.vx * deltaTime;
                p.y += p.vy * deltaTime;
                p.rotation += p.rotationSpeed * deltaTime;
                
                // Gravity for some types
                if (type == ParticleType.DEATH || type == ParticleType.LEVEL_UP || type == ParticleType.EXP_ORB) {
                    p.vy += 200 * deltaTime;
                }
                
                // Fade out
                double alpha = p.life / (maxLife / particleCount);
                p.color = p.color.deriveColor(0, 1, 1, alpha);
            }
        }
    }
    
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        for (Particle p : particles) {
            if (p.life > 0) {
                double rx = p.x - cameraX;
                double ry = p.y - cameraY;
                
                // Skip if off screen
                if (rx < -50 || rx > gc.getCanvas().getWidth() + 50 ||
                    ry < -50 || ry > gc.getCanvas().getHeight() + 50) {
                    continue;
                }
                
                gc.save();
                gc.translate(rx, ry);
                gc.rotate(Math.toDegrees(p.rotation));
                
                gc.setFill(p.color);
                
                switch (type) {
                    case HIT, MONSTER_ATTACK -> {
                        gc.fillOval(-p.size / 2, -p.size / 2, p.size, p.size);
                    }
                    case DEATH -> {
                        gc.fillRect(-p.size / 2, -p.size / 2, p.size, p.size);
                    }
                    case HEAL -> {
                        // Cross shape
                        gc.fillRect(-p.size / 2, -p.size / 4, p.size, p.size / 2);
                        gc.fillRect(-p.size / 4, -p.size / 2, p.size / 2, p.size);
                    }
                    case MAGIC -> {
                        gc.fillOval(-p.size / 2, -p.size / 2, p.size, p.size);
                        // Inner glow
                        gc.setFill(p.color.brighter());
                        gc.fillOval(-p.size / 4, -p.size / 4, p.size / 2, p.size / 2);
                    }
                    case SWORD_SWING -> {
                        gc.fillRect(-p.size, -p.size / 3, p.size * 2, p.size / 1.5);
                    }
                    case WARNING -> {
                        // Triangle warning
                        gc.fillPolygon(
                            new double[]{0, -p.size, p.size},
                            new double[]{-p.size, p.size, p.size},
                            3
                        );
                    }
                    case POISON -> {
                        gc.fillOval(-p.size / 2, -p.size / 2, p.size, p.size);
                        // Bubble effect
                        gc.setFill(p.color.deriveColor(0, 1, 1, 0.3));
                        gc.fillOval(-p.size / 4, -p.size / 4, p.size / 2, p.size / 2);
                    }
                    case LEVEL_UP -> {
                        // Star shape
                        drawStar(gc, p.size);
                    }
                    case DASH -> {
                        gc.fillOval(-p.size / 2, -p.size / 2, p.size, p.size);
                    }
                    case EXP_ORB -> {
                        // Glowing orb
                        gc.setFill(p.color);
                        gc.fillOval(-p.size / 2, -p.size / 2, p.size, p.size);
                        gc.setFill(p.color.brighter());
                        gc.fillOval(-p.size / 4, -p.size / 4, p.size / 2, p.size / 2);
                    }
                }
                
                gc.restore();
            }
        }
    }
    
    private void drawStar(GraphicsContext gc, double size) {
        double[] xPoints = new double[10];
        double[] yPoints = new double[10];
        
        for (int i = 0; i < 10; i++) {
            double angle = i * Math.PI / 5 - Math.PI / 2;
            double radius = (i % 2 == 0) ? size : size / 2;
            xPoints[i] = Math.cos(angle) * radius;
            yPoints[i] = Math.sin(angle) * radius;
        }
        
        gc.fillPolygon(xPoints, yPoints, 10);
    }
    
    public boolean isDead() {
        return life <= 0;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public ParticleType getType() { return type; }
}