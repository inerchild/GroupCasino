package com.github.zipcodewilmington.casino.games.Poker;
import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.utils.AnsiColor;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Deck;
import com.github.zipcodewilmington.utils.IOConsole;

import java.util.ArrayList;
import java.util.List;


public class PokerGame implements GameInterface {

    private final List<PokerPlayer> players;
    private final List<Card> communityCards;
    private final IOConsole console;
    private final Deck deck;
    private final PotManager potManager;
    private final BettingRound bettingRound;
    private final double smallBlind;
    private int dealerPosition;
    
    public PokerGame() {
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.console = new IOConsole(AnsiColor.PURPLE);
        this.deck = new Deck();
        this.potManager = new PotManager();
        this.smallBlind = 10.0;
        this.bettingRound = new BettingRound(smallBlind);
        this.dealerPosition = 0;
    }

    @Override
    public void add(PlayerInterface player) {
        if (player instanceof PokerPlayer) {
            players.add((PokerPlayer) player);
        }
    }

    @Override
    public void remove(PlayerInterface player) {
        players.remove(player);
    }

    @Override
    public void run() {
        console.println("=== TEXAS HOLD'EM POKER ===");

        setupNPCs();

        if (players.isEmpty()) {
            console.println("No players! Cannot start game.");
            return;
        }

        boolean playing = true;

        while (playing) {
            playHand();

            eliminateBrokePlayers();

            if (players.size() < 2) {
                console.println("Not enough players to continue!");
                break;
            }

            String input = console.getStringInput("Play another hand? (y/n): ");
            if (!input.equalsIgnoreCase("y")) {
                playing = false;
            }

            dealerPosition = (dealerPosition + 1) % players.size();
        }

        console.println("Thanks for playing!");
    }

    private void setupNPCs() {
        int minPlayers = 3 + (int)(Math.random() * 3);
        int npcsNeeded = Math.max(0, minPlayers - players.size());

        String[] npcNames = {
            "AggroBot", "BluffMaster", "The Shark", "Chip Leader",
            "CheapoBot", "Mr. Fold", "The Rock", "Tight Eddie",
            "WildCardBot", "Crazy Carl", "Random Randy", "The Gambler",
            "All-In Annie", "Call Station", "Big Blind Bob", "The Professor"
        };
        
        List<String> usedNames = new ArrayList<>();

        for (int i = 0; i < npcsNeeded; i++) {
            NPCPlayer.Personality personality;
            String name;

            switch (i % 3) {
                case 0:
                    personality = NPCPlayer.Personality.AGGRESSIVE;
                    break;
                case 1:
                    personality = NPCPlayer.Personality.CONSERVATIVE;
                    break;
                default:
                    personality = NPCPlayer.Personality.RANDOM;
                    break;
            }
            do {
                name = npcNames[(int)(Math.random() * npcNames.length)];
            } while (usedNames.contains(name));
            
            usedNames.add(name);
            players.add(new NPCPlayer(name + " " + (i + 1), personality));
        }
        console.println("Players in game: " + players.size());
        for (PokerPlayer player : players) {
            console.println("  - " + player.getName() + " ($" + String.format("%.2f", player.getBalance()) + ")");
        }
    }

    private void playHand() {
        console.println("\n=== NEW HAND ===");

        deck.shuffle();
        communityCards.clear();
        potManager.reset();
        bettingRound.reset();

        for (PokerPlayer player : players) {
            player.resetForNewHand();
        }

        postBlinds();
        dealHoleCards();

        TableRenderer.renderMessage("PRE-FLOP");
        showTable();
        bettingRound.runBettingRound(players, potManager);

        if (isHandOver()) {
            endHand();
            return;
        }

        TableRenderer.renderMessage("FLOP");
        deck.drawCard(); 
        dealCommunityCards(3);
        showTable();
        bettingRound.reset();
        bettingRound.runBettingRound(players, potManager);

        if (isHandOver()) {
            endHand();
            return;
        }

        TableRenderer.renderMessage("TURN");
        deck.drawCard(); 
        dealCommunityCards(1);
        showTable();
        bettingRound.reset();
        bettingRound.runBettingRound(players, potManager);

        if (isHandOver()) {
            endHand();
            return;
        }

        TableRenderer.renderMessage("RIVER");
        deck.drawCard(); 
        dealCommunityCards(1);
        showTable();
        bettingRound.reset();
        bettingRound.runBettingRound(players, potManager);

        TableRenderer.renderMessage("SHOWDOWN");
        showTable();
        endHand();
    }

