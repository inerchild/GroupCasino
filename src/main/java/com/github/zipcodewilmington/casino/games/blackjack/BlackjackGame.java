package com.github.zipcodewilmington.casino.games.blackjack;

import com.github.zipcodewilmington.utils.Deck;
import java.util.Scanner;

public class BlackjackGame {
    
    private Deck deck;
    private BlackjackDealer dealer;
    private BlackjackPlayer player;
    private Scanner scanner;

    public BlackjackGame(String playerName, int startingBalance) {
        this.deck = new Deck();
        this.dealer = new BlackjackDealer();
        this.player = new BlackjackPlayer(playerName, startingBalance);
        this.scanner = new Scanner(System.in);
    }

    public void play(){
        System.out.println("Welcome to Blackjack, " + player.getName() + "!");
        System.out.println("Starting balance: $" + player.getBalance());

        while (!player.isBroke()) {
            System.out.println("\n" + "=".repeat(50));
            playRound();
            
            if (player.isBroke()) {
                System.out.println("\nYou're out of money! Game over.");
                break;
            }

            System.out.print("\nPlay another round? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (!choice.equals("y")) {
                break;
            }
        }

        System.out.println("\nThanks for playing! Final balance: $" + player.getBalance());
    }

    private void playRound() {
        dealer = new BlackjackDealer();
        player = new BlackjackPlayer(player.getName(), player.getBalance());

        if (deck.size() < 20) {
            deck = new Deck();
            deck.shuffle();
            System.out.println("Shuffling new deck....");
        }


        if (!placeBet()) {
            return;
        }

        dealInitialCards();
        
        if (checkForDealerBlackjack()) {
            return;
        }

        if (player.getHand().isBlackjack()) {
            System.out.println("\n Blackjack! You win!");
            int payout = (int)(player.getCurrentBet() * 1.5);
            player.win(payout);
            System.out.println("Balance: $" + player.getBalance());
            return;
        }

        boolean playerSplit = playerTurn();
        
        if (playerSplit) {
            return;
        }
        
        if (player.getHand().isBusted()) {
            System.out.println("\n Bust! Dealer wins.");
            player.lose();
            System.out.println("Balance: $" + player.getBalance());
            return;
        }

        dealerTurn();

        determineWinner();
    }

    private boolean placeBet() {
        System.out.println("\nYour balance: $" + player.getBalance());
        System.out.print("Place your bet (or 0 to quit): $");
       
        try {
            int betAmount = Integer.parseInt(scanner.nextLine().trim());
            
            if (betAmount == 0) {
                return false;
            }
            
            if (player.placeBet(betAmount)) {
                System.out.println("Bet placed: $" + betAmount);
                return true;
            } else {
                System.out.println("Invalid bet! Bet must be between $1 and $" + player.getBalance());
                return placeBet();
            }
        } catch (NumberFormatException e){
            System.out.println("Please enter a valid number.");
            return placeBet();
        }
    }

    private void dealInitialCards() {
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());

