package com.github.zipcodewilmington.casino.games.blackjack;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface;

public class BlackjackPlayer implements PlayerInterface {
    
    private CasinoAccount account;
    private BlackjackHand hand;
    private int currentBet;

    public BlackjackPlayer(CasinoAccount account) {
        this.account = account;
        this.hand = new BlackjackHand();
        this.currentBet = 0;
    }

    @Override
    public CasinoAccount getArcadeAccount() {
        return account;
    }

    @Override
    public CasinoAccount play() {
        return account;
    }

    public BlackjackHand getHand() {
        return hand;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int getHandValue() {
        return hand.getValue();
    }

    public void receiveCard(com.github.zipcodewilmington.utils.Card card) {
        hand.addCard(card);
    }

    public void hit(com.github.zipcodewilmington.utils.Deck deck) {
        com.github.zipcodewilmington.utils.Card newCard = deck.drawCard();
        hand.addCard(newCard);
    }

    public boolean doubleDown(com.github.zipcodewilmington.utils.Deck deck) {
        double balance = account.getAccountBalance();
        if (balance < currentBet) {
            return false;
        }
        account.debitAccount((double) currentBet);
        currentBet *= 2;
        hit(deck);
        return true;
    }

    public boolean placeBet(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > account.getAccountBalance().intValue()) {
            return false;
        }
        currentBet = amount;
        account.debitAccount((double) amount);
        return true;
    }

    public void win(int payout) {
        account.creditAccount((double) (currentBet + payout));
        currentBet = 0;
    }

    public void lose() {
        currentBet = 0;
    }

    public void push() {
        account.creditAccount((double) currentBet);
        currentBet = 0;
    }

    public boolean canAffordBet(int amount) {
        return account.getAccountBalance().intValue() >= amount;
    }

    public boolean isBroke() {
        return account.getAccountBalance() <= 0;
    }

    public void resetHand() {
        hand = new BlackjackHand();
        currentBet = 0;
    }
}