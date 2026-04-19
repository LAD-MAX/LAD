// 文件路径: src/com/example/tank/util/AudioManager.java
package com.example.tank.util;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

public class AudioManager {
    private static Clip bgmClip;

    // 播放 BGM
    public static void playBGM(Class<?> clazz) {
        stopBGM();
        try {
            // 尝试从 classpath 加载
            URL url = clazz.getResource("/bgm.wav");
            if (url == null) {
                // 备用：直接从文件系统加载
                File file = new File("resources/bgm.wav");
                if (file.exists()) {
                    url = file.toURI().toURL();
                } else {
                    System.err.println("BGM 文件未找到");
                    return;
                }
            }
            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(stream);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
            System.out.println("BGM 开始播放");
        } catch (Exception e) {
            System.err.println("BGM 播放失败: " + e.getMessage());
        }
    }

    // 停止 BGM
    public static void stopBGM() {
        if (bgmClip != null) {
            try {
                bgmClip.stop();
                bgmClip.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bgmClip = null;
            System.out.println("BGM 已停止");
        }
    }

    // 播放音效
    public static void playSound(Class<?> clazz, String fileName) {
        new Thread(() -> {
            try {
                // 尝试从 classpath 加载
                URL url = clazz.getResource("/" + fileName);
                if (url == null) {
                    // 备用：直接从文件系统加载
                    File file = new File("resources/" + fileName);
                    if (file.exists()) {
                        url = file.toURI().toURL();
                    } else {
                        System.err.println("音效文件未找到: " + fileName);
                        return;
                    }
                }
                AudioInputStream stream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (Exception e) {
                System.err.println("音效播放失败: " + fileName);
            }
        }).start();
    }
}