package com.example.tank.entity;

import com.example.tank.util.Dir;

public class Tank {
    private int x, y;
    private Dir dir;
    private final boolean isPlayer;

    // 坦克特性
    private int hp = 3;                  // 血量
    private int maxHp = 3;               // 最大血量
    private int speed = 3;               // 移动速度
    private boolean bulletPierce = false; // 子弹穿墙

    // 冷却时间（玩家可设为0）
    private long lastShootTime = 0;
    private long shootCooldown = 0;      // 0 = 无冷却

    public Tank(int x, int y, Dir dir, boolean isPlayer) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.isPlayer = isPlayer;

        if (isPlayer) {
            this.hp = 3;
            this.maxHp = 3;
            this.shootCooldown = 0;  // 玩家无冷却
        } else {
            this.hp = 1;
            this.maxHp = 1;
            this.shootCooldown = 1500;  // 敌人1.5秒冷却
        }
    }

    // ==================== 特性方法 ====================

    public boolean canShoot() {
        if (shootCooldown == 0) return true;
        return System.currentTimeMillis() - lastShootTime >= shootCooldown;
    }

    public void recordShoot() {
        lastShootTime = System.currentTimeMillis();
    }

    public long getCooldownRemaining() {
        if (shootCooldown == 0) return 0;
        long elapsed = System.currentTimeMillis() - lastShootTime;
        return Math.max(0, shootCooldown - elapsed);
    }

    public long getShootCooldown() {
        return shootCooldown;
    }

    public void takeDamage() {
        if (hp > 0) hp--;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void heal() {
        hp = maxHp;
    }

    // ==================== Getter/Setter ====================

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getSize() { return 40; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public Dir getDir() { return dir; }
    public void setDir(Dir dir) { this.dir = dir; }
    public boolean isPlayer() { return isPlayer; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }

    public boolean isBulletPierce() { return bulletPierce; }
    public void setBulletPierce(boolean bulletPierce) { this.bulletPierce = bulletPierce; }

    public void setShootCooldown(long cooldown) { this.shootCooldown = cooldown; }
}