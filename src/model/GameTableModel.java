package model;

import javax.swing.table.AbstractTableModel;

public class GameTableModel extends AbstractTableModel {
    private final int size;
    private final char[][] board;

    public GameTableModel(int size) {
        this.size = size;
        board = new char[size][size];
        generateBoard();
    }
    private void generateBoard() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                board[y][x] = '.'; // Empty cell
            }
        }
        for (int i = 0; i < size; i++) {
            board[0][i] = '#'; // Top wall
            board[size - 1][i] = '#'; // Bottom wall
            board[i][0] = '#'; // Left wall
            board[i][size - 1] = '#'; // Right wall
        }
        for (int y = 2; y < size - 1; y++) {
            for (int x = 0; x < size - 1; x++) {
                if(Math.random() < 0.15) {
                    board[y][x] = '#'; // Random walls
                }
            }
        }
        board[1][1] = 'P'; // Player start position
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        return board[rowIndex][columnIndex];
    }
}
