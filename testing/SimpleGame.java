package testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleGame extends JPanel implements ActionListener, KeyListener {
    int x = 50, y = 50; // player position
    Timer timer;

    public SimpleGame() {
        // Game loop: updates every 16 ms (~60 FPS)
        timer = new Timer(16, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(x, y, 30, 30); // draw player
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update game state here (movement, physics, collisions)
        repaint(); // redraw the screen
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)  x -= 5;
        if (key == KeyEvent.VK_RIGHT) x += 5;
        if (key == KeyEvent.VK_UP)    y -= 5;
        if (key == KeyEvent.VK_DOWN)  y += 5;
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple 2D Game");
        SimpleGame game = new SimpleGame();
        frame.add(game);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
