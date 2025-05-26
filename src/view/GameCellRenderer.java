package view;

import model.GameCell;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class GameCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setHorizontalAlignment(CENTER);
        cell.setOpaque(true);
        GameCell gameCell = (GameCell) value;

        if (gameCell.isWall()) {
            cell.setBackground(Color.BLUE.darker());
            cell.setText("");
        } else if (gameCell.hasPlayer()) {
            cell.setBackground(Color.BLACK);
            cell.setForeground(Color.YELLOW);
            cell.setText("P");
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
