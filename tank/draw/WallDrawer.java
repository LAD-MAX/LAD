package com.example.tank.draw;

import java.awt.*;
import com.example.tank.entity.Wall;
import com.example.tank.entity.Block;

public class WallDrawer {

    // 砖墙颜色
    private static final Color BRICK_COLOR = new Color(180, 100, 50);
    private static final Color BRICK_BORDER = new Color(140, 70, 30);
    private static final Color BRICK_HIGHLIGHT = new Color(200, 130, 80);

    // 钢墙颜色
    private static final Color STEEL_COLOR = new Color(180, 180, 190);
    private static final Color STEEL_BORDER = new Color(120, 120, 130);
    private static final Color STEEL_HIGHLIGHT = new Color(220, 220, 230);

    public static void draw(Graphics g, Wall wall) {
        for (Block b : wall.getBlocks()) {
            drawBlock(g, b);
        }
    }

    private static void drawBlock(Graphics g, Block b) {
        int x = b.getX();
        int y = b.getY();
        int size = Block.SIZE;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (b.getType() == Block.BlockType.BRICK) {
            drawBrick(g2d, x, y, size, b);
        } else {
            drawSteel(g2d, x, y, size);
        }

        g2d.dispose();
    }

    // 绘制砖墙 - 带裂纹效果
    private static void drawBrick(Graphics2D g, int x, int y, int size, Block b) {
        // 根据剩余血量改变颜色
        float hpPercent = (float) b.getHp() / b.getMaxHp();
        Color baseColor;
        if (hpPercent > 0.6f) {
            baseColor = BRICK_COLOR;
        } else if (hpPercent > 0.3f) {
            baseColor = new Color(160, 80, 40);
        } else {
            baseColor = new Color(120, 50, 20);
        }

        // 主色
        g.setColor(baseColor);
        g.fillRect(x, y, size, size);

        // 边框
        g.setColor(BRICK_BORDER);
        g.drawRect(x, y, size - 1, size - 1);

        // 高光
        g.setColor(BRICK_HIGHLIGHT);
        g.drawLine(x + 1, y + 1, x + size - 2, y + 1);
        g.drawLine(x + 1, y + 1, x + 1, y + size - 2);

        // 砖纹 - 水平线
        g.setColor(BRICK_BORDER);
        g.drawLine(x, y + size/2, x + size, y + size/2);

        // 裂纹效果（血量越低裂纹越多）
        if (hpPercent < 0.6f) {
            g.setColor(new Color(80, 30, 10));
            g.drawLine(x + 2, y + 2, x + size - 3, y + size - 3);
        }
        if (hpPercent < 0.3f) {
            g.setColor(new Color(80, 30, 10));
            g.drawLine(x + size - 3, y + 2, x + 2, y + size - 3);
        }
    }

    // 绘制钢墙 - 金属质感
    private static void drawSteel(Graphics2D g, int x, int y, int size) {
        // 渐变效果
        GradientPaint gradient = new GradientPaint(
                x, y, new Color(200, 200, 210),
                x + size, y + size, new Color(150, 150, 160)
        );
        g.setPaint(gradient);
        g.fillRect(x, y, size, size);
        g.setColor(STEEL_BORDER);
        g.drawRect(x, y, size - 1, size - 1);

        // 高光
        g.setColor(STEEL_HIGHLIGHT);
        g.drawLine(x + 1, y + 1, x + size - 2, y + 1);
        g.drawLine(x + 1, y + 1, x + 1, y + size - 2);
        g.setColor(new Color(100, 100, 110));
        int dotSize = 2;
        g.fillOval(x + 2, y + 2, dotSize, dotSize);
        g.fillOval(x + size - 4, y + 2, dotSize, dotSize);
        g.fillOval(x + 2, y + size - 4, dotSize, dotSize);
        g.fillOval(x + size - 4, y + size - 4, dotSize, dotSize);
        g.setColor(new Color(80, 80, 90));
        g.drawLine(x + size/2, y + 1, x + size/2, y + size - 2);
        g.drawLine(x + 1, y + size/2, x + size - 2, y + size/2);
    }
}