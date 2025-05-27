package view;

import model.GameCell;
import model.GameTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class GameWindow extends JFrame {
    private JTable table;
    private GameTableModel tableModel;
    public GameWindow(int width, int height) {
        setTitle("Chase & Chew - Board Size: " + width + "x" + height);
        setLocationRelativeTo(null);

        tableModel = new GameTableModel();
        tableModel.generateMaze(width, height);

        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new GameCellRenderer());
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setTableHeader(null);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        int cellSize = Math.max(5, Math.min(700 / width, 500 / height)) + 5;
        table.setRowHeight(cellSize);

        for (int i = 0; i < width; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(cellSize);
        }
        JScrollPane scrollPane = getScrollPane(width, height, cellSize);
        table.setTableHeader(null);
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private JScrollPane getScrollPane(int width, int height, int cellSize) {
        JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        int tableWidth = cellSize * width;
        int tableHeight = cellSize * height;
        scrollPane.setPreferredSize(new Dimension(tableWidth, tableHeight));
        return scrollPane;
    }

    public JTable getTable() {
        return table;
    }
    public GameTableModel getTableModel() {
        return tableModel;
    }
}

