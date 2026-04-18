package com.example.tank.entity;

import java.awt.*;

public class Block {
    // 改为private封装，同时把SIZE改为public static final（规范常量写法）
    private int x, y;
    public static final int SIZE = 8; // 常量改为public，跨包可直接访问
    private int hp = 10;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHit(int bx, int by) {
        return new Rectangle(bx, by, 10, 10).intersects(
                new Rectangle(x, y, SIZE, SIZE));
    }

    // 提供public的getter方法，供外部包访问私有属性
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}