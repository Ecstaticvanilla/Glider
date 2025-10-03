package src;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Controller implements KeyListener {
    private final Set<Integer> pressedKeys = new HashSet<>();

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
