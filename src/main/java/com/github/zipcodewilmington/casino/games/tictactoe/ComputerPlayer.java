package com.github.zipcodewilmington.casino.games.tictactoe;

import java.util.Random;
import com.github.zipcodewilmington.casino.CasinoAccount;

class ComputerPlayer extends TicTacToePlayer {
    private Random random;
    
    public ComputerPlayer(CasinoAccount account) {
        super(account);
        this.random = new Random();
    }
    
    @Override
    public int[] makeMove(Board board) {
        System.out.print("   🤖 Computer analyzing");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(400);
                System.out.print(".");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
        
        // Try to win
        int[] winMove = findWinningMove(board, symbol);
        if (winMove != null) {
            System.out.println("   💡 Computer plays: Row " + winMove[0] + ", Col " + winMove[1]);
            return winMove;
        }
        
        // Block opponent
        char opponentSymbol = (symbol == 'X') ? 'O' : 'X';
        int[] blockMove = findWinningMove(board, opponentSymbol);
        if (blockMove != null) {
            System.out.println("   🛡️ Computer blocks: Row " + blockMove[0] + ", Col " + blockMove[1]);
            return blockMove;
        }
        
        // Take center
        if (board.isCellAvailable(1, 1)) {
            System.out.println("   🎯 Computer takes center: Row 1, Col 1");
            return new int[]{1, 1};
        }
        
        // Take corner
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] corner : corners) {
            if (board.isCellAvailable(corner[0], corner[1])) {
                System.out.println("   📐 Computer takes corner: Row " + corner[0] + ", Col " + corner[1]);
                return corner;
            }
        }
        
        // Take any cell
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.isCellAvailable(i, j)) {
                    System.out.println("   ✓ Computer plays: Row " + i + ", Col " + j);
                    return new int[]{i, j};
                }
            }
        }
        
        return new int[]{0, 0};
    }
    
    private int[] findWinningMove(Board board, char checkSymbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.isCellAvailable(i, j)) {
                    board.placeMove(i, j, checkSymbol);
                    if (board.checkWin(checkSymbol)) {
                        board.undoMove(i, j);
                        return new int[]{i, j};
                    }
                    board.undoMove(i, j);
                }
            }
        }
        return null;
    }
}