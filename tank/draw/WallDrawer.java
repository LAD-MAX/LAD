package com.example.tank.draw;

import java.awt.*;

import com.example.tank.entity.Wall;
import com.example.tank.entity.Block;

public class WallDrawer {
    public static void draw(Graphics g, Wall wall) {
        g.setColor(new Color(165, 80, 50));
        // 用wall.getBlocks()替代直接访问wall.blocks
        for (Block b : wall.getBlocks()) {
            // 用b.getX()、b.getY()替代直接访问b.x、b.y
            g.fillRect(b.getX(), b.getY(), Block.SIZE, Block.SIZE);
        }
    }
}
