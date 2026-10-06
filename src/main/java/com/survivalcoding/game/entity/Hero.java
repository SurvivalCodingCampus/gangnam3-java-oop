package com.survivalcoding.game.entity;

import com.survivalcoding.game.GameConfig;
import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import com.survivalcoding.game.animation.Animation;
import com.survivalcoding.game.animation.AnimationManager;
import com.survivalcoding.game.animation.ParticleEffect;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

/**
 * Player-controlled hero character.
 * Supports movement, attack, magic, dash, and items.
 */
public class Hero extends GameEntity {
    
    // Hero stats
    private int attackDamage = 15;
    private int magicDamage = 30;
    private int mp = 50;
    private int maxMp = 50;
    private int level = 1;
    private int exp = 0;
    private int expToNextLevel = 100;
    
    // Cooldowns
    private double attackCooldown = 0;
    private double magicCooldown = 0;
    private double dashCooldown = 0;
    private double itemCooldown = 0;
    
    
    // State
    private boolean isDashing = false;
    private double dashTimer = 0;
    private double dashDirectionX = 0;
    private double dashDirectionY = 0;
    
    // Animations
    private Animation idleAnimation;
    private Animation walkAnimation;
    private Animation attackAnimation;
    private Animation dashAnimation;
    private Animation hurtAnimation;
    private Animation currentAnimation;
    
    // Visual
    private final Random random = new Random();
    private double swordSwingAngle = 0;
    private double swordSwingTimer = 0;
    private boolean isAttacking = false;
    
    public Hero(String name, double x, double y) {
        super(x, y, 48, 64, 100);
        this.frameCount = 4;
        this.frameDuration = 0.15;
        initializeAnimations();
    }
    
    private void initializeAnimations() {
        // Create procedural animations (since we don't have sprite sheets)
        idleAnimation = new Animation("idle", 4, 0.2, true);
        walkAnimation = new Animation("walk", 6, 0.1, true);
        attackAnimation = new Animation("attack", 4, 0.08, false);
        dashAnimation = new Animation("dash", 3, 0.05, false);
        hurtAnimation = new Animation("hurt", 2, 0.1, false);
        
        currentAnimation = idleAnimation;
    }
    
    @Override
    public void update(double deltaTime, InputHandler input, GameState gameState) {
        // Update cooldowns
        updateCooldowns(deltaTime);

        if (isStunned()) {
            velocityX = 0;
            velocityY = 0;
            updatePosition(deltaTime, gameState);
            updateAnimation(deltaTime);
            return;
        }

        // Handle dash state
        updateDash(deltaTime);
        
        // updatePosition은 프레임당 한 번만 호출해야 한다 (두 번이면 이동 속도가 2배가 된다)
        if (isDashing) {
            updatePosition(deltaTime, gameState);
        } else {
            handleMovement(input, deltaTime, gameState);
        }
        
        // Handle actions
        handleActions(input, gameState);
        
        // Update animation
        updateAnimationState(deltaTime);
        
        // Update sword swing effect
        if (swordSwingTimer > 0) {
            swordSwingTimer -= deltaTime;
            swordSwingAngle += deltaTime * 15; // Fast swing
        }
        
        // Update base animation timer
        updateAnimation(deltaTime);
        
        // Regenerate MP slowly
        if (mp < maxMp) {
            mp = Math.min(mp + (int)(deltaTime * GameConfig.HERO_MP_REGEN_PER_SECOND), maxMp);
        }
    }
    
    private void updateCooldowns(double deltaTime) {
        if (attackCooldown > 0) attackCooldown -= deltaTime;
        if (magicCooldown > 0) magicCooldown -= deltaTime;
        if (dashCooldown > 0) dashCooldown -= deltaTime;
        if (itemCooldown > 0) itemCooldown -= deltaTime;
    }
    
    private void updateDash(double deltaTime) {
        if (isDashing) {
            dashTimer -= deltaTime;
            
            // Apply dash velocity
            velocityX = dashDirectionX * speed * GameConfig.HERO_DASH_SPEED_MULTIPLIER;
            velocityY = dashDirectionY * speed * GameConfig.HERO_DASH_SPEED_MULTIPLIER;
            
            // Invincibility during dash
            hitFlashTimer = Math.max(hitFlashTimer, 0.01);
            
            if (dashTimer <= 0) {
                isDashing = false;
                velocityX *= 0.5;
                velocityY *= 0.5;
            }
        }
    }
    
