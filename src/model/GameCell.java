package model;

public class GameCell {
    private boolean isWall;
    private boolean hasDot;
    private boolean hasPlayer = false;
    private Ghost ghost;
    private boolean hasGhost;
    private PowerUp powerUp;

    public boolean isWall() {
        return isWall;
    }
    public void setWall(boolean wall) {
        isWall = wall;
    }
    public boolean hasDot() {
        return hasDot;
    }
    public void setDot(boolean dot) {
        hasDot = dot;
    }
    public boolean hasPlayer() {
        return hasPlayer;
    }
    public void setPlayer(boolean player) {
        hasPlayer = player;
    }
    public void setGhost(Ghost ghost) {
        this.ghost = ghost;
    }
    public Ghost getGhost() {
        return ghost;
    }
    public boolean hasGhost() {
        return ghost != null;
    }
    public PowerUp getPowerUp() {
        return powerUp;
    }
    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }
}
