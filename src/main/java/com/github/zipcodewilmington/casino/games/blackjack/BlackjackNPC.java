package com.github.zipcodewilmington.casino.games.blackjack;

import com.github.zipcodewilmington.casino.CasinoAccount;
import java.util.Random;

public class BlackjackNPC extends BlackjackPlayer {
    
    public enum Strategy {
        CONSERVATIVE,
        AGGRESSIVE,
        RANDOM
    }
    
    private Strategy strategy;
    private Random random;

    public BlackjackNPC(CasinoAccount account, Strategy strategy) {
        super(account);
        this.strategy = strategy;
        this.random = new Random();
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public boolean shouldHit(int dealerUpCardValue) {
        int handValue = getHandValue();
        
        switch (strategy) {
            case CONSERVATIVE:
                if (getHand().isSoft()) {
                    return handValue < 18;
                }
                return handValue < 17;
                
            case AGGRESSIVE:
                if (getHand().isSoft()) {
                    return handValue < 19;
                }
                if (dealerUpCardValue >= 7) {
                    return handValue < 17;
                }
                return handValue < 12;
                
            case RANDOM:
                if (handValue >= 21) {
                    return false;
                }
                if (handValue <= 11) {
                    return true;
                }
                return random.nextBoolean();
                
            default:
                return handValue < 17;
        }
    }

    public boolean shouldDoubleDown(int dealerUpCardValue) {
        if (getHand().size() != 2) {
            return false;
        }
        
        if (!canAffordBet(getCurrentBet())) {
            return false;
        }
        
        int handValue = getHandValue();
        
        switch (strategy) {
            case CONSERVATIVE:
                return false;
                
            case AGGRESSIVE:
                if (handValue == 11) {
                    return true;
                }
                if (handValue == 10 && dealerUpCardValue < 10) {
                    return true;
                }
                if (getHand().isSoft() && handValue >= 16 && handValue <= 18) {
                    return dealerUpCardValue >= 4 && dealerUpCardValue <= 6;
                }
                return false;
                
            case RANDOM:
                return random.nextInt(100) < 30;
                
            default:
                return false;
        }
    }

    public boolean shouldSplit(int dealerUpCardValue) {
        if (!getHand().canSplit()) {
            return false;
        }
        
        if (!canAffordBet(getCurrentBet())) {
            return false;
        }
        
        switch (strategy) {
            case CONSERVATIVE:
                return false;
                
            case AGGRESSIVE:
                return true;
                
            case RANDOM:
                return random.nextBoolean();
                
            default:
                return false;
        }
    }

    public int decideBetAmount(int minBet, int maxBet) {
        int accountBalance = getArcadeAccount().getAccountBalance().intValue();
        
        switch (strategy) {
            case CONSERVATIVE:
                return minBet;
                
            case AGGRESSIVE:
                int aggressiveBet = Math.min(maxBet, accountBalance / 10);
                return Math.max(minBet, aggressiveBet);
                
            case RANDOM:
                int affordableMax = Math.min(maxBet, accountBalance);
                if (affordableMax <= minBet) {
                    return minBet;
                }
                return minBet + random.nextInt(affordableMax - minBet + 1);
                
            default:
                return minBet;
        }
    }

    public String getStrategyName() {
        switch (strategy) {
            case CONSERVATIVE:
                return "Conservative";
            case AGGRESSIVE:
                return "Aggressive";
            case RANDOM:
                return "Random";
            default:
                return "Unknown";
        }
    }
}