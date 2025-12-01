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
    private Integer currentPoint = null;
    
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

         printIntroBanner(); 
        

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

                System.out.println("\n----------------------  PLAYER INFO  ------------------------");
                System.out.println("Player: " + player.getName());
                System.out.printf("Balance: $%.2f%n", account.getAccountBalance());
                System.out.println("-------------------------------------------------------------\n");

                printCrapsTable();
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

    private void printIntroBanner() {
    System.out.println(
            "\n" +
            "============================================================\n" +
            "                   WELCOME TO THE CRAPS TABLE               \n" +
            "------------------------------------------------------------\n" +
            "               Roll the dice. Make your bets.               \n" +
            "                  Good luck, shooter!                       \n" +
            "============================================================\n"
    );
}

    private BetType promptForBetType() {
        while (true) {
            System.out.println("\nChoose your bet type:");
            System.out.println(" 1) Pass Line");
            System.out.println(" 2) Don't Pass Line");
            System.out.println(" 3) Field Bet (single roll)");
            System.out.println(" 4) Come Bet");
            System.out.println(" 5) Don't Come Bet");
            System.out.println(" 6) Place Bet (4,5,6,8,9,10)");
            System.out.println(" 7) Quit");

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

    public void resolvePassLineBet(CrapsPlayer player, double baseBet) {
        CasinoAccount account = player.getArcadeAccount();

        account.debitAccount(baseBet);
        System.out.printf("%s bets $%.2f on Pass Line.%n", player.getName(), baseBet);

        int comeOutRoll = rollDice();
        System.out.println("Come-out roll: " + comeOutRoll);

        if (comeOutRoll == 7 || comeOutRoll == 11) {
            System.out.println("You rolled a natural! Pass Line WINS!");
            account.creditAccount(baseBet * 2);
            printBalance(account);
            currentPoint = null;
            return;
        }

        if (comeOutRoll == 2 || comeOutRoll == 3 || comeOutRoll == 12) {
            System.out.println("Craps! Pass Line loses.");
            printBalance(account);
            currentPoint = null;
            return;
        }

        int point = comeOutRoll;
        System.out.println("Point is set to: " + point);
        currentPoint = point;

        double oddsBet = 0.0;
        if (account.getAccountBalance() > 0 && promptYesNo("Place odds on Pass Line? (y/n): ")) {
            double maxOdds = Math.min(baseBet, account.getAccountBalance());
            oddsBet = promptForOddsAmount(account, maxOdds);
            if (oddsBet > 0) {
                account.debitAccount(oddsBet);
                System.out.printf("Odds bet of $%.2f placed behind Pass Line.%n", oddsBet);
            }
        }

        System.out.println("Keep rolling: hit " + point + " to win, or 7 to lose.");

        while (true) {
            int roll = rollDice();
            System.out.println("You rolled: " + roll);

            if (roll == point) {
                System.out.println("You made your point! Pass Line WINS!");
                account.creditAccount(baseBet * 2);
                if (oddsBet > 0) {
                    double oddsWin = calculatePassOddsWin(point, oddsBet);
                    account.creditAccount(oddsBet + oddsWin);
                }
                printBalance(account);
                currentPoint = null;
                return;
            } else if (roll == 7) {
                System.out.println("Seven out! Pass Line loses.");
                printBalance(account);
                currentPoint = null;
                return;
            } else {
                System.out.println("No decision, roll again...");
            }
        }
    }

    public void resolveDontPassBet(CrapsPlayer player, double baseBet) {
        CasinoAccount account = player.getArcadeAccount();

        account.debitAccount(baseBet);
        System.out.printf("%s bets $%.2f on Don't Pass Line.%n", player.getName(), baseBet);

        int comeOutRoll = rollDice();
        System.out.println("Come-out roll: " + comeOutRoll);

        if (comeOutRoll == 7 || comeOutRoll == 11) {
            System.out.println("Seven or Eleven on Don't Pass. You LOSE.");
            printBalance(account);
            currentPoint = null;
            return;
        }

        if (comeOutRoll == 2 || comeOutRoll == 3) {
            System.out.println("Don't Pass WINS on 2 or 3!");
            account.creditAccount(baseBet * 2);
            printBalance(account);
            currentPoint = null;
            return;
        }

        if (comeOutRoll == 12) {
            System.out.println("12 is a push on Don't Pass. Bet is returned.");
            account.creditAccount(baseBet);
            printBalance(account);
            currentPoint = null;
            return;
        }

        int point = comeOutRoll;
        System.out.println("Point is set to: " + point + " (Don't Pass wants 7 before point).");
        currentPoint = point;

        double oddsBet = 0.0;
        if (account.getAccountBalance() > 0 && promptYesNo("Place odds on Don't Pass Line? (y/n): ")) {
            double maxOdds = Math.min(baseBet, account.getAccountBalance());
            oddsBet = promptForOddsAmount(account, maxOdds);
            if (oddsBet > 0) {
                account.debitAccount(oddsBet);
                System.out.printf("Odds bet of $%.2f placed behind Don't Pass.%n", oddsBet);
            }
        }

        while (true) {
            int roll = rollDice();
            System.out.println("You rolled: " + roll);

            if (roll == 7) {
                System.out.println("Seven out! Don't Pass WINS!");
                account.creditAccount(baseBet * 2);
                if (oddsBet > 0) {
                    double oddsWin = calculateDontPassOddsWin(point, oddsBet);
                    account.creditAccount(oddsBet + oddsWin);
                }
                printBalance(account);
                currentPoint = null;
                return;
            } else if (roll == point) {
                System.out.println("Point hit. Don't Pass loses.");
                printBalance(account);
                currentPoint = null;
                return;
            } else {
                System.out.println("No decision, roll again...");
            }
        }
    }

    public void resolveFieldBet(CrapsPlayer player, double bet) {
        CasinoAccount account = player.getArcadeAccount();

        account.debitAccount(bet);
        System.out.printf("%s bets $%.2f on the Field.%n", player.getName(), bet);

        int roll = rollDice();
        System.out.println("Field roll: " + roll);

        if (roll == 2) {
            double win = bet * 2; 
            System.out.println("2 rolled! Field pays 2:1.");
            account.creditAccount(bet + win);
        } else if (roll == 12) {
            double win = bet * 3; 
            System.out.println("12 rolled! Field pays 3:1.");
            account.creditAccount(bet + win);
        } else if (roll == 3 || roll == 4 || roll == 9 || roll == 10 || roll == 11) {
            System.out.println("Field wins! Pays 1:1.");
            account.creditAccount(bet * 2);
        } else {
            System.out.println("Field loses.");
        }

        printBalance(account);
    }

    public void resolveComeBet(CrapsPlayer player, double bet) {
        CasinoAccount account = player.getArcadeAccount();

        account.debitAccount(bet);
        System.out.printf("%s bets $%.2f on Come.%n", player.getName(), bet);

        int roll = rollDice();
        System.out.println("First roll for Come bet: " + roll);

        if (roll == 7 || roll == 11) {
            System.out.println("Come bet WINS on 7 or 11!");
            account.creditAccount(bet * 2);
            printBalance(account);
            return;
        }

        if (roll == 2 || roll == 3 || roll == 12) {
            System.out.println("Come bet loses on 2, 3, or 12.");
            printBalance(account);
            return;
        }

        int comePoint = roll;
        System.out.println("Come point is set to: " + comePoint + ". Hit it again before 7 to win.");

        while (true) {
            int nextRoll = rollDice();
            System.out.println("You rolled: " + nextRoll);
            if (nextRoll == comePoint) {
                System.out.println("Come point hit! Come bet WINS.");
                account.creditAccount(bet * 2);
                printBalance(account);
                return;
            } else if (nextRoll == 7) {
                System.out.println("Seven out. Come bet loses.");
                printBalance(account);
                return;
            } else {
                System.out.println("No decision, roll again...");
            }
        }
    }

    public void resolveDontComeBet(CrapsPlayer player, double bet) {
        CasinoAccount account = player.getArcadeAccount();

        account.debitAccount(bet);
        System.out.printf("%s bets $%.2f on Don't Come.%n", player.getName(), bet);

        int roll = rollDice();
        System.out.println("First roll for Don't Come bet: " + roll);

        if (roll == 7 || roll == 11) {
            System.out.println("7 or 11 on Don't Come. You LOSE.");
            printBalance(account);
            return;
        }

        if (roll == 2 || roll == 3) {
            System.out.println("Don't Come WINS on 2 or 3!");
            account.creditAccount(bet * 2);
            printBalance(account);
            return;
        }

        if (roll == 12) {
            System.out.println("12 is a push on Don't Come. Bet returned.");
            account.creditAccount(bet);
            printBalance(account);
            return;
        }

        int dontComePoint = roll;
        System.out.println("Don't Come point is set to: " + dontComePoint + ". 7 before point to win.");

        while (true) {
            int nextRoll = rollDice();
            System.out.println("You rolled: " + nextRoll);
            if (nextRoll == 7) {
                System.out.println("Seven out! Don't Come WINS.");
                account.creditAccount(bet * 2);
                printBalance(account);
                return;
            } else if (nextRoll == dontComePoint) {
                System.out.println("Point hit. Don't Come loses.");
                printBalance(account);
                return;
            } else {
                System.out.println("No decision, roll again...");
            }
        }
    }

    public void resolvePlaceBet(CrapsPlayer player, double bet) {
        CasinoAccount account = player.getArcadeAccount();

        int targetNumber = promptForPlaceNumber();
        if (targetNumber == -1) {
            System.out.println("Place bet cancelled.");
            return;
        }

        account.debitAccount(bet);
        System.out.printf("%s places $%.2f on %d.%n", player.getName(), bet, targetNumber);
        System.out.println("Rolling until " + targetNumber + " (win) or 7 (lose).");

        while (true) {
            int roll = rollDice();
            System.out.println("You rolled: " + roll);

            if (roll == targetNumber) {
                double win = calculatePlaceBetWin(targetNumber, bet);
                System.out.printf("You hit %d! Place bet wins with appropriate odds.%n", targetNumber);
                account.creditAccount(bet + win);
                printBalance(account);
                return;
            } else if (roll == 7) {
                System.out.println("Seven out. Place bet loses.");
                printBalance(account);
                return;
            } else {
                System.out.println("No decision, roll again...");
            }
        }
    }

      private double calculatePassOddsWin(int point, double oddsBet) {
        switch (point) {
            case 4:
            case 10:
                return oddsBet * 2.0;
            case 5:
            case 9:
                return oddsBet * 3.0 / 2.0;
            case 6:
            case 8:
                return oddsBet * 6.0 / 5.0;
            default:
                return 0.0;
        }
    }

    private double calculateDontPassOddsWin(int point, double oddsBet) {
        switch (point) {
            case 4:
            case 10:
                return oddsBet / 2.0;
            case 5:
            case 9:
                return oddsBet * 2.0 / 3.0;
            case 6:
            case 8:
                return oddsBet * 5.0 / 6.0;
            default:
                return 0.0;
        }
    }

    private double calculatePlaceBetWin(int number, double bet) {
        switch (number) {
            case 4:
            case 10:
                return bet * 9.0 / 5.0;
            case 5:
            case 9:
                return bet * 7.0 / 5.0;
            case 6:
            case 8:
                return bet * 7.0 / 6.0;
            default:
                return 0.0;
        }
    }

    private void printPointStatus() {
        if (currentPoint == null) {
            System.out.println("POINT: OFF\n");
        } else {
            System.out.println("POINT: [ " + currentPoint + " ]\n");
        }
    }
        
        protected int rollDice() {
            showRollingAnimation();

            int die1 = random.nextInt(6) + 1;
            int die2 = random.nextInt(6) + 1;
            int sum = die1 + die2;

            printDice(die1, die2, sum);

            return sum;
        }

        private void printDice(int d1, int d2, int sum) {
            String[] die1Lines = diceFace(d1).split("\n");
            String[] die2Lines = diceFace(d2).split("\n");

            System.out.println("\nYour roll:");

            for (int i = 0; i < die1Lines.length; i++) {
                System.out.println(die1Lines[i] + "   " + die2Lines[i]);
            }

            System.out.println("Total: " + sum + "\n");
        }

        private void showRollingAnimation() {
            try {
                String[] frames = { "Rolling.", "Rolling..", "Rolling..." };

                for (String frame : frames) {
                    System.out.print("\r" + frame);
                    Thread.sleep(200);  
                }

                System.out.print("\r"); 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private String diceFace(int n) {
            switch (n) {
                case 1:
                    return  "┌─────┐\n" +
                            "│     │\n" +
                            "│  ●  │\n" +
                            "│     │\n" +
                            "└─────┘";
                case 2:
                    return  "┌─────┐\n" +
                            "│●    │\n" +
                            "│     │\n" +
                            "│    ●│\n" +
                            "└─────┘";
                case 3:
                    return  "┌─────┐\n" +
                            "│●    │\n" +
                            "│  ●  │\n" +
                            "│    ●│\n" +
                            "└─────┘";
                case 4:
                    return  "┌─────┐\n" +
                            "│●   ●│\n" +
                            "│     │\n" +
                            "│●   ●│\n" +
                            "└─────┘";
                case 5:
                    return  "┌─────┐\n" +
                            "│●   ●│\n" +
                            "│  ●  │\n" +
                            "│●   ●│\n" +
                            "└─────┘";
                case 6:
                    return  "┌─────┐\n" +
                            "│●   ●│\n" +
                            "│●   ●│\n" +
                            "│●   ●│\n" +
                            "└─────┘";
                default:
                    return "";
            }
        }

    private void printBalance(CasinoAccount account) {
        System.out.printf("New balance: $%.2f%n", account.getAccountBalance());
    }

    private void printCrapsTable() {
        System.out.println(
                "========================= CRAPS TABLE ========================\n" +
                "| PASS LINE | COME | FIELD | DON'T PASS | PLACE BETS         |\n" +
                "--------------------------------------------------------------\n" +
                "| Field: Wins on 2,3,4,9,10,11 (2 pays 2:1, 12 pays 3:1)     |\n" +
                "| Pass Line: Win on 7/11, Lose on 2/3/12, otherwise point    |\n" +
                "| Place Bets: Choose numbers 4,5,6,8,9,10                    |\n" +
                "==============================================================\n"
        );
        printPointStatus();
    }
}