package src;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


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
    private int maxJumpHeight = 80;
    private int jumpDuration = 50;
    private long lastJumpTime = 0;
    private long jumpCooldown = 500; 
    private int speed = 5;
    private int hitboxwidth = 3*width/5,hitboxheight = 3*height/5;

    public enum PlayerState { ALIVE, DEAD }
    public enum PlayerAction { IDLE, RUN, JUMP }

    private Map<PlayerAction, BufferedImage[]> animations = new HashMap<>();
    private int numFrames = 7;
    BufferedImage[] frames = new BufferedImage[numFrames];
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private int frameDelay = 100;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = PlayerState.ALIVE;
        this.action = PlayerAction.IDLE;
        loadFrames(this.frames);
        loadAnimations();
    }

    private void loadFrames(BufferedImage[] frames) {
        try {
            BufferedImage spriteSheet = ImageIO.read(
                getClass().getResourceAsStream("/sprites/Player/spriteSheetPlayer.png")
            );

            for (int i = 0; i < numFrames; i++) {
                frames[i] = spriteSheet.getSubimage(i * this.width, 0, this.width, this.height);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Could not find resource: /sprites/Player/spriteSheetPlayer.png");
        }
    }


    // Deprecated
    // private Image safeLoad(String path) {
    //     try {
    //         URL res = getClass().getResource(path);
    //         if (res == null) {
    //             return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    //         }
    //         return new ImageIcon(res).getImage();
    //     } catch (Exception ex) {
    //         return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    //     }
    // }

    private void loadAnimations() {
        animations.put(PlayerAction.IDLE, new BufferedImage[] {
           frames[0],
           frames[1],
           frames[3],
           frames[1],
           frames[0]
        });

        animations.put(PlayerAction.RUN, new BufferedImage[] {
            frames[0],
            frames[5],
            frames[4],
            frames[5],
            frames[6],
            frames[0]
        });

        animations.put(PlayerAction.JUMP, new BufferedImage[] {
            frames[0],
            frames[2]
        });
    }

    public void handleInput(Controller controller, int panelWidth, int panelHeight) {
        int dx = 0, dy = 0;
        if (controller.isKeyPressed(KeyEvent.VK_RIGHT)) dx += speed;
        if (controller.isKeyPressed(KeyEvent.VK_LEFT))  dx -= speed;
        if (controller.isKeyPressed(KeyEvent.VK_UP))    dy -= speed;
        if (controller.isKeyPressed(KeyEvent.VK_DOWN))  dy += speed;


        if (controller.isKeyPressed(KeyEvent.VK_SPACE)) {
            jump();
        }

        if (dx != 0 || dy != 0) {
            changePosition(dx, dy, panelWidth, panelHeight);
        } else {
            stop();
        }

    }

    public void update() {
        long now = System.currentTimeMillis();
        BufferedImage[] frames = animations.get(action);
        if (frames != null && frames.length > 0) {
            if (now - lastFrameTime > frameDelay) {
                frameIndex = (frameIndex + 1) % frames.length;
                lastFrameTime = now;
            }
            if (frameIndex >= frames.length) frameIndex = 0;
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
        BufferedImage[] frames = animations.get(action);
        if (frames == null || frames.length == 0) return;
        Image currentFrame = frames[frameIndex];

        double jumpFactor = (maxJumpHeight == 0) ? 0 : (jumpOffset / (double) maxJumpHeight);
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
            BufferedImage[] frames = animations.get(action);
            if (frames != null && frameIndex >= frames.length) frameIndex = 0;
        }
    }

    public void changePosition(int dx, int dy, int panelWidth, int panelHeight) {
        x += dx;
        y += dy;

        if (x < 0) x = 0;
        if (x > panelWidth - width) x = panelWidth - width;
        //20 buffer on top to take in mind the jump
        if (y < 20) y = 20;
        if (y > panelHeight - height) y = panelHeight - height;

        if (dx > 0) facingRight = true;
        if (dx < 0) facingRight = false;
        // setAction(PlayerAction.RUN);
        if (!isJumping) {
            setAction(PlayerAction.RUN);
        }

    }

    public void stop() {
        setAction(PlayerAction.IDLE);
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

    public Rectangle getBounds() {
        return new Rectangle(x+width/5, y+height/5-jumpOffset, hitboxwidth, hitboxheight);
    }

    public int[] getCoordinates() {
        return new int[] { x, y };
    }

    public boolean takeDamage() {
        lives--;
        if (lives <= 0) {
            lives = 0;
            state = PlayerState.DEAD;
            return true;
        }
        return false;
    }

    public int getLives() {
        return lives;
    }

    public int getFeetY() {
        return jumpOffset + height/5;
    }

}
