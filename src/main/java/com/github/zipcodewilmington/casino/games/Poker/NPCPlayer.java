package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.casino.CasinoAccount;
import java.util.Random;

public class NPCPlayer extends PokerPlayer {
    
    public enum Personality {
        AGGRESSIVE,
        CONSERVATIVE,
        RANDOM
    }

    private final Personality personality;
    private final Random random;

    public NPCPlayer(String name, Personality personality) {                    // constructor
        super(createNPCAccount(name));
        this.personality = personality;
        this.random = new Random();
    }

    private static CasinoAccount createNPCAccount(String name) {                // Creates NPC with a name and account with a randomized balance
        CasinoAccount account = new CasinoAccount(name, "npc_password");

        double randomBalance = 500 + (Math.random() * 1000);
        account.setAccountBalance(randomBalance);
        return account;
    }

    public Personality getPersonality() {                               // Gets the NPCs personality type
        return personality;
    }

    public PlayerAction makeDecision(double currentBetToMatch, double potSize) {            // AI decision making method
        double handStrength = evaluateHandStrength();                                       // calculates hand strength
        double amountToCall = getAmountToCall(currentBetToMatch);                           // amount needed to call

        if (!hasChips()) {                      // cant play with no money :(
            return PlayerAction.FOLD;
        }

        if (isAllIn()) {                        // checks if already all-in
            return PlayerAction.CHECK;  
        }

        switch (personality) {                  // switch case for personality
            case AGGRESSIVE:
                return makeAggressiveDecision(handStrength, amountToCall, potSize);

            case CONSERVATIVE:
                return makeConservativeDecision(handStrength, amountToCall, potSize);

            case RANDOM:
                return makeRandomDecision(amountToCall);

            default:
                return PlayerAction.FOLD;
        }
    }

    private PlayerAction makeAggressiveDecision(double handStrength, double amountToCall, double potSize) {

        if (amountToCall == 0) {                                        // if there is no bet to match, raise aggressively
            if (handStrength > 0.3 || random.nextDouble() < 0.5) {
                return PlayerAction.RAISE;                              // bet with medium + hands or bluff
            }
            return PlayerAction.CHECK;
        }

        if (handStrength > 0.6) {                           // when facing a bet
            return PlayerAction.RAISE;                                  // raise if strong hand
        } else if (handStrength > 0.3 || random.nextDouble() < 0.4) {
            return amountToCall >= getBalance() ? PlayerAction.ALL_IN : PlayerAction.CALL;
        } else {
            return random.nextDouble() < 0.3 ? PlayerAction.CALL : PlayerAction.FOLD;       // sometimes call even on weak hands
        }
    }

    private PlayerAction makeConservativeDecision(double handStrength, double amountToCall, double potSize) {

        if (amountToCall == 0) {            // no bet to match
            if (handStrength > 0.7) {
                return PlayerAction.RAISE;  // only bet on strong hand
            }
            return PlayerAction.CHECK;
        }

        if (handStrength > 0.7) {               // when facing a bet, needs strong hand to continue
            return PlayerAction.RAISE;
        } else if (handStrength > 0.5) {
            return amountToCall >= getBalance() ? PlayerAction.ALL_IN : PlayerAction.CALL;
        } else {
            return PlayerAction.FOLD;           // fold all weak hands
        }
    }

    private PlayerAction makeRandomDecision(double amountToCall) {
        if (amountToCall == 0) {                                        // random action with no bet
            return random.nextBoolean() ? PlayerAction.CHECK : PlayerAction.RAISE;
        } else {
            int choice = random.nextInt(3);                             // random action against bet
            switch (choice) {
                case 0: return PlayerAction.FOLD;
                case 1: return amountToCall >= getBalance() ? PlayerAction.ALL_IN : PlayerAction.CALL;
                case 2: return PlayerAction.RAISE;
                default: return PlayerAction.FOLD;
            }
        }
    }

    private double evaluateHandStrength() {                 // aaisgns value to hands (0.0 to 1.0)
        if (getHand().getAllCards().size() < 5) {
            return 0.3;                                     // assumes mediocre pre-flop
        }

        try {
            HandRank handRank = getHand().getBestHand();

            switch (handRank.getHandType()) {
                case ROYAL_FLUSH: return 1.0;
                case STRAIGHT_FLUSH: return 0.95;
                case FOUR_OF_A_KIND: return 0.9;
                case FULL_HOUSE: return 0.85;
                case FLUSH: return 0.75;
                case STRAIGHT: return 0.65;
                case THREE_OF_A_KIND: return 0.55;
                case TWO_PAIR: return 0.45;
                case PAIR: return 0.35;
                case HIGH_CARD: return 0.2;
                default: return 0.3;
            }
        } catch (Exception e) {             // if evaluation fails, assume weak hand
            return 0.3;
        }
    }

    public double calculateRaiseAmount(double currentBet, double potSize) {         // calculates how much to raise based on personality type
        double raiseAmount;

        switch (personality) {
            case AGGRESSIVE:
                raiseAmount = potSize * (0.5 + (random.nextDouble() * 0.5));        // aggressive raises by 50-100% of pot
                break;

            case CONSERVATIVE:
                raiseAmount = potSize * (0.25 + (random.nextDouble() * 0.25));      // conservative raise by 25-50% of pot
                break;

            case RANDOM:
                raiseAmount = potSize * (0.1 + (random.nextDouble() * 1.4));        // random raises by 10-150% of pot
                break;

            default:
                raiseAmount = potSize * 0.5;
        }

        double minimumRaise = Math.max(currentBet * 2, 10.0);               // minimum raise is double the current bet (or small blind if no bet)
        raiseAmount = Math.max(raiseAmount, minimumRaise);
        raiseAmount = Math.min(raiseAmount, getBalance());                  // cant raise more than baance

        return raiseAmount;
    }

    @Override
    public String toString() {
            return String.format("%s [%s] (Balance: $%.2f, Current Bet: $%.2f, Folded: %s)", 
            getName(), personality, getBalance(), getCurrentBet(), isFolded());
    }
}
