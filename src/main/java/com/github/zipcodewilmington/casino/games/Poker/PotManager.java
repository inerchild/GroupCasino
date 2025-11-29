package com.github.zipcodewilmington.casino.games.Poker;

import java.util.ArrayList;
import java.util.List;

public class PotManager {
    private static class Pot {
        private double amount;
        private final List<PokerPlayer> eligiblePlayers;

        public Pot() {
            this.amount = 0;
            this.eligiblePlayers = new ArrayList<>();
        }

        public void addAmount(double amount) {
            this.amount += amount;
        }

        public double getAmount() {
            return amount;
        }

        public void addEligiblePlayer(PokerPlayer player) {
            if (!eligiblePlayers.contains(player)) {
                eligiblePlayers.add(player);
            }
        }

        public List<PokerPlayer> getEligiblePlayers() {
            return new ArrayList<>(eligiblePlayers);
        }

        public boolean isPlayerEligible(PokerPlayer player) {
            return eligiblePlayers.contains(player);
        }
    }

    private final List<Pot> pots;

    public PotManager() {
        this.pots = new ArrayList<>();
        this.pots.add(new Pot());
    }

    public void collectBets(List<PokerPlayer> players) {

        List<PokerPlayer> activePlayers = new ArrayList<>();
        for (PokerPlayer player : players) {
            if (!player.isFolded() && player.getTotalBet() > 0) {
                activePlayers.add(player);
            }
        }

        if (activePlayers.isEmpty()) {
            return;
        }

        activePlayers.sort((p1, p2) -> Double.compare(p1.getTotalBet(), p2.getTotalBet()));

        double previousBetLevel = 0;

        for (int i = 0; i < activePlayers.size(); i++) {
            PokerPlayer currentPlayer = activePlayers.get(i);
            double currentBetLevel = currentPlayer.getTotalBet();

            if (currentBetLevel > previousBetLevel) {
                double amountPerPlayer = currentBetLevel - previousBetLevel;

            Pot pot = (i == 0) ? pots.get(0) : createSidePot();

            for (int j = i; j < activePlayers.size(); j++) {
                PokerPlayer player = activePlayers.get(j);
                pot.addAmount(amountPerPlayer);
                pot.addEligiblePlayer(player);
            }

            previousBetLevel = currentBetLevel;
            }
        }
    }

    private Pot createSidePot() {
        Pot sidePot = new Pot();
        pots.add(sidePot);
        return sidePot;
    }

    public void awardPot(int potIndex, List<PokerPlayer> winners) {
        if (potIndex >= pots.size() || winners.isEmpty()) {
            return;
        }

        Pot pot = pots.get(potIndex);
        double amountPerWinner = pot.getAmount() / winners.size();

        for (PokerPlayer winner : winners) {
            winner.receiveWinnings(amountPerWinner);
        }
    }

    public double getTotalPotSize() {
        double total = 0;
        for (Pot pot : pots) {
            total += pot.getAmount();
        }
        return total;
    }

    public double getPotSize(int potIndex) {
        if (potIndex >= pots.size()) {
            return 0;
        }
        return pots.get(potIndex).getAmount();
    }

    public int getNumberOfPots() {
        return pots.size();
    }

    public List<PokerPlayer> getEligiblePlayers(int potIndex) {
        if (potIndex >= pots.size()) {
            return new ArrayList<>();
        }
        return pots.get(potIndex).getEligiblePlayers();
    }

    public boolean isPlayerEligible(int potIndex, PokerPlayer player) {
        if (potIndex >= pots.size()) {
            return false;
        }
        return pots.get(potIndex).isPlayerEligible(player);
    }

    public void reset() {
        pots.clear();
        pots.add(new Pot());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total Pot: $").append(String.format("%.2f", getTotalPotSize()));

        if (pots.size() > 1) {
            sb.append("\n  Main Pot: $").append(String.format("%.2f", pots.get(0).getAmount()));
            for (int i = 1; i < pots.size(); i++) {
                sb.append("\n  Side Pot ").append(i).append(": $")
                  .append(String.format("%.2f", pots.get(i).getAmount()));
            }
        }
        return sb.toString();
    }
}
