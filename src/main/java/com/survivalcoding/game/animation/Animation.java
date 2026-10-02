package com.survivalcoding.game.animation;

/**
 * Simple animation class for frame-based animations.
 */
public class Animation {
    
    private final String name;
    private final int frameCount;
    private final double frameDuration;
    private final boolean loop;
    
    private int currentFrame = 0;
    private double timer = 0;
    private boolean finished = false;
    
    public Animation(String name, int frameCount, double frameDuration, boolean loop) {
        this.name = name;
        this.frameCount = frameCount;
        this.frameDuration = frameDuration;
        this.loop = loop;
    }
    
    public void update(double deltaTime) {
        if (finished && !loop) return;
        
        timer += deltaTime;
        if (timer >= frameDuration) {
            timer = 0;
            currentFrame++;
            
            if (currentFrame >= frameCount) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frameCount - 1;
                    finished = true;
                }
            }
        }
    }
    
    public void reset() {
        currentFrame = 0;
        timer = 0;
        finished = false;
    }
    
    public int getCurrentFrame() { return currentFrame; }
    public int getFrameCount() { return frameCount; }
    public double getFrameDuration() { return frameDuration; }
    public boolean isLoop() { return loop; }
    public boolean isFinished() { return finished; }
    public String getName() { return name; }
}