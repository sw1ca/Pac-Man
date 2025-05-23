package view;

import javax.swing.*;

public class MenuWindow extends JFrame {
    public MenuWindow() {
        setTitle("Pac Man");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("Chase & Chew");
        label.setBounds(360, 95, 300, 20);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBounds(150, 200, 500, 50);
        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.setBounds(150, 300, 500, 50);
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(150, 400, 500, 50);

        newGameButton.addActionListener(e -> {});
        highScoresButton.addActionListener(e -> {});
        exitButton.addActionListener(e -> { System.exit(0); });

        panel.add(label);
        panel.add(newGameButton);
        panel.add(highScoresButton);
        panel.add(exitButton);
        add(panel);

        setVisible(true);
    }
}