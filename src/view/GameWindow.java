package view;

import model.GameTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class GameWindow extends JFrame {
    private JTable table;
    private GameTableModel tableModel;
    public GameWindow(int size) {
        setTitle("Chase & Chew - Board Size: " + size + "x" + size);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tableModel = new GameTableModel();
        tableModel.generateMaze(size);

        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new GameCellRenderer());
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setTableHeader(null);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        int cellSize = Math.max(5, 700 / size);
        table.setRowHeight(cellSize);
        for (int i = 0; i < size; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(cellSize);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        table.setTableHeader(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }
    public GameTableModel getTableModel() {
        return tableModel;
    }
}

