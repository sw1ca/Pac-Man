package view;

import controller.GameController;
import model.Map;
import model.Player;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameWindow extends JFrame {
    private JTable table;
    private Map tableModel;
    private final Player player;
    private JLabel scoreLabel;
    private GameController gameController;
    private JPanel lifePanel;
    private List<JLabel> lifeLabels;
    private ImageIcon heartIcon;
    private final int MAX_LIVES = 3;

    public GameWindow(int width, int height, Player player) {
        this.player = player;
        setTitle("Chase & Chew - Board Size: " + width + "x" + height);
        setLocationRelativeTo(null);
        setResizable(true);
        setBackground(Color.BLACK);
        getContentPane().setBackground(Color.BLACK);

        tableModel = new Map();
        tableModel.generateMaze(width, height);

        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/heart.png")));
        Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        heartIcon = new ImageIcon(scaledImage);
        lifePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        lifePanel.setBackground(Color.BLACK);
        lifeLabels = new ArrayList<>();
        for (int i = 0; i < MAX_LIVES; i++) {
            JLabel lifeLabel = new JLabel(heartIcon);
            lifeLabels.add(lifeLabel);
            lifePanel.add(lifeLabel);
        }
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setOpaque(true);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.add(lifePanel, BorderLayout.WEST);
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new GameCellRenderer(player));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setTableHeader(null);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setBackground(Color.BLACK);

        int cellSize = Math.max(5, Math.min(700 / width, 500 / height)) + 5;
        table.setRowHeight(cellSize);

        for (int i = 0; i < width; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(cellSize);
        }
        JScrollPane scrollPane = getScrollPane(width, height, cellSize);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);
        table.setTableHeader(null);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        table.setFocusable(true);
        table.requestFocusInWindow();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                closeGameWindow();
            }
        });
    }
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
    public void closeGameWindow() {
        if(gameController != null) {
            gameController.stopMoving();
            gameController.addScore();
        }
        dispose();
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
    public void updateLives(int lives) {
        for (int i = 0; i < MAX_LIVES; i++) {
            if (i < lives) {
                lifeLabels.get(i).setVisible(true);
            } else {
                lifeLabels.get(i).setVisible(false);
            }
        }
        lifePanel.repaint();
    }

    public JTable getTable() {
        return table;
    }
    public Map getTableModel() {
        return tableModel;
    }
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}

