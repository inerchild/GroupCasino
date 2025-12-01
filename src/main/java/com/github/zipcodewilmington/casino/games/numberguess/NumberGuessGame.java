package com.github.zipcodewilmington.casino.games.numberguess;

import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.casino.CasinoAccount;


import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by leon on 7/21/2020.
 */
public class NumberGuessGame implements GameInterface {

    
    private List<PlayerInterface> players;
    private int secretNumber;
    private int maxRange;
    private int maxAttempts;
    private int currentAttempts;


    public NumberGuessGame() {
        this.players = new ArrayList<>();
        this.secretNumber = (int)(Math.random() * 100) + 1;
        this.currentAttempts = 0;
    }

    public NumberGuessGame(String difficulty) {
        this.players = new ArrayList<>();

        if (difficulty.equals("EASY")) {
            this.maxRange = 50;
            this.maxAttempts = 10;
        } else if (difficulty.equals("MEDIUM")) { 
            this.maxRange = 100;
            this.maxAttempts = 7;
        } else if (difficulty.equals("HARD")) {
            this.maxRange = 1000;
            this.maxAttempts = 10;
            this.currentAttempts = 0;
            
        }
            
        
        this.secretNumber = (int)(Math.random() * maxRange) + 1;
    }

    public NumberGuessGame(int secretNumber) {
        this.players = new ArrayList<>();
        this.secretNumber = secretNumber;
        this.currentAttempts = 0;
    }

    //Add these GETTER methods:
    public int getMaxRange() {
        return maxRange;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public boolean isCorrectGuess (int guess) {
        return guess ==secretNumber;
    }

    public String getHint (int guess) {
        if (guess <secretNumber) {
            return "HIGHER";
        } else if (guess > secretNumber) {
            return "LOWER";
        } else {
            return "CORRECT";
        }
    }

    public int getCurrentAttempts (){
        return currentAttempts;
    }

    public void makeGuess(int guess){
        currentAttempts++;
    }

    public class TestGame {
        public static void main(String[] args) {
        // create a test account and player
        CasinoAccount account = new CasinoAccount("TestPlayer", "password"); 
        NumberGuessPlayer player = new NumberGuessPlayer(account);

        // create and run the game
        NumberGuessGame game = new NumberGuessGame();
        game.add(player);
        game.run();

        }
    }

    private void printVictoryCelebration(String playerName, int attempts) {
        try {
            for (int i = 0; i < 6; i++) {
                System.out.print("\033[H\033[2J"); // Clear screen
                System.out.flush();
                System.out.println("🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊");
                System.out.println("  *** CONGRATULATIONS!  ***");
                System.out.println("🎈 " + playerName + " WINS! 🎈");
                System.out.println("🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊");
                System.out.println("\n");
                Thread.sleep(250);
                System.out.print("\033[H\033[2J"); // Clear again for flash
                System.out.flush();
                Thread.sleep(250);
            }
            // Final message stays on screen
            System.out.println("🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊");
            System.out.println("  *** CONGRATULATIONS!  ***");
            System.out.println("🎈 " + playerName + " WINS! 🎈");
            System.out.println("🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊🎉🎊");
            System.out.println("\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    @Override
    public void add(PlayerInterface player) {
        players.add (player);

    }

    



    @Override 
    public void remove(PlayerInterface player) {
        players.remove(player);
    
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        //ask for difficulty
        System.out.println ("Welcome to Number Guess Game");
        System.out.println("Choose difficulty: EASY, MEDIUM, HARD");
        System.out.println("Enter difficulty: ");
        String difficulty = scanner.nextLine().toUpperCase();

        //Set range and attempts based on difficulty
        if (difficulty.equals("EASY")) {
            this.maxRange = 50;
            this.maxAttempts = 10;
        } else if (difficulty.equals("MEDIUM")) {
            this.maxRange = 100;
            this.maxAttempts = 7; 
        } else if (difficulty.equals("HARD")) {
            this.maxRange = 1000;
            this.maxAttempts = 10;
        }

        this.secretNumber = (int)(Math.random() * maxRange) + 1;
        

        System.out.println("I'm thinking of a number between 1 and " + maxRange + "...");
        System.out.println("You have " + maxAttempts + " attempts. ");


        for  (PlayerInterface player : players) {
             
             System.out.println("\n" + player.getArcadeAccount().getAccountName() + "' s' turn!");

             while (currentAttempts < maxAttempts) {
                System.out.print("Attempt " + (currentAttempts + 1) + "/" + maxAttempts + " - Enter your guess: ");
                Integer guess = player.play();
                makeGuess(guess); //increment attempts

                if (isCorrectGuess(guess)) {
                    printVictoryCelebration(player.getArcadeAccount().getAccountName(), currentAttempts);
                    return;
                } else {
                    System.out.println(getHint(guess));
                }
             }
        }

        System.out.println("Game over! You ran out of attempts. The number was " + secretNumber); 

    }

}