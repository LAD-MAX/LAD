package com.example.tank.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ImageLoader {

    // 图片对象
    public static BufferedImage bg;

    // 静态初始化块，加载所有图片
    static {
        bg = loadImage("/bg.png");  // 改路径
    }

    // 加载图片方法
    public static BufferedImage loadImage(String path) {
        try {
            // 方法1：从 classpath 加载
            URL url = ImageLoader.class.getResource(path);
            if (url != null) {
                System.out.println("图片加载成功(URL): " + path);
                return ImageIO.read(url);
            }

            // 方法2：从文件系统加载
            File file = new File("resources" + path.replace("/", File.separator));
            if (file.exists()) {
                System.out.println("图片加载成功(File): " + file.getAbsolutePath());
                return ImageIO.read(file);
            }

            System.err.println("图片不存在: " + path);
            return null;
        } catch (Exception e) {
            System.err.println("加载图片失败: " + path);
            e.printStackTrace();
            return null;
        }
    }
}