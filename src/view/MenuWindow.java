package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuWindow extends JFrame {
    private JButton newGameButton;
    private JButton highScoresButton;
    private JButton exitButton;

    public MenuWindow() {
        setTitle("Chase & Chew");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);

        JLabel label = new JLabel("Chase & Chew");
        label.setBounds(360, 95, 300, 20);
        label.setForeground(Color.YELLOW);

        newGameButton = new JButton("New Game");
        newGameButton.setBounds(150, 200, 500, 50);

        highScoresButton = new JButton("High Scores");
        highScoresButton.setBounds(150, 300, 500, 50);

        exitButton = new JButton("Exit");
        exitButton.setBounds(150, 400, 500, 50);

        panel.add(label);
        panel.add(newGameButton);
        panel.add(highScoresButton);
        panel.add(exitButton);
        add(panel);

        setVisible(true);
    }

    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    public void addHighScoresListener(ActionListener listener) {
        highScoresButton.addActionListener(listener);
    }

    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}
