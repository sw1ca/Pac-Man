package model;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTableModel extends AbstractTableModel {
    private GameCell[][] board;
    private int size;

    public void generateMaze(int size) {
        this.size = size;
        board = new GameCell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new GameCell();
                board[i][j].setWall(true);
            }
        }

        generateMazeDFS(1, 1, new boolean[size][size]);

        // Add dot
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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
        visited[x][y] = true;
        board[x][y].setWall(false);

        List<int[]> directions = Arrays.asList(new int[]{0, 2}, new int[]{0, -2}, new int[]{2, 0}, new int[]{-2, 0});
        Collections.shuffle(directions);

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (isValid(newX, newY) && !visited[newX][newY]) {
                board[x + direction[0] / 2][y + direction[1] / 2].setWall(false);
                generateMazeDFS(newX, newY, visited);
            }
        }
    }

    private boolean isValid(int x, int y) {
        return x > 0 && y > 0 && x < size - 1 && y < size - 1;
    }

    @Override
    public int getRowCount() {
        return size;
    }

    @Override
    public int getColumnCount() {
        return size;
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