    private void handleMovement(InputHandler input, double deltaTime, GameState gameState) {
        double moveX = input.getMoveX();
        double moveY = input.getMoveY();
        
        // Normalize diagonal movement
        if (moveX != 0 && moveY != 0) {
            double len = Math.sqrt(moveX * moveX + moveY * moveY);
            moveX /= len;
            moveY /= len;
        }
        
        // Set velocity
        velocityX = moveX * speed;
        velocityY = moveY * speed;
        
        // Update facing direction
        if (moveX > 0) facingRight = true;
        else if (moveX < 0) facingRight = false;
        
        updatePosition(deltaTime, gameState);
    }
    
    private void handleActions(InputHandler input, GameState gameState) {
        // Attack (Space)
        if (input.isAttackPressed() && attackCooldown <= 0) {
            performAttack(gameState);
        }
        
        // Magic (M)
        if (input.isMagicPressed() && magicCooldown <= 0 && mp >= GameConfig.HERO_MAGIC_COST) {
            performMagic(gameState);
        }
        
        // Dash (Shift)
        if (input.isDashPressed() && dashCooldown <= 0) {
            performDash(input);
        }
        
        // Item (E)
        if (input.isItemPressed() && itemCooldown <= 0) {
            useItem(gameState);
        }
    }
    
    private void performAttack(GameState gameState) {
        attackCooldown = GameConfig.HERO_ATTACK_COOLDOWN_SECONDS;
        isAttacking = true;
        swordSwingTimer = 0.3;
        swordSwingAngle = facingRight ? -Math.PI / 2 : Math.PI / 2;
        
        currentAnimation = attackAnimation;
        currentAnimation.reset();
        
        // Create attack hitbox
        double range = GameConfig.HERO_ATTACK_RANGE;
        double attackX = x + (facingRight ? range / 2 : -range / 2);
        double attackY = y;
        
        // Check monster collisions
        for (Monster monster : gameState.getMonsters()) {
            if (monster.isAlive()) {
                double dx = monster.getX() - attackX;
                double dy = monster.getY() - attackY;
                double dist = Math.sqrt(dx * dx + dy * dy);
                
                if (dist < range / 2 + monster.getWidth() / 2) {
                    monster.takeDamage(attackDamage);
                    gameState.addScore(10);
                    gameState.addFloatingText(new com.survivalcoding.game.animation.FloatingText(
                        monster.getX(), monster.getY() - 20,
                        "-" + attackDamage, Color.RED));
                    
                    // Knockback
                    double kbAngle = Math.atan2(monster.getY() - y, monster.getX() - x);
                    monster.applyKnockback(Math.cos(kbAngle) * 300, Math.sin(kbAngle) * 300);
                    
                    // Hit particles
                    gameState.addParticle(new ParticleEffect(
                        monster.getX(), monster.getY(),
                        ParticleEffect.ParticleType.HIT,
                        Color.RED, 8
                    ));
                    
                    // Screen shake
                    gameState.shakeCamera(0.3);
                    
                    if (!monster.isAlive()) {
                        gameState.incrementMonstersKilled();
                        gameState.addScore(50);
                        
                        // Death particles
                        for (int i = 0; i < 15; i++) {
                            gameState.addParticle(new ParticleEffect(
                                monster.getX(), monster.getY(),
                                ParticleEffect.ParticleType.DEATH,
                                Color.ORANGE, 20
                            ));
                        }
                    }
                }
            }
        }
        
        // Attack visual effect
        gameState.addParticle(new ParticleEffect(
            attackX, attackY,
            ParticleEffect.ParticleType.SWORD_SWING,
            facingRight ? Color.CYAN : Color.YELLOW,
            12
        ));
    }
    
    private void performMagic(GameState gameState) {
        magicCooldown = GameConfig.HERO_MAGIC_COOLDOWN_SECONDS;
        mp -= GameConfig.HERO_MAGIC_COST;
        
        // Fireball projectile
        double angle = facingRight ? 0 : Math.PI;
        double startX = x + (facingRight ? width / 2 + 10 : -width / 2 - 10);
        double startY = y - 10;
        
        Projectile fireball = new Projectile(
            startX, startY,
            Math.cos(angle) * GameConfig.HERO_MAGIC_PROJECTILE_SPEED,
            Math.sin(angle) * GameConfig.HERO_MAGIC_PROJECTILE_SPEED,
            magicDamage, 16, 16, Color.ORANGE, Projectile.Type.FIREBALL
        );
        gameState.addProjectile(fireball);
        
        // Cast particles
        for (int i = 0; i < 10; i++) {
            gameState.addParticle(new ParticleEffect(
                x, y - 20,
                ParticleEffect.ParticleType.MAGIC,
                Color.ORANGE, 15
            ));
        }
        
        gameState.shakeCamera(0.5);
    }
    