    private void postBlinds() {
        int smallBlindPos = (dealerPosition + 1) % players.size();
        int bigBlindPos = (dealerPosition + 2) % players.size();

        console.println("Dealer: " + players.get(dealerPosition).getName());

        PokerPlayer smallBlindPlayer = players.get(smallBlindPos);
        PokerPlayer bigBlindPlayer = players.get(bigBlindPos);

        console.println("Small Blind: " + smallBlindPlayer.getName() + " posts $" + smallBlind);
        console.println("Big Blind: " + bigBlindPlayer.getName() + " posts $" + (smallBlind * 2));

        smallBlindPlayer.placeBet(smallBlind);
        bigBlindPlayer.placeBet(smallBlind * 2);

        bettingRound.setCurrentBet(smallBlind * 2);
    }

    private void dealHoleCards() {
        for (int i = 0; i < 2; i++) {
            for (PokerPlayer player : players) {
                Card card = deck.drawCard();
                player.receiveHoleCard(card);
            }
        }
    }

    private void dealCommunityCards(int count) {
        for (int i = 0; i < count ; i++) {
            Card card = deck.drawCard();
            communityCards.add(card);

            for(PokerPlayer player : players) {
                player.getHand().addCommunityCard(card);
            }
        }
    }

    private void showTable() {
        PokerPlayer humanPlayer = getHumanPlayer();
            if (humanPlayer != null) {
                TableRenderer.renderTable(
                    players, 
                    communityCards, 
                    getCurrentPotSize(),
                    bettingRound.getCurrentBet(), 
                    humanPlayer
            );
        }
    }


    private boolean isHandOver() {
        int activePlayers = 0;
        for (PokerPlayer player : players) {
            if (!player.isFolded()) {
                activePlayers++;
            }
        }
        return activePlayers <= 1;
    }

    private void endHand() {
        potManager.collectBets(players); 

        console.println("\n" + potManager.toString());

        for (int i = 0; i < potManager.getNumberOfPots(); i++) {
            List<PokerPlayer> eligiblePlayers = potManager.getEligiblePlayers(i);

            if (eligiblePlayers.isEmpty()) {
                continue;
            }

            if (eligiblePlayers.size() == 1) {
                console.println(eligiblePlayers.get(0).getName() + " wins pot " + (i + 1) + "!");
                potManager.awardPot(i, eligiblePlayers);
                continue;
            }
            
            List<PokerPlayer> winners = determineWinners(eligiblePlayers);

            if (winners.size() == 1) {
                PokerPlayer winner = winners.get(0);
                console.println(winner.getName() + " wins pot " + (i + 1) + " with " + 
                        winner.getHand().getBestHand().getDescription() + "!");
            } else {
                console.println("Pot " + (i + 1) + " split between:");
                for (PokerPlayer winner : winners) {
                    console.println("  - " + winner.getName() + " with " + 
                            winner.getHand().getBestHand().getDescription());
                }
            }
            potManager.awardPot(i, winners);
        }
        console.println("\nFinal Balances:");
        for (PokerPlayer player : players) {
            console.println(" " + player.getName() + ": $" + String.format("%.2f", player.getBalance()));
        }
    }

    private List<PokerPlayer> determineWinners(List<PokerPlayer> eligiblePlayers) {
        List<PokerPlayer> winners = new ArrayList<>();
        HandRank bestHand = null;

        for (PokerPlayer player : eligiblePlayers) {
            if (player.isFolded()) {
                continue;
            }

            HandRank playerHand = player.getHand().getBestHand();

            if (bestHand == null || playerHand.compareTo(bestHand) > 0) {
                winners.clear();
                winners.add(player);
                bestHand = playerHand;
            } else if (playerHand.compareTo(bestHand) == 0) {
                winners.add(player);
            }
        }
        return winners;
    }

    private void eliminateBrokePlayers() {
        players.removeIf(player -> !player.hasChips());
    }

    private PokerPlayer getHumanPlayer() {
        for (PokerPlayer player : players) {
            if (!(player instanceof NPCPlayer)) {
                return player;
            }
        }
        return players.isEmpty() ? null : players.get(0);
    }

    private double getCurrentPotSize() {
        double total = potManager.getTotalPotSize();
        
        for (PokerPlayer player : players) {
            total += player.getTotalBet();
        }
        
        return total;
    }
}
