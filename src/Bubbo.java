package src;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Bubbo extends Enemy {
    private static BufferedImage spriteSheet;
    private static BufferedImage[] attackFrames;
    private static BufferedImage[] dyingFrames;

    static {
        try {
            spriteSheet = ImageIO.read(
                Bubbo.class.getResourceAsStream("/sprites/enemies/bubbo/spriteSheetbubbo.png")
            );
            int frameWidth = 50;
            int frameHeight = 50;
            attackFrames = new BufferedImage[4];
            for (int i = 0; i < 4; i++) {
                attackFrames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            // dyingFrames = new BufferedImage[2];
            // for (int i = 0; i < 2; i++) {
            //     dyingFrames[i] = spriteSheet.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
            // }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Bubbo(int x, int y) {
        super(x, y , 50 ,50, 5);
    }
    private int hitboxwidth = 3 * width / 5;
    private int hitboxheight = 2 * height / 5;
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + width/5, y + 2*height/5, this.hitboxwidth, this.hitboxheight);
    }
    @Override
    protected void loadAnimations() {
        animations.put(EnemyAction.ATTACKING, attackFrames);
        // animations.put(EnemyAction.DYING, dyingFrames);
    }
}
