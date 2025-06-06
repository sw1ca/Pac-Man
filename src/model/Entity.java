package model;

public abstract class Entity {
    protected float positionX, positionY;
    protected int tileX, tileY;
    protected int previousX, previousY;
    protected float moveSpeed;
    protected static final float DEFAULT_SPEED = 0.05f;

    public void updatePosition() {}
    public int getTileX() {
        return Math.round(positionX);
    }

    public int getTileY() {
        return Math.round(positionY);
    }

    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
        this.tileX = Math.round(x);
        this.tileY = Math.round(y);
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}