    private void performDash(InputHandler input) {
        dashCooldown = GameConfig.HERO_DASH_COOLDOWN_SECONDS;
        isDashing = true;
        dashTimer = GameConfig.HERO_DASH_DURATION_SECONDS;
        
        double moveX = input.getMoveX();
        double moveY = input.getMoveY();
        
        // If no movement input, dash in facing direction
        if (moveX == 0 && moveY == 0) {
            moveX = facingRight ? 1 : -1;
        }
        
        // Normalize
        double len = Math.sqrt(moveX * moveX + moveY * moveY);
        dashDirectionX = moveX / len;
        dashDirectionY = moveY / len;
        
        // Dash particles
        // (Added in render via gameState)
    }
    
    private void useItem(GameState gameState) {
        itemCooldown = GameConfig.HERO_ITEM_COOLDOWN_SECONDS;
        
        // Heal potion
        int healAmount = GameConfig.HERO_POTION_HEAL;
        int oldHp = hp;
        heal(healAmount);
        int actualHeal = hp - oldHp;
        
        if (actualHeal > 0) {
            gameState.addFloatingText(new com.survivalcoding.game.animation.FloatingText(
                x, y - 20, "+" + actualHeal, Color.LIGHTGREEN));
            // Heal particles
            for (int i = 0; i < 10; i++) {
                gameState.addParticle(new ParticleEffect(
                    x + (random.nextDouble() - 0.5) * 30,
                    y + (random.nextDouble() - 0.5) * 30,
                    ParticleEffect.ParticleType.HEAL,
                    Color.GREEN, 15
                ));
            }
        }
        
        gameState.shakeCamera(0.1);
    }
    
