package com.example.tank.entity;

import com.example.tank.util.Dir;

public class Bullet {
    private int x, y;
    private Dir dir;
    private int speed = 6;
    private int size = 10; // 子弹的大小，用于绘制
    private Tank owner;

    public Bullet(int x, int y, Dir dir, Tank owner) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.owner = owner;
    }

    // 补全getter方法
    public int getX() { return x; }
    public int getY() { return y; }
    public Dir getDir() { return dir; }
    public int getSpeed() { return speed; }
    public int getSize() { return size; } // 新增的getSize()
    public Tank getOwner() { return owner; }

    // 补全setter方法
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}