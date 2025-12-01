package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.utils.Card;

public class PokerPlayer implements PlayerInterface {
    
    private final CasinoAccount account;                // <----- Link to casino account
    private final Hand hand;
    private double currentBet;
    private double totalBet;
    private boolean folded;
    private final String name;

    public PokerPlayer(CasinoAccount account) {             // **** Constructor
        this.account = account;
        this.hand = new Hand();
        this.currentBet = 0;
        this.totalBet = 0;
        this.folded = false;
        this.name = account.getAccountName();
    }

    public double getBalance() {
        return account.getAccountBalance();
    }

    public String getName() {
        return name;
    }

    public CasinoAccount getAccount() {
        return account;
    }

    public Hand getHand() {
        return hand;
    }

    public void receiveHoleCard(Card card) {
        hand.addHoleCard(card);
    }

    public void placeBet(double amount) {
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Cannot bet more than current balance");
        }
        account.debitAccount(amount);
        currentBet += amount;
        totalBet += amount;
    }

    public void receiveWinnings(double amount) {
        account.creditAccount(amount);
    }

    public void fold() {
        folded = true;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public boolean isFolded() {
        return folded;
    }

    public double getTotalBet() {
        return totalBet;
    }

    public void resetCurrentBet() {             // resets current bet, not total bet
        currentBet = 0;
    }

    public void resetForNewHand() {
        hand.clear();
        currentBet = 0;
        totalBet = 0;
        folded = false;
    }

    public boolean hasChips() {
        return getBalance() > 0;
    }

    public double getAmountToCall(double currentBetToMatch) {

        double amountToCall = currentBetToMatch - currentBet;

        if (amountToCall > getBalance()) {
            return getBalance();                            // <----- if they dont have enough, they can only all-in
        }

        return Math.max(0, amountToCall);
    }

    public boolean isAllIn() {
        return getBalance() == 0 && !folded;
    }

    @Override
    public boolean equals(Object obj) {                     // <----- Deterines if two PokerPlayer objects are the same player
        if (this == obj) 
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        PokerPlayer other = (PokerPlayer) obj;
        return this.account.equals(other.account);
    }

    @Override                                           // <------ generates hash code for this player
    public int hashCode() {
        return account.hashCode();
    }

    @Override
    public CasinoAccount getArcadeAccount() {
        return account;
    }

    @Override                       // doenst work without this, DO NOT REMOVE
    public Object play() {
        return null;
    }

    @Override                                           // <------ converts playerObject to string for easy reading
    public String toString() {
        return String.format("%s (Balance: $%.2f, Current Bet: $%.2f, Folded: %s)", 
            name, getBalance(), currentBet, folded);
    }
}
