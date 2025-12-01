package com.github.zipcodewilmington.casino.games.blackjack;

import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Deck;
import com.github.zipcodewilmington.utils.IOConsole;
import com.github.zipcodewilmington.utils.AnsiColor;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGame implements GameInterface {
    
    private Deck deck;
    private BlackjackDealer dealer;
    private BlackjackPlayer player;
    private IOConsole console;
    private List<BlackjackNPC> npcs;
    private static final int MIN_BET = 10;
    private static final int MAX_BET = 500;

    public BlackjackGame() {
        this.deck = new Deck();
        this.dealer = new BlackjackDealer();
        this.console = new IOConsole(AnsiColor.GREEN);
        this.npcs = new ArrayList<>();
    }

    @Override
    public void add(PlayerInterface player) {
        this.player = (BlackjackPlayer) player;
    }

    @Override
    public void remove(PlayerInterface player) {
        this.player = null;
    }
    
    private void createNPCs() {
        CasinoAccount conservativeAccount = new CasinoAccount("Conservative Bot", "npc1");
        conservativeAccount.creditAccount(1000.0);
        npcs.add(new BlackjackNPC(conservativeAccount, BlackjackNPC.Strategy.CONSERVATIVE));
        
        CasinoAccount aggressiveAccount = new CasinoAccount("Aggressive Bot", "npc2");
        aggressiveAccount.creditAccount(1000.0);
        npcs.add(new BlackjackNPC(aggressiveAccount, BlackjackNPC.Strategy.AGGRESSIVE));
        
        CasinoAccount randomAccount = new CasinoAccount("Random Bot", "npc3");
        randomAccount.creditAccount(1000.0);
        npcs.add(new BlackjackNPC(randomAccount, BlackjackNPC.Strategy.RANDOM));
    }

    @Override
    public void run() {
        console.println("Welcome to Blackjack, " + player.getArcadeAccount().getAccountName() + "!");
        console.println("Starting balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
        
        createNPCs();
        console.println("\nPlaying against " + npcs.size() + " NPCs:");
        for (BlackjackNPC npc : npcs) {
            console.println("  - " + npc.getArcadeAccount().getAccountName() + " (" + npc.getStrategyName() + ")");
        }

        while (!player.isBroke()) {
            console.println("\n" + "=".repeat(50));
            playRound();
            
            if (player.isBroke()) {
                console.println("\nYou're out of money! Game over.");
                break;
            }

            String choice = console.getStringInput("\nPlay another round? (y/n): ");
            if (!choice.trim().toLowerCase().equals("y")) {
                break;
            }
        }

        console.println("\nThanks for playing! Final balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
    }

    private void playRound() {
        dealer = new BlackjackDealer();
        player.resetHand();
        
        for (BlackjackNPC npc : npcs) {
            npc.resetHand();
        }

        if (deck.size() < 20) {
            deck = new Deck();
            deck.shuffle();
            console.println("Shuffling new deck....");
        }

        if (!placeBet()) {
            return;
        }

        dealInitialCards();
        
        if (checkForDealerBlackjack()) {
            return;
        }

        if (player.getHand().isBlackjack()) {
            console.println("\nBlackjack! You win!");
            int payout = (int)(player.getCurrentBet() * 1.5);
            player.win(payout);
            console.println("Balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
        } else {
            boolean playerSplit = playerTurn();
            
            if (playerSplit) {
                playNPCTurns();
                return;
            }
            
            if (player.getHand().isBusted()) {
                console.println("\nBust! You lose.");
                player.lose();
                console.println("Balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
            }
        }
        
        playNPCTurns();
        
        boolean anyoneNeedsDealer = !player.getHand().isBusted();
        for (BlackjackNPC npc : npcs) {
            if (!npc.isBroke() && !npc.getHand().isBusted() && !npc.getHand().isBlackjack()) {
                anyoneNeedsDealer = true;
                break;
            }
        }
        
        if (anyoneNeedsDealer) {
            dealerTurn();
        }
        
        if (!player.getHand().isBlackjack() && !player.getHand().isBusted()) {
            determineWinner();
        }
        
        determineNPCWinners();
    }

    private boolean placeBet() {
        console.println("\nYour balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
        String betInput = console.getStringInput("Place your bet (or 0 to quit): $");
       
        try {
            int betAmount = Integer.parseInt(betInput.trim());
            
            if (betAmount == 0) {
                return false;
            }
            
            if (player.placeBet(betAmount)) {
                console.println("Bet placed: $" + betAmount);
            } else {
                console.println("Invalid bet! Bet must be between $1 and $" + player.getArcadeAccount().getAccountBalance().intValue());
                return placeBet();
            }
        } catch (NumberFormatException e){
            console.println("Please enter a valid number.");
            return placeBet();
        }
        
        for (BlackjackNPC npc : npcs) {
            if (!npc.isBroke()) {
                int npcBet = npc.decideBetAmount(MIN_BET, MAX_BET);
                npc.placeBet(npcBet);
                console.println(npc.getArcadeAccount().getAccountName() + " bets: $" + npcBet);
            }
        }
        
        return true;
    }

    private void dealInitialCards() {
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());
        
        for (BlackjackNPC npc : npcs) {
            if (!npc.isBroke()) {
                npc.receiveCard(deck.drawCard());
            }
        }
        
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());
        
        for (BlackjackNPC npc : npcs) {
            if (!npc.isBroke()) {
                npc.receiveCard(deck.drawCard());
            }
        }

        console.println("\nDealer shows: " + dealer.getUpCard());
        console.println("Your hand: " + player.getHand());
    }

    private boolean checkForDealerBlackjack() {
        if (dealer.shouldDealerCheckForBlackjack() && dealer.hasBlackjack()) {
            console.println("\nDealer reveals: " + dealer.getHand());
            console.println("Dealer has BLACKJACK!");
            
            if (player.getHand().isBlackjack()) {
                console.println("Push! You also have blackjack.");
                player.push();
            } else {
                console.println("You lose.");
                player.lose();
            }
            console.println("Balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
            
            for (BlackjackNPC npc : npcs) {
                if (npc.isBroke()) continue;
                
                if (npc.getHand().isBlackjack()) {
                    npc.push();
                } else {
                    npc.lose();
                }
            }
            
            return true;
        }
        return false;
    }
    
    private boolean playerTurn() {
        if (player.getHand().canSplit() && player.canAffordBet(player.getCurrentBet())) {
            console.println("\nYour hand: " + player.getHand());
            String choice = console.getStringInput("You have a pair! Would you like to split? (y/n): ");
            
            if (choice.trim().toLowerCase().equals("y") || choice.trim().toLowerCase().equals("yes")) {
                handleSplit();
                return true;
            }
        }

        playHand(player.getHand(), "Your hand");
        return false;
    }
    
    private void playHand(BlackjackHand hand, String handName) {
        while (!hand.isBusted()) {
            console.println("\n" + handName + ": " + hand);
            
            String prompt = "Choose action - (h)it, (s)tand";
            if (hand.size() == 2 && player.canAffordBet(player.getCurrentBet())) {
                prompt += ", (d)ouble down";
            }
            prompt += ": ";
            
            String choice = console.getStringInput(prompt);
            
            switch (choice.trim().toLowerCase()) {
                case "h":
                case "hit":
                    hand.addCard(deck.drawCard());
                    console.println("You drew: " + hand.getCards().get(hand.size() - 1));
                    break;
                    
                case "s":
                case "stand":
                    console.println("You stand at " + hand.getValue());
                    return;
                    
                case "d":
                case "double":
                    if (hand.size() == 2 && player.canAffordBet(player.getCurrentBet())) {
                        int additionalBet = player.getCurrentBet();

                        if (player.placeBet(additionalBet)) {
                            hand.addCard(deck.drawCard());
                            console.println("Double down! Drew: " + hand.getCards().get(hand.size() - 1));
                            console.println(handName + ": " + hand);
                            return;
                        }
                    } else {
                        console.println("Cannot double down.");
                    }
                    break;
                    
                default:
                    console.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void handleSplit() {
        console.println("\n=== SPLITTING PAIR ===");
        
        int originalBet = player.getCurrentBet();
        if (!player.placeBet(originalBet)) {
            console.println("Not enough funds to split. Playing single hand.");
            playHand(player.getHand(), "Your hand");
            return;
        }
        
        BlackjackHand firstHand = player.getHand();
        BlackjackHand secondHand = firstHand.split();
        
        firstHand.addCard(deck.drawCard());
        secondHand.addCard(deck.drawCard());
        
        console.println("First hand: " + firstHand);
        console.println("Second hand: " + secondHand);
        
        console.println("\n--- Playing First Hand ---");
        playHand(firstHand, "First hand");
        
        boolean firstHandBusted = firstHand.isBusted();
        int firstHandValue = firstHand.getValue();
        
        if (firstHandBusted) {
            console.println("First hand busted!");
        } else {
            console.println("First hand final value: " + firstHandValue);
        }
        
        console.println("\n--- Playing Second Hand ---");
        playHand(secondHand, "Second hand");
        
        boolean secondHandBusted = secondHand.isBusted();
        int secondHandValue = secondHand.getValue();
        
        if (secondHandBusted) {
            console.println("Second hand busted!");
        } else {
            console.println("Second hand final value: " + secondHandValue);
        }
        
        dealerTurn();
        
        int dealerValue = dealer.getHandValue();
        boolean dealerBusted = dealer.getHand().isBusted();
        
        console.println("\n=== SPLIT HAND RESULTS ===");
        
        console.println("\nFirst Hand:");
        if (firstHandBusted) {
            console.println(" Busted - You lose");
        } else if (dealerBusted) {
            console.println(" You win! Dealer busted.");
            player.win(originalBet);
        } else if (firstHandValue > dealerValue) {
            console.println(" You win! " + firstHandValue + " beats " + dealerValue);
            player.win(originalBet);
        } else if (firstHandValue < dealerValue) {
            console.println(" Dealer wins. " + dealerValue + " beats " + firstHandValue);
        } else {
            console.println(" Push! Tie at " + firstHandValue);
            player.push();
        }
        
        console.println("\nSecond Hand:");
        if (secondHandBusted) {
            console.println(" Busted - You lose");
        } else if (dealerBusted) {
            console.println(" You win! Dealer busted.");
            player.win(originalBet);
        } else if (secondHandValue > dealerValue) {
            console.println(" You win! " + secondHandValue + " beats " + dealerValue);
            player.win(originalBet);
        } else if (secondHandValue < dealerValue) {
            console.println(" Dealer wins. " + dealerValue + " beats " + secondHandValue);
        } else {
            console.println(" Push! Tie at " + secondHandValue);
            player.push();
        }
        
        console.println("\nBalance: $" + player.getArcadeAccount().getAccountBalance().intValue());
    }

    private void dealerTurn() {
        console.println("\n--- Dealer's Turn ---");
        console.println("Dealer reveals: " + dealer.getHand());
        
        dealer.playTurn(deck);
        
        console.println("Dealer's final hand: " + dealer.getHand());
        
        if (dealer.getHand().isBusted()) {
            console.println("Dealer busts!");
        }
    }

    private void determineWinner() {
        int playerValue = player.getHandValue();
        int dealerValue = dealer.getHandValue();
        
        console.println("\n--- Results ---");
        console.println("Your hand: " + playerValue);
        console.println("Dealer's hand: " + dealerValue);
        
        if (dealer.getHand().isBusted()) {
            console.println(" You win! Dealer busted.");
            player.win(player.getCurrentBet());
        } else if (playerValue > dealerValue) {
            console.println(" You win! " + playerValue + " beats " + dealerValue);
            player.win(player.getCurrentBet());
        } else if (playerValue < dealerValue) {
            console.println(" Dealer wins. " + dealerValue + " beats " + playerValue);
            player.lose();
        } else {
            console.println(" Push! It's a tie at " + playerValue);
            player.push();
        }
        
        console.println("Balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
    }
    
    private void playNPCTurns() {
        console.println("\n" + "=".repeat(50));
        console.println("NPC TURNS");
        console.println("=".repeat(50));
        
        int dealerUpCardValue = getCardValue(dealer.getUpCard());
        
        for (BlackjackNPC npc : npcs) {
            if (npc.isBroke()) {
                console.println("\n" + npc.getArcadeAccount().getAccountName() + " is broke!");
                continue;
            }
            
            console.println("\n--- " + npc.getArcadeAccount().getAccountName() + " (" + npc.getStrategyName() + ") ---");
            console.println("Hand: " + npc.getHand());
            
            if (npc.getHand().isBlackjack()) {
                console.println("BLACKJACK!");
            } else {
                playNPCHand(npc, dealerUpCardValue);
                
                if (npc.getHand().isBusted()) {
                    console.println("BUSTED!");
                    npc.lose();
                    console.println("Balance: $" + npc.getArcadeAccount().getAccountBalance().intValue());
                }
            }
        }
    }
    
    private void determineNPCWinners() {
        console.println("\n" + "=".repeat(50));
        console.println("NPC RESULTS");
        console.println("=".repeat(50));
        
        for (BlackjackNPC npc : npcs) {
            if (npc.isBroke()) {
                continue;
            }
            
            console.println("\n--- " + npc.getArcadeAccount().getAccountName() + " ---");
            
            if (npc.getHand().isBlackjack()) {
                int payout = (int)(npc.getCurrentBet() * 1.5);
                npc.win(payout);
                console.println("BLACKJACK! Wins with 3:2 payout");
            } else if (npc.getHand().isBusted()) {
                console.println("Already busted - no payout needed");
            } else {
                determineNPCWinner(npc);
            }
            
            console.println("Balance: $" + npc.getArcadeAccount().getAccountBalance().intValue());
        }
    }
    
    private void playNPCHand(BlackjackNPC npc, int dealerUpCardValue) {
        if (npc.shouldDoubleDown(dealerUpCardValue)) {
            console.println("Decision: DOUBLE DOWN");
            npc.doubleDown(deck);
            console.println("Final hand: " + npc.getHand());
            return;
        }
        
        while (npc.shouldHit(dealerUpCardValue) && !npc.getHand().isBusted()) {
            console.println("Decision: HIT");
            npc.hit(deck);
            console.println("Hand: " + npc.getHand());
        }
        
        if (!npc.getHand().isBusted()) {
            console.println("Decision: STAND");
            console.println("Final hand: " + npc.getHand());
        }
    }
    
    private void determineNPCWinner(BlackjackNPC npc) {
        int npcValue = npc.getHandValue();
        int dealerValue = dealer.getHandValue();
        
        if (dealer.getHand().isBusted()) {
            console.println("Wins! Dealer busted.");
            npc.win(npc.getCurrentBet());
        } else if (npcValue > dealerValue) {
            console.println("Wins! " + npcValue + " beats " + dealerValue);
            npc.win(npc.getCurrentBet());
        } else if (npcValue < dealerValue) {
            console.println("Loses. " + dealerValue + " beats " + npcValue);
            npc.lose();
        } else {
            console.println("Push! Tie at " + npcValue);
            npc.push();
        }
    }
    
    private int getCardValue(Card card) {
        if (card == null) return 0;
        
        switch (card.getRank()) {
            case ACE:
                return 11;
            case KING:
            case QUEEN:
            case JACK:
                return 10;
            default:
                return card.getRank().getValue();
        }
    }
}