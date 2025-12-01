package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.utils.Card;
import java.util.List;

public class TableRenderer {
    
    private static final int TABLE_WIDTH = 70;

    public static void renderTable(List<PokerPlayer> players, List<Card> communityCards, 
            double potSize, double currentBet, PokerPlayer humanPlayer) {
            
            // CardRenderer.clearScreen();
            StringBuilder table = new StringBuilder();

            table.append("╔").append("═".repeat(TABLE_WIDTH - 2)).append("╗\n");
            table.append("║").append(center("TEXAS HOLD'EM POKER", TABLE_WIDTH - 2)).append("║\n");
            table.append("╠").append("═".repeat(TABLE_WIDTH - 2)).append("╣\n");

            table.append(renderNPCs(players, humanPlayer));
            table.append(renderCommunityCards(communityCards));

            table.append("║").append(center(String.format("POT: $%.2f", potSize), TABLE_WIDTH - 2)).append("║\n");
                if (currentBet > 0) {
                    table.append("║").append(center(String.format("Current Bet: $%.2f", currentBet), TABLE_WIDTH - 2)).append("║\n");
            }
            table.append("║").append(" ".repeat(TABLE_WIDTH - 2)).append("║\n");

            table.append(renderPlayerHand(humanPlayer));
            table.append("╚").append("═".repeat(TABLE_WIDTH - 2)).append("╝\n");

            System.out.println(table.toString());
        }

    private static String renderNPCs(List<PokerPlayer> players, PokerPlayer humanPlayer) {

        StringBuilder npcs = new StringBuilder();
        StringBuilder npcLine1 = new StringBuilder("║  ");
        StringBuilder npcLine2 = new StringBuilder("║  ");
        StringBuilder npcLine3 = new StringBuilder("║  ");

        int npcCount = 0;
        for (PokerPlayer player : players) {
            if (player.equals(humanPlayer))
                continue;

            String name = truncate(player.getName(), 15);
            String status = getPlayerStatus(player);
            String balance = String.format("$%.0f", player.getBalance());

            npcLine1.append(padRight(name, 18));
            npcLine2.append(padRight(status, 18));
            npcLine3.append(padRight(balance, 18));

            npcCount++;
            if (npcCount >= 4)
                break;
        }

        npcs.append(padRight(npcLine1.toString(), TABLE_WIDTH - 1)).append("║\n");
        npcs.append(padRight(npcLine2.toString(), TABLE_WIDTH - 1)).append("║\n");
        npcs.append(padRight(npcLine3.toString(), TABLE_WIDTH - 1)).append("║\n");
        npcs.append("║").append(" ".repeat(TABLE_WIDTH - 2)).append("║\n");

        return npcs.toString();
    }

    private static String renderCommunityCards(List<Card> communityCards) {

        StringBuilder cards = new StringBuilder();

        cards.append("║").append(center("COMMUNITY CARDS", TABLE_WIDTH - 2)).append("║\n");
        cards.append("║").append(" ".repeat(TABLE_WIDTH - 2)).append("║\n");

        if (communityCards.isEmpty()) {
            cards.append("║").append(center("(No cards dealt yet)", TABLE_WIDTH - 2)).append("║\n");
        } else {
            int hiddenCount = 5 - communityCards.size();
            String cardArt = CardRenderer.renderCardsWithHidden(communityCards, hiddenCount);
            
            String[] cardLines = cardArt.split("\n");
            for (String line : cardLines) {
                cards.append("║").append(center(line, TABLE_WIDTH - 2)).append("║\n");
            }
        }
        cards.append("║").append(" ".repeat(TABLE_WIDTH - 2)).append("║\n");

        return cards.toString();
    }

    private static String renderPlayerHand(PokerPlayer humanPlayer) {
        
        StringBuilder hand = new StringBuilder();

        hand.append("║").append(center("YOUR HAND (" + humanPlayer.getName() + ")", TABLE_WIDTH - 2)).append("║\n");
        hand.append("║").append(" ".repeat(TABLE_WIDTH - 2)).append("║\n");
        
        List<Card> holeCards = humanPlayer.getHand().getHoleCards();
        
        if (holeCards.isEmpty()) {
            hand.append("║").append(center("(No cards yet)", TABLE_WIDTH - 2)).append("║\n");
        } else {
            String cardArt = CardRenderer.renderCardsHorizontal(holeCards);
            String[] cardLines = cardArt.split("\n");
            for (String line : cardLines) {
                hand.append("║").append(center(line, TABLE_WIDTH - 2)).append("║\n");
            }
        }

        String balanceInfo = String.format("Balance: $%.2f   Current Bet: $%.2f", 
                                          humanPlayer.getBalance(), 
                                          humanPlayer.getCurrentBet());
        hand.append("║").append(center(balanceInfo, TABLE_WIDTH - 2)).append("║\n");
    
        if (humanPlayer.getHand().getAllCards().size() >= 5) {
            try {
                HandRank bestHand = humanPlayer.getHand().getBestHand();
                String handDesc = "Best Hand: " + bestHand.getDescription();
                hand.append("║").append(center(handDesc, TABLE_WIDTH - 2)).append("║\n");
            } catch (Exception e) {
                //skip
            }
        }
        hand.append("║").append(" ".repeat(TABLE_WIDTH - 2)).append("║\n");
        
        return hand.toString();
    }

    private static String getPlayerStatus(PokerPlayer player) {
        if (player.isFolded()) {
            return "[FOLDED]";
        } else if (player.isAllIn()) {
            return "[ALL-IN]";
        } else if (player.getCurrentBet() > 0) {
            return "[BETTING]";
        } else {
            return "[ACTIVE]";
        }
    }

    private static String center(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }

        int padding = (width - text.length()) / 2;
        int extraPadding = (width - text.length()) % 2;

        return " ".repeat(padding) + text + " ".repeat(padding + extraPadding);
    }

    private static String padRight(String str, int length) {
        if (str.length() >= length) {
            return str.substring(0, length);
        }
        return str + " ".repeat(length - str.length());
    }

    private static String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 2) + "..";
    }

    public static void renderSeperator() {
        System.out.println("\n" + "=".repeat(TABLE_WIDTH) + "\n");
    }

    public static void renderMessage(String message) {
        System.out.println("╔" + "═".repeat(TABLE_WIDTH - 2) + "╗");
        System.out.println("║" + center(message, TABLE_WIDTH - 2) + "║");
        System.out.println("╚" + "═".repeat(TABLE_WIDTH - 2) + "╝");
    }
}