package testing;

import javax.swing.*;
import java.awt.*;
import src.Player;
import src.Controller;

class GamePanel extends JPanel {
    private final Player player;
    private final Controller controller;
    private final Timer timer;
    private Image background;

    private long startTime;  
    private int elapsedSeconds;

    public GamePanel() {
        setPreferredSize(new Dimension(1500, 1000));
        background = new ImageIcon(getClass().getResource("/sprites/backgrounds/bg1.png")).getImage();

        player = new Player(100, 400);
        controller = new Controller(player, this::repaint);

        timer = new Timer(1000 / 60, controller);
        timer.start();

        startTime = System.currentTimeMillis(); 

        addKeyListener(controller);
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        long now = System.currentTimeMillis();
        elapsedSeconds = (int)((now - startTime) / 1000);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("Time: " + elapsedSeconds + "s", 20, 40);

        player.draw(g);
    }
}


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Player Test Environment"); 
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GamePanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
