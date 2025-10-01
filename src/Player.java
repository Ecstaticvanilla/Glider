package src;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class Player {
    private int lives = 3;
    private int x, y;
    private PlayerState state;
    private PlayerAction action;
    private int width = 50, height = 50;

    //Can Add States Later
    public enum PlayerState { ALIVE, DEAD }
    public enum PlayerAction { IDLE, RUN, JUMP }

    private Map<PlayerAction, Image[]> animations; 
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private int frameDelay = 300; 

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
            new ImageIcon(getClass().getResource("/sprites/idle/2.png")).getImage()
        });

        animations.put(PlayerAction.RUN, new Image[] {
            new ImageIcon(getClass().getResource("/sprites/running/1.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/running/2.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/running/3.png")).getImage()
        });

        animations.put(PlayerAction.JUMP, new Image[] {
            new ImageIcon(getClass().getResource("/sprites/jump/1.png")).getImage(),
            new ImageIcon(getClass().getResource("/sprites/jump/2.png")).getImage()
        });
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime > frameDelay) {
            frameIndex = (frameIndex + 1) % animations.get(action).length;
            lastFrameTime = now;
        }
    }

    public void draw(java.awt.Graphics g) {
        Image[] frames = animations.get(action);
        g.drawImage(frames[frameIndex], this.x, this.y, this.width, this.height, null);
    }

    public void setAction(PlayerAction newAction) {
        if (this.action != newAction) {
            this.action = newAction;
            this.frameIndex = 0; 
            this.lastFrameTime = System.currentTimeMillis();
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

    public void changePosition(int xchange, int ychange) {
        this.x += xchange;
        this.y += ychange;
        setAction(PlayerAction.RUN);
    }

    public void changeState(PlayerState newState) {
        this.state = newState;
    }    
    
    public void stop() {
        this.action = PlayerAction.IDLE;
    }

    public void jump() {
        setAction(PlayerAction.JUMP);
    }
    
    public void land() {
        setAction(PlayerAction.IDLE);
    }
}
