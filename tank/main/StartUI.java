package com.example.tank.main;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.io.File;

public class StartUI extends JFrame {
    private Clip bgm;
    private Image bgImage;

    public StartUI() {
        testResourcePath();  // ← 调用测试

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadBackgroundImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setLayout(new GridBagLayout());
        add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);



        JButton btnStart = createStyledButton("开始游戏");
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(btnStart, gbc);

        JButton btnExit = createStyledButton("退出游戏");
        gbc.gridy = 2;
        panel.add(btnExit, gbc);

        // 合并成一个 ActionListener
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("开始游戏按钮被点击，准备停止 BGM");
                stopBGM();
                System.out.println("BGM 停止完成，关闭窗口");
                dispose();
                System.out.println("创建游戏窗口");
                new GameFrame();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBGM();
                dispose();
                System.exit(0);
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopBGM();
            }
        });

        setVisible(true);
        playBGM();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("黑体", Font.BOLD, 40));
        button.setPreferredSize(new Dimension(300, 80));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.YELLOW);
                button.setFont(new Font("黑体", Font.BOLD, 44));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
                button.setFont(new Font("黑体", Font.BOLD, 40));
            }
        });

        return button;
    }

    private void loadBackgroundImage() {
        // 改成 /bg.png（根据测试结果，这个能找到）
        URL url = getClass().getResource("/bg.png");
        if (url != null) {
            bgImage = new ImageIcon(url).getImage();
            System.out.println("背景图加载成功: " + url);
        } else {
            // 备用方案：直接用文件
            File file = new File("resources/bg.png");
            if (file.exists()) {
                bgImage = new ImageIcon(file.getAbsolutePath()).getImage();
                System.out.println("背景图加载成功(文件方式): " + file.getAbsolutePath());
            } else {
                System.err.println("背景图加载失败");
            }
        }
    }

    private void playBGM() {
        try {
            // 改成 /bgm.wav
            URL bgmUrl = getClass().getResource("/bgm.wav");
            if (bgmUrl == null) {
                // 备用方案：直接用文件
                File file = new File("resources/bgm.wav");
                if (file.exists()) {
                    bgmUrl = file.toURI().toURL();
                } else {
                    System.err.println("BGM 文件未找到");
                    return;
                }
            }
            AudioInputStream stream = AudioSystem.getAudioInputStream(bgmUrl);
            bgm = AudioSystem.getClip();
            bgm.open(stream);
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
            bgm.start();
            System.out.println("BGM 开始播放: " + bgmUrl);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("不支持的音频格式，WAV 文件需要是 PCM 格式");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBGM() {
        if (bgm != null) {
            try {
                bgm.stop();
                bgm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bgm = null;
            System.out.println("BGM 已停止");
        }
    }

    private void testResourcePath() {
        System.out.println("=== 资源路径测试 ===");

        URL url1 = getClass().getResource("/resources/bg.png");
        System.out.println("getResource(\"/resources/bg.png\"): " + url1);

        URL url2 = getClass().getResource("/bg.png");
        System.out.println("getResource(\"/bg.png\"): " + url2);

        File file1 = new File("resources/bg.png");
        System.out.println("new File(\"resources/bg.png\"): exists=" + file1.exists() + ", path=" + file1.getAbsolutePath());

        System.out.println("当前工作目录: " + System.getProperty("user.dir"));
        System.out.println("=== 测试结束 ===");
    }
}