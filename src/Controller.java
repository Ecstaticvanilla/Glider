package src;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Controller implements ActionListener, KeyListener {
    private final Player player;
    private final Runnable repaintCallback;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public Controller(Player player, Runnable repaintCallback) {
        this.player = player;
        this.repaintCallback = repaintCallback;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dx = 0, dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) dx += 5;
        if (pressedKeys.contains(KeyEvent.VK_LEFT))  dx -= 5;
        if (pressedKeys.contains(KeyEvent.VK_UP))    dy -= 5;
        if (pressedKeys.contains(KeyEvent.VK_DOWN))  dy += 5;
        if (dx != 0 || dy != 0) {
            player.changePosition(dx, dy,1500,1000);
        } else {
            player.stop();
        }

        if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
            player.jump();
        }

        player.update();
        repaintCallback.run();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_R) {
            player.resetPosition(100, 400);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.land();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
