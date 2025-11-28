package com.github.zipcodewilmington.casino.games.tictactoe;

import java.util.Scanner;

// Main class to run the game
public class TicTacToeMain {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

// Represents a single cell on the board
class Cell {
    private char value;
    
    public Cell() {
        this.value = ' ';
    }
    
    public char getValue() {
        return value;
    }
    
    public void setValue(char value) {
        this.value = value;
    }
    
    public boolean isEmpty() {
        return value == ' ';
    }
}

// Represents a player in the game
class Player {
    private String name;
    private char symbol;
    
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public char getSymbol() {
        return symbol;
    }
}

// Represents the game board
class Board {
    private Cell[][] cells;
    private static final int SIZE = 3;
    
    public Board() {
        cells = new Cell[SIZE][SIZE];
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell();
            }
        }
    }
    
    public void display() {
        System.out.println("\n-------------");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(cells[i][j].getValue() + " | ");
            }
            System.out.println("\n-------------");
        }
    }
    
    public boolean placeMove(int row, int col, char symbol) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        if (cells[row][col].isEmpty()) {
            cells[row][col].setValue(symbol);
            return true;
        }
        return false;
    }
    
    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean checkWin(char symbol) {
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (cells[i][0].getValue() == symbol && 
                cells[i][1].getValue() == symbol && 
                cells[i][2].getValue() == symbol) {
                return true;
            }
        }
        
        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (cells[0][j].getValue() == symbol && 
                cells[1][j].getValue() == symbol && 
                cells[2][j].getValue() == symbol) {
                return true;
            }
        }
        
        // Check diagonals
        if (cells[0][0].getValue() == symbol && 
            cells[1][1].getValue() == symbol && 
            cells[2][2].getValue() == symbol) {
            return true;
        }
        
        if (cells[0][2].getValue() == symbol && 
            cells[1][1].getValue() == symbol && 
            cells[2][0].getValue() == symbol) {
            return true;
        }
        
        return false;
    }
    
    public void reset() {
        initializeBoard();
    }
}

// Main game controller
class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Scanner scanner;
    
    public Game() {
        board = new Board();
        scanner = new Scanner(System.in);
        initializePlayers();
    }
    
    private void initializePlayers() {
        System.out.print("Enter Player 1 name: ");
        String name1 = scanner.nextLine();
        player1 = new Player(name1, 'X');
        
        System.out.print("Enter Player 2 name: ");
        String name2 = scanner.nextLine();
        player2 = new Player(name2, 'O');
        
        currentPlayer = player1;
    }
    
    public void start() {
        System.out.println("\n=== Welcome to Tic Tac Toe ===");
        System.out.println(player1.getName() + " is X");
        System.out.println(player2.getName() + " is O");
        
        boolean gameRunning = true;
        
        while (gameRunning) {
            playRound();
            gameRunning = askPlayAgain();
            if (gameRunning) {
                board.reset();
                currentPlayer = player1;
            }
        }
        
        System.out.println("Thanks for playing!");
        scanner.close();
    }
    
    private void playRound() {
        boolean roundActive = true;
        
        while (roundActive) {
            board.display();
            System.out.println("\n" + currentPlayer.getName() + "'s turn (" + 
                             currentPlayer.getSymbol() + ")");
            
            if (makeMove()) {
                if (board.checkWin(currentPlayer.getSymbol())) {
                    board.display();
                    System.out.println("\n🎉 " + currentPlayer.getName() + " wins!");
                    roundActive = false;
                } else if (board.isFull()) {
                    board.display();
                    System.out.println("\nIt's a draw!");
                    roundActive = false;
                } else {
                    switchPlayer();
                }
            }
        }
    }
    
    private boolean makeMove() {
        try {
            System.out.print("Enter row (0-2): ");
            int row = scanner.nextInt();
            System.out.print("Enter column (0-2): ");
            int col = scanner.nextInt();
            
            if (board.placeMove(row, col, currentPlayer.getSymbol())) {
                return true;
            } else {
                System.out.println("Invalid move! Try again.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter numbers 0-2.");
            scanner.nextLine(); // Clear buffer
            return false;
        }
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }
    
    private boolean askPlayAgain() {
        System.out.print("\nPlay again? (y/n): ");
        scanner.nextLine(); // Clear buffer
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("y");
    }
}              