    private void updateAnimationState(double deltaTime) {
        if (isDashing) {
            currentAnimation = dashAnimation;
        } else if (isAttacking) {
            currentAnimation = attackAnimation;
            if (attackAnimation.isFinished()) {
                isAttacking = false;
                currentAnimation = idleAnimation;
            }
        } else if (velocityX != 0 || velocityY != 0) {
            currentAnimation = walkAnimation;
        } else {
            currentAnimation = idleAnimation;
        }
        
        currentAnimation.update(deltaTime);
        currentFrame = currentAnimation.getCurrentFrame();
    }
    
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        
        if (isAlive()) {
            currentAnimation = hurtAnimation;
            currentAnimation.reset();
        }
    }
    
    @Override
    protected void onDeath() {
        // Death animation handled in renderer
    }
    
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double renderX = x - cameraX;
        double renderY = y - cameraY;
        
        // Don't render if off screen (with margin)
        if (renderX < -width || renderX > gc.getCanvas().getWidth() + width ||
            renderY < -height || renderY > gc.getCanvas().getHeight() + height) {
            return;
        }
        
        gc.save();
        
        // Hit flash effect
        if (isHitFlashing()) {
            gc.setGlobalAlpha(0.5 + 0.5 * Math.sin(hitFlashTimer * 100));
        }
        
        // Flip for facing direction
        if (!facingRight) {
            gc.translate(renderX + width / 2, renderY);
            gc.scale(-1, 1);
            gc.translate(-(renderX + width / 2), -renderY);
        }
        
        // Draw hero body (procedural)
        drawHero(gc, renderX, renderY);
        
        // Draw sword swing effect
        if (swordSwingTimer > 0) {
            drawSwordSwing(gc, renderX, renderY);
        }
        
        // Draw HP bar above hero
        drawHealthBar(gc, renderX, renderY - 40);
        
        // Draw MP bar
        drawManaBar(gc, renderX, renderY - 30);
        
        gc.restore();
    }
    
    private void drawHero(GraphicsContext gc, double rx, double ry) {
        // Body
        gc.setFill(Color.web("#4A90D9"));
        gc.fillOval(rx + 8, ry + 16, 32, 32);
        
        // Head
        gc.setFill(Color.web("#FFDBAC"));
        gc.fillOval(rx + 12, ry + 4, 24, 24);
        
        // Eyes
        gc.setFill(Color.BLACK);
        gc.fillOval(rx + 18, ry + 10, 4, 4);
        gc.fillOval(rx + 28, ry + 10, 4, 4);
        
        // Hair
        gc.setFill(Color.web("#8B4513"));
        gc.fillOval(rx + 10, ry + 2, 28, 14);
        
        // Cape
        gc.setFill(Color.web("#8B0000"));
        gc.fillOval(rx + 4, ry + 20, 40, 20);
        
        // Arms
        gc.setFill(Color.web("#FFDBAC"));
        double armOffset = Math.sin(animationTimer * 10) * 3;
        gc.fillOval(rx + 2, ry + 20 + armOffset, 10, 20);
        gc.fillOval(rx + 36, ry + 20 - armOffset, 10, 20);
        
        // Legs (walking animation)
        double legOffset = Math.sin(animationTimer * 15) * 5;
        gc.setFill(Color.web("#2E4A8E"));
        gc.fillOval(rx + 12, ry + 44 + legOffset, 12, 20);
        gc.fillOval(rx + 24, ry + 44 - legOffset, 12, 20);
        
        // Sword (always visible)
        gc.setFill(Color.web("#C0C0C0"));
        gc.fillRect(rx + 38, ry + 20, 4, 30);
        gc.setFill(Color.web("#8B4513"));
        gc.fillRect(rx + 36, ry + 48, 8, 8);
    }
    
    private void drawSwordSwing(GraphicsContext gc, double rx, double ry) {
        gc.save();
        gc.translate(rx + width / 2, ry + height / 2);
        gc.rotate(Math.toDegrees(swordSwingAngle));
        
        double alpha = swordSwingTimer / 0.3;
        gc.setGlobalAlpha(alpha);
        
        // Sword trail
        gc.setStroke(Color.web("#00FFFF", alpha));
        gc.setLineWidth(4);
        gc.strokeLine(0, 0, 60, 0);
        
        gc.setStroke(Color.web("#FFFFFF", alpha * 0.5));
        gc.setLineWidth(2);
        gc.strokeLine(0, 0, 80, 0);
        
        gc.restore();
    }
    
    private void drawHealthBar(GraphicsContext gc, double rx, double ry) {
        double barWidth = 50;
        double barHeight = 6;
        
        // Background
        gc.setFill(Color.web("#333333"));
        gc.fillRect(rx + (width - barWidth) / 2, ry, barWidth, barHeight);
        
        // Health
        double hpRatio = (double) hp / maxHp;
        Color hpColor = hpRatio > 0.5 ? Color.GREEN : (hpRatio > 0.25 ? Color.YELLOW : Color.RED);
        gc.setFill(hpColor);
        gc.fillRect(rx + (width - barWidth) / 2, ry, barWidth * hpRatio, barHeight);
        
        // Border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.strokeRect(rx + (width - barWidth) / 2, ry, barWidth, barHeight);
    }
    
    private void drawManaBar(GraphicsContext gc, double rx, double ry) {
        double barWidth = 50;
        double barHeight = 4;
        
        gc.setFill(Color.web("#333333"));
        gc.fillRect(rx + (width - barWidth) / 2, ry, barWidth, barHeight);
        
        double mpRatio = (double) mp / maxMp;
        gc.setFill(Color.web("#0088FF"));
        gc.fillRect(rx + (width - barWidth) / 2, ry, barWidth * mpRatio, barHeight);
    }
    
    public void addExp(int amount) {
        exp += amount;
        while (exp >= expToNextLevel) {
            exp -= expToNextLevel;
            levelUp();
        }
    }
    
    private void levelUp() {
        level++;
        maxHp += 20;
        hp = maxHp;
        maxMp += 10;
        mp = maxMp;
        attackDamage += 3;
        magicDamage += 5;
        expToNextLevel = (int)(expToNextLevel * 1.5);
    }
    
    // Getters
    public int getAttackDamage() { return attackDamage; }
    public int getMagicDamage() { return magicDamage; }
    public int getMp() { return mp; }
    public int getMaxMp() { return maxMp; }
    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getExpToNextLevel() { return expToNextLevel; }
    public double getAttackCooldown() { return attackCooldown; }
    public double getMagicCooldown() { return magicCooldown; }
    public double getDashCooldown() { return dashCooldown; }
    public boolean isDashing() { return isDashing; }
    public boolean isAttacking() { return isAttacking; }
}