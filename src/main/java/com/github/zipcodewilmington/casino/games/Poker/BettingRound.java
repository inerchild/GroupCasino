package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.utils.IOConsole;
import com.github.zipcodewilmington.utils.AnsiColor;
import java.util.List;


public class BettingRound {
    
    private final IOConsole console;
    private double currentBet;
    private double minimumRaise;
    private final double smallBlind;

    public BettingRound(double smallBlind) {
        this.console = new IOConsole(AnsiColor.PURPLE);
        this.currentBet = 0;
        this.minimumRaise = smallBlind * 2;
        this.smallBlind = smallBlind;
    }

    public void runBettingRound(List<PokerPlayer> players, PotManager potManager) {
        for (PokerPlayer player : players) {
            player.resetCurrentBet();
        }

        boolean bettingComplete = false;

        while (!bettingComplete) {
            bettingComplete = true;

            for (PokerPlayer player : players) {
                if (player.isFolded() || player.isAllIn()) {
                    continue;
                }

                if (player.getCurrentBet() < currentBet || currentBet == 0) {
                    PlayerAction action = getPlayerAction(player, potManager);
                    processAction(player, action, potManager);

                    if (action == PlayerAction.RAISE) {
                        bettingComplete = false;
                    }
                }
            }

            int activePlayers = countActivePlayers(players);
            if (activePlayers <= 1) {
                break;
            }
        }
    }

    private PlayerAction getPlayerAction(PokerPlayer player, PotManager potManager) {
        if (player instanceof NPCPlayer) {
            NPCPlayer npc = (NPCPlayer) player;
            return npc.makeDecision(currentBet, potManager.getTotalPotSize());
        } else {
            return getHumanPlayerAction(player);
        }
    }

    private PlayerAction getHumanPlayerAction(PokerPlayer player) {

        console.println("\n" + player.getName() + "'s turn");
        console.println("Your balance: $" + String.format("%.2f", player.getBalance()));
        console.println("Current bet to match: $" + String.format("%.2f", currentBet));
        console.println("Your current bet: $" + String.format("%.2f", player.getCurrentBet()));

        double amountToCall = player.getAmountToCall(currentBet);
        StringBuilder options = new StringBuilder("Options: ");

        if (currentBet == 0) {
            options.append("[C]heck, [R]aise, [F]old, [A]ll-in");
        } else {
            options.append("[F]old");
            if (amountToCall > 0 && amountToCall < player.getBalance()) {
                options.append(", [C]all $").append(String.format("%.2f", amountToCall));
            }
            if (player.getBalance() > amountToCall) {
                options.append(", [R]aise");
            }
            options.append(", [A]ll-in");
        }
        console.println(options.toString());
        String input = console.getStringInput("Enter action: ").toUpperCase();

        switch (input) {
            case "F":
                return PlayerAction.FOLD;
            case "C":
                return (currentBet == 0) ? PlayerAction.CHECK : PlayerAction.CALL;
            case "R":
                return PlayerAction.RAISE;
            case "A":
                return PlayerAction.ALL_IN;
            default:
                console.println("Invalid input. Checking.");
                return PlayerAction.CHECK;
        }
    }

    private void processAction(PokerPlayer player, PlayerAction action, PotManager potManager) {
        switch (action) {
            case FOLD:
                player.fold();
                console.println(player.getName() + " folds.");
                break;
            case CHECK:
                console.println(player.getName() + " checks.");
                break;
            case CALL:
                double amountToCall = player.getAmountToCall(currentBet);
                player.placeBet(amountToCall);
                console.println(player.getName() + " calls $" + String.format("%.2f", amountToCall));
                break;
            case RAISE:
                double raiseAmount = getRaiseAmount(player, potManager);
                player.placeBet(raiseAmount);
                currentBet = player.getCurrentBet();
                minimumRaise = raiseAmount;
                console.println(player.getName() + " raises to $" + String.format("%.2f", currentBet));
                break;
            case ALL_IN:
                double allInAmount = player.getBalance();
                player.placeBet(allInAmount);
                if (player.getCurrentBet() > currentBet) {
                    currentBet = player.getCurrentBet();
                }
                console.println(player.getName() + " goes ALL-IN with $" + String.format("%.2f", allInAmount));
                break;
        }
    }

    private double getRaiseAmount(PokerPlayer player, PotManager potManager) {
        if (player instanceof NPCPlayer) {
            NPCPlayer npc = (NPCPlayer) player;
            return npc.calculateRaiseAmount(currentBet, potManager.getTotalPotSize());
        } else {
            double minRaise = currentBet + minimumRaise;
            double maxRaise = player.getBalance();

            console.println("Minimum raise: $" + String.format("%.2f", minRaise));
            console.println("Maximum raise: $" + String.format("%.2f", maxRaise));

            double raiseAmount = console.getDoubleInput("Enter raise amount: ");

            if (raiseAmount < minRaise) {
                console.println("Raise too small. Raising to minimum.");
                raiseAmount = minRaise;
            }
            if (raiseAmount > maxRaise) {
                console.println("Raise too large. Going all-in.");
                raiseAmount = maxRaise;
            }
            return raiseAmount;
        }
    }

    private int countActivePlayers(List<PokerPlayer> players) {
        int count = 0;
        for (PokerPlayer player : players) {
            if (!player.isFolded()) {
                count++;
            }
        }
        return count;
    }

    public void reset() {
        currentBet = 0;
        minimumRaise = smallBlind * 2;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(double amount) {
        this.currentBet = amount;
    }
}
