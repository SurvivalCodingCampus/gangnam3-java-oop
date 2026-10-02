package com.survivalcoding.game.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages sprite sheet animations.
 * Supports loading from sprite sheets and rendering frames.
 */
public class AnimationManager {
    
    private final Map<String, AnimationData> animations = new HashMap<>();
    
    public static class AnimationData {
        public final Image spriteSheet;
        public final int frameWidth;
        public final int frameHeight;
        public final int frameCount;
        public final int rows;
        public final int cols;
        public final double frameDuration;
        public final boolean loop;
        
        public AnimationData(Image spriteSheet, int frameWidth, int frameHeight, 
                           int frameCount, int rows, int cols, double frameDuration, boolean loop) {
            this.spriteSheet = spriteSheet;
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
            this.frameCount = frameCount;
            this.rows = rows;
            this.cols = cols;
            this.frameDuration = frameDuration;
            this.loop = loop;
        }
    }
    
    /**
     * Load animation from sprite sheet.
     * Expected layout: frames arranged in rows x cols grid.
     */
    public void loadAnimation(String name, String resourcePath, 
                              int frameWidth, int frameHeight,
                              int frameCount, int rows, int cols,
                              double frameDuration, boolean loop) {
        Image spriteSheet = new Image(getClass().getResourceAsStream(resourcePath));
        animations.put(name, new AnimationData(spriteSheet, frameWidth, frameHeight,
            frameCount, rows, cols, frameDuration, loop));
    }
    
    /**
     * Render animation frame at position.
     */
    public void render(GraphicsContext gc, String animationName, 
                       double x, double y, double scale, int currentFrame, boolean facingRight) {
        AnimationData data = animations.get(animationName);
        if (data == null) return;
        
        int frame = Math.min(currentFrame, data.frameCount - 1);
        int col = frame % data.cols;
        int row = frame / data.cols;
        
        double sx = col * data.frameWidth;
        double sy = row * data.frameHeight;
        
        gc.save();
        
        if (!facingRight) {
            gc.translate(x + data.frameWidth * scale, y);
            gc.scale(-scale, scale);
            gc.translate(-x, -y);
        } else {
            gc.scale(scale, scale);
        }
        
        gc.drawImage(data.spriteSheet, sx, sy, data.frameWidth, data.frameHeight,
                     x, y, data.frameWidth * scale, data.frameHeight * scale);
        
        gc.restore();
    }
    
    public boolean hasAnimation(String name) {
        return animations.containsKey(name);
    }
    
    public AnimationData getAnimation(String name) {
        return animations.get(name);
    }
}