package com.github.zipcodewilmington.casino.games.blackjack;

import com.github.zipcodewilmington.utils.Card;


public class BlackjackPlayer {

    private String name;
    private BlackjackHand hand;
    private int balance;        
    private int currentBet;

    public BlackjackPlayer(String name, int startingBalance) {
        this.name = name;
        this.hand = new BlackjackHand();
        this.balance = startingBalance;
        this.currentBet = 0;
        }

    public String getName() {
        return name;
    }

    public BlackjackHand getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void hit(com.github.zipcodewilmington.utils.Deck deck) {
        Card newCard = deck.drawCard();
        hand.addCard(newCard);
    }

    public boolean placeBet(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance) {
            return false;
        }
        currentBet = amount;
        balance -= amount;
        return true;
    }

    public void win(int payout) {
        balance += currentBet + payout;
        currentBet = 0;
    }
    
    public void lose() {
        currentBet = 0;
    }

    public void push() {
        balance += currentBet;
        currentBet = 0;
    }

    public boolean canAffordBet(int amount) {
        return balance >= amount;
    }

    public boolean isBroke() {
        return balance <=0;
    }
}

