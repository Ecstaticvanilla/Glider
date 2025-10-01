package testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.Player;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Player Test Environment");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GamePanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

// ================= Game Panel =================
class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Player player;
    private Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        player = new Player(100, 400); // 👈 use your Player constructor

        timer = new Timer(16, this); // ~60 FPS
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.update();  // update animation
        player.draw(g);   // draw player
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint(); // repaint every frame
    }

    // ---------------- Key Controls ----------------
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_RIGHT) {
            player.changePosition(5, 0);
        }
        if (code == KeyEvent.VK_LEFT) {
            player.changePosition(-5, 0);
        }       
        if (code == KeyEvent.VK_UP) {
            player.changePosition(0, -5);
        }       
        if (code == KeyEvent.VK_DOWN) {
            player.changePosition(0, 5);
        }
        if (code == KeyEvent.VK_SPACE) {
            player.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_LEFT || code == KeyEvent.VK_SPACE) {
            player.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}
