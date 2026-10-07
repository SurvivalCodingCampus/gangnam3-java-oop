package com.survivalcoding.game.entity;

import com.survivalcoding.game.animation.FloatingText;
import com.survivalcoding.game.animation.ParticleEffect;
import com.survivalcoding.game.engine.GameState;
import javafx.scene.paint.Color;

/**
 * Temporary status effect applied to an entity.
 */
public class StatusEffect {
    public enum Type {
        POISON,
        BURN,
        STUN
    }

    private final Type type;
    private final double duration;
    private final int damagePerTick;
    private final double tickInterval;
    private double remaining;
    private double tickTimer = 0;

    public StatusEffect(Type type, double duration, int damagePerTick, double tickInterval) {
        this.type = type;
        this.duration = duration;
        this.damagePerTick = damagePerTick;
        this.tickInterval = tickInterval;
        this.remaining = duration;
    }

    public void update(double deltaTime, GameEntity target, GameState gameState) {
        remaining -= deltaTime;

        if (isDamageOverTime() && tickInterval > 0 && remaining > 0) {
            tickTimer += deltaTime;
            while (tickTimer >= tickInterval) {
                tickTimer -= tickInterval;
                target.takeDamage(damagePerTick);
                gameState.addFloatingText(new FloatingText(
                        target.getX(), target.getY() - 20,
                        "-" + damagePerTick,
                        type == Type.POISON ? Color.LIGHTGREEN : Color.ORANGE
                ));
                gameState.addParticle(new ParticleEffect(
                        target.getX(), target.getY(),
                        type == Type.POISON ? ParticleEffect.ParticleType.POISON : ParticleEffect.ParticleType.HIT,
                        type == Type.POISON ? Color.GREEN : Color.ORANGE,
                        8
                ));
            }
        }
    }

    private boolean isDamageOverTime() {
        return type == Type.POISON || type == Type.BURN;
    }

    public boolean isExpired() {
        return remaining <= 0;
    }

    public Type getType() {
        return type;
    }

    public double getRemaining() {
        return Math.max(0, remaining);
    }
}
