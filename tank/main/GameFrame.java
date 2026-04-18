package com.example.tank.main;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("坦克大战");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        setVisible(true);
    }
}
