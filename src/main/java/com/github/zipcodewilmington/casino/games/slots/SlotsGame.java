package com.github.zipcodewilmington.casino.games.slots;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Main Slots game that implements GameInterface
//Handles the game loop and player interactions
public class SlotsGame implements GameInterface {
    private List<PlayerInterface> players;
    private SlotMachine slotMachine;
    private Scanner scanner;
    private boolean isRunning;

    public SlotsGame() {
        this.players = new ArrayList<>();
        this.slotMachine = new SlotMachine(SymbolSet.createVegaSymbolSet());
        this.scanner = new Scanner(System.in);
        this.isRunning = false;
    }

    @Override
    public void add(PlayerInterface player) {
        players.add(player);
        //If it's a SlotsPlayer, give them access to the slot machine
        if (player instanceof SlotsPlayer) {
            ((SlotsPlayer) player).setSlotMachine(slotMachine);
        }
    }

    @Override
    public void remove(PlayerInterface player) {
        players.remove(player);
    }

    @Override
    public void run() {
        if (players.isEmpty()) {
            System.out.println("No players in the game!");
            return;
        }

        //Slots is single-player, only use the first player
        PlayerInterface player = players.get(0);

        isRunning = true;
        displayWelcome();
        playWithPlayer(player);
    }

    private void displayWelcome() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          🎰 WELCOME TO VEGAS SLOTS 🎰         ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println("\n💎 Match symbols to win big prizes!");
        System.out.println("💣 Watch out for BOMBS - they lose your bet!");
        System.out.println("☠️  BEWARE: The Skull of Doom lurks in the shadows...\n");
    }

    private void playWithPlayer(PlayerInterface player) {
        CasinoAccount account = player.getArcadeAccount();

        System.out.println("\n" + account.getAccountName() + "'s turn!");
        System.out.println("Current Balance: $" + String.format("%.2f", account.getAccountBalance()));

        while (isRunning) {
            System.out.println("\n───────────────────────────────────────────");
            System.out.println("Balance: $" + String.format("%.2f", account.getAccountBalance()));
            System.out.println("───────────────────────────────────────────");
            System.out.println("1. Spin ($10 bet)");
            System.out.println("2. Spin ($25 bet)");
            System.out.println("3. Spin ($50 bet)");
            System.out.println("4. Custom bet");
            System.out.println("5. View paytable");
            System.out.println("6. Exit game");
            System.out.print("\nChoice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    spinWithBet(account, 10.0);
                    break;
                case "2":
                    spinWithBet(account, 25.0);
                    break;
                case "3":
                    spinWithBet(account, 50.0);
                    break;
                case "4":
                    customBet(account);
                    break;
                case "5":
                    displayPayTable();
                    break;
                case "6":
                    System.out.println("\n👋 Thanks for playing! Final balance: $" 
                        + String.format("%.2f", account.getAccountBalance()));
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            //Checks if player is broke
            if (account.getAccountBalance() <= 0) {
                System.out.println("\nYou're out of money! Game over.");
                return;
            }
        }
    }

    private void spinWithBet(CasinoAccount account, double betAmount) {
        if (account.getAccountBalance() < betAmount) {
            System.out.println("\nInsufficient funds! You need $" + betAmount);
            return;
        }

        //Deduct bet
        account.setAccountBalance(account.getAccountBalance() - betAmount);
        slotMachine.placeBet(betAmount);

        //Spin animation
        System.out.println("\n Spinning...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //Spin the machine
        List<Symbol> result = slotMachine.spin();

        //Display the result
        System.out.println("\n┌─────┬─────┬─────┐");
        System.out.print("│  " + result.get(0).getIcon() + "  │  " 
            + result.get(1).getIcon() + "  │  " + result.get(2).getIcon() + "  │");
        System.out.println("\n└─────┴─────┴─────┘");

        //Calculates payout
        double payout = slotMachine.calculatePayout();

        //Handles results
        if (slotMachine.hasBomb()) {
            System.out.println("\n💥 BOOM! You hit a BOMB!");
            System.out.println("Lost your bet of $" + String.format("%.2f", betAmount));
        } else if (slotMachine.isJackpot()) {
            System.out.println("\n🎉🎉🎉 JACKPOT! 🎉🎉🎉");
            System.out.println("💰 Triple " + result.get(0).getName() + "!");
            System.out.println("You won: $" + String.format("%.2f", payout));
            account.setAccountBalance(account.getAccountBalance() + betAmount + payout);
        } else if (slotMachine.isWin()) {
            System.out.println("\n✨ Winner! ✨");
            System.out.println("You won: $" + String.format("%.2f", payout));
            account.setAccountBalance(account.getAccountBalance() + betAmount + payout);
        } else {
            System.out.println("\nNo match. Better luck next time!");
        }

        System.out.println("New Balance: $" + String.format("%.2f", account.getAccountBalance()));
    }

    private void customBet(CasinoAccount account) {
        System.out.print("Enter bet amount: $");
        try {
            double betAmount = Double.parseDouble(scanner.nextLine().trim());
            if (betAmount <= 0) {
            System.out.println("Bet must be positive!");
            return;
        }
        spinWithBet(account, betAmount); 
        } catch (NumberFormatException e) {
        System.out.println("Invalid amount!");
        }
    }

    private void displayPayTable() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║              💎 PAYTABLE 💎                   ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║ Symbol        │ Multiplier │ Description      ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║ 🍒 Cherry     │    3x      │ Classic fruit    ║");
        System.out.println("║ 🍋 Lemon      │    3x      │ Sour but sweet   ║");
        System.out.println("║ 🍊 Orange     │    4x      │ Citrus delight   ║");
        System.out.println("║ 🍇 Grape      │    5x      │ Purple power     ║");
        System.out.println("║ 🔔 Bell       │    7x      │ Ring the bell!   ║");
        System.out.println("║ ⭐ Star       │    8x      │ Reach for stars  ║");
        System.out.println("║ 7️⃣  Seven     │   10x      │ Lucky seven!     ║");
        System.out.println("║ 💎 Diamond    │   20x      │ Rare & valuable  ║");
        System.out.println("║ 💣 Bomb       │    0x      │ LOSE YOUR BET!   ║");
        System.out.println("║ ☠️  Skull     │   ???      │ [REDACTED]       ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println("\nMatch 2 or 3 symbols to win!");
        System.out.println("Jackpot (3 match) = bet x multiplier x 3\n");
    }

    //Gets the slot machine being used
    public SlotMachine getSlotMachine() {
        return slotMachine;
    }

    //Get player in the game
    public List<PlayerInterface> getPlayers() {
        return new ArrayList<>(players);
    }
}
