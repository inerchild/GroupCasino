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

  private enum BetType {
        PASS_LINE,
        DONT_PASS,
        FIELD,
        COME,
        DONT_COME,
        PLACE,
        QUIT
    }

    private final List<CrapsPlayer> players = new ArrayList<>();
    private final Scanner scanner;
    private final Random random = new Random();

    
    public CrapsGame() {
        this(new Scanner(System.in));
    }

    public CrapsGame(Scanner scanner) {
        this.scanner = scanner;
    }

     @Override
    public void add(PlayerInterface player) {
        if (!(player instanceof CrapsPlayer)) {
            throw new IllegalArgumentException("CrapsGame only supports CrapsPlayer.");
        }
        players.add((CrapsPlayer) player);
    }

    @Override
    public void remove(PlayerInterface player) {
        players.remove(player);
    }

    @Override
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

            for (Iterator<CrapsPlayer> it = players.iterator(); it.hasNext(); ) {
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

                BetType betType = promptForBetType();
                if (betType == BetType.QUIT) {
                    System.out.println(player.getName() + " leaves the craps table.");
                    it.remove();
                    continue;
                }

                Double bet = promptForBet(account);
                if (bet == null) {
                    System.out.println(player.getName() + " leaves the craps table.");
                    it.remove();
                    continue;
                }

                switch (betType) {
                    case PASS_LINE:
                        resolvePassLineBet(player, bet);
                        break;
                    case DONT_PASS:
                        resolveDontPassBet(player, bet);
                        break;
                    case FIELD:
                        resolveFieldBet(player, bet);
                        break;
                    case COME:
                        resolveComeBet(player, bet);
                        break;
                    case DONT_COME:
                        resolveDontComeBet(player, bet);
                        break;
                    case PLACE:
                        resolvePlaceBet(player, bet);
                        break;
                    default:
                        break;
                }

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
            }
        }

        System.out.println("Craps game over.");
    }

     private BetType promptForBetType() {
        while (true) {
            System.out.println("\nChoose your bet type:");
            System.out.println("1. Pass Line");
            System.out.println("2. Don't Pass Line");
            System.out.println("3. Field Bet (single roll)");
            System.out.println("4. Come Bet");
            System.out.println("5. Don't Come Bet");
            System.out.println("6. Place Bet (4,5,6,8,9,10)");
            System.out.println("7. Quit to Casino Menu");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return BetType.PASS_LINE;
                case "2":
                    return BetType.DONT_PASS;
                case "3":
                    return BetType.FIELD;
                case "4":
                    return BetType.COME;
                case "5":
                    return BetType.DONT_COME;
                case "6":
                    return BetType.PLACE;
                case "7":
                    return BetType.QUIT;
                default:
                    System.out.println("Invalid selection. Please enter a number 1–7.");
            }
        }
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

    private boolean promptYesNo(String message) {
        while (true) {
            System.out.print(message);
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

    private int promptForPlaceNumber() {
        while (true) {
            System.out.print("Enter number to place bet on (4,5,6,8,9,10) or 'q' to cancel: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q") || input.equals("quit")) {
                return -1;
            }

            try {
                int number = Integer.parseInt(input);
                if (number == 4 || number == 5 || number == 6 ||
                    number == 8 || number == 9 || number == 10) {
                    return number;
                } else {
                    System.out.println("Invalid place number. Must be 4,5,6,8,9, or 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter 4,5,6,8,9, or 10.");
            }
        }
    }

    private double promptForOddsAmount(CasinoAccount account, double maxAllowed) {
        if (maxAllowed <= 0 || account.getAccountBalance() <= 0) {
            return 0.0;
        }

        while (true) {
            System.out.printf("Enter odds bet amount (max $%.2f, or 0 for no odds): ", maxAllowed);
            String input = scanner.nextLine().trim().toLowerCase();

            try {
                double amount = Double.parseDouble(input);
                if (amount < 0) {
                    System.out.println("Amount must be 0 or greater.");
                } else if (amount == 0) {
                    return 0.0;
                } else if (amount > maxAllowed || amount > account.getAccountBalance()) {
                    System.out.println("Odds bet cannot exceed the allowed maximum or your balance.");
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a numeric amount.");
            }
        }
    }


//
    
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

