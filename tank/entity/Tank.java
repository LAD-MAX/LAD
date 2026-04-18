package com.example.tank.entity;

import com.example.tank.util.Dir;

public class Tank {
    // 改为private封装属性
    private int x, y;
    private int size = 40;
    private int speed = 3;
    private Dir dir;
    private boolean isPlayer;

    // 构造方法（和你原来的保持一致）
    public Tank(int x, int y, Dir dir, boolean isPlayer) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.isPlayer = isPlayer;
    }

    // -------------------------- 补全的getter/setter方法 --------------------------
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public int getSpeed() {
        return speed;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public boolean isPlayer() {
        return isPlayer;
    }
}