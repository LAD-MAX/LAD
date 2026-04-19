package com.example.tank.main;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
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

public class GamePanel extends JPanel implements KeyListener, Runnable {

    private void playSound(String name) {
        new Thread(() -> {
            try {
                URL url = getClass().getResource(name);
                if (url == null) return;

                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) clip.close();
                });
            } catch (Exception ignored) {}
        }).start();
    }

    private int GW = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int GH = Toolkit.getDefaultToolkit().getScreenSize().height;

    private Tank player;
    private List<Tank> enemies = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private List<HitEffect> effects = new ArrayList<>();

    private List<Bullet> delBullet = new ArrayList<>();
    private List<Tank> delTank = new ArrayList<>();
    private List<HitEffect> delEffect = new ArrayList<>();

    private boolean up, down, left, right;
    private boolean gameOver = false;
    private boolean paused = false;
    private int score = 0;
    private Random rand = new Random();

    private JButton btnResume, btnRestart, btnBack;
    private JPanel pausePanel;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        pausePanel = new JPanel();
        pausePanel.setBounds((GW - 300)/2, (GH - 240)/2, 300, 240);
        pausePanel.setLayout(null);
        pausePanel.setBackground(new Color(0,0,0,190));
        pausePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        pausePanel.setVisible(false);

        btnResume = new JButton("继续游戏");
        btnRestart = new JButton("重新开始");
        btnBack    = new JButton("返回主界面");

        btnResume.setBounds(50,30,200,50);
        btnRestart.setBounds(50,90,200,50);
        btnBack   .setBounds(50,150,200,50);

        pausePanel.add(btnResume);
        pausePanel.add(btnRestart);
        pausePanel.add(btnBack);
        add(pausePanel);

        btnResume.addActionListener(e-> paused = false);
        btnRestart.addActionListener(e-> { initGame(); paused = false; });
        btnBack.addActionListener(e -> {
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            gameFrame.dispose();
            new StartUI();
        });

        initGame();
        new Thread(this).start();
    }

    private void initGame() {
        gameOver = false;
        paused = false;
        score = 0;
        enemies.clear();
        bullets.clear();
        walls.clear();
        effects.clear();

        generateWalls();

        int px = GW / 2 - 20;
        int py = GH - 100;
        while (collide(px, py, 40, 40)) {
            py -= 10;
        }
        player = new Tank(px, py, Dir.UP, true);

        for (int i=0; i<3; i++) {
            int tx, ty;
            do {
                tx = rand.nextInt(GW - 100) + 50;
                ty = rand.nextInt(150) + 50;
            } while (collide(tx, ty, 40, 40));
            enemies.add(new Tank(tx, ty, Dir.DOWN, false));
        }
    }

    private void generateWalls() {
        walls.clear();
        for (int i = 0; i < 20; ) {
            int type = rand.nextInt(5) + 1;
            int x = rand.nextInt(GW - 80) + 40;
            int y = rand.nextInt(GH - 240) + 50;

            Rectangle test = new Rectangle(x, y, 60, 60);
            boolean overlap = false;

            for (Wall w : walls) {
                for (Block b : w.getBlocks()) {
                    Rectangle br = new Rectangle(b.getX(), b.getY(), Block.SIZE, Block.SIZE);
                    if (test.intersects(br)) {
                        overlap = true;
                        break;
                    }
                }
                if (overlap) break;
            }
            if (overlap) continue;

            walls.add(new Wall(x, y, type));
            i++;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!gameOver && !paused) {
                logic();
            }
            repaint();
            try { Thread.sleep(16); } catch (Exception ignored) {}
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

        while (enemies.size() < 2) {
            int tx, ty;
            do {
                tx = rand.nextInt(GW - 100) + 50;
                ty = rand.nextInt(150) + 50;
            } while (collide(tx, ty, 40, 40));
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
        int nx = player.getX();
        int ny = player.getY();

        if (up)    { player.setDir(Dir.UP);    ny -= player.getSpeed(); }
        if (down)  { player.setDir(Dir.DOWN);  ny += player.getSpeed(); }
        if (left)  { player.setDir(Dir.LEFT);  nx -= player.getSpeed(); }
        if (right) { player.setDir(Dir.RIGHT); nx += player.getSpeed(); }

        nx = Math.max(0, Math.min(GW - 40, nx));
        ny = Math.max(0, Math.min(GH - 40, ny));

        if (!collide(nx, ny, 40, 40) && !tankCollide(player, nx, ny)) {
            player.setX(nx);
            player.setY(ny);
        }
    }

    private void moveEnemies() {
        for (Tank t : enemies) {
            if (rand.nextInt(120) < 3)
                t.setDir(Dir.values()[rand.nextInt(4)]);

            int nx = t.getX();
            int ny = t.getY();
            switch (t.getDir()) {
                case UP:    ny -= 2; break;
                case DOWN:  ny += 2; break;
                case LEFT:  nx -= 2; break;
                case RIGHT: nx += 2; break;
            }

            if (nx < 0 || nx > GW-40 || ny < 0 || ny > GH-40) continue;
            if (!collide(nx, ny, 40, 40) && !tankCollide(t, nx, ny)) {
                t.setX(nx);
                t.setY(ny);
            }

            if (rand.nextInt(200) < 3) fire(t);
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
        for (Bullet b : bullets) {
            boolean hit = false;

            for (Wall wall : walls) {
                Iterator<Block> it = wall.getBlocks().iterator();
                while (it.hasNext()) {
                    Block blk = it.next();
                    if (blk.isHit(b.getX(), b.getY())) {
                        blk.setHp(blk.getHp() - 1);
                        if (blk.getHp() <= 0) {
                            it.remove();
                        }
                        effects.add(new HitEffect(b.getX(), b.getY()));
                        AudioManager.playSound(getClass(), "hit.wav");         // ← 加上 /resources/
                        delBullet.add(b);
                        hit = true;
                        break;
                    }
                }
                if (hit) break;
            }
            if (hit) continue;

            if (!b.getOwner().isPlayer() && hit(b.getX(), b.getY(), b.getSize(), b.getSize(), player.getX(), player.getY(), 40, 40)) {
                delBullet.add(b);
                effects.add(new HitEffect(player.getX() + 20, player.getY() + 20));  // ← 添加爆炸效果
                AudioManager.playSound(getClass(), "boom.wav");    // ← 添加玩家爆炸音效
                gameOver = true;
                return;
            }

            if (b.getOwner().isPlayer()) {
                for (Tank t : enemies) {
                    if (hit(b.getX(), b.getY(), b.getSize(), b.getSize(), t.getX(), t.getY(), 40, 40)) {
                        effects.add(new HitEffect(b.getX(), b.getY()));
                        delBullet.add(b);
                        delTank.add(t);
                        AudioManager.playSound(getClass(), "hit.wav");
                        AudioManager.playSound(getClass(), "boom.wav");
                        score += 50;
                        break;
                    }
                }
            }
        }
    }

    private boolean collide(int x, int y, int w, int h) {
        Rectangle me = new Rectangle(x, y, w, h);
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
        return false;
    }

    private boolean hit(int x1,int y1,int w1,int h1, int x2,int y2,int w2,int h2) {
        return new Rectangle(x1,y1,w1,h1).intersects(new Rectangle(x2,y2,w2,h2));
    }

    private void fire(Tank tank) {
        int cx = tank.getX() + 20;
        int cy = tank.getY() + 20;
        bullets.add(new Bullet(cx-5, cy-5, tank.getDir(), tank));
        AudioManager.playSound(getClass(), "fire.wav");
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
        g.drawString("WASD移动  空格发射  ESC暂停", 20, GH - 20);

        if (gameOver) {
            g.setColor(new Color(0,0,0,180));
            g.fillRect(0,0,GW,GH);
            g.setColor(Color.RED);
            g.setFont(new Font("黑体", Font.BOLD, 60));
            g.drawString("游戏结束", GW/2 - 160, GH/2);
        }

        pausePanel.setVisible(paused);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            return;
        }
        if (paused || gameOver) return;

        switch (code) {
            case KeyEvent.VK_W: up    = true; break;
            case KeyEvent.VK_S: down  = true; break;
            case KeyEvent.VK_A: left  = true; break;
            case KeyEvent.VK_D: right = true; break;
            case KeyEvent.VK_SPACE: fire(player); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: up    = false; break;
            case KeyEvent.VK_S: down  = false; break;
            case KeyEvent.VK_A: left  = false; break;
            case KeyEvent.VK_D: right = false; break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
}