package view;

import model.Map;
import model.Player;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class GameWindow extends JFrame {
    private JTable table;
    private Map tableModel;
    private final Player player;
    private JLabel scoreLabel;
    public GameWindow(int width, int height, Player player) {
        this.player = player;
        setTitle("Chase & Chew - Board Size: " + width + "x" + height);
        setLocationRelativeTo(null);

        tableModel = new Map();
        tableModel.generateMaze(width, height);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setOpaque(true);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(scoreLabel, BorderLayout.NORTH);

        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new GameCellRenderer(player));
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
        table.setFocusable(true);
        table.requestFocusInWindow();
        setVisible(true);
    }
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
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
    public Map getTableModel() {
        return tableModel;
    }
}

