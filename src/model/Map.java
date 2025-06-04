package model;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map extends AbstractTableModel {
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

        Random random = new Random();
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (board[i][j].isWall() && random.nextDouble() < 0.2) {
                    board[i][j].setWall(false);
                }
            }
        }

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
        fireTableDataChanged();
    }

    private void generateMazeDFS(int x, int y, boolean[][] visited) {
        visited[y][x] = true;
        board[y][x].setWall(false);

        List<int[]> directions = Arrays.asList(
                new int[]{0, 2}, new int[]{0, -2}, new int[]{2, 0}, new int[]{-2, 0}
        );
        Collections.shuffle(directions);

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isValid(newX, newY) && !visited[newY][newX]) {
                int wallX = x + dir[0] / 2;
                int wallY = y + dir[1] / 2;
                board[wallY][wallX].setWall(false);

                generateMazeDFS(newX, newY, visited);
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
    public GameCell getCell(int row, int col) {
        return board[row][col];
    }
}