package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class Enemy {
    protected int health = 3;
    protected boolean isVisible = true;
    protected int x, y;
    protected int width = 50, height = 50;
    protected int hitboxwidth = 30, hitboxheight = 20;
    protected boolean facingRight = true;
    protected EnemyAction action;

    public enum EnemyAction { DYING, ATTACKING }

    protected Map<EnemyAction, Image[]> animations = new HashMap<>();
    protected int frameIndex = 0;
    protected long lastFrameTime = 0;
    protected int frameDelay = 100;
    protected int speed = 2;

    public Enemy(int x, int y,int width, int height, int health) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.action = EnemyAction.ATTACKING;
        loadAnimations();
    }

    protected Image safeLoad(String path) {
        try {
            URL res = getClass().getResource(path);
            if (res == null) return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            return new javax.swing.ImageIcon(res).getImage();
        } catch (Exception ex) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
    }

    protected abstract void loadAnimations();

    public void update() {
        long now = System.currentTimeMillis();
        Image[] frames = animations.get(action);
        if (frames != null && frames.length > 0) {
            if (now - lastFrameTime > frameDelay) {
                frameIndex = (frameIndex + 1) % frames.length;
                lastFrameTime = now;
            }
            if (frameIndex >= frames.length) frameIndex = 0;
        }
    }

    public void draw(Graphics g) {
        if (!isVisible) return;
        Graphics2D g2d = (Graphics2D) g;
        Image[] frames = animations.get(action);
        if (frames == null || frames.length == 0) return;
        Image current = frames[frameIndex];

        int shadowW = (int)(width * 0.8);
        int shadowH = (int)(height * 0.2);
        int shadowX = x + (width - shadowW) / 2;
        int shadowY = y + height - 5;
        g2d.setColor(new Color(0,0,0,100));
        g2d.fillOval(shadowX, shadowY, shadowW, shadowH);

        if (facingRight) {
            g2d.drawImage(current, x, y, width, height, null);
        } else {
            AffineTransform t = AffineTransform.getTranslateInstance(x + width, y);
            t.scale(-1, 1);
            g2d.drawImage(current, t, null);
        }
    }

    public void setAction(EnemyAction newAction) {
        if (this.action != newAction) {
            this.action = newAction;
            this.frameIndex = 0;
            this.lastFrameTime = System.currentTimeMillis();
        } else {
            Image[] frames = animations.get(action);
            if (frames != null && frameIndex >= frames.length) frameIndex = 0;
        }
    }

    public boolean takeDamage() {
        health--;
        if (health <= 0) {
            health = 0;
            isVisible = false;
            action = EnemyAction.DYING;
            return true;
        }
        return false;
    }

    public void changePosition(int dx, int dy, int panelWidth, int panelHeight) {
        x += dx;
        y += dy;

        if (x < 0) x = 0;
        if (x > panelWidth - width) x = panelWidth - width;
        if (y < 20) y = 20;
        if (y > panelHeight - height) y = panelHeight - height;

        if (dx > 0) facingRight = true;
        if (dx < 0) facingRight = false;
        setAction(EnemyAction.ATTACKING);
    }

    public void resetPosition(int nx, int ny) {
        x = nx; y = ny;
        setAction(EnemyAction.ATTACKING);
        isVisible = true;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, this.hitboxwidth, this.hitboxheight);
    }

    public void pathFinding(int playerX, int playerY, int panelWidth, int panelHeight) {
        int dx = Integer.compare(playerX, this.x) * speed;
        int dy = Integer.compare(playerY, this.y) * speed;
        changePosition(dx, dy, panelWidth, panelHeight);
    }

    public void pathFinding(int playerX, int playerY) {
        pathFinding(playerX, playerY, 1500, 1000);
    }
}
