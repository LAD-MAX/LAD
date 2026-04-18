package com.example.tank.util;

import javax.sound.sampled.*;
import java.io.InputStream;

public class AudioPlayer {
    private Clip clip;

    // 音效对象
    public static AudioPlayer bgm;
    public static AudioPlayer boom;
    public static AudioPlayer fire;
    public static AudioPlayer hit;

    // 静态初始化块，加载所有音效
    static {
        bgm = loadAudio("bgm.wav");
        boom = loadAudio("boom.wav");
        fire = loadAudio("fire.wav");
        hit = loadAudio("hit.wav");
    }

    // 加载音效方法
    public static AudioPlayer loadAudio(String path) {
        try {
            InputStream is = AudioPlayer.class.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                System.err.println("音效不存在: " + path);
                return null;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            AudioPlayer player = new AudioPlayer();
            player.clip = clip;
            return player;
        } catch (Exception e) {
            System.err.println("加载音效失败: " + path);
            return null;
        }
    }

    // 播放一次
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // 循环播放（背景音乐）
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // 停止播放
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}