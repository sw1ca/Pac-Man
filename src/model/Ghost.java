package model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Ghost {
    public enum ColorType {
        RED, BLUE, PINK, ORANGE
    }
    public enum SpawnCorner {
        CENTER, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
    private float positionX;
    private float positionY;
    private int targetX = 0;
    private int targetY = 0;
    private static final float MOVE_SPEED = 0.05f;
    private final Map map;
    private final Random random = new Random();
    private final ColorType color;

    public Ghost(Map map, ColorType color, SpawnCorner corner) {
        this.map = map;
        this.color = color;
        spawnGhostInCorner(corner);
        this.positionX = targetX;
        this.positionY = targetY;
    }
    private void spawnGhostInCorner(SpawnCorner corner) {
        int width = map.getColumnCount();
        int height = map.getRowCount();

        int startX, endX, startY, endY;
        int cornerSize = 3; // Size of the corner area

        switch (corner) {
            case CENTER -> {
                startX = width / 2 - cornerSize / 2;
                endX = startX + cornerSize;
                startY = height / 2 - cornerSize / 2;
                endY = startY + cornerSize;
            }
            case TOP_RIGHT -> {
                startX = width - cornerSize;
                endX = width;
                startY = 0;
                endY = cornerSize;
            }
            case BOTTOM_LEFT -> {
                startX = 0;
                endX = cornerSize;
                startY = height - cornerSize;
                endY = height;
            }
            case BOTTOM_RIGHT -> {
                startX = width - cornerSize;
                endX = width;
                startY = height - cornerSize;
                endY = height;
            }
            default -> throw new IllegalArgumentException("Unknown corner: " + corner);
        }
        do {
            targetX = startX + random.nextInt(endX - startX);
            targetY = startY + random.nextInt(endY - startY);
        } while (map.getCell(targetY, targetX).isWall() || map.getCell(targetY, targetX).hasGhost());
        map.getCell(targetY, targetX).setGhost(this);
    }
    public void chooseNewTargetCell() {
        List<int[]> directions = new java.util.ArrayList<>(List.of(new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1}));
        Collections.shuffle(directions, random);

        for (int[] direction : directions) {
            int newX = targetX + direction[0];
            int newY = targetY + direction[1];

            if (newY >= 0 && newY < map.getRowCount() &&
                    newX >= 0 && newX < map.getColumnCount() &&
                    !map.getCell(newY, newX).isWall() &&
                    !map.getCell(newY, newX).hasGhost()) {

                map.getCell(targetY, targetX).setGhost(null);
                targetX = newX;
                targetY = newY;

                map.getCell(targetY, targetX).setGhost(this);
                return;
            }
        }
    }
    public void updatePosition() {
        float directionX = targetX - positionX;
        float directionY = targetY - positionY;

        if (Math.abs(directionX) < MOVE_SPEED && Math.abs(directionY) < MOVE_SPEED) {
            positionX = targetX;
            positionY = targetY;
            chooseNewTargetCell();
        } else {
            float length = (float) Math.sqrt(directionX*directionX + directionY*directionY);
            positionX += MOVE_SPEED * directionX / length;
            positionY += MOVE_SPEED * directionY / length;
        }
    }
    public int getX() {
        return Math.round(positionX);
    }

    public int getY() {
        return Math.round(positionY);
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public ColorType getColor() {
        return color;
    }
}
