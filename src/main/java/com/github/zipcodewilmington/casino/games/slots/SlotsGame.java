package com.github.zipcodewilmington.casino.games.slots;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SlotsGame implements GameInterface {
    private List<PlayerInterface> players;
    private SlotMachine slotMachine;
    private SymbolSet symbolSet;
    private Scanner scanner;
    private boolean isRunning;
    
    public SlotsGame() {
        this.players = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.isRunning = false;
        this.symbolSet = selectTheme();
        this.slotMachine = new SlotMachine(symbolSet);
    }
    
    private SymbolSet selectTheme() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          🎰 WELCOME TO SLOTS 🎰               ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println("\nSelect your theme:");
        System.out.println("1. 🎰 Vegas Classic");
        System.out.println("2. 🏴‍☠️ Pirate's Treasure");
        System.out.println("3. 🚀 Space Adventure");
        System.out.print("\nChoice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "2":
                System.out.println("\n🏴‍☠️ Ahoy, matey! Setting sail for treasure! 🏴‍☠️\n");
                return SymbolSet.createPirateSymbolSet();
            case "3":
                System.out.println("\n🚀 Blast off to the stars! 🚀\n");
                return SymbolSet.createSpaceSymbolSet();
            case "1":
            default:
                System.out.println("\n🎰 Welcome to Vegas! 🎰\n");
                return SymbolSet.createVegaSymbolSet();
        }
    }
    
    @Override
    public void add(PlayerInterface player) {
        players.add(player);
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
        
        if (account == null) {
            System.out.println("Error: No account found. Please create an account first.");
            return;
        }
        
        System.out.println("\n🎰 " + account.getAccountName() + "'s turn!");
        System.out.println("💰 Current Balance: $" + String.format("%.2f", account.getAccountBalance()));
        
        while (isRunning) {
            System.out.println("\n───────────────────────────────────────────");
            System.out.println("💰 Balance: $" + String.format("%.2f", account.getAccountBalance()));
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
                    displayPaytable();
                    break;
                case "6":
                    System.out.println("\n👋 Thanks for playing! Final balance: $" 
                        + String.format("%.2f", account.getAccountBalance()));
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
            
            if (account.getAccountBalance() <= 0) {
                System.out.println("\n💸 You're out of money! Game over.");
                return;
            }
        }
    }
    
    private void spinWithBet(CasinoAccount account, double betAmount) {
        if (account.getAccountBalance() < betAmount) {
            System.out.println("\n❌ Insufficient funds! You need $" + betAmount);
            return;
        }
        
        account.setAccountBalance(account.getAccountBalance() - betAmount);
        slotMachine.placeBet(betAmount);
        
        List<Symbol> result = slotMachine.spin();
        animateSpinResult(result);
        
        double payout = slotMachine.calculatePayout();
        
        if (slotMachine.hasBomb()) {
            System.out.println("\n💥 BOOM! You hit a BOMB!");
            System.out.println("💸 Lost your bet of $" + String.format("%.2f", betAmount));
        } else if (slotMachine.isJackpot()) {
            System.out.println("\n🎉🎉🎉 JACKPOT! 🎉🎉🎉");
            System.out.println("💰 Triple " + result.get(0).getName() + "!");
            System.out.println("💵 You won: $" + String.format("%.2f", payout));
            account.setAccountBalance(account.getAccountBalance() + betAmount + payout);
        } else if (slotMachine.isWin()) {
            System.out.println("\n✨ Winner! ✨");
            System.out.println("💵 You won: $" + String.format("%.2f", payout));
            account.setAccountBalance(account.getAccountBalance() + betAmount + payout);
        } else {
            System.out.println("\n😔 No match. Better luck next time!");
        }
        
        System.out.println("💰 New Balance: $" + String.format("%.2f", account.getAccountBalance()));
    }
    
    private void customBet(CasinoAccount account) {
        System.out.print("Enter bet amount: $");
        try {
            double betAmount = Double.parseDouble(scanner.nextLine().trim());
            if (betAmount <= 0) {
                System.out.println("❌ Bet must be positive!");
                return;
            }
            spinWithBet(account, betAmount);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount!");
        }
    }
    
    private void displayPaytable() {
        System.out.println("\n");
        System.out.println("═════════════════════════════════════════");
        System.out.println("            💎 PAYTABLE 💎");
        System.out.println("═════════════════════════════════════════");
        System.out.println();
        System.out.println("  🍒 Cherry .......... 3x");
        System.out.println("  🍋 Lemon ........... 3x");
        System.out.println("  🍊 Orange .......... 4x");
        System.out.println("  🍇 Grape ........... 5x");
        System.out.println("  🔔 Bell ............ 7x");
        System.out.println("  ⭐ Star ............ 8x");
        System.out.println("  7️⃣  Seven .......... 10x");
        System.out.println("  💎 Diamond ......... 20x");
        System.out.println("  💣 Bomb ............ LOSE BET!");
        System.out.println("  ☠️  Skull .......... [REDACTED]");
        System.out.println();
        System.out.println("═════════════════════════════════════════");
        System.out.println("  Match 2 symbols = win!");
        System.out.println("  Match 3 symbols = JACKPOT!");
        System.out.println("  Jackpot = bet × multiplier × 3");
        System.out.println("═════════════════════════════════════════\n");
    }
    
    public SlotMachine getSlotMachine() {
        return slotMachine;
    }
    
    public List<PlayerInterface> getPlayers() {
        return new ArrayList<>(players);
    }
    
    private void animateSpinResult(List<Symbol> finalResult) {
        List<Symbol> allSymbols = symbolSet.getAllSymbols();
        int spinFrames = 12;
        
        try {
            System.out.println("\n🎰 SPINNING...\n");
            
            for (int frame = 0; frame < spinFrames; frame++) {
                if (frame > 0) {
                    System.out.print("\033[1A");
                    System.out.print("\033[2K");
                }
                
                Symbol s1, s2, s3;
                if (frame == spinFrames - 1) {
                    s1 = finalResult.get(0);
                    s2 = finalResult.get(1);
                    s3 = finalResult.get(2);
                } else {
                    s1 = allSymbols.get((int)(Math.random() * allSymbols.size()));
                    s2 = allSymbols.get((int)(Math.random() * allSymbols.size()));
                    s3 = allSymbols.get((int)(Math.random() * allSymbols.size()));
                }
                
                System.out.println("      " + s1.getIcon() + "      " + s2.getIcon() + "      " + s3.getIcon());
                
                int delay = frame < spinFrames - 3 ? 70 : 300;
                Thread.sleep(delay);
            }
            
            System.out.print("\033[1A");
            System.out.print("\033[2K");
            System.out.println("\n╔═══════════╦═══════════╦═══════════╗");
            System.out.println("║     " + finalResult.get(0).getIcon() + "    ║     " + finalResult.get(1).getIcon() + "    ║     " + finalResult.get(2).getIcon() + "    ║");
            System.out.println("╚═══════════╩═══════════╩═══════════╝");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("\n╔═══════════╦═══════════╦═══════════╗");
            System.out.println("║     " + finalResult.get(0).getIcon() + "    ║     " + finalResult.get(1).getIcon() + "    ║     " + finalResult.get(2).getIcon() + "    ║");
            System.out.println("╚═══════════╩═══════════╩═══════════╝\n");
        }
    }
}