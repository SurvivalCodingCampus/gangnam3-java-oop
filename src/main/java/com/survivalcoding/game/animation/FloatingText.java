package com.survivalcoding.game.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Small floating combat text that rises and fades.
 */
public class FloatingText {
    private final double x;
    private double y;
    private final String text;
    private final Color color;
    private final double maxLife;
    private double life;

    public FloatingText(double x, double y, String text, Color color) {
        this(x, y, text, color, 1.0);
    }

    public FloatingText(double x, double y, String text, Color color, double lifeSeconds) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.maxLife = Math.max(0.1, lifeSeconds);
        this.life = this.maxLife;
    }

    public void update(double deltaTime) {
        y -= 30 * deltaTime;
        life -= deltaTime;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double alpha = Math.max(0, life / maxLife);
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setFill(color);
        gc.setFont(Font.font(14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, x - cameraX, y - cameraY);
        gc.restore();
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public String getText() { return text; }
    public Color getColor() { return color; }
    public double getLife() { return life; }
}
