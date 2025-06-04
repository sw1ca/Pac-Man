package model;

import javax.swing.*;
import java.awt.*;

public class PowerUp {
    public enum PowerUpType {
        SPEED_BOOST, SCORE_BOOST, HP_BOOST, GHOST_FREEZE, GHOSTS_SLOW
    }
    private final PowerUpType type;
    private final Image icon;

    public PowerUp(PowerUpType type) {
        this.type = type;
        this.icon = loadIconForType(type);
    }
    private Image loadIconForType(PowerUpType type) {
        String path = switch (type) {
            case SPEED_BOOST -> "/assets/greenApple.png";
            case SCORE_BOOST -> "/assets/cherry.png";
            case HP_BOOST -> "/assets/apple.png";
            case GHOST_FREEZE -> "/assets/orange.png";
            case GHOSTS_SLOW -> "/assets/strawberry.png";
        };
        return new ImageIcon(getClass().getResource(path)).getImage();
    }
    public PowerUpType getType() {
        return type;
    }
}
