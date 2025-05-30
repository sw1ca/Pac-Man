package controller;

import model.*;
import view.GameWindow;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final Player player;
    private final GameWindow window;
    private final Map model;
    private int directionX = 0;
    private int directionY = 0;
    private boolean moving = false;
    private List<Ghost> ghosts = new ArrayList<>();

    public GameController(GameWindow window, Player player) {
        this.window = window;
        window.setGameController(this);
        this.player = player;
        this.model = window.getTableModel();

        // Initialize player position in the model
        model.getCell(player.getY(), player.getX()).setPlayer(true);
        model.fireTableDataChanged();

        ghosts.add(new Ghost(model, Ghost.ColorType.RED, Ghost.SpawnCorner.CENTER));
        ghosts.add(new Ghost(model, Ghost.ColorType.BLUE, Ghost.SpawnCorner.TOP_RIGHT));
        ghosts.add(new Ghost(model, Ghost.ColorType.PINK, Ghost.SpawnCorner.BOTTOM_LEFT));
        ghosts.add(new Ghost(model, Ghost.ColorType.ORANGE, Ghost.SpawnCorner.BOTTOM_RIGHT));

        // Ghosts movement
        new Thread(() -> {
            while (true) {
                for (Ghost ghost : ghosts) {
                    ghost.updatePosition();
                }
                model.fireTableDataChanged();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        window.getTable().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> setDirection(-1, 0);
                    case KeyEvent.VK_RIGHT -> setDirection(1, 0);
                    case KeyEvent.VK_UP -> setDirection(0, -1);
                    case KeyEvent.VK_DOWN -> setDirection(0, 1);
                }
            }
        });

        window.getTable().setFocusable(true);
        window.getTable().requestFocusInWindow();
    }

    private void setDirection(int directionX, int directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
        if (directionX == -1 && directionY == 0) player.setDirection(Player.Direction.LEFT);
        else if (directionX == 1 && directionY == 0) player.setDirection(Player.Direction.RIGHT);
        else if (directionX == 0 && directionY == -1) player.setDirection(Player.Direction.UP);
        else if (directionX == 0 && directionY == 1) player.setDirection(Player.Direction.DOWN);

        if (!moving) {
            moving = true;
            startMovingThread();
        }
    }

    public void stopMoving() {
        moving = false;
    }

    private boolean scoreSaved = false;
    public void addScore() {
        if(scoreSaved) return; // Prevent multiple score prompts
        scoreSaved = true; // Mark that the score has been saved
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(window, "Bravo! Enter your nickname: ");
            if (name != null && !name.trim().isEmpty()) {
                HighScores.addScore(name.trim(), player.getScore());
                JOptionPane.showMessageDialog(window, "Score saved!");
            }
            window.dispose();
        });
    }

    private void startMovingThread() {
        new Thread(() -> {
            long lastMoveTime = 0;

            while (moving) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastMoveTime >= 120) {
                    int newX = player.getX() + directionX;
                    int newY = player.getY() + directionY;

                    if (canMoveTo(newY, newX)) {
                        model.getCell(player.getY(), player.getX()).setPlayer(false);

                        GameCell newCell = model.getCell(newY, newX);
                        newCell.setPlayer(true);

                        if (newCell.hasDot()) {
                            newCell.setDot(false);
                            player.addScore(20);
                            window.updateScore(player.getScore());

                            if (allDotsCollected()) {
                                moving = false;
                                addScore();
                            }
                        }

                        player.setPosition(newX, newY);

                        model.fireTableDataChanged();
                    }

                    lastMoveTime = currentTime;
                }

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    private boolean canMoveTo(int row, int column) {
        return row >= 0 && row < model.getRowCount()
                && column >= 0 && column < model.getColumnCount()
                && !model.getCell(row, column).isWall();
    }
    private void startAnimationThread() {
        new Thread(() -> {
            while (true) {
                player.toggleMouth();
                model.fireTableDataChanged();

                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
    private boolean allDotsCollected() {
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (model.getCell(row, col).hasDot()) {
                    return false;
                }
            }
        }
        return true;
    }
}