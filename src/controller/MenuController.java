package controller;

import view.GameWindow;
import view.MenuWindow;

import javax.swing.*;

public class MenuController {
    private MenuWindow view;

    public MenuController(MenuWindow view) {
        this.view = view;

        this.view.addNewGameListener(e -> startNewGame());
        this.view.addHighScoresListener(e -> showHighScores());
        this.view.addExitListener(e -> exitApplication());
    }

    private void startNewGame() {
        while(true) {
            String input = JOptionPane.showInputDialog(view, "Enter board size (10-100)", "New Game", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return;
            }
            try {
                int size = Integer.parseInt(input);
                if(size >= 10 && size <= 100) {
                    openGameWindow(size);
                    break;
                } else {
                    JOptionPane.showMessageDialog(view, "Please enter a number between 10 and 100.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void openGameWindow(int size) {
        GameWindow gameWindow = new GameWindow(size);
        gameWindow.setVisible(true);
    }

    private void showHighScores() {}

    private void exitApplication() {
        System.exit(0);
    }
}
