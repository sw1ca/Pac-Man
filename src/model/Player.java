package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Player extends JPanel implements Runnable {
    private int x, y;
    private int directionX, directionY;
    private int speed = 5;
    private boolean running = true;
    private Image playerImage;
    private String currentDirection = "right";
    private boolean mouthOpen = true;
    private Image rightOpen1, rightOpen2;
    private Image leftOpen1, leftOpen2;
    private Image downOpen1, downOpen2;
    private Image upOpen1, upOpen2;
    public Player() {
        loadImages();
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.BLACK);

        x = 1;
        y = 1;
        directionX = 0;
        directionY = 0;

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        directionX = -speed;
                        directionY = 0;
                    }
                    case KeyEvent.VK_RIGHT -> {
                        directionX = speed;
                        directionY = 0;
                    }
                    case KeyEvent.VK_UP -> {
                        directionX = 0;
                        directionY = -speed;
                    }
                    case KeyEvent.VK_DOWN -> {
                        directionX = 0;
                        directionY = speed;
                    }
                }
            }
        });
    }
    private void loadImages() {
        try {
            rightOpen1 = new ImageIcon("/assets/rightOpen1.png").getImage();
            rightOpen2 = new ImageIcon("/assets/rightOpen2.png").getImage();
            leftOpen1 = new ImageIcon("/assets/leftOpen1.png").getImage();
            leftOpen2 = new ImageIcon("/assets/leftOpen2.png").getImage();
            downOpen1 = new ImageIcon("/assets/downOpen1.png").getImage();
            downOpen2 = new ImageIcon("/assets/downOpen2.png").getImage();
            upOpen1 = new ImageIcon("/assets/upOpen1.png").getImage();
            upOpen2 = new ImageIcon("/assets/upOpen2.png").getImage();
            playerImage = rightOpen1; // Default image
        } catch (Exception e) {
            System.err.println("Error loading player images: " + e.getMessage());
        }
    }
    private void move() {
        x += directionX * speed;
        y += directionY * speed;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > getWidth() - 30) x = getWidth() - 30;
        if (y > getHeight() - 30) y = getHeight() - 30;
    }
    private void updatePlayerImage() {
        mouthOpen = !mouthOpen; // Toggle mouth state
        switch(currentDirection) {
            case "right" -> playerImage = mouthOpen ? rightOpen1 : rightOpen2;
            case "left" -> playerImage = mouthOpen ? leftOpen1 : leftOpen2;
            case "down" -> playerImage = mouthOpen ? downOpen1 : downOpen2;
            case "up" -> playerImage = mouthOpen ? upOpen1 : upOpen2;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(playerImage != null) {
            g.drawImage(playerImage, x, y, 30, 30, this);
        }
    }
    @Override
    public void run() {
        running = true;
        while(running) {
            move();
            repaint();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
