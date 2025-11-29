package com.github.zipcodewilmington.casino.games.tictactoe;


import java.util.Scanner;
import com.github.zipcodewilmington.casino.CasinoAccount;

class HumanPlayer extends TicTacToePlayer {
    private Scanner scanner;
    
    public HumanPlayer(CasinoAccount account) {
        super(account);
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public int[] makeMove(Board board) {
        while (true) {
            try {
                System.out.print("   Enter row (0-2): ");
                int row = scanner.nextInt();
                System.out.print("   Enter col (0-2): ");
                int col = scanner.nextInt();
                scanner.nextLine();
                
                if (row >= 0 && row < 3 && col >= 0 && col < 3) {
                    return new int[]{row, col};
                } else {
                    System.out.println("   ⚠ Invalid! Use numbers 0-2.");
                }
            } catch (Exception e) {
                System.out.println("   ⚠ Invalid input! Enter numbers only.");
                scanner.nextLine();
            }
        }
    }
}