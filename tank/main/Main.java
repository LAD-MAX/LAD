package com.example.tank.main;

// 添加这两个导入
import com.example.tank.util.ImageLoader;
import com.example.tank.util.AudioPlayer;



public class Main {


    public static void main(String[] args) {
        System.out.println("游戏启动中...");

        // 检查资源加载情况
        checkResources();

        // 播放背景音乐
        if (AudioPlayer.bgm != null) {
            AudioPlayer.bgm.loop();
            System.out.println("背景音乐已启动");
        } else {
            System.out.println("背景音乐加载失败，继续运行游戏");
        }

        // 启动游戏界面
        new StartUI();
    }

    // 检查资源加载情况
    private static void checkResources() {
        System.out.println("====== 检查资源加载情况 =====");
        System.out.println("背景图片：" + (ImageLoader.bg != null ? "✓" : "✗"));
        System.out.println("背景音乐：" + (AudioPlayer.bgm != null ? "✓" : "✗"));
        System.out.println("爆炸音效：" + (AudioPlayer.boom != null ? "✓" : "✗"));
        System.out.println("射击音效：" + (AudioPlayer.fire != null ? "✓" : "✗"));
        System.out.println("击中音效：" + (AudioPlayer.hit != null ? "✓" : "✗"));
        System.out.println("==============");
    }
}
