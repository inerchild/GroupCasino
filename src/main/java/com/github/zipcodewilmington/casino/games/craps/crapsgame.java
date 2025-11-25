package com.github.zipcodewilmington.casino.games.craps;


import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class CrapsGame implements GameInterface{

    private final List<CrapsPlayer> players = new ArrayList<>();
    private final Scanner scanner;
    private final Random random = new Random();

    public CrapsGame() {
        this(new Scanner(System.in));
    }

    public CrapsGame(Scanner scanner) {
        this.scanner = scanner;
    }



    public void add(PlayerInterface player) {
        if (!(player instanceof CrapsPlayer)) {
            throw new IllegalArgumentException("CrapsGame only supports CrapsPlayer.");
        }
        players.add((CrapsPlayer) player);
    }

    public void remove(PlayerInterface player) {
        players.remove((CrapsPlayer) player);
    }

    public void run() {
        if (players.isEmpty()) {
            System.out.println("No players at the craps table.");
            return;
        }

    System.out.println("====================================");
    System.out.println("      Welcome to the Craps Table    ");
    System.out.println("====================================");

    boolean gameStillRunning = true;

       while (gameStillRunning && !players.isEmpty()) {

            for (Iterator<CrapsPlayer> it = players.iterator(); it.hasNext();) {
                CrapsPlayer player = it.next();
                CasinoAccount account = player.getArcadeAccount();

                if (account.getAccountBalance() <= 0) {
                    System.out.println(player.getName() + ", you are out of money. Leaving the table.");
                    it.remove();
                    continue;
                }

                System.out.println("\n------------------------------------");
                System.out.println("Player: " + player.getName());
                System.out.printf("Current balance: $%.2f%n", account.getAccountBalance());

                Double bet = promptForBet(account);
                if (bet == null) {
                    System.out.println(player.getName() + " leaves the craps table.");
                    it.remove();
                    continue;
                }

                playCrapsRound(player, bet);

                if (account.getAccountBalance() <= 0) {
                    System.out.println("You are out of money, " + player.getName() + ". Goodbye.");
                    it.remove();
                    continue;
                }

                if (!promptPlayAnotherRound(player)) {
                    System.out.println(player.getName() + " leaves the craps table.");
                    it.remove();
                }
            }

            if (players.isEmpty()) {
                System.out.println("\nAll players have left the craps table.");
                gameStillRunning = false;
            } else {
            }
        }

        System.out.println("Craps game over.");
    }


    private Double promptForBet(CasinoAccount account) {
        while (true) {
            System.out.printf("Enter your bet amount (or 'q' to leave table): ");

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q") || input.equals("quit")) {
                return null;
            }

        
        try {
                double bet = Double.parseDouble(input);
                if (bet <= 0) {
                    System.out.println("Bet must be greater than 0.");
                } else if (bet > account.getAccountBalance()) {
                    System.out.println("You cannot bet more than your current balance.");
                } else {
                    return bet;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a numeric bet.");
            }
        }   
    }

    private void playCrapsRound(CrapsPlayer player, double bet) {
        CasinoAccount account = player.getArcadeAccount();

        account.debitAccount(bet);
        System.out.printf("%s bets $%.2f%n", player.getName(), bet);

        int comeOutRoll = rollDice();
        System.out.println("Come-out roll: " + comeOutRoll);

        if (comeOutRoll == 7 || comeOutRoll == 11) {
            System.out.println("You rolled a natural! You WIN!");
            account.creditAccount(bet * 2);
            printBalance(account);
            return;
        }
    

        if (comeOutRoll == 2 || comeOutRoll == 3 || comeOutRoll == 12) {
            System.out.println("Craps! You LOSE.");
            printBalance(account);
            return;
        }

         int point = comeOutRoll;
        System.out.println("Point is set to: " + point);
        System.out.println("Keep rolling: hit " + point + " to win, or 7 to lose.");

        while (true) {
            int roll = rollDice();
            System.out.println("You rolled: " + roll);

            if (roll == point) {
                System.out.println("You made your point! You WIN!");
                account.creditAccount(bet * 2);
                printBalance(account);
                return;
            } else if (roll == 7) {
                System.out.println("Seven out! You LOSE.");
                printBalance(account);
                return;
            } else {
                System.out.println("No decision, roll again...");
            }
        
        }
    }

      private boolean promptPlayAnotherRound(CrapsPlayer player) {
        while (true) {
            System.out.print("Play another round, " + player.getName() + "? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'.");
            }
        }
    }


      private int rollDice() {
        int die1 = random.nextInt(6) + 1; 
        int die2 = random.nextInt(6) + 1; 
        int sum = die1 + die2;
        System.out.println("Dice: [" + die1 + "][" + die2 + "] (total " + sum + ")");
        return sum;
    }

    private void printBalance(CasinoAccount account) {
        System.out.printf("New balance: $%.2f%n", account.getAccountBalance());
    }
}