        System.out.println("\nDealer shows: " + dealer.getUpCard());
        System.out.println("Your hand: " + player.getHand());
    }

    private boolean checkForDealerBlackjack() {
        if (dealer.shouldDealerCheckForBlackjack() && dealer.hasBlackjack()) {
            System.out.println("\nDealer reveals: " + dealer.getHand());
            System.out.println("Dealer has BLACKJACK!");
            
            if (player.getHand().isBlackjack()) {
                System.out.println("Push! You also have blackjack.");
                player.push();
            } else {
                System.out.println("You lose.");
                player.lose();
            }
            System.out.println("Balance: $" + player.getBalance());
            return true;
        }
        return false;
    }
    
    private boolean playerTurn() {

        if (player.getHand().canSplit() && player.canAffordBet(player.getCurrentBet())) {
            System.out.println("\nYour hand: " + player.getHand());
            System.out.print("You have a pair! Would you like to split? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            if (choice.equals("y") || choice.equals("yes")) {
                handleSplit();
                return true;
            }
        }

        playHand(player.getHand(), "Your hand");
        return false;
    }
    
    private void playHand(BlackjackHand hand, String handName) {
        while (!hand.isBusted()) {
            System.out.println("\n" + handName + ": " + hand);
            System.out.print("Choose action - (h)it, (s)tand");
            
            if (hand.size() == 2 && player.canAffordBet(player.getCurrentBet())) {
                System.out.print(", (d)ouble down");
            }
            
            System.out.print(": ");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            switch (choice) {
                case "h":
                case "hit":
                    hand.addCard(deck.drawCard());
                    System.out.println("You drew: " + hand.getCards().get(hand.size() - 1));
                    break;
                    
                case "s":
                case "stand":
                    System.out.println("You stand at " + hand.getValue());
                    return;
                    
                case "d":
                case "double":
                    if (hand.size() == 2 && player.canAffordBet(player.getCurrentBet())) {
                        int additionalBet = player.getCurrentBet();

                        if (player.placeBet(additionalBet)) {
                            hand.addCard(deck.drawCard());

                            System.out.println("Double down! Drew: " + hand.getCards().get(hand.size() - 1));
                            System.out.println(handName + ": " + hand);
                            return;
                        }
                    } else {
                        System.out.println("Cannot double down.");
                    }
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void handleSplit() {
        System.out.println("\n=== SPLITTING PAIR ===");
        
        int originalBet = player.getCurrentBet();
        if (!player.placeBet(originalBet)) {
            System.out.println("Not enough funds to split. Playing single hand.");
            playHand(player.getHand(), "Your hand");
            return;
        }
        
        BlackjackHand firstHand = player.getHand();
        BlackjackHand secondHand = firstHand.split();
        
        firstHand.addCard(deck.drawCard());
        secondHand.addCard(deck.drawCard());
        
        System.out.println("First hand: " + firstHand);
        System.out.println("Second hand: " + secondHand);
        
        System.out.println("\n--- Playing First Hand ---");
        playHand(firstHand, "First hand");
        
        boolean firstHandBusted = firstHand.isBusted();
        int firstHandValue = firstHand.getValue();
        
        if (firstHandBusted) {
            System.out.println("First hand busted!");
        } else {
            System.out.println("First hand final value: " + firstHandValue);
        }
        
        System.out.println("\n--- Playing Second Hand ---");
        playHand(secondHand, "Second hand");
        
        boolean secondHandBusted = secondHand.isBusted();
        int secondHandValue = secondHand.getValue();
        
        if (secondHandBusted) {
            System.out.println("Second hand busted!");
        } else {
            System.out.println("Second hand final value: " + secondHandValue);
        }
        
        dealerTurn();
        
        int dealerValue = dealer.getHandValue();
        boolean dealerBusted = dealer.getHand().isBusted();
        
        System.out.println("\n=== SPLIT HAND RESULTS ===");
        
        System.out.println("\nFirst Hand:");
        if (firstHandBusted) {
            System.out.println(" Busted - You lose");
        } else if (dealerBusted) {
            System.out.println(" You win! Dealer busted.");
            player.win(originalBet);
        } else if (firstHandValue > dealerValue) {
            System.out.println(" You win! " + firstHandValue + " beats " + dealerValue);
            player.win(originalBet);
        } else if (firstHandValue < dealerValue) {
            System.out.println(" Dealer wins. " + dealerValue + " beats " + firstHandValue);
        } else {
            System.out.println(" Push! Tie at " + firstHandValue);
            player.push();
        }
        
        System.out.println("\nSecond Hand:");
        if (secondHandBusted) {
            System.out.println(" Busted - You lose");
        } else if (dealerBusted) {
            System.out.println(" You win! Dealer busted.");
            player.win(originalBet);
        } else if (secondHandValue > dealerValue) {
            System.out.println(" You win! " + secondHandValue + " beats " + dealerValue);
            player.win(originalBet);
        } else if (secondHandValue < dealerValue) {
            System.out.println(" Dealer wins. " + dealerValue + " beats " + secondHandValue);
        } else {
            System.out.println(" Push! Tie at " + secondHandValue);
            player.push();
        }
        
        System.out.println("\nBalance: $" + player.getBalance());
    }

    private void dealerTurn() {
        System.out.println("\n--- Dealer's Turn ---");
        System.out.println("Dealer reveals: " + dealer.getHand());
        
        dealer.playTurn(deck);
        
        System.out.println("Dealer's final hand: " + dealer.getHand());
        
        if (dealer.getHand().isBusted()) {
            System.out.println("Dealer busts!");
        }
    }

    private void determineWinner() {
        int playerValue = player.getHandValue();
        int dealerValue = dealer.getHandValue();
        
        System.out.println("\n--- Results ---");
        System.out.println("Your hand: " + playerValue);
        System.out.println("Dealer's hand: " + dealerValue);
        
        if (dealer.getHand().isBusted()) {
            System.out.println(" You win! Dealer busted.");
            player.win(player.getCurrentBet());
        } else if (playerValue > dealerValue) {
            System.out.println(" You win! " + playerValue + " beats " + dealerValue);
            player.win(player.getCurrentBet());
        } else if (playerValue < dealerValue) {
            System.out.println(" Dealer wins. " + dealerValue + " beats " + playerValue);
            player.lose();
        } else {
            System.out.println(" Push! It's a tie at " + playerValue);
            player.push();
        }
        
        System.out.println("Balance: $" + player.getBalance());
    }
}