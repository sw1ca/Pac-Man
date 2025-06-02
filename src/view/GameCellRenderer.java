package view;

import model.GameCell;
import model.Ghost;
import model.Player;
import model.PowerUp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class GameCellRenderer extends DefaultTableCellRenderer {
    private final Player player;
    private final Map<Ghost.ColorType, ImageIcon> ghostImage = new HashMap<>();
    private final Map<Player.Direction, ImageIcon> openIcons = new EnumMap<>(Player.Direction.class);
    private final Map<Player.Direction, ImageIcon> closedIcons = new EnumMap<>(Player.Direction.class);
    private final Map<PowerUp.PowerUpType, ImageIcon> powerUpIcons = new EnumMap<>(PowerUp.PowerUpType.class);


    public GameCellRenderer(Player player) {
        this.player = player;

        ghostImage.put(Ghost.ColorType.RED,   new ImageIcon(getClass().getResource("/assets/redGhost.png")));
        ghostImage.put(Ghost.ColorType.PINK,  new ImageIcon(getClass().getResource("/assets/pinkGhost.png")));
        ghostImage.put(Ghost.ColorType.ORANGE,new ImageIcon(getClass().getResource("/assets/orangeGhost.png")));
        ghostImage.put(Ghost.ColorType.BLUE,  new ImageIcon(getClass().getResource("/assets/blueGhost.png")));

        openIcons.put(Player.Direction.RIGHT, loadIcon("/assets/rightOpen1.png"));
        closedIcons.put(Player.Direction.RIGHT, loadIcon("/assets/rightOpen2.png"));

        openIcons.put(Player.Direction.LEFT, loadIcon("/assets/leftOpen1.png"));
        closedIcons.put(Player.Direction.LEFT, loadIcon("/assets/leftOpen2.png"));

        openIcons.put(Player.Direction.UP, loadIcon("/assets/upOpen1.png"));
        closedIcons.put(Player.Direction.UP, loadIcon("/assets/upOpen2.png"));

        openIcons.put(Player.Direction.DOWN, loadIcon("/assets/downOpen1.png"));
        closedIcons.put(Player.Direction.DOWN, loadIcon("/assets/downOpen2.png"));

        powerUpIcons.put(PowerUp.PowerUpType.SPEED_BOOST, loadIcon("/assets/greenApple.png"));
        powerUpIcons.put(PowerUp.PowerUpType.SCORE_BOOST, loadIcon("/assets/cherry.png"));
        powerUpIcons.put(PowerUp.PowerUpType.HP_BOOST, loadIcon("/assets/apple.png"));
        powerUpIcons.put(PowerUp.PowerUpType.GHOST_FREEZE, loadIcon("/assets/orange.png"));
        powerUpIcons.put(PowerUp.PowerUpType.GHOSTS_SLOW, loadIcon("/assets/strawberry.png"));
    }

    private ImageIcon loadIcon(String path) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setHorizontalAlignment(CENTER);
        cell.setOpaque(true);
        GameCell gameCell = (GameCell) value;

        // Clear previous settings
        cell.setBackground(Color.BLACK);
        cell.setForeground(Color.WHITE);
        cell.setIcon(null);
        cell.setText("");

        if (gameCell.hasGhost()) {
            Ghost ghost = gameCell.getGhost();
            ImageIcon icon = ghostImage.get(ghost.getColor());
            cell.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } else if (gameCell.hasPlayer()) {
            cell.setBackground(Color.BLACK);

            Player.Direction dir = player.getDirection();
            boolean mouthOpen = player.isMouthOpen();

            ImageIcon icon;
            if (mouthOpen) {
                icon = openIcons.getOrDefault(dir, openIcons.get(Player.Direction.RIGHT));
            } else {
                icon = closedIcons.getOrDefault(dir, closedIcons.get(Player.Direction.RIGHT));
            }
            cell.setIcon(icon);
        } else if (gameCell.isWall()) {
            cell.setBackground(Color.BLUE);
            cell.setText("");
        } else if (gameCell.getPowerUp() != null) {
            cell.setBackground(Color.BLACK);
            PowerUp powerUp = gameCell.getPowerUp();
            ImageIcon icon = powerUpIcons.get(powerUp.getType());
            if (icon != null) {
                cell.setIcon(icon);
            } else {
                cell.setText("★");
                cell.setForeground(Color.GREEN);
            }
        } else if (gameCell.hasDot()) {
            cell.setBackground(Color.BLACK);
            cell.setForeground(Color.WHITE);
            cell.setText("•");
        } else {}

        return cell;
    }
}
