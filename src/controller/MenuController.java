package controller;

import model.Player;
import view.GameWindow;
import view.MenuWindow;

import javax.swing.*;
import java.awt.*;

public class MenuController {
    private MenuWindow view;

    public MenuController(MenuWindow view) {
        this.view = view;

        this.view.addNewGameListener(e -> startNewGame());
        this.view.addHighScoresListener(e -> showHighScores());
        this.view.addExitListener(e -> exitApplication());
    }

    private void startNewGame() {
        while (true) {
            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
            JTextField widthField = new JTextField();
            JTextField heightField = new JTextField();

            panel.add(new JLabel("Enter width (10-100):"));
            panel.add(widthField);
            panel.add(new JLabel("Enter height (10-100):"));
            panel.add(heightField);

            int result = JOptionPane.showConfirmDialog(view, panel, "New Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }
            try {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());

                if (width >= 10 && width <= 100 && height >= 10 && height <= 100) {
                    openGameWindow(width, height);
                    break;
                } else {
                    JOptionPane.showMessageDialog(view, "Width and height must be between 10 and 100.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Please enter valid numbers for width and height.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void openGameWindow(int width, int height) {
        GameWindow gameWindow = new GameWindow(width, height);
        gameWindow.setVisible(true);
        Player player = new Player(gameWindow.getTableModel(), 1, 1);
        GameController gameController = new GameController(gameWindow, player);
        gameController.start();
    }

    private void showHighScores() {}

    private void exitApplication() {
        System.exit(0);
    }
}
