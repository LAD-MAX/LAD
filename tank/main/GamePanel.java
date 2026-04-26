package com.example.tank.main;

import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.example.tank.util.Dir;
import com.example.tank.entity.Tank;
import com.example.tank.entity.Bullet;
import com.example.tank.entity.Block;
import com.example.tank.entity.Wall;
import com.example.tank.entity.HitEffect;
import com.example.tank.draw.WallDrawer;
import com.example.tank.draw.TankDrawer;
import com.example.tank.draw.BulletDrawer;
import com.example.tank.util.AudioManager;

public class GamePanel extends JPanel implements Runnable {

    private final int GW = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final int GH = Toolkit.getDefaultToolkit().getScreenSize().height;

    private Tank player;
    private final List<Tank> enemies = new CopyOnWriteArrayList<>();
    private final List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private final List<Wall> walls = new CopyOnWriteArrayList<>();
    private final List<HitEffect> effects = new CopyOnWriteArrayList<>();

    private final List<Bullet> delBullet = new ArrayList<>();
    private final List<Tank> delTank = new ArrayList<>();
    private final List<HitEffect> delEffect = new ArrayList<>();

    private boolean up, down, left, right;
    private boolean gameOver = false;
    private boolean paused = false;
    private int score = 0;
    private final Random rand = new Random();

    private final JPanel pausePanel;
    private final JPanel gameOverPanel;
    private final int difficulty;

    public GamePanel(int difficulty) {
        this.difficulty = difficulty;

        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);

        setupKeyBindings();

        // ==================== 暂停面板 ====================
        pausePanel = new JPanel();
        pausePanel.setBounds((GW - 300) / 2, (GH - 240) / 2, 300, 240);
        pausePanel.setLayout(null);
        pausePanel.setBackground(new Color(0, 0, 0, 190));
        pausePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        pausePanel.setVisible(false);

        JButton btnResume = new JButton("继续游戏");
        JButton btnPauseRestart = new JButton("重新开始");
        JButton btnPauseBack = new JButton("返回主界面");

        btnResume.setBounds(50, 30, 200, 50);
        btnPauseRestart.setBounds(50, 90, 200, 50);
        btnPauseBack.setBounds(50, 150, 200, 50);

        pausePanel.add(btnResume);
        pausePanel.add(btnPauseRestart);
        pausePanel.add(btnPauseBack);
        add(pausePanel);

        btnResume.addActionListener(e -> paused = false);
        btnPauseRestart.addActionListener(e -> {
            initGame();
            paused = false;
        });
        btnPauseBack.addActionListener(e -> {
            if (!gameOver) {
                StartUI.saveScore(score);
            }
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            gameFrame.dispose();
            new StartUI();
        });

        // ==================== 游戏结束面板 ====================
        gameOverPanel = new JPanel();
        gameOverPanel.setBounds((GW - 400) / 2, (GH - 300) / 2, 400, 300);
        gameOverPanel.setLayout(null);
        gameOverPanel.setBackground(new Color(0, 0, 0, 200));
        gameOverPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        gameOverPanel.setVisible(false);

