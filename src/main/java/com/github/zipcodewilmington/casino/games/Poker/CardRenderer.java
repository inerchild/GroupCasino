package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import java.util.ArrayList;
import java.util.List;

public class CardRenderer {
    
    public static String renderCard(Card card) {
        String rank = getRankString(card.getRank());
        String suit = getSuitSymbol(card.getSuit());

        List<String> lines = new ArrayList<>();
        lines.add("┌─────────┐");
        lines.add("│" + padRight(rank, 9) + "│");
        lines.add("│         │");
        lines.add("│    " + suit + "    │");
        lines.add("│         │");
        lines.add("│" + padLeft(rank, 9) + "│");
        lines.add("└─────────┘");
        
        return String.join("\n", lines);
    }

    public static String renderHiddenCard() {
        List<String> lines = new ArrayList<>();
        lines.add("┌─────────┐");
        lines.add("│░░░░░░░░░│");
        lines.add("│░░░░░░░░░│");
        lines.add("│░░░░░░░░░│");
        lines.add("│░░░░░░░░░│");
        lines.add("│░░░░░░░░░│");
        lines.add("└─────────┘");
        
        return String.join("\n", lines);
    }

    public static String renderCardsHorizontal(List<Card> cards) {
        if (cards.isEmpty()) {
            return "";
        }

        List<List<String>> cardLines = new ArrayList<>();
        for (Card card : cards) {
            String rendered = renderCard(card);
            cardLines.add(List.of(rendered.split("\n")));
        }

        StringBuilder result = new StringBuilder();
        int numLines = 7;

        for (int i = 0; i < numLines; i++) {
            for (List<String> card : cardLines) {
                result.append(card.get(i));
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String renderCardsWithHidden(List<Card> visibleCards, int hiddenCount) {
        List<String> allCardStrings = new ArrayList<>();

        for (Card card : visibleCards) {
            allCardStrings.add(renderCard(card));
        }

        for (int i = 0; i < hiddenCount; i++) {
        allCardStrings.add(renderHiddenCard());
        }

        if (allCardStrings.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int numLines = 7;

        for (int i = 0; i < numLines; i++) {
            for (String cardStr : allCardStrings) {
                String[] lines = cardStr.split("\n");
                result.append(lines[i]);
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    private static String getRankString(Rank rank){
        switch (rank) {
            case ACE:   return "A";
            case TWO:   return "2";
            case THREE: return "3";
            case FOUR:  return "4";
            case FIVE:  return "5";
            case SIX:   return "6";
            case SEVEN: return "7";
            case EIGHT: return "8";
            case NINE:  return "9";
            case TEN:   return "10";
            case JACK:  return "J";
            case QUEEN: return "Q";
            case KING:  return "K";
            default:    return "?";
        }
    }

    private static String getSuitSymbol(Suit suit) {
        switch (suit) {
            case SPADES:   return "♠";
            case HEARTS:   return "♥";
            case DIAMONDS: return "♦";
            case CLUBS:    return "♣";
            default:       return "?";
        }
    }

    private static String padRight(String str, int length) {
        if (str.length() >= length) {
            return str;
        }
        return str + " ".repeat(length - str.length());
    }

    private static String padLeft(String str, int length) {
        if (str.length() >= length) {
            return str;
        }
        return " ".repeat(length - str.length()) + str;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
 }
