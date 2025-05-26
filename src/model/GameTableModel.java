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
                if(y == 0 || y == size - 1 || x == 0 || x == size - 1) {
                    board[y][x] = '#'; // Wall
                } else if (Math.random() < 0.15){
                    board[y][x] = '#'; // Random wall
                } else {
                    board[y][x] = '.'; // Empty space
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
