package com.example.tank.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.io.*;
import com.example.tank.util.AudioManager;

public class StartUI extends JFrame {

    // 设置面板的组件引用（用于刷新显示）
    private JComboBox<String> hpComboBox;
    private JComboBox<String> speedComboBox;
    private JCheckBox pierceCheckBox;
    private JComboBox<String> cooldownComboBox;


    private Image bgImage;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel mainMenuPanel;
    private JPanel settingsPanel;
    private JPanel helpPanel;
    private JPanel scorePanel;

    private static boolean soundEnabled = true;
    private int difficulty = 2;

    // ==================== 玩家特性设置 ====================
    private static int playerHp = 3;              // 玩家血量 (1-5)
    private static int playerSpeed = 3;            // 移动速度 (2=慢, 3=普通, 4=快)
    private static boolean bulletPierce = false;   // 子弹穿墙
    private static int playerShootCooldown = 0;    // 子弹冷却 (0=无, 1=短, 2=中, 3=长)

    private static final Color MENU_BG_COLOR = new Color(0, 0, 0, 180);
    private static final Color HOVER_COLOR = Color.YELLOW;
    private static final Color TEXT_COLOR = Color.WHITE;

    public StartUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadBackgroundImage();

        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        container.setLayout(new BorderLayout());
        add(container);

