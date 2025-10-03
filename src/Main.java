package src;

import javax.swing.*;
import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Glider - Test");
            GamePanel gp = new GamePanel();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(gp);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);

            gp.requestFocusInWindow();
        });
    }
}
