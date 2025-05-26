package model;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTableModel extends AbstractTableModel {
    private GameCell[][] board;
    private int width;
    private int height;

    public void generateMaze(int width, int height) {
        this.width = width;
        this.height = height;
        board = new GameCell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new GameCell();
                board[i][j].setWall(true);
            }
        }

        generateMazeDFS(1, 1, new boolean[height][width]);

        // Add dot
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!board[i][j].isWall()) {
                    board[i][j].setDot(true);
                }
            }
        }

        // Player placement
        board[1][1].setPlayer(true);
        fireTableStructureChanged();
    }

    private void generateMazeDFS(int x, int y, boolean[][] visited) {
        if(x <= 0 || y <= 0 || x >= width - 1 || y >= height - 1) {
            return; // Out of bounds
        }
        visited[y][x] = true;
        board[y][x].setWall(false);

        List<int[]> directions = Arrays.asList(new int[]{0, 2}, new int[]{0, -2}, new int[]{2, 0}, new int[]{-2, 0});
        Collections.shuffle(directions);

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (isValid(newX, newY)) {
                int midX = x + direction[0] / 2;
                int midY = y + direction[1] / 2;

                if (midX >= 0 && midX < width && midY >= 0 && midY < height) {
                    board[midY][midX].setWall(false);
                }

                if (!visited[newY][newX]) {
                    generateMazeDFS(newX, newY, visited);
                }
            }
        }
    }

    private boolean isValid(int x, int y) {
        return x > 0 && y > 0 && x < width - 1 && y < height - 1;
    }

    @Override
    public int getRowCount() {
        return height;
    }

    @Override
    public int getColumnCount() {
        return width;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return board[row][col];
    }

    public GameCell[][] getBoard() {
        return board;
    }

    public GameCell getCell(int row, int col) {
        return board[row][col];
    }
}