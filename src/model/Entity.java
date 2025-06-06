package model;

public abstract class Entity implements Movable, Positionable {
    protected float positionX, positionY;
    protected int tileX, tileY;
    protected int previousX, previousY;
    protected float moveSpeed;
    protected static final float DEFAULT_SPEED = 0.05f;

    public abstract void updatePosition();
    @Override
    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
        this.tileX = Math.round(x);
        this.tileY = Math.round(y);
    }
    @Override
    public void setPosition(int x, int y) {
        setPosition((float) x, (float) y);
    }
    public float getPositionX() {
        return positionX;
    }
    public float getPositionY() {
        return positionY;
    }
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}
