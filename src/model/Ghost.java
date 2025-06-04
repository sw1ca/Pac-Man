package model;

import java.util.ArrayList;
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
    private int previousX, previousY;
    private int targetX = 0;
    private int targetY = 0;
    private int lastDirectionX = 0;
    private int lastDirectionY = 0;
    private int tileX, tileY;
    private int previousTileX, previousTileY;
    private static final float DEFAULT_SPEED = 0.05f;
    private float moveSpeed = DEFAULT_SPEED;
    private boolean frozen = false;
    private final Map map;
    private Random random = new Random();
    private final ColorType color;

    public Ghost(Map map, ColorType color, SpawnCorner corner) {
        this.map = map;
        this.color = color;
        spawnGhostInCorner(corner);
        this.positionX = targetX;
        this.positionY = targetY;
        this.previousX = targetX;
        this.previousY = targetY;
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
    public void chooseNewTargetCell() {
        List<int[]> directions = new ArrayList<>(List.of(new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1}));

        List<int[]> validDirections = new ArrayList<>();
        for (int[] direction : directions) {
            int newX = targetX + direction[0];
            int newY = targetY + direction[1];

            if (newY >= 0 && newY < map.getRowCount() &&
                    newX >= 0 && newX < map.getColumnCount() &&
                    !map.getCell(newY, newX).isWall()) {

                // Prefer directions that aren't backwards
                if (direction[0] != -lastDirectionX || direction[1] != -lastDirectionY || random.nextFloat() > 0.8) {
                    validDirections.add(direction);
                }
            }
        }

        // If no valid directions except backwards, allow backwards movement
        if (validDirections.isEmpty()) {
            for (int[] direction : directions) {
                int newX = targetX + direction[0];
                int newY = targetY + direction[1];

                if (newY >= 0 && newY < map.getRowCount() &&
                        newX >= 0 && newX < map.getColumnCount() &&
                        !map.getCell(newY, newX).isWall()) {
                    validDirections.add(direction);
                }
            }
        }

        if (!validDirections.isEmpty()) {
            int[] chosenDirection = validDirections.get(random.nextInt(validDirections.size()));

            map.getCell(targetY, targetX).setGhost(null);
            targetX += chosenDirection[0];
            targetY += chosenDirection[1];
            map.getCell(targetY, targetX).setGhost(this);

            lastDirectionX = chosenDirection[0];
            lastDirectionY = chosenDirection[1];
        }
    }
    public void setTemporarySpeed(float speed, int durationMs) {
        this.moveSpeed = speed;
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.moveSpeed = DEFAULT_SPEED;
        }).start();
    }
    public void updatePosition() {
        if(frozen) return;
        float directionX = targetX - positionX;
        float directionY = targetY - positionY;

        if (Math.abs(directionX) < moveSpeed && Math.abs(directionY) < moveSpeed) {
            positionX = targetX;
            positionY = targetY;

            previousTileX = tileX;
            previousTileY = tileY;

            tileX = Math.round(positionX);
            tileY = Math.round(positionY);

            chooseNewTargetCell();
        } else {
            float length = (float) Math.sqrt(directionX*directionX + directionY*directionY);
            positionX += moveSpeed * directionX / length;
            positionY += moveSpeed * directionY / length;
        }
    }
    public float getX() {
        return Math.round(positionX);
    }
    public float getY() {
        return Math.round(positionY);
    }
    public int getPreviousTileX() {
        return previousTileX;
    }
    public int getPreviousTileY() {
        return previousTileY;
    }
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
    public ColorType getColor() {
        return color;
    }
}
