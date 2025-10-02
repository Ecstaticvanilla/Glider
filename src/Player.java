package src;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private int lives = 3;
    private int x, y;
    private PlayerState state;
    private PlayerAction action;
    private int width = 50, height = 50;
    private boolean facingRight = true; 
    private int jumpOffset = 0;        
    private boolean isJumping = false;
    private int jumpFrame = 0;
    private int maxJumpHeight = 40;
    private int jumpDuration = 20;     
    private long lastJumpTime = 0;
    private long jumpCooldown = 500; 
    int speed = 10;

    //Can Add States Later
    public enum PlayerState { ALIVE, DEAD }
    public enum PlayerAction { IDLE, RUN, JUMP }

    private Map<PlayerAction, Image[]> animations; 
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private int frameDelay = 100; 

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = PlayerState.ALIVE;
        this.action = PlayerAction.IDLE;
        animations = new HashMap<>();
        loadAnimations();
    }

    // Getters
    public int[] getCoordinates() {
        return new int[] { this.x, this.y };
    }

    public PlayerState getState() {
        return this.state;
    }    
    
    public PlayerAction getAction() {
        return this.action;
    }    

    public int getLives() {
        return this.lives;
    }

    // Load Animations
        private void loadAnimations() {
        animations.put(PlayerAction.IDLE, new Image[] {
            new ImageIcon(getClass().getResource("/sprites/idle/1.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/idle/2.png")).getImage(),  
            new ImageIcon(getClass().getResource("/sprites/idle/3.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/idle/4.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/idle/5.png")).getImage()
        });

        animations.put(PlayerAction.RUN, new Image[] {
            new ImageIcon(getClass().getResource("/sprites/running/1.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/running/2.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/running/3.png")).getImage(), 
            new ImageIcon(getClass().getResource("/sprites/running/4.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/running/5.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/running/6.png")).getImage()
        });

        animations.put(PlayerAction.JUMP, new Image[] {
            new ImageIcon(getClass().getResource("/sprites/jump/1.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/jump/2.png")).getImage()
        });
    }

    public void update() {
        long now = System.currentTimeMillis();
        Image[] frames = animations.get(action);

        if (frames != null && frames.length > 0) {
            if (now - lastFrameTime > frameDelay) {
                if (action == PlayerAction.JUMP) {
                    if (frameIndex < frames.length - 1) {
                        frameIndex++;
                    }
                } else {
                    frameIndex = (frameIndex + 1) % frames.length;
                }
                lastFrameTime = now;
            }
        }

        if (isJumping) {
            double t = (double) jumpFrame / jumpDuration;
            jumpOffset = (int)(-4 * maxJumpHeight * t * (t - 1)); 
            jumpFrame++;
            if (jumpFrame > jumpDuration) {
                isJumping = false;
                jumpOffset = 0;
                setAction(PlayerAction.IDLE);
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Image[] frames = animations.get(action);
        Image currentFrame = frames[frameIndex];

        double jumpFactor = jumpOffset / (double) maxJumpHeight;
        int shadowWidth = (int) (width * 0.8 * (1 - jumpFactor * 0.5));
        int shadowHeight = (int) (height * 0.2 * (1 - jumpFactor * 0.5));
        int shadowX = x + (width - shadowWidth) / 2;
        int shadowY = y + height - 5;

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(shadowX, shadowY, shadowWidth, shadowHeight);

        if (facingRight) {
            g2d.drawImage(currentFrame, x, y - jumpOffset, width, height, null);
        } else {
            AffineTransform transform = AffineTransform.getTranslateInstance(x + width, y - jumpOffset);
            transform.scale(-1, 1);
            g2d.drawImage(currentFrame, transform, null);
        }
    }

    public void setAction(PlayerAction newAction) {
        if (this.action != newAction) {
            this.action = newAction;
            this.frameIndex = 0; 
            this.lastFrameTime = System.currentTimeMillis();
        } else {
            if (frameIndex >= animations.get(action).length) {
                frameIndex = 0;
            }
        }
    }

    // Stat Changers
    public void oneUp() {
        this.lives++;
    }    

    public boolean takeDamage() {
        this.lives--;
        if (this.lives <= 0) {
            this.lives = 0;
            this.state = PlayerState.DEAD;
            return true;
        }
        return false;
    }

    public void changePosition(int dx, int dy, int panelWidth, int panelHeight) {
        this.x += dx;
        this.y += dy;

        if (x < 0) x = 0;
        if (x > panelWidth - width) x = panelWidth - width;

        if (y < 20) y = 20;
        if (y > panelHeight - height) y = panelHeight - height;

        if (dx > 0) facingRight = true;
        if (dx < 0) facingRight = false;
        setAction(PlayerAction.RUN);
    }

    public void resetPosition(int x, int y) {
        this.x = x;
        this.y = y;
        setAction(PlayerAction.IDLE);
    }

    public void changeState(PlayerState newState) {
        this.state = newState;
    }    
    
    public void stop() {
        this.action = PlayerAction.IDLE;
    }

    public void jump() {
        long now = System.currentTimeMillis();
        if (!isJumping && now - lastJumpTime >= jumpCooldown) {
            isJumping = true;
            jumpFrame = 0;
            lastJumpTime = now;
            setAction(PlayerAction.JUMP);
        }
    }


    public void land() {
        setAction(PlayerAction.IDLE);
    }
}
