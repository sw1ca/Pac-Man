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
            java.util.List<PlayerScore> scores = HighScores.getHighScores();

            DefaultListModel<String> model = new DefaultListModel<>();
            for (PlayerScore entry : scores) {
                model.addElement(entry.getName() + " - " + entry.getScore());
            }

            JList<String> list = new JList<>(model);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setPreferredSize(new Dimension(300, 200));

            Object[] options = {"Clear Scores", "Close"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    scrollPane,
                    "High Scores",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (result == JOptionPane.YES_OPTION) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to clear all high scores?",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    HighScores.clearHighScores();
                    JOptionPane.showMessageDialog(null, "High scores have been cleared.");
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
