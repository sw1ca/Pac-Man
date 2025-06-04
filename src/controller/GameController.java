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
    private int moveDelay = 120;
    private boolean moving = false;
    private List<Ghost> ghosts = new ArrayList<>();
    private int timer = 0;
    private boolean timerRunning = true;

    public GameController(GameWindow window, Player player) {
        this.window = window;
        window.setGameController(this);
        this.player = player;
        this.model = window.getTableModel();

        // Initialize player position in the model
        model.getCell(player.getY(), player.getX()).setPlayer(true);
        model.fireTableDataChanged();

        initializeGhosts();
        initializeKeyListeners();
        initializeGameThreads();
    }
    private void initializeGhosts() {
        ghosts.add(new Ghost(model, Ghost.ColorType.RED, Ghost.SpawnCorner.CENTER));
        ghosts.add(new Ghost(model, Ghost.ColorType.BLUE, Ghost.SpawnCorner.TOP_RIGHT));
        ghosts.add(new Ghost(model, Ghost.ColorType.PINK, Ghost.SpawnCorner.BOTTOM_LEFT));
        ghosts.add(new Ghost(model, Ghost.ColorType.ORANGE, Ghost.SpawnCorner.BOTTOM_RIGHT));
    }
    private void initializeKeyListeners() {
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
    private void initializeGameThreads() {
        startGhostThread();
        startAnimationThread();
        startTimerThread();
        startCollisionCheckThread();
    }
    private void startGhostThread() {
        new Thread(() -> {
            long lastPowerUpTime = System.currentTimeMillis();
            while (true) {
                for (Ghost ghost : ghosts) {
                    ghost.updatePosition();
                }
                long now = System.currentTimeMillis();
                if (now - lastPowerUpTime >= 5000) {
                    lastPowerUpTime = now;
                    if (Math.random() < 0.25) {
                        spawnPowerUpAtGhostPreviousPosition();
                    }
                }

                model.fireTableDataChanged();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
    private void startCollisionCheckThread() {
        new Thread(() -> {
            while(true) {
                checkColisionWithGhosts();
                try {
                    Thread.sleep(50); // Check collisions every 50 ms
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
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
        if (scoreSaved) return; // Prevent multiple score prompts
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
                if (currentTime - lastMoveTime >= moveDelay) {
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
                        if (newCell.hasPowerUp()) {
                            PowerUp powerUp = newCell.getPowerUp();
                            newCell.setPowerUp(null);

                            switch (powerUp.getType()) {
                                case SPEED_BOOST -> setSpeedBoost(5000);
                                case SCORE_BOOST -> {
                                    player.addScore(1000);
                                    window.updateScore(player.getScore());
                                }
                                case HP_BOOST -> {
                                    player.addLife();
                                    window.updateLives(player.getLives());
                                }
                                case GHOST_FREEZE -> freezeGhosts(5000);
                                case GHOSTS_SLOW -> ghostsSlow(5000);
                            }
                        }

                        player.setPosition(newX, newY);
                        checkColisionWithGhosts();

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
    private void startTimerThread() {
        new Thread(() -> {
            while (timerRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }

                timer++;
                window.updateTime(timer);
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

    private void showGameOver() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(window, "Game Over!");
            window.dispose();
            timerRunning = false;
        });
    }

    private void resetPlayerPosition() {
        int startX = 1;
        int startY = 1;
        model.getCell(player.getY(), player.getX()).setPlayer(false);
        player.setPosition(startX, startY);
        model.getCell(startY, startX).setPlayer(true);
        model.fireTableDataChanged();
    }

    private void checkColisionWithGhosts() {
        for (Ghost ghost : ghosts) {
            if (ghost.getX() == player.getX() && ghost.getY() == player.getY()) {
                player.loseLife();
                window.updateLives(player.getLives());

                if (player.getLives() <= 0) {
                    moving = false;
                    showGameOver();
                    this.addScore();
                } else {
                    resetPlayerPosition();
                }
                break;
            }
        }
    }

    public void setSpeedBoost(int durationMs) {
        moveDelay = 60; // Speed up 2 times
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            moveDelay = 120; // Reset to normal speed
        }).start();
    }

    public void freezeGhosts(int durationMs) {
        List<Thread> freezeThreads = new ArrayList<>();
        for (Ghost ghost : ghosts) {
            Thread freezeThread = new Thread(() -> {
                ghost.setFrozen(true);
                try {
                    Thread.sleep(durationMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ghost.setFrozen(false);
                }
            });
            freezeThreads.add(freezeThread);
            freezeThread.start();
        }
    }

    public void ghostsSlow(int durationMs) {
        for (Ghost ghost : ghosts) {
            ghost.setTemporarySpeed(0.025f, durationMs);
        }
    }

    private void spawnPowerUpAtGhostPreviousPosition() {
        for (Ghost ghost : ghosts) {
            if (Math.random() < 0.25) {
                int previousX = ghost.getPreviousTileX();
                int previousY = ghost.getPreviousTileY();

                if (previousX >= 0 && previousY >= 0 &&
                        previousX < model.getColumnCount() && previousY < model.getRowCount()) {

                    GameCell cell = model.getCell(previousY, previousX);
                    if (cell.getPowerUp() == null && !cell.isWall() && !cell.hasPlayer() && !cell.hasGhost()) {
                        PowerUp.PowerUpType[] types = PowerUp.PowerUpType.values();
                        PowerUp.PowerUpType randomType = types[(int) (Math.random() * types.length)];
                        PowerUp powerUp = new PowerUp(randomType);
                        cell.setPowerUp(powerUp);
                    }
                }
            }
        }
    }
}