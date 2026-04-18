package com.example.tank.main;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class StartUI extends JFrame {
    private Clip bgm;

    public StartUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL url = getClass().getResource("resources/bg.png");
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new GridBagLayout());
        add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JButton btnStart = new JButton("开始游戏");
        btnStart.setFont(new Font("黑体", Font.BOLD, 40));
        btnStart.setPreferredSize(new Dimension(300, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btnStart, gbc);

        JButton btnExit = new JButton("退出游戏");
        btnExit.setFont(new Font("黑体", Font.BOLD, 40));
        btnExit.setPreferredSize(new Dimension(300, 80));
        gbc.gridy = 1;
        panel.add(btnExit, gbc);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBGM();
                dispose();
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

        playBGM();
        setVisible(true);
    }

    private void playBGM() {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource("bgm.wav"));
            bgm = AudioSystem.getClip();
            bgm.open(stream);
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
            bgm.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBGM() {
        if (bgm != null && bgm.isRunning()) {
            bgm.stop();
            bgm.close();
        }
    }
}