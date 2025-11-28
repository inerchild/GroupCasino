package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.utils.Card;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    
    private final List<Card> holeCards;
    private final List<Card> communityCards;
    private final HandEvaluator evaluator;

    public Hand() {
        this.holeCards = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.evaluator = new HandEvaluator();
    }

    public void addHoleCard(Card card) {
        if (holeCards.size() >= 2) {
            throw new IllegalStateException("Cannot add more than 2 hole cards");
        }
        holeCards.add(card);
    }

    public void addCommunityCard(Card card) {
        if (communityCards.size() >= 5) {
            throw new IllegalStateException("Cannot add more than 5 community cards");
        }
        communityCards.add(card);
    }

    public List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(holeCards);
        allCards.addAll(communityCards);
        return allCards;
    }

    public List<Card> getHoleCards() {
        return new ArrayList<>(holeCards);
    }

    public List<Card> getCommunityCards() {
        return new ArrayList<>(communityCards);
    }

    public HandRank getBestHand() {
        List<Card> allCards = getAllCards();

        if (allCards.size() < 5) {
            throw new IllegalStateException("Need at least 5 cards to evaluate hand");
        }

        if (allCards.size() == 5) {
            return evaluator.evaluateHand(allCards);
        }
        return findBestCombination(allCards);
    }

    private HandRank findBestCombination(List<Card> cards) {
        List<List<Card>> combinations = generateCombinations(cards, 5);

        HandRank bestHand = null;

        for (List<Card> combo : combinations) {
            HandRank currentHand = evaluator.evaluateHand(combo);

            if (bestHand == null || currentHand.compareTo(bestHand) > 0) {
                bestHand = currentHand;
            }
        }
        return bestHand;
    }

    private List<List<Card>> generateCombinations(List<Card> cards, int k) {
        List<List<Card>> result = new ArrayList<>();
        generateCombinationsHelper(cards, k, 0, new ArrayList<>(), result);

        return result;
    }

    private void generateCombinationsHelper(List<Card> cards, int k, int start, 
        List<Card> current, List<List<Card>> result) {

            if (current.size() == k) {
                result.add(new ArrayList<>(current));
                return;
            }
            for (int i = start; i < cards.size(); i++) {
                current.add(cards.get(i));
                generateCombinationsHelper(cards, k, i + 1, current, result);
                current.remove(current.size() - 1);
        }
    }

    public void clear() {
        holeCards.clear();
        communityCards.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hole Cards: ");
        if (holeCards.isEmpty()) {
            sb.append("None");
        } else {
            for (int i = 0; i < holeCards.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(holeCards.get(i));
            }
        }
        sb.append(" | Community Cards: ");
        if (communityCards.isEmpty()) {
            sb.append("None");
        } else {
            for (int i = 0; i < communityCards.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(communityCards.get(i));
            }
        }
        return sb.toString();
    }
}
