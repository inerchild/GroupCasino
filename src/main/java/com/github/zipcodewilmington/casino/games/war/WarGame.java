package com.github.zipcodewilmington.casino.games.war;

import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.utils.Deck;
import com.github.zipcodewilmington.utils.Card;

public class WarGame implements GameInterface {
    
    WarPlayer player;
    Deck deck;

        /**
     * adds a player to the game
     * @param player the player to be removed from the game
     */
    public void add(PlayerInterface player) {
        this.player = (WarPlayer) player;
    }

    /**
     * removes a player from the game
     * @param player the player to be removed from the game
     */
    public void remove(PlayerInterface player) {
        this.player = null;
    }

    /**
     * specifies how the game will run
     */
    public void run() {
        System.out.println("Welcome to War!");
        double bet = player.play();
        deck = new Deck();
        deck.shuffle();
        round(bet);
        System.out.println("Thanks for playing War!");
        System.out.println("Your account balance is: $" + this.player.getArcadeAccount().getAccountBalance());
    }

    public void round (double bet) {
        Card dealer = deck.drawCard();
        Card playerCard = deck.drawCard();

        System.out.println("Dealer's card: " + dealer.toString());
        System.out.println("Your card: " + playerCard.toString());

        if (dealer.compareTo(playerCard) < 0) {
            System.out.println("You win!");
            if (bet > 0) {
                ((WarPlayer) player).getArcadeAccount().creditAccount(bet);
            }
        } else if (dealer.compareTo(playerCard) > 0) {
            System.out.println("You lose!");
            if (bet > 0) {
                ((WarPlayer) player).getArcadeAccount().debitAccount(bet);
            }
        } else {
            System.out.println("It's a tie!");
            if (player.tie()){
                round(bet * 2);
            }
        }
    }
}
