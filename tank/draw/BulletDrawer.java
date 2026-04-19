package com.example.tank.draw;

import java.awt.*;
import com.example.tank.entity.Bullet;
import com.example.tank.entity.HitEffect;

public class BulletDrawer {
    public static void drawBullet(Graphics g, Bullet b) {
        g.setColor(Color.YELLOW);
        g.fillOval(b.getX(), b.getY(), b.getSize(), b.getSize());
    }

    public static void drawHitEffect(Graphics g, HitEffect e) {
        // 限制 alpha 值不超过 255
        int alpha = Math.min(255, e.getTimer() * 30);
        g.setColor(new Color(255, 255, 255, alpha));
        g.fillOval(e.getX() - 8, e.getY() - 8, 16, 16);
    }
}