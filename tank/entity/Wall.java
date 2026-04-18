package com.example.tank.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wall {
    // 改为private，封装属性
    private List<Block> blocks = new ArrayList<>();
    private int type;
    private Random rand = new Random();

    public Wall(int sx, int sy, int type) {
        this.type = type;
        int s = Block.SIZE;

        int h = 5 + rand.nextInt(4);
        int w = 4 + rand.nextInt(3);

        switch (type) {
            case 1 -> {
                for (int i = 0; i < h; i++) blocks.add(new Block(sx, sy + i * s));
                for (int i = 0; i < w; i++) blocks.add(new Block(sx + i * s, sy + (h-1)*s));
            }
            case 2 -> {
                for (int i = 0; i < w; i++) blocks.add(new Block(sx + i*s, sy));
                for (int i = 1; i < h; i++) blocks.add(new Block(sx + w/2*s, sy + i*s));
            }
            case 3 -> {
                for (int i = 0; i < h; i++) blocks.add(new Block(sx, sy+i*s));
                for (int i = 0; i < h; i++) blocks.add(new Block(sx+(w-1)*s, sy+i*s));
                for (int i = 1; i < w-1; i++) blocks.add(new Block(sx+i*s, sy+h/3*s));
                for (int i = 1; i < w-1; i++) blocks.add(new Block(sx+i*s, sy+h*2/3*s));
            }
            case 4 -> {
                for (int i = 0; i < h; i++) blocks.add(new Block(sx, sy+i*s));
                for (int i = 0; i < w-1; i++) blocks.add(new Block(sx+i*s, sy));
                for (int i = 0; i < w-2; i++) blocks.add(new Block(sx+i*s, sy+h/3*s));
            }
            case 5 -> {
                for (int i = 0; i < h; i++) blocks.add(new Block(sx, sy+i*s));
                for (int i = 0; i < w; i++) blocks.add(new Block(sx+i*s, sy));
                for (int i = 0; i < w; i++) blocks.add(new Block(sx+i*s, sy+h/3*s));
                for (int i = 0; i < w; i++) blocks.add(new Block(sx+i*s, sy+h*2/3*s));
            }
        }
    }

    // 供外部包访问blocks列表
    public List<Block> getBlocks() {
        return blocks;
    }

    // 供外部包访问type属性
    public int getType() {
        return type;
    }
}

