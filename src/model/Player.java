package model;

public class Player {
    private int x, y;
    private boolean mouthOpen = false;
    private int score = 0;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
    private Direction direction = Direction.NONE;
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean isMouthOpen() {
        return mouthOpen;
    }
    public void toggleMouth() {
        mouthOpen = !mouthOpen;
    }
    public void addScore(int points) {
        score += points;
    }
    public int getScore() {
        return score;
    }
}