package com.example.tank.draw;

import java.awt.*;

import com.example.tank.entity.Tank;

public class TankDrawer {
    public static void draw(Graphics g, Tank t) {
        // 用getter方法替代直接访问私有变量
        int x = t.getX();
        int y = t.getY();
        int size = t.getSize();

        Color bodyColor = t.isPlayer() ? new Color(40, 180, 60) : new Color(180, 50, 40);
        Color turretColor = t.isPlayer() ? new Color(20, 140, 40) : new Color(140, 30, 20);

        int cx = x + size / 2;
        int cy = y + size / 2;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(cx, cy);

        // 用getDir()获取方向
        switch (t.getDir()) {
            case UP:    break;
            case DOWN:  g2d.rotate(Math.PI);     break;
            case LEFT:  g2d.rotate(-Math.PI/2);  break;
            case RIGHT: g2d.rotate(Math.PI/2);   break;
        }

        g2d.setColor(new Color(90, 90, 90));
        g2d.fillRect(-size/2, -size/2, 6, size);
        g2d.fillRect(size/2 - 6, -size/2, 6, size);

        g2d.setColor(bodyColor);
        g2d.fillRect(-size/2 + 6, -size/2 + 6, size - 12, size - 12);

        g2d.setColor(turretColor);
        int turretSize = size / 2;
        g2d.fillOval(-turretSize/2, -turretSize/2, turretSize, turretSize);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(0, 0, 0, -size/2 - 4);

        g2d.dispose();
    }
}