        JLabel gameOverLabel = new JLabel("游戏结束", JLabel.CENTER);
        gameOverLabel.setFont(new Font("黑体", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds(0, 30, 400, 60);
        gameOverPanel.add(gameOverLabel);

        JLabel scoreLabel = new JLabel("得分: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("黑体", Font.BOLD, 36));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setBounds(0, 100, 400, 50);
        scoreLabel.setName("gameOverScoreLabel");
        gameOverPanel.add(scoreLabel);

        JButton btnGameOverRestart = new JButton("重新开始");
        btnGameOverRestart.setFont(new Font("黑体", Font.BOLD, 24));
        btnGameOverRestart.setBounds(75, 180, 250, 45);
        btnGameOverRestart.setBackground(new Color(50, 50, 50));
        btnGameOverRestart.setForeground(Color.WHITE);
        btnGameOverRestart.setFocusPainted(false);
        btnGameOverRestart.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        gameOverPanel.add(btnGameOverRestart);

        JButton btnGameOverBack = new JButton("返回主界面");
        btnGameOverBack.setFont(new Font("黑体", Font.BOLD, 24));
        btnGameOverBack.setBounds(75, 235, 250, 45);
        btnGameOverBack.setBackground(new Color(50, 50, 50));
        btnGameOverBack.setForeground(Color.WHITE);
        btnGameOverBack.setFocusPainted(false);
        btnGameOverBack.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        gameOverPanel.add(btnGameOverBack);

        add(gameOverPanel);

        btnGameOverRestart.addActionListener(e -> {
            gameOverPanel.setVisible(false);
            initGame();
        });

        btnGameOverBack.addActionListener(e -> {
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            gameFrame.dispose();
            new StartUI();
        });

        addButtonHoverEffect(btnGameOverRestart);
        addButtonHoverEffect(btnGameOverBack);
        addButtonHoverEffect(btnResume);
        addButtonHoverEffect(btnPauseRestart);
        addButtonHoverEffect(btnPauseBack);

        initGame();
        new Thread(this).start();
    }

    private void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(80, 80, 80));
                button.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 50));
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "W_pressed");
        am.put("W_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused && !gameOver) up = true;
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "W_released");
        am.put("W_released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                up = false;
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "S_pressed");
        am.put("S_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused && !gameOver) down = true;
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "S_released");
        am.put("S_released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                down = false;
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "A_pressed");
        am.put("A_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused && !gameOver) left = true;
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "A_released");
        am.put("A_released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                left = false;
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D_pressed");
        am.put("D_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused && !gameOver) right = true;
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "D_released");
        am.put("D_released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                right = false;
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "SPACE");
        am.put("SPACE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused && !gameOver) fire(player);
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "ESCAPE");
        am.put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) paused = !paused;
            }
        });
    }

    private void initGame() {
        gameOver = false;
        paused = false;
        score = 0;
        enemies.clear();
        bullets.clear();
        walls.clear();
        effects.clear();
        gameOverPanel.setVisible(false);

        generateWalls();

        int boundLeft = 16;
        int boundRight = GW - 16;
        int boundTop = 16;
        int boundBottom = GH - 16;

        int px = GW / 2 - 20;
        int py = boundBottom - 60;
        while (collide(px, py)) {
            py -= 10;
        }
        player = new Tank(px, py, Dir.UP, true);

        // 应用玩家设置
        player.setMaxHp(StartUI.getPlayerHp());
        player.setHp(StartUI.getPlayerHp());
        player.setSpeed(StartUI.getPlayerSpeed());
        player.setBulletPierce(StartUI.isBulletPierce());

        int cooldownIndex = StartUI.getPlayerShootCooldown();
        long cooldownMs = switch (cooldownIndex) {
            case 1 -> 300;
            case 2 -> 500;
            case 3 -> 800;
            default -> 0;
        };
        player.setShootCooldown(cooldownMs);

        int enemyCount = switch (difficulty) {
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            default -> 3;
        };

        for (int i = 0; i < enemyCount; i++) {
            int tx, ty;
            int attempts = 0;
            do {
                tx = rand.nextInt(boundRight - boundLeft - 80) + boundLeft + 40;
                ty = rand.nextInt(150) + boundTop + 40;
                attempts++;
                if (attempts > 100) break;
            } while (collide(tx, ty) || tankCollideWithEnemies(tx, ty, null));
            enemies.add(new Tank(tx, ty, Dir.DOWN, false));
        }
    }

    private boolean tankCollideWithEnemies(int x, int y, Tank self) {
        Rectangle r = new Rectangle(x, y, 40, 40);
        for (Tank t : enemies) {
            if (t == self) continue;
            Rectangle tr = new Rectangle(t.getX(), t.getY(), 40, 40);
            if (r.intersects(tr)) return true;
        }
        return false;
    }

    private void generateWalls() {
        walls.clear();

        int blockSize = Block.SIZE;

        // 上边界 - 两层
        for (int layer = 0; layer < 2; layer++) {
            int y = layer * blockSize;
            for (int x = 0; x < GW; x += blockSize) {
                walls.add(Wall.createSingleBlock(x, y, Block.BlockType.STEEL));
            }
        }

        // 下边界 - 两层
        for (int layer = 0; layer < 2; layer++) {
            int y = GH - (layer + 1) * blockSize;
            for (int x = 0; x < GW; x += blockSize) {
                walls.add(Wall.createSingleBlock(x, y, Block.BlockType.STEEL));
            }
        }

        // 左边界 - 两层
        for (int layer = 0; layer < 2; layer++) {
            int x = layer * blockSize;
            for (int y = 2 * blockSize; y < GH - 2 * blockSize; y += blockSize) {
                walls.add(Wall.createSingleBlock(x, y, Block.BlockType.STEEL));
            }
        }

        // 右边界 - 两层
        for (int layer = 0; layer < 2; layer++) {
            int x = GW - (layer + 1) * blockSize;
            for (int y = 2 * blockSize; y < GH - 2 * blockSize; y += blockSize) {
                walls.add(Wall.createSingleBlock(x, y, Block.BlockType.STEEL));
            }
        }

        // 内部混合正方形墙壁
        int startX = 50;
        int startY = 60;
        int endX = GW - 70;
        int endY = GH - 100;
        int spacing = 110;

        for (int x = startX; x < endX; x += spacing) {
            for (int y = startY; y < endY; y += spacing) {
                int offsetX = rand.nextInt(21) - 10;
                int offsetY = rand.nextInt(21) - 10;

                int wallX = x + offsetX;
                int wallY = y + offsetY;

                if (wallX < 30 || wallX > GW - 70) continue;
                if (wallY < 40 || wallY > GH - 90) continue;

                if (Math.abs(wallX + 20 - GW / 2) < 100 && wallY > GH - 220) continue;
                if (wallY < 160 && Math.abs(wallX + 20 - GW / 2) < 130) continue;

                if (rand.nextInt(100) < 85) {
                    walls.add(createMixedSquareWall(wallX, wallY, 40));
                }
            }
        }

        // 四个角落的混合堡垒
        walls.add(createMixedSquareWall(50, 50, 40));
        walls.add(createMixedSquareWall(GW - 90, 50, 40));
        walls.add(createMixedSquareWall(50, GH - 140, 40));
        walls.add(createMixedSquareWall(GW - 90, GH - 140, 40));
    }

    private Wall createMixedSquareWall(int x, int y, int size) {
        Wall wall = new Wall(0, 0, 1);
        wall.getBlocks().clear();

        int blockSize = Block.SIZE;
        int blocksPerSide = size / blockSize;

        for (int i = 0; i < blocksPerSide; i++) {
            for (int j = 0; j < blocksPerSide; j++) {
                Block.BlockType type;

                if (i == 0 || i == blocksPerSide - 1 || j == 0 || j == blocksPerSide - 1) {
                    type = Block.BlockType.BRICK;
                } else {
                    if (rand.nextInt(100) < 70) {
                        type = Block.BlockType.STEEL;
                    } else {
                        type = Block.BlockType.BRICK;
                    }
                }

                wall.getBlocks().add(new Block(
                        x + i * blockSize,
                        y + j * blockSize,
                        type
                ));
            }
        }

        return wall;
    }

    @Override
    public void run() {
        while (true) {
            if (!gameOver && !paused) {
                logic();
            }
            repaint();
            try {
                Thread.sleep(16);
            } catch (Exception ignored) {}
        }
    }

    private void logic() {
        movePlayer();
        moveEnemies();
        moveBullets();
        checkCollisions();
        updateEffects();

        bullets.removeAll(delBullet);
        enemies.removeAll(delTank);
        effects.removeAll(delEffect);
        delBullet.clear();
        delTank.clear();
        delEffect.clear();

        int minEnemies = switch (difficulty) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> 2;
        };

        while (enemies.size() < minEnemies) {
            int tx, ty;
            int attempts = 0;
            do {
                tx = rand.nextInt(GW - 200) + 100;
                ty = rand.nextInt(150) + 50;
                attempts++;
                if (attempts > 100) break;
            } while (collide(tx, ty) || tankCollideWithEnemies(tx, ty, null));
            enemies.add(new Tank(tx, ty, Dir.DOWN, false));
        }
    }

    private void updateEffects() {
        for (HitEffect e : effects) {
            int t = e.getTimer();
            t--;
            e.setTimer(t);
            if (t <= 0) {
                delEffect.add(e);
            }
        }
    }

    private void movePlayer() {
        if (!player.isAlive()) {
            gameOver = true;
            updateGameOverPanel();
            return;
        }

        int nx = player.getX();
        int ny = player.getY();

        if (up) {
            player.setDir(Dir.UP);
            ny -= player.getSpeed();
        }
        if (down) {
            player.setDir(Dir.DOWN);
            ny += player.getSpeed();
        }
        if (left) {
            player.setDir(Dir.LEFT);
            nx -= player.getSpeed();
        }
        if (right) {
            player.setDir(Dir.RIGHT);
            nx += player.getSpeed();
        }

        nx = Math.max(0, Math.min(GW - 40, nx));
        ny = Math.max(0, Math.min(GH - 40, ny));

        if (!collide(nx, ny) && !tankCollide(player, nx, ny)) {
            player.setX(nx);
            player.setY(ny);
        }
    }

    // ==================== 敌人AI ====================

    private void trackPlayer(Tank enemy) {
        int dx = player.getX() - enemy.getX();
        int dy = player.getY() - enemy.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                enemy.setDir(Dir.RIGHT);
            } else {
                enemy.setDir(Dir.LEFT);
            }
        } else {
            if (dy > 0) {
                enemy.setDir(Dir.DOWN);
            } else {
                enemy.setDir(Dir.UP);
            }
        }
    }

    private void shootAtPlayer(Tank enemy) {
        trackPlayer(enemy);
        fire(enemy);
    }

    private void moveEnemies() {
        for (Tank t : enemies) {
            // ==================== 根据难度决定AI ====================
            if (difficulty >= 2) {
                // 普通和困难：追踪玩家
                if (rand.nextInt(100) < 80) {
                    trackPlayer(t);
                } else {
                    if (rand.nextInt(120) < 3) {
                        t.setDir(Dir.values()[rand.nextInt(4)]);
                    }
                }
            } else {
                // 简单：随机移动
                if (rand.nextInt(120) < 3) {
                    t.setDir(Dir.values()[rand.nextInt(4)]);
                }
            }

            int nx = t.getX();
            int ny = t.getY();
            switch (t.getDir()) {
                case UP:    ny -= 2; break;
                case DOWN:  ny += 2; break;
                case LEFT:  nx -= 2; break;
                case RIGHT: nx += 2; break;
            }

            if (nx < 0 || nx > GW - 40 || ny < 0 || ny > GH - 40) continue;
            if (!collide(nx, ny) && !tankCollide(t, nx, ny)) {
                t.setX(nx);
                t.setY(ny);
            }

            // ==================== 射击逻辑 ====================
            int shootRate = switch (difficulty) {
                case 1 -> 1200;
                case 2 -> 800;
                case 3 -> 400;
                default -> 900;
            };

            long enemyCooldown = switch (difficulty) {
                case 1 -> 1500;
                case 2 -> 1000;
                case 3 -> 500;
                default -> 1500;
            };
            t.setShootCooldown(enemyCooldown);

            if (t.canShoot() && rand.nextInt(shootRate) < 3) {
                if (difficulty >= 2 && rand.nextInt(100) < 70) {
                    shootAtPlayer(t);
                } else {
                    fire(t);
                }
                t.recordShoot();
            }
        }
    }

    private void moveBullets() {
        for (Bullet b : bullets) {
            switch (b.getDir()) {
                case UP:    b.setY(b.getY() - b.getSpeed()); break;
                case DOWN:  b.setY(b.getY() + b.getSpeed()); break;
                case LEFT:  b.setX(b.getX() - b.getSpeed()); break;
                case RIGHT: b.setX(b.getX() + b.getSpeed()); break;
            }
            if (b.getX() < 0 || b.getX() > GW || b.getY() < 0 || b.getY() > GH)
                delBullet.add(b);
        }
    }

    private void checkCollisions() {
        delBullet.clear();
        delTank.clear();

        for (Bullet b : bullets) {
            boolean hit = false;

            // 墙体碰撞（如果不是玩家穿墙子弹）
            if (!(b.getOwner().isPlayer() && b.getOwner().isBulletPierce())) {
                for (Wall wall : walls) {
                    Iterator<Block> it = wall.getBlocks().iterator();
                    while (it.hasNext()) {
                        Block blk = it.next();
                        if (blk.isHit(b.getX(), b.getY())) {
                            if (blk.getType() == Block.BlockType.BRICK) {
                                int centerX = blk.getX() + Block.SIZE / 2;
                                int centerY = blk.getY() + Block.SIZE / 2;

                                effects.add(new HitEffect(centerX - 16, centerY - 16));
                                effects.add(new HitEffect(centerX + 8, centerY - 8));
                                effects.add(new HitEffect(centerX - 8, centerY + 8));

                                int radius = Block.SIZE * 2;
                                for (Wall w : walls) {
                                    Iterator<Block> blockIt = w.getBlocks().iterator();
                                    while (blockIt.hasNext()) {
                                        Block target = blockIt.next();
                                        if (target.getType() == Block.BlockType.BRICK) {
                                            int targetCenterX = target.getX() + Block.SIZE / 2;
                                            int targetCenterY = target.getY() + Block.SIZE / 2;

                                            int dx = Math.abs(targetCenterX - centerX);
                                            int dy = Math.abs(targetCenterY - centerY);

                                            if (dx <= radius && dy <= radius) {
                                                blockIt.remove();
                                            }
                                        }
                                    }
                                }

                                AudioManager.playSound(getClass(), "boom.wav");
                            } else {
                                effects.add(new HitEffect(b.getX(), b.getY()));
                                AudioManager.playSound(getClass(), "hit.wav");
                            }

                            delBullet.add(b);
                            hit = true;
                            break;
                        }
                    }
                    if (hit) break;
                }
            }
            if (hit) continue;

            // 玩家被击中
            if (!b.getOwner().isPlayer() && hit(b.getX(), b.getY(), b.getSize(), b.getSize(),
                    player.getX(), player.getY())) {
                delBullet.add(b);
                effects.add(new HitEffect(player.getX() + 20, player.getY() + 20));
                AudioManager.playSound(getClass(), "boom.wav");

                player.takeDamage();
                if (!player.isAlive()) {
                    StartUI.saveScore(score);
                    gameOver = true;
                    updateGameOverPanel();
                    return;
                }
            }

            // 敌人被击中
            if (b.getOwner().isPlayer()) {
                for (Tank t : enemies) {
                    if (hit(b.getX(), b.getY(), b.getSize(), b.getSize(), t.getX(), t.getY())) {
                        effects.add(new HitEffect(b.getX(), b.getY()));
                        delBullet.add(b);
                        delTank.add(t);
                        AudioManager.playSound(getClass(), "boom.wav");
                        score += 50;
                        break;
                    }
                }
            }
        }

        bullets.removeAll(delBullet);
        enemies.removeAll(delTank);
    }

    private void updateGameOverPanel() {
        for (Component c : gameOverPanel.getComponents()) {
            if ("gameOverScoreLabel".equals(c.getName())) {
                ((JLabel) c).setText("得分: " + score);
                break;
            }
        }
        gameOverPanel.setVisible(true);
    }

    private boolean collide(int x, int y) {
        Rectangle me = new Rectangle(x, y, 40, 40);
        for (Wall wall : walls) {
            for (Block b : wall.getBlocks()) {
                Rectangle br = new Rectangle(b.getX(), b.getY(), Block.SIZE, Block.SIZE);
                if (me.intersects(br)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tankCollide(Tank self, int nx, int ny) {
        Rectangle r = new Rectangle(nx, ny, 40, 40);
        for (Tank t : enemies) {
            if (self == t) continue;
            Rectangle tr = new Rectangle(t.getX(), t.getY(), 40, 40);
            if (r.intersects(tr)) return true;
        }
        if (!self.isPlayer() && player != null) {
            Rectangle pr = new Rectangle(player.getX(), player.getY(), 40, 40);
            if (r.intersects(pr)) return true;
        }
        return false;
    }

    private boolean hit(int x1, int y1, int w1, int h1, int x2, int y2) {
        return new Rectangle(x1, y1, w1, h1).intersects(new Rectangle(x2, y2, 40, 40));
    }

    private void fire(Tank tank) {
        if (!tank.isPlayer() && !tank.canShoot()) {
            return;
        }

        int cx = tank.getX() + 20;
        int cy = tank.getY() + 20;
        bullets.add(new Bullet(cx - 5, cy - 5, tank.getDir(), tank));
        AudioManager.playSound(getClass(), "fire.wav");

        if (!tank.isPlayer()) {
            tank.recordShoot();
        } else {
            tank.recordShoot();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Wall w : walls) WallDrawer.draw(g, w);
        TankDrawer.draw(g, player);
        for (Tank t : enemies) TankDrawer.draw(g, t);
        for (Bullet b : bullets) BulletDrawer.drawBullet(g, b);
        for (HitEffect e : effects) BulletDrawer.drawHitEffect(g, e);

        g.setColor(Color.WHITE);
        g.setFont(new Font("黑体", Font.BOLD, 20));
        g.drawString("分数: " + score, 20, 30);

        // 显示玩家血量
        if (player != null) {
            g.setColor(Color.RED);
            g.drawString("血量: " + player.getHp() + "/" + player.getMaxHp(), 20, 60);

            // ==================== 装弹提醒 ====================
            if (!player.canShoot()) {
                long remaining = player.getCooldownRemaining();
                if (remaining > 0) {
                    if (remaining < 200) {
                        g.setColor(Color.GREEN);
                    } else if (remaining < 500) {
                        g.setColor(Color.YELLOW);
                    } else {
                        g.setColor(Color.ORANGE);
                    }

                    double seconds = remaining / 1000.0;
                    String cooldownText = String.format("⏳ 装弹中: %.1f秒", seconds);
                    g.setFont(new Font("黑体", Font.BOLD, 20));
                    g.drawString(cooldownText, 20, 90);

                    long totalCooldown = player.getShootCooldown();
                    if (totalCooldown > 0) {
                        int barWidth = 150;
                        int barHeight = 8;
                        int barX = 20;
                        int barY = 100;

                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(barX, barY, barWidth, barHeight);

                        int fillWidth = (int) (barWidth * (totalCooldown - remaining) / totalCooldown);
                        if (remaining < 200) {
                            g.setColor(Color.GREEN);
                        } else if (remaining < 500) {
                            g.setColor(Color.YELLOW);
                        } else {
                            g.setColor(Color.ORANGE);
                        }
                        g.fillRect(barX, barY, fillWidth, barHeight);

                        g.setColor(Color.WHITE);
                        g.drawRect(barX, barY, barWidth, barHeight);
                    }
                }
            } else {
                g.setColor(Color.GREEN);
                g.setFont(new Font("黑体", Font.BOLD, 18));
                g.drawString("🔫 可以射击!", 20, 90);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("黑体", Font.BOLD, 20));
        g.drawString("WASD移动  空格发射  ESC暂停", 20, GH - 20);

        pausePanel.setVisible(paused);
        gameOverPanel.setVisible(gameOver);
    }
}