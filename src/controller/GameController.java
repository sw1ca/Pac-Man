package controller;

import model.Map;
import model.Player;
import view.GameWindow;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private final Player player;
    private final GameWindow window;
    private final Map model;
    private int directionX = 0;
    private int directionY = 0;
    private boolean moving = false;

    public GameController(GameWindow window, Player player) {
        this.window = window;
        this.player = player;
        this.model = window.getTableModel();

        // Initialize player position in the model
        model.getCell(player.getY(), player.getX()).setPlayer(true);
        model.fireTableDataChanged();

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
        startAnimationThread();
    }

    private void setDirection(int dx, int dy) {
        this.directionX = dx;
        this.directionY = dy;
        if (dx == -1 && dy == 0) player.setDirection(Player.Direction.LEFT);
        else if (dx == 1 && dy == 0) player.setDirection(Player.Direction.RIGHT);
        else if (dx == 0 && dy == -1) player.setDirection(Player.Direction.UP);
        else if (dx == 0 && dy == 1) player.setDirection(Player.Direction.DOWN);

        if (!moving) {
            moving = true;
            startMovingThread();
        }
    }

    private void stopMoving() {
        moving = false;
    }

    private boolean isDirectionKey(int keyCode) {
        return keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT
                || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN;
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
                        model.getCell(newY, newX).setPlayer(true);
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

    private boolean canMoveTo(int row, int col) {
        return row >= 0 && row < model.getRowCount()
                && col >= 0 && col < model.getColumnCount()
                && !model.getCell(row, col).isWall();
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
}