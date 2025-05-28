package view;

import model.PlayerScore;
import model.HighScores;

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
        highScoresButton.addActionListener(e -> {
            JTextArea scoresArea = new JTextArea(15, 30);
            scoresArea.setEditable(false);

            StringBuilder sb = new StringBuilder();
            for (PlayerScore entry : HighScores.getHighScores()) {
                sb.append(entry.getName()).append(" - ").append(entry.getScore()).append("\n");
            }
            if (sb.length() == 0) sb.append("No high scores yet.");
            scoresArea.setText(sb.toString());

            JScrollPane scrollPane = new JScrollPane(scoresArea);

            Object[] options = {"Clear Scores", "Close"};

            int choice = JOptionPane.showOptionDialog(
                    null,
                    scrollPane,
                    "High Scores",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (choice == JOptionPane.YES_OPTION) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to clear all scores?",
                        "Confirm Clear",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    HighScores.clearHighScores();
                    JOptionPane.showMessageDialog(null, "Scores cleared.");
                }
            }
        });

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
