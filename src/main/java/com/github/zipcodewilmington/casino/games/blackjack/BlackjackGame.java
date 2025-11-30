package com.github.zipcodewilmington.casino.games.blackjack;

import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Deck;
import com.github.zipcodewilmington.utils.IOConsole;
import com.github.zipcodewilmington.utils.AnsiColor;

public class BlackjackGame implements GameInterface {
    
    private Deck deck;
    private BlackjackDealer dealer;
    private BlackjackPlayer player;
    private IOConsole console;

    public BlackjackGame() {
        this.deck = new Deck();
        this.dealer = new BlackjackDealer();
        this.console = new IOConsole(AnsiColor.GREEN);
    }

    @Override
    public void add(PlayerInterface player) {
        this.player = (BlackjackPlayer) player;
    }

    @Override
    public void remove(PlayerInterface player) {
        this.player = null;
    }

    @Override
    public void run() {
        console.println("Welcome to Blackjack, " + player.getArcadeAccount().getAccountName() + "!");
        console.println("Starting balance: $" + player.getArcadeAccount().getAccountBalance().intValue());

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
            return;
        }

        boolean playerSplit = playerTurn();
        
        if (playerSplit) {
            return;
        }
        
        if (player.getHand().isBusted()) {
            console.println("\nBust! Dealer wins.");
            player.lose();
            console.println("Balance: $" + player.getArcadeAccount().getAccountBalance().intValue());
            return;
        }

        dealerTurn();
        determineWinner();
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
                return true;
            } else {
                console.println("Invalid bet! Bet must be between $1 and $" + player.getArcadeAccount().getAccountBalance().intValue());
                return placeBet();
            }
        } catch (NumberFormatException e){
            console.println("Please enter a valid number.");
            return placeBet();
        }
    }

    private void dealInitialCards() {
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());

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
}