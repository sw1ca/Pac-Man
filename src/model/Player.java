package model;

import javax.swing.*;

public class Player extends JPanel implements Runnable {
    private int x, y;
    private int directionX, directionY;
    private final Map model;
    private boolean running = true;
    public Player(Map model, int startX, int startY) {
        this.model = model;
        this.x = startX;
        this.y = startY;
        this.directionX = 0;
        this.directionY = 0;
    }
    public void setDirection(int directionX, int directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    private void move() {
        int newX = x + directionX;
        int newY = y + directionY;

        if(newX >= 0 && newX < model.getColumnCount() && newY >= 0 && newY < model.getRowCount()) {
            if(!model.getCell(newY, newX).isWall()) {
                model.getCell(y, x).setPlayer(false);
                x = newX;
                y = newY;
                model.getCell(y, x).setPlayer(true);
                model.fireTableDataChanged();
            }
        }
    }
    @Override
    public void run() {
        while(running) {
            move();
            try {
                Thread.sleep(200); // Adjust speed as needed
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
    public void stop() {
        running = false;
    }
}
