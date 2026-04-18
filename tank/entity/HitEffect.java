package com.example.tank.entity;

public class HitEffect {
    private int x;
    private int y;
    private int timer = 10;

    public HitEffect(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getTimer() { return timer; }
    public void setTimer(int timer) { this.timer = timer; }
}