        JLabel titleLabel = new JLabel("", JLabel.CENTER);
        titleLabel.setFont(new Font("黑体", Font.BOLD, 80));
        titleLabel.setForeground(new Color(255, 220, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 60, 0));
        container.add(titleLabel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        createMainMenuPanel();
        createSettingsPanel();
        createHelpPanel();
        createScorePanel();

        contentPanel.add(mainMenuPanel, "main");
        contentPanel.add(settingsPanel, "settings");
        contentPanel.add(helpPanel, "help");
        contentPanel.add(scorePanel, "score");

        container.add(contentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        JLabel versionLabel = new JLabel("v2.0 - 数据科学与大数据技术", JLabel.CENTER);
        versionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        versionLabel.setForeground(new Color(160, 160, 160));
        versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        bottomPanel.add(versionLabel, BorderLayout.SOUTH);
        container.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
        AudioManager.playBGM(getClass());
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    // ==================== Getter 方法 ====================
    public static int getPlayerHp() {
        return playerHp;
    }

    public static int getPlayerSpeed() {
        return playerSpeed;
    }

    public static boolean isBulletPierce() {
        return bulletPierce;
    }

    public static int getPlayerShootCooldown() {
        return playerShootCooldown;
    }

    private void createMainMenuPanel() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setOpaque(false);
        mainMenuPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnStart = createMenuButton("▶ 开始游戏");
        JButton btnSettings = createMenuButton("⚙ 设置");
        JButton btnHelp = createMenuButton("? 操作说明");
        JButton btnScore = createMenuButton("🏆 排行榜");
        JButton btnExit = createMenuButton("✕ 退出游戏");

        gbc.gridy = 0; mainMenuPanel.add(btnStart, gbc);
        gbc.gridy = 1; mainMenuPanel.add(btnSettings, gbc);
        gbc.gridy = 2; mainMenuPanel.add(btnHelp, gbc);
        gbc.gridy = 3; mainMenuPanel.add(btnScore, gbc);
        gbc.gridy = 4; mainMenuPanel.add(btnExit, gbc);

        btnStart.addActionListener(e -> {
            AudioManager.stopBGM();
            dispose();
            new GameFrame(difficulty);
        });

        btnSettings.addActionListener(e -> cardLayout.show(contentPanel, "settings"));
        btnHelp.addActionListener(e -> cardLayout.show(contentPanel, "help"));
        btnScore.addActionListener(e -> cardLayout.show(contentPanel, "score"));
        btnExit.addActionListener(e -> {
            AudioManager.stopBGM();
            dispose();
            System.exit(0);
        });
    }

    private void createSettingsPanel() {

        settingsPanel = new JPanel();
        settingsPanel.setOpaque(false);
        settingsPanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(MENU_BG_COLOR);
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 220, 0), 2),
                BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);

        JLabel titleLabel = new JLabel("游戏设置", JLabel.CENTER);
        titleLabel.setFont(new Font("黑体", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 220, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 20, 20, 20);
        innerPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 15, 8, 15);

        int row = 1;

        // ==================== 音效开关 ====================
        JLabel soundLabel = new JLabel("音效开关：");
        soundLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        soundLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        innerPanel.add(soundLabel, gbc);

        JCheckBox soundCheckBox = new JCheckBox();
        soundCheckBox.setSelected(soundEnabled);
        soundCheckBox.setOpaque(false);
        soundCheckBox.setForeground(TEXT_COLOR);
        soundCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        soundCheckBox.setText(soundEnabled ? "开启" : "关闭");
        soundCheckBox.addActionListener(e -> {
            soundEnabled = soundCheckBox.isSelected();
            soundCheckBox.setText(soundEnabled ? "开启" : "关闭");
            if (soundEnabled) {
                AudioManager.playBGM(getClass());
            } else {
                AudioManager.stopBGM();
            }
        });
        gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(soundCheckBox, gbc);
        row++;

        // ==================== 游戏难度 ====================
        JLabel diffLabel = new JLabel("游戏难度：");
        diffLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        diffLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        innerPanel.add(diffLabel, gbc);

        String[] difficulties = {"简单", "普通", "困难"};
        JComboBox<String> diffComboBox = new JComboBox<>(difficulties);
        diffComboBox.setSelectedIndex(difficulty - 1);
        diffComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        diffComboBox.setBackground(new Color(50, 50, 50));
        diffComboBox.setForeground(TEXT_COLOR);
        diffComboBox.addActionListener(e -> {
            difficulty = diffComboBox.getSelectedIndex() + 1;
            applyDifficultyPreset(difficulty);   // 应用预设
            updateSettingsDisplay();             // 刷新显示
        });
        gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(diffComboBox, gbc);
        row++;

        // ==================== 分隔线 ====================
        JSeparator separator1 = new JSeparator();
        separator1.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        innerPanel.add(separator1, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 15, 8, 15);
        row++;

        // ==================== 玩家血量 ====================
        JLabel hpLabel = new JLabel("玩家血量：");
        hpLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        hpLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        innerPanel.add(hpLabel, gbc);

        JPanel hpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        hpPanel.setOpaque(false);

        String[] hpOptions = {"1", "2", "3", "4", "5"};
        JComboBox<String> hpComboBox = new JComboBox<>(hpOptions);
        hpComboBox.setSelectedIndex(playerHp - 1);
        hpComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        hpComboBox.setBackground(new Color(50, 50, 50));
        hpComboBox.setForeground(TEXT_COLOR);
        hpComboBox.addActionListener(e -> playerHp = hpComboBox.getSelectedIndex() + 1);
        hpPanel.add(hpComboBox);

        JLabel hpUnitLabel = new JLabel("条命");
        hpUnitLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        hpUnitLabel.setForeground(TEXT_COLOR);
        hpPanel.add(hpUnitLabel);

        gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(hpPanel, gbc);
        row++;

        // ==================== 移动速度 ====================
        JLabel speedLabel = new JLabel("移动速度：");
        speedLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        speedLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        innerPanel.add(speedLabel, gbc);

        String[] speedOptions = {"慢", "普通", "快"};
        JComboBox<String> speedComboBox = new JComboBox<>(speedOptions);
        speedComboBox.setSelectedIndex(playerSpeed - 2);
        speedComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        speedComboBox.setBackground(new Color(50, 50, 50));
        speedComboBox.setForeground(TEXT_COLOR);
        speedComboBox.addActionListener(e -> {
            int index = speedComboBox.getSelectedIndex();
            playerSpeed = index + 2;
        });
        gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(speedComboBox, gbc);
        row++;

        // ==================== 子弹穿墙 ====================
        JLabel pierceLabel = new JLabel("子弹穿墙：");
        pierceLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        pierceLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        innerPanel.add(pierceLabel, gbc);

        JCheckBox pierceCheckBox = new JCheckBox();
        pierceCheckBox.setSelected(bulletPierce);
        pierceCheckBox.setOpaque(false);
        pierceCheckBox.setForeground(TEXT_COLOR);
        pierceCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        pierceCheckBox.setText(bulletPierce ? "开启" : "关闭");
        pierceCheckBox.addActionListener(e -> {
            bulletPierce = pierceCheckBox.isSelected();
            pierceCheckBox.setText(bulletPierce ? "开启" : "关闭");
        });
        gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(pierceCheckBox, gbc);
        row++;

        // ==================== 子弹冷却 ====================
        JLabel cooldownLabel = new JLabel("子弹冷却：");
        cooldownLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        cooldownLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        innerPanel.add(cooldownLabel, gbc);

        String[] cooldownOptions = {"无冷却", "短 (0.3秒)", "中 (0.5秒)", "长 (0.8秒)"};
        JComboBox<String> cooldownComboBox = new JComboBox<>(cooldownOptions);
        cooldownComboBox.setSelectedIndex(playerShootCooldown);
        cooldownComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        cooldownComboBox.setBackground(new Color(50, 50, 50));
        cooldownComboBox.setForeground(TEXT_COLOR);
        cooldownComboBox.addActionListener(e -> {
            playerShootCooldown = cooldownComboBox.getSelectedIndex();
        });
        gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(cooldownComboBox, gbc);
        row++;

        // ==================== 返回按钮 ====================
        JButton btnBack = createSmallButton("返回主菜单");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 20, 0, 20);
        innerPanel.add(btnBack, gbc);
        btnBack.addActionListener(e -> cardLayout.show(contentPanel, "main"));

        settingsPanel.add(innerPanel);
    }

    // 应用难度预设
    private void applyDifficultyPreset(int diff) {
        switch (diff) {
            case 1: // 简单
                playerHp = 3;
                playerSpeed = 2;
                playerShootCooldown = 1;  // 短冷却
                bulletPierce = false;
                break;
            case 2: // 普通
                playerHp = 2;
                playerSpeed = 3;
                playerShootCooldown = 2;  // 中冷却
                bulletPierce = false;
                break;
            case 3: // 困难
                playerHp = 3;
                playerSpeed = 4;
                playerShootCooldown = 0;  // 无冷却
                bulletPierce = true;
                break;
        }
    }

    // 刷新设置面板的显示
    private void updateSettingsDisplay() {
        if (hpComboBox != null) {
            hpComboBox.setSelectedIndex(playerHp - 1);
        }
        if (speedComboBox != null) {
            speedComboBox.setSelectedIndex(playerSpeed - 2);
        }
        if (pierceCheckBox != null) {
            pierceCheckBox.setSelected(bulletPierce);
            pierceCheckBox.setText(bulletPierce ? "开启" : "关闭");
        }
        if (cooldownComboBox != null) {
            cooldownComboBox.setSelectedIndex(playerShootCooldown);
        }
    }

    private void createHelpPanel() {
        helpPanel = new JPanel();
        helpPanel.setOpaque(false);
        helpPanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(MENU_BG_COLOR);
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 220, 0), 2),
                BorderFactory.createEmptyBorder(30, 60, 30, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);

        JLabel titleLabel = new JLabel("操作说明", JLabel.CENTER);
        titleLabel.setFont(new Font("黑体", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 220, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 20, 30, 20);
        innerPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 20, 8, 20);

        String[][] controls = {
                {"W", "向上移动"}, {"S", "向下移动"}, {"A", "向左移动"},
                {"D", "向右移动"}, {"空格", "发射子弹"}, {"ESC", "暂停游戏"}
        };

        for (int i = 0; i < controls.length; i++) {
            JLabel keyLabel = new JLabel(controls[i][0], JLabel.CENTER);
            keyLabel.setFont(new Font("Consolas", Font.BOLD, 22));
            keyLabel.setForeground(new Color(255, 220, 0));
            keyLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            keyLabel.setPreferredSize(new Dimension(80, 40));
            gbc.gridx = 0; gbc.gridy = i + 1; gbc.anchor = GridBagConstraints.EAST;
            innerPanel.add(keyLabel, gbc);

            JLabel descLabel = new JLabel(controls[i][1]);
            descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 22));
            descLabel.setForeground(TEXT_COLOR);
            gbc.gridx = 1; gbc.gridy = i + 1; gbc.anchor = GridBagConstraints.WEST;
            innerPanel.add(descLabel, gbc);
        }

        JButton btnBack = createSmallButton("返回主菜单");
        gbc.gridx = 0; gbc.gridy = controls.length + 1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 20, 0, 20);
        innerPanel.add(btnBack, gbc);
        btnBack.addActionListener(e -> cardLayout.show(contentPanel, "main"));

        helpPanel.add(innerPanel);
    }

    private void createScorePanel() {
        scorePanel = new JPanel();
        scorePanel.setOpaque(false);
        scorePanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(MENU_BG_COLOR);
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 220, 0), 2),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        innerPanel.setPreferredSize(new Dimension(500, 450));

        JLabel titleLabel = new JLabel("🏆 历史得分记录 🏆", JLabel.CENTER);
        titleLabel.setFont(new Font("黑体", Font.BOLD, 30));
        titleLabel.setForeground(new Color(255, 220, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        innerPanel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        loadAllScores(listModel);

        JList<String> scoreList = new JList<>(listModel);
        scoreList.setFont(new Font("Consolas", Font.PLAIN, 18));
        scoreList.setForeground(new Color(200, 255, 200));
        scoreList.setBackground(new Color(30, 30, 40));
        scoreList.setSelectionBackground(new Color(60, 60, 80));
        scoreList.setFixedCellHeight(30);

        DefaultListCellRenderer renderer = (DefaultListCellRenderer) scoreList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(scoreList);
        scrollPane.setPreferredSize(new Dimension(400, 280));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 120)));
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.setOpaque(false);

        JLabel statsLabel = new JLabel(getScoreStats(listModel), JLabel.CENTER);
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        statsLabel.setForeground(new Color(180, 180, 200));
        bottomPanel.add(statsLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setOpaque(false);

        JButton btnClear = createSmallButton("清空记录");
        btnClear.setForeground(new Color(255, 100, 100));
        JButton btnBack = createSmallButton("返回主菜单");

        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);
        bottomPanel.add(buttonPanel);

        innerPanel.add(bottomPanel, BorderLayout.SOUTH);

        btnClear.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要清空所有历史记录吗？", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                clearAllScores();
                listModel.clear();
                statsLabel.setText(getScoreStats(listModel));
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(contentPanel, "main"));

        scorePanel.add(innerPanel);
    }

    private void loadAllScores(DefaultListModel<String> model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("scores.txt"))) {
            String line;
            int rank = 1;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                model.addElement(String.format("%2d. %s", rank++, line));
            }
            if (model.isEmpty()) {
                model.addElement("   暂无历史记录");
            }
        } catch (Exception e) {
            model.addElement("   暂无历史记录");
        }
    }

    private String getScoreStats(DefaultListModel<String> model) {
        if (model.isEmpty() || model.get(0).contains("暂无记录")) {
            return "总场次: 0  |  最高分: 0  |  平均分: 0";
        }

        int total = 0;
        int maxScore = 0;
        int sum = 0;

        for (int i = 0; i < model.size(); i++) {
            String line = model.get(i);
            if (line.contains("暂无记录")) continue;

            try {
                int fenIndex = line.indexOf("分");
                if (fenIndex > 0) {
                    int numStart = fenIndex - 1;
                    while (numStart >= 0) {
                        char c = line.charAt(numStart);
                        if (c >= '0' && c <= '9') {
                            numStart--;
                        } else {
                            break;
                        }
                    }
                    numStart++;

                    String scoreStr = line.substring(numStart, fenIndex);
                    int score = Integer.parseInt(scoreStr);

                    maxScore = Math.max(maxScore, score);
                    sum += score;
                    total++;
                }
            } catch (Exception ignored) {}
        }

        if (total == 0) {
            return "总场次: 0  |  最高分: 0  |  平均分: 0";
        }

        int avg = sum / total;
        return String.format("总场次: %d  |  最高分: %d  |  平均分: %d", total, maxScore, avg);
    }

    private void clearAllScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt"))) {
            writer.write("");
        } catch (Exception ignored) {}
    }

    public static void saveScore(int score) {
        String timeStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String record = String.format("%6d分 - %s", score, timeStr);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
            writer.write(record);
            writer.newLine();
        } catch (Exception ignored) {}
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("黑体", Font.BOLD, 36));
        button.setPreferredSize(new Dimension(320, 70));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(HOVER_COLOR);
                button.setFont(new Font("黑体", Font.BOLD, 40));
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(TEXT_COLOR);
                button.setFont(new Font("黑体", Font.BOLD, 36));
            }
        });
        return button;
    }

    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("黑体", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(160, 45));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(HOVER_COLOR);
                button.setBorder(BorderFactory.createLineBorder(HOVER_COLOR, 2));
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(TEXT_COLOR);
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            }
        });
        return button;
    }

    private void loadBackgroundImage() {
        URL url = getClass().getResource("/bg.png");
        if (url != null) {
            bgImage = new ImageIcon(url).getImage();
        } else {
            File file = new File("resources/bg.png");
            if (file.exists()) {
                bgImage = new ImageIcon(file.getAbsolutePath()).getImage();
            }
        }
    }
}