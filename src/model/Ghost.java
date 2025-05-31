package model;

import java.util.ArrayList;
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
    private int lastDirectionX = 0;
    private int lastDirectionY = 0;
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
        if(cornerSize > width) cornerSize = width;
        if(cornerSize > height) cornerSize = height;

        switch (corner) {
            case CENTER -> {
                startX = Math.max(0, width / 2 - cornerSize / 2);
                endX = Math.min(width, startX + cornerSize);
                startY = Math.max(0, height / 2 - cornerSize / 2);
                endY = Math.min(height, startY + cornerSize);
            }
            case TOP_RIGHT -> {
                startX = Math.max(0, width - cornerSize);
                endX = width;
                startY = 0;
                endY = Math.min(cornerSize, height);
            }
            case BOTTOM_LEFT -> {
                startX = 0;
                endX = Math.min(cornerSize, width);
                startY = Math.max(0, height - cornerSize);
                endY = height;
            }
            case BOTTOM_RIGHT -> {
                startX = Math.max(0, width - cornerSize);
                endX = width;
                startY = Math.max(0, height - cornerSize);
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
    private int distanceInDirection(int x, int y, int directionX, int directionY) { // Counts steps in a given direction until hitting a wall or ghost
        int steps = 0;
        while(true) {
            x += directionX;
            y += directionY;
            if(y < 0 || y >= map.getRowCount() || x < 0 || x >= map.getColumnCount()) break;
            if(map.getCell(y, x).isWall() || map.getCell(y, x).hasGhost()) break;
            steps++;
        }
        return steps;
    }
    public void chooseNewTargetCell() {
        List<int[]> directions = new ArrayList<>(List.of(new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1}));
        directions.removeIf(direction -> direction[0] == -lastDirectionX && direction[1] == -lastDirectionY); // Remove the opposite direction of the last move

        if(directions.isEmpty()) {
            directions = new ArrayList<>(List.of(new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1})); // If no directions left, allow all
        }

        int maxDistance = -1;
        int[] bestDirection = null;

        for (int[] direction : directions) {
            int distance = distanceInDirection(targetX, targetY, direction[0], direction[1]);
            if (distance > maxDistance) {
                maxDistance = distance;
                bestDirection = direction;
            }
        }
        if (bestDirection != null) {
            int newX = targetX + bestDirection[0];
            int newY = targetY + bestDirection[1];

            if (newY >= 0 && newY < map.getRowCount() &&
                    newX >= 0 && newX < map.getColumnCount() &&
                    !map.getCell(newY, newX).isWall() &&
                    !map.getCell(newY, newX).hasGhost()) {

                map.getCell(targetY, targetX).setGhost(null);
                targetX = newX;
                targetY = newY;
                map.getCell(targetY, targetX).setGhost(this);

                // Remember the last direction
                lastDirectionX = bestDirection[0];
                lastDirectionY = bestDirection[1];
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
