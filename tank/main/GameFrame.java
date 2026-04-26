package com.example.tank.main;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(int difficulty) {
        setTitle("坦克大战");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        add(new GamePanel(difficulty));
        setVisible(true);
    }
}