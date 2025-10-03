package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private final Player player;
    private final Controller controller;
    private final Timer timer;
    private Image background;

    private long startTime;
    private int elapsedSeconds;

    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();
    private long lastSpawnTime = 0;
    private int spawnDelay = 3000; 

    public GamePanel() {
        setPreferredSize(new Dimension(1500, 1000));
        background = loadBackground("/sprites/backgrounds/bg1.png");

        player = new Player(100, 300);
        controller = new Controller();
        addKeyListener(controller);
        setFocusable(true);
        requestFocusInWindow();

        timer = new Timer(16, e -> gameLoop());
        timer.start();

        startTime = System.currentTimeMillis();
    }

    private Image loadBackground(String path) {
        try {
            java.net.URL u = getClass().getResource(path);
            if (u == null) return new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
            return new ImageIcon(u).getImage();
        } catch (Exception ex) {
            return new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private void checkCollisions() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            if (player.getBounds().intersects(e.getBounds())) {
                if (player.getFeetY() < e.getBounds().y) {
                    enemies.remove(i);
                } 
                else {
                    boolean playerDead = player.takeDamage();
                    enemies.remove(i);
                    if (playerDead) {
                        timer.stop();
                    }
                }
            }
        }
    }

    private void gameLoop() {
        player.handleInput(controller, getWidth(), getHeight());
        player.update();
        long now = System.currentTimeMillis();
        if (now - lastSpawnTime > spawnDelay) {
            int x = random.nextInt(Math.max(1, getWidth() - 100));
            int y = random.nextInt(Math.max(1, getHeight() - 100));
            enemies.add(new Bubbo(x, y));
            lastSpawnTime = now;
        }
        for (Enemy e : enemies) {
            e.update();
            e.pathFinding(player.getCoordinates()[0], player.getCoordinates()[1], getWidth(), getHeight());
        }
        checkCollisions();
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw background stretched
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        long now = System.currentTimeMillis();
        elapsedSeconds = (int)((now - startTime) / 1000);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Time: " + elapsedSeconds + "s", 20, 30);
        g.drawString("Lives: " + player.getLives(), 20, 60);

        player.draw(g);
        //debugging
        g.setColor(Color.RED);
        g.drawRect(player.getBounds().x, player.getBounds().y,
        player.getBounds().width, player.getBounds().height);
        for (Enemy e : enemies) {
            e.draw(g);

            //debugging
            g.setColor(Color.BLACK);
            Rectangle b = e.getBounds();
            g.drawRect(b.x, b.y, b.width, b.height);
        }

    }    
}
