package controller;

import model.GameTableModel;
import model.Player;
import view.GameWindow;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private Player player;
    private final GameWindow window;
    private final GameTableModel model;

    private int playerRow = 1;
    private int playerColumn = 1;

    public GameController(GameWindow window, Player player) {
        this.window = window;
        this.player = player;
        this.model = window.getTableModel();

        window.getTable().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> player.setDirection(-1, 0);
                    case KeyEvent.VK_RIGHT -> player.setDirection(1, 0);
                    case KeyEvent.VK_UP -> player.setDirection(0, -1);
                    case KeyEvent.VK_DOWN -> player.setDirection(0, 1);
                }
            }
        });
        window.getTable().setFocusable(true);
        window.getTable().requestFocusInWindow();
    }
    private void handleKeyPress(int keyCode) {
        int newRow = playerRow;
        int newCol = playerColumn;

        switch (keyCode) {
            case KeyEvent.VK_UP -> newRow--;
            case KeyEvent.VK_DOWN -> newRow++;
            case KeyEvent.VK_LEFT -> newCol--;
            case KeyEvent.VK_RIGHT -> newCol++;
            default -> {
                return;
            }
        }

        // check if the new position is valid
        if (canMoveTo(newRow, newCol)) {
            model.getCell(playerRow, playerColumn).setPlayer(false);
            model.getCell(newRow, newCol).setPlayer(true);

            playerRow = newRow;
            playerColumn = newCol;

            model.fireTableDataChanged();
        }
    }

    private boolean canMoveTo(int row, int col) {
        if (row < 0 || row >= model.getRowCount()) return false;
        if (col < 0 || col >= model.getColumnCount()) return false;
        return !model.getCell(row, col).isWall();
    }
    public void start() {
        new Thread(player).start();
    }
}
