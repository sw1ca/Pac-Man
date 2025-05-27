package view;

import model.GameCell;
import model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class GameCellRenderer extends DefaultTableCellRenderer {
    private final Player player;
    private final Map<Player.Direction, ImageIcon> openIcons = new EnumMap<>(Player.Direction.class);
    private final Map<Player.Direction, ImageIcon> closedIcons = new EnumMap<>(Player.Direction.class);


    public GameCellRenderer(Player player) {
        this.player = player;

        openIcons.put(Player.Direction.RIGHT, loadIcon("/assets/rightOpen1.png"));
        closedIcons.put(Player.Direction.RIGHT, loadIcon("/assets/rightOpen2.png"));

        openIcons.put(Player.Direction.LEFT, loadIcon("/assets/leftOpen1.png"));
        closedIcons.put(Player.Direction.LEFT, loadIcon("/assets/leftOpen2.png"));

        openIcons.put(Player.Direction.UP, loadIcon("/assets/upOpen1.png"));
        closedIcons.put(Player.Direction.UP, loadIcon("/assets/upOpen2.png"));

        openIcons.put(Player.Direction.DOWN, loadIcon("/assets/downOpen1.png"));
        closedIcons.put(Player.Direction.DOWN, loadIcon("/assets/downOpen2.png"));
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

        cell.setIcon(null);
        cell.setText("");

        if (gameCell.isWall()) {
            cell.setBackground(Color.BLUE.darker());
            cell.setText("");
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
        } else if (gameCell.hasGhost()) {
            cell.setBackground(Color.BLACK);
            cell.setForeground(Color.RED);
            cell.setText("G");
        } else if (gameCell.getPowerUp() != null) {
            cell.setBackground(Color.BLACK);
            cell.setForeground(Color.GREEN);
            cell.setText("★");
        } else if (gameCell.hasDot()) {
            cell.setBackground(Color.BLACK);
            cell.setForeground(Color.WHITE);
            cell.setText("•");
        } else {
            cell.setBackground(Color.BLACK);
            cell.setText("");
        }

        return cell;
    }
}
