package com.example.tank.main;

import com.example.tank.util.ImageLoader;

public class Main {

    public static void main(String[] args) {
        System.out.println("游戏启动中...");
        checkResources();
        new StartUI();
    }

    private static void checkResources() {
        System.out.println("====== 检查资源加载情况 =====");
        System.out.println("背景图片：" + (ImageLoader.bg != null ? "✓" : "✗"));
        System.out.println("==============");
    }
}