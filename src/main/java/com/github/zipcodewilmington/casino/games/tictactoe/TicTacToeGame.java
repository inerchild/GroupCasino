package com.github.zipcodewilmington.casino.games.tictactoe;

import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// ========== MAIN GAME CLASS ==========
/**
 * TicTacToe game that implements GameInterface
 * Supports Human vs Computer gameplay with nested array board
 */
public class TicTacToeGame implements GameInterface {
    
    private List<PlayerInterface> players;
    private Board board;
    private Scanner scanner;
    
    public TicTacToeGame() {
        this.players = new ArrayList<>();
        this.board = new Board();
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void add(PlayerInterface player) {
        if (players.size() < 2) {
            players.add(player);
            System.out.println("✓ " + player.getArcadeAccount().getAccountName() + " joined the game!");
        } else {
            System.out.println("✗ Game is full! Maximum 2 players.");
        }
    }
    
    @Override
    public void remove(PlayerInterface player) {
        players.remove(player);
        System.out.println("✓ " + player.getArcadeAccount().getAccountName() + " left the game.");
    }
    
    @Override
    public void run() {
        if (players.size() != 2) {
            System.out.println("⚠ Need exactly 2 players to start! Current players: " + players.size());
            return;
        }
        
        printGameHeader();
        
        TicTacToePlayer player1 = (TicTacToePlayer) players.get(0);
        TicTacToePlayer player2 = (TicTacToePlayer) players.get(1);
        
        player1.setSymbol('X');
        player2.setSymbol('O');
        
        System.out.println("🎮 " + player1.getArcadeAccount().getAccountName() + " plays as [X]");
        System.out.println("🎮 " + player2.getArcadeAccount().getAccountName() + " plays as [O]");
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🎲 LET'S PLAY!");
        System.out.println("=".repeat(50));
        
        playRound(player1, player2);
        
        System.out.println("\n👋 Thanks for playing Tic Tac Toe!");
    }
    
    private void printGameHeader() {
        System.out.println("\n" + "╔" + "═".repeat(48) + "╗");
        System.out.println("║" + " ".repeat(12) + "TIC TAC TOE SHOWDOWN" + " ".repeat(16) + "║");
        System.out.println("╚" + "═".repeat(48) + "╝");
    }
    
    private void playRound(TicTacToePlayer player1, TicTacToePlayer player2) {
        TicTacToePlayer currentPlayer = player1;
        boolean roundActive = true;
        
        while (roundActive) {
            board.display();
            System.out.println("\n▶ " + currentPlayer.getArcadeAccount().getAccountName() + 
                             "'s turn [" + currentPlayer.getSymbol() + "]");
            
            int[] move = currentPlayer.makeMove(board);
            int row = move[0];
            int col = move[1];
            
            if (board.placeMove(row, col, currentPlayer.getSymbol())) {
                if (board.checkWin(currentPlayer.getSymbol())) {
                    board.display();
                    displayWinner(currentPlayer);
                    roundActive = false;
                } else if (board.isFull()) {
                    board.display();
                    displayDraw();
                    roundActive = false;
                } else {
                    currentPlayer = (currentPlayer == player1) ? player2 : player1;
                }
            } else {
                System.out.println("❌ Invalid move! That cell is occupied. Try again.");
            }
        }
    }
    
    private void displayWinner(TicTacToePlayer winner) {
        System.out.println("\n" + "★".repeat(50));
        System.out.println("🎉🎊 " + winner.getArcadeAccount().getAccountName().toUpperCase() + " WINS! 🎊🎉");
        System.out.println("★".repeat(50));
        System.out.println("   🏆 VICTORY WITH [" + winner.getSymbol() + "] 🏆");
        System.out.println("★".repeat(50) + "\n");
    }
    
    private void displayDraw() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("🤝 IT'S A DRAW! 🤝");
        System.out.println("═".repeat(50));
        System.out.println("   No winner this time - well played both!");
        System.out.println("═".repeat(50) + "\n");
    }
}

