package model;
public class Player {
    private float positionX, positionY;
    private int tileX, tileY;
    private boolean mouthOpen = false;
    private int score = 0;
    private int lives = 3;
    private static final int MAX_LIVES = 3;

    public Player(int startX, int startY) {
        setPosition(startX, startY);
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
        return Math.round(getPositionX());
    }
    public int getY() {
        return Math.round(getPositionY());
    }
    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
        this.tileX = Math.round(x);
        this.tileY = Math.round(y);
    }
    public void setPosition(int x, int y) {
        setPosition((float) x, (float) y);
    }
    public float getPositionX() {
        return positionX;
    }
    public float getPositionY() {
        return positionY;
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
    public int getLives() {
        return lives;
    }
    public void loseLife() {
        lives--;
    }
    public void addLife() {
        if(lives < MAX_LIVES) {
            lives++;
        }
    }
}