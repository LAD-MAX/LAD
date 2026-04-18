package com.example.tank.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageLoader {

    // 图片对象
    public static BufferedImage bg;

    // 静态初始化块，加载所有图片
    static {
        bg = loadImage("bg.png");
    }

    // 加载图片方法
    public static BufferedImage loadImage(String path) {
        try {
            InputStream is = ImageLoader.class.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                System.err.println("图片不存在: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("加载图片失败: " + path);
            e.printStackTrace();
            return null;
        }
    }
}