package com.example.tank.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Wall {
    private List<Block> blocks = new ArrayList<>();
    private int type;
    private Random rand = new Random();
    public Wall(int x, int y, int type) {
        this.type = type;
        int s = Block.SIZE;
        switch (type) {
            case 1 -> makeBrickWall(x, y);
            case 2 -> makeSteelWall(x, y);
            case 3 -> makeBrickSquare(x, y);
            case 4 -> makeSteelSquare(x, y);
            default -> makeBrickWall(x, y);
        }
    }
    public static Wall createSingleBlock(int x, int y, Block.BlockType type) {
        Wall wall = new Wall(0, 0, 1);  // 临时
        wall.type = (type == Block.BlockType.BRICK) ? 1 : 2;
        wall.blocks.clear();
        wall.blocks.add(new Block(x, y, type));
        return wall;
    }
    private void makeBrickWall(int sx, int sy) {
        int len = 6 + rand.nextInt(5);
        for (int i = 0; i < len; i++) {
            blocks.add(new Block(sx + i * Block.SIZE, sy, Block.BlockType.BRICK));
        }
    }
    private void makeSteelWall(int sx, int sy) {
        int len = 4 + rand.nextInt(4);
        for (int i = 0; i < len; i++) {
            blocks.add(new Block(sx + i * Block.SIZE, sy, Block.BlockType.STEEL));
        }
    }
    private void makeBrickSquare(int sx, int sy) {
        int size = 3;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blocks.add(new Block(sx + i * Block.SIZE, sy + j * Block.SIZE, Block.BlockType.BRICK));
            }
        }
    }
    private void makeSteelSquare(int sx, int sy) {
        int size = 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blocks.add(new Block(sx + i * Block.SIZE, sy + j * Block.SIZE, Block.BlockType.STEEL));
            }
        }
    }
    private void makeMixedWall(int sx, int sy) {
        int w = 4, h = 3;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Block.BlockType bt = (i == 0 || i == w-1 || j == 0 || j == h-1)
                        ? Block.BlockType.STEEL : Block.BlockType.BRICK;
                blocks.add(new Block(sx + i * Block.SIZE, sy + j * Block.SIZE, bt));
            }
        }
    }
    private void makeBrickLine(int sx, int sy) {
        int len = 8 + rand.nextInt(6);
        boolean horizontal = rand.nextBoolean();
        for (int i = 0; i < len; i++) {
            int x = sx + (horizontal ? i * Block.SIZE : 0);
            int y = sy + (horizontal ? 0 : i * Block.SIZE);
            blocks.add(new Block(x, y, Block.BlockType.BRICK));
        }
    }
    private void makeSteelLine(int sx, int sy) {
        int len = 5 + rand.nextInt(4);
        boolean horizontal = rand.nextBoolean();
        for (int i = 0; i < len; i++) {
            int x = sx + (horizontal ? i * Block.SIZE : 0);
            int y = sy + (horizontal ? 0 : i * Block.SIZE);
            blocks.add(new Block(x, y, Block.BlockType.STEEL));
        }
    }
    private void makeFortress(int sx, int sy) {
        int size = 5;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Block.BlockType bt;
                if ((i == 0 && j == 0) || (i == 0 && j == size-1) ||
                        (i == size-1 && j == 0) || (i == size-1 && j == size-1)) {
                    bt = Block.BlockType.STEEL;
                } else {
                    bt = Block.BlockType.BRICK;
                }
                blocks.add(new Block(sx + i * Block.SIZE, sy + j * Block.SIZE, bt));
            }
        }
    }

    public List<Block> getBlocks() { return blocks; }
    public int getType() { return type; }
}