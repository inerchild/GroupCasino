package com.github.zipcodewilmington.casino.games.tictactoe;

public  class Board {
    // NESTED ARRAY: 3x3 grid for tic tac toe cells
    private char[][] cells;
    private static final int SIZE = 3;
    
    public Board() {
        cells = new char[SIZE][SIZE];
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = ' ';
            }
        }
    }
    
    public void display() {
        System.out.println("\n    ┌───┬───┬───┐");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("  " + i + " │");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(" " + cells[i][j] + " │");
            }
            System.out.println();
            if (i < SIZE - 1) {
                System.out.println("    ├───┼───┼───┤");
            }
        }
        System.out.println("    └───┴───┴───┘");
        System.out.println("      0   1   2");
    }
    
    public boolean placeMove(int row, int col, char symbol) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        if (cells[row][col] == ' ') {
            cells[row][col] = symbol;
            return true;
        }
        return false;
    }
    
    public void undoMove(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            cells[row][col] = ' ';
        }
    }
    
    public boolean isCellAvailable(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        return true;
    }
    
    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean checkWin(char symbol) {
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (cells[i][0] == symbol && 
                cells[i][1] == symbol && 
                cells[i][2] == symbol) {
                return true;
            }
        }
        
        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (cells[0][j] == symbol && 
                cells[1][j] == symbol && 
                cells[2][j] == symbol) {
                return true;
            }
        }
        
        // Check diagonal (top-left to bottom-right)
        if (cells[0][0] == symbol && 
            cells[1][1] == symbol && 
            cells[2][2] == symbol) {
            return true;
        }
        
        // Check diagonal (top-right to bottom-left)
        if (cells[0][2] == symbol && 
            cells[1][1] == symbol && 
            cells[2][0] == symbol) {
            return true;
        }
        
        return false;
    }
    
    public void reset() {
        initializeBoard();
    }
}