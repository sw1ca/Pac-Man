package view;

import model.GameTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class GameWindow extends JFrame {
    private int boardSize;
    private JTable table;
    private GameTableModel tableModel;
    public GameWindow(int size) {
        this.boardSize = size;
        setTitle("Chase & Chew - Board Size: " + size + "x" + size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tableModel = new GameTableModel(size);
        table = new JTable(tableModel);

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
}

