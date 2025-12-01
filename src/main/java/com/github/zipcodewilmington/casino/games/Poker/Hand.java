package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.utils.Card;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    
    private final List<Card> holeCards;                     // <---- players 2 private cards
    private final List<Card> communityCards;                // <---- Shared cards (flop turn river)
    private final HandEvaluator evaluator;

    public Hand() {                                         // ***** Constructor initializes empty hand
        this.holeCards = new ArrayList<>();                     
        this.communityCards = new ArrayList<>();                
        this.evaluator = new HandEvaluator();
    }

    public void addHoleCard(Card card) {                    // **** Adds a hole card (2 max)
        if (holeCards.size() >= 2) {
            throw new IllegalStateException("Cannot add more than 2 hole cards");
        }
        holeCards.add(card);
    }

    public void addCommunityCard(Card card) {               // ***** Adds a community card (5 max)
        if (communityCards.size() >= 5) {
            throw new IllegalStateException("Cannot add more than 5 community cards");
        }
        communityCards.add(card);
    }

    public List<Card> getAllCards() {                       // **** Gets all cards (hole and community) for comparison
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(holeCards);
        allCards.addAll(communityCards);
        return allCards;
    }

    public List<Card> getHoleCards() {                      // **** Gets the players hole cards
        return new ArrayList<>(holeCards);
    }

    public List<Card> getCommunityCards() {                 // **** Gets the community cards
        return new ArrayList<>(communityCards);
    }

    public HandRank getBestHand() {                        // ***** returns the best hand from the 7 cards gathered
        List<Card> allCards = getAllCards();

        if (allCards.size() < 5) {
            throw new IllegalStateException("Need at least 5 cards to evaluate hand");
        }

        if (allCards.size() == 5) {
            return evaluator.evaluateHand(allCards);
        }
        return findBestCombination(allCards);
    }

    private HandRank findBestCombination(List<Card> cards) {            // **** Finds best 5-card combo from a list of cards
        List<List<Card>> combinations = generateCombinations(cards, 5);

        HandRank bestHand = null;

        for (List<Card> combo : combinations) {                     // <---- evaluates each 5 card combo
            HandRank currentHand = evaluator.evaluateHand(combo);

            if (bestHand == null || currentHand.compareTo(bestHand) > 0) {      // <----- keeps track of best hand so far
                bestHand = currentHand;
            }
        }
        return bestHand;
    }

    private List<List<Card>> generateCombinations(List<Card> cards, int k) {        // ***** Generates all possible combos of 'k' cards from list
                                                                                    // uses recursion to find all combos
        List<List<Card>> result = new ArrayList<>();
        generateCombinationsHelper(cards, k, 0, new ArrayList<>(), result);

        return result;
    }

    private void generateCombinationsHelper(List<Card> cards, int k, int start,     // **** Helper method for generating combos
        List<Card> current, List<List<Card>> result) {

            if (current.size() == k) {                                  // <----- best case: if we've chosed k cards, add this combo to results
                result.add(new ArrayList<>(current));
                return;
            }
            for (int i = start; i < cards.size(); i++) {                // <------ trys adding each remaining card
                current.add(cards.get(i));                              // <------ choose this card
                generateCombinationsHelper(cards, k, i + 1, current, result);       // <------ recurse
                current.remove(current.size() - 1);                     // <----- backtrack (undo choice)
        }
    }

    public void clear() {                               // ***** Clears all cards from hand (new round or hand)
        holeCards.clear();
        communityCards.clear();
    }

    @Override
    public String toString() {                          // **** Returns string that represents hand, shows hole cards and community cards seperately
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
