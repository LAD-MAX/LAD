package com.example.tank.entity;

import java.awt.*;

public class Block {
    private int x, y;
    public static final int SIZE = 8;
    private int hp;
    public enum BlockType {
        BRICK,   // 砖墙 - 可摧毁
        STEEL    // 钢墙 - 不可摧毁
    }
    private BlockType type;
    private int maxHp;
    public Block(int x, int y) {
        this(x, y, BlockType.BRICK);
    }
    public Block(int x, int y, BlockType type) {
        this.x = x;
        this.y = y;
        this.type = type;

        if (type == BlockType.BRICK) {
            this.hp = 10;
            this.maxHp = 10;
        } else {
            this.hp = Integer.MAX_VALUE;  // 钢墙不可摧毁
            this.maxHp = Integer.MAX_VALUE;
        }
    }
    public boolean isHit(int bx, int by) {
        return new Rectangle(bx, by, 10, 10).intersects(
                new Rectangle(x, y, SIZE, SIZE));
    }
    public boolean isDestructible() {
        return type == BlockType.BRICK;
    }
    public BlockType getType() {
        return type;
    }
    public void takeDamage(int damage) {
        if (type == BlockType.BRICK) {
            hp -= damage;
        }
    }
    public boolean isDestroyed() {
        return hp <= 0;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
}