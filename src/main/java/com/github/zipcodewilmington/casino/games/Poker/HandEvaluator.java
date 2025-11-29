package com.github.zipcodewilmington.casino.games.Poker;

import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {                                               // ******Main evaluation method: takes 5 cards and returns a HandRank
    public HandRank evaluateHand(List<Card> cards) {
        if (cards == null || cards.size() != 5) {
            throw new IllegalArgumentException("Must provide exactly 5 cards to evaluate");
        }
        List<Card> sortedCards = new ArrayList<>(cards);                                            // Sort cards by descending rank
        sortedCards.sort((c1, c2) -> Integer.compare(                         
            c2.getRank().getValue(),
            c1.getRank().getValue()
        ));

        HandRank royalFlush = checkRoyalFlush(sortedCards);                         // all these methods check each type from best to worst
        if (royalFlush != null)
            return royalFlush;
        HandRank straightFlush = checkStraightFlush(sortedCards);
        if (straightFlush != null)
            return straightFlush;
        HandRank fourOfAKind = checkFourOfAKind(sortedCards);
        if (fourOfAKind != null)
            return fourOfAKind;
        HandRank fullHouse = checkFullHouse(sortedCards);
        if (fullHouse != null)
            return fullHouse;
        HandRank flush = checkFlush(sortedCards);
        if (flush != null)
            return flush;
        HandRank straight = checkStraight(sortedCards);
        if (straight != null)
            return straight;
        HandRank threeOfAKind = checkThreeOfAKind(sortedCards);
        if (threeOfAKind != null)
            return threeOfAKind;
        HandRank twoPair = checkTwoPair(sortedCards);
        if (twoPair != null)
            return twoPair;
        HandRank pair = checkPair(sortedCards);
        if (pair != null)
            return pair;
        return checkHighCard(sortedCards);
    }

    private HandRank checkRoyalFlush(List<Card> cards) {                                   // ***** CHECK FOR ROYAL FLUSH (A-K-Q-J-10 + same suit)
        if (!isFlush(cards) || !isStraight(cards)) {                 // <------ check first if straight flush
            return null;
        }
        if (cards.get(0).getRank().getValue() == 14) {          // <------ check if high card is an ace
            return new HandRank(HandType.ROYAL_FLUSH, Arrays.asList(14), new ArrayList<>());
        }
        return null;
    }

    private HandRank checkStraightFlush(List<Card> cards) {                     // ***** CHECK FOR STAIGHT FLUSH (5 consecutive cards of the same suit (not including royal))
        if (isFlush(cards) && isStraight(cards)) {
            int highCard = getHighCardOfStraight(cards);                // <------- high card of straight it primary value
            return new HandRank(HandType.STRAIGHT_FLUSH, Arrays.asList(highCard), new ArrayList<>());
        }
        return null;
    }

    private HandRank checkFourOfAKind(List<Card> cards) {                                                   // ****** CHECK FOR 4 OF A KIND (Four cards of same rank (K-K-K-K-5))
        Map<Integer, Integer> rankCounts = getRankCounts(cards);

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {          // <------ create a map that counts how many time each rank appears 
            if (entry.getValue() == 4) {                                                            // map would be 13: 4 (King: value 13) appears 4 times
                int quadValue = entry.getKey();                                                         // 5: 1 (Five (value 5) appears 1 time)
                int kicker = cards.stream()
                    .map(c -> c.getRank().getValue())
                    .filter(v -> v != quadValue)
                    .findFirst()
                    .orElse(0);

                return new HandRank(
                    HandType.FOUR_OF_A_KIND,
                    Arrays.asList(quadValue),
                    Arrays.asList(kicker)
                );    
            }
        }
        return null;
    }

    private HandRank checkFullHouse(List<Card> cards) {                                         // ****** CHECK FULL HOUSE (K-K-K-6-6)
        Map<Integer, Integer> rankCounts = getRankCounts(cards);
            Integer threeValue = null;
            Integer pairValue = null;

            for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
                if (entry.getValue() == 3) {                // <----- check for three of a kind
                    threeValue = entry.getKey();
                } else if (entry.getValue() == 2) {         // <----- check for two pair
                    pairValue = entry.getKey();
                }
            }

            if (threeValue != null && pairValue != null) {      // <----- if both, return full house
                return new HandRank(
                    HandType.FULL_HOUSE,
                    Arrays.asList(threeValue, pairValue),
                    new ArrayList<>()
                );
        }
        return null;
    }

    private HandRank checkFlush(List<Card> cards) {                 // *****CHECK FOR FLUSH (5 cards of same suit)
        if (isFlush(cards)) {
            List<Integer> values = cards.stream()           // <------ compare all 5 cards
                .map(c -> c.getRank().getValue())
                .collect(Collectors.toList());

            return new HandRank(
                HandType.FLUSH,
                Arrays.asList(values.get(0)),           // <------ highest card is primary, rest are kickers
                values.subList(1, 5)
            );
        }
        return null;
    }

    private HandRank checkStraight(List<Card> cards) {          // *******CHECK FOR STRAIGHT (2-3-4-5-6) ((Ace can be low or high))
        if (isStraight(cards)) {
            int highCard = getHighCardOfStraight(cards);
            return new HandRank(
                HandType.STRAIGHT,
                Arrays.asList(highCard),
                new ArrayList<>()
            );
        }
        return null;
    }

    private HandRank checkThreeOfAKind(List<Card> cards) {          // CHECK FOR 3 OF A KIND (K-K-K-2-7)
        Map<Integer, Integer> rankCounts = getRankCounts(cards);

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 3) {
                int threeValue = entry.getKey();        // <--- kickers are two cards not part of the 3

                List<Integer> kickers = cards.stream()
                    .map(c -> c.getRank().getValue())
                    .filter(v -> v != threeValue)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

                return new HandRank(
                    HandType.THREE_OF_A_KIND,
                    Arrays.asList(threeValue),
                    kickers
                );
            }
        }
        return null;
    }

    private HandRank checkTwoPair(List<Card> cards) {               // ******CHECK FOR TWO PAIR (K-K-J-J-4)
        Map<Integer, Integer> rankCounts = getRankCounts(cards);

        List<Integer> pairValues = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 2) {
                pairValues.add(entry.getKey());
            }
        }
        if (pairValues.size() == 2) {                   // <----- Sort pairs highest first
            pairValues.sort(Comparator.reverseOrder());
            int kicker = cards.stream()                 // <----- kicker is card not part of either pair
                .map(c -> c.getRank().getValue())
                .filter(v -> !pairValues.contains(v))
                .findFirst()
                .orElse(0);

            return new HandRank(
                HandType.TWO_PAIR,
                pairValues,
                Arrays.asList(kicker)
            );
        }
        return null;
    }

    private HandRank checkPair(List<Card> cards) {              // ****** CHECK FOR PAIR (K-K-2-6-8)
        Map<Integer, Integer> rankCounts = getRankCounts(cards);

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 2) {
                int pairValue = entry.getKey();

                List<Integer> kickers = cards.stream()      // <---- kickers are 3 cards not part of pair
                    .map(c -> c.getRank().getValue())
                    .filter(v -> v != pairValue)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

                return new HandRank(
                    HandType.PAIR,
                    Arrays.asList(pairValue),
                    kickers
                );
            }
        }
        return null;
    }

    private HandRank checkHighCard(List<Card> cards) {              // ***** CHECK FOR HIGH CARD
        List<Integer> values = cards.stream()           // <--- all 5 cards used for compare
            .map(c -> c.getRank().getValue())
            .collect(Collectors.toList());

        return new HandRank(
            HandType.HIGH_CARD,
            Arrays.asList(values.get(0)),
            values.subList(1, 5)
        );
    }

    private boolean isFlush(List<Card> cards) {                 // ***** CHECK IF ALL 5 CARDS ARE SAME SUIT
        Suit firstSuit = cards.get(0).getSuit();
        return cards.stream().allMatch(c -> c.getSuit() == firstSuit);
    }

    private boolean isStraight(List<Card> cards) {              // ****** CHECK IF STRAIGHT (handles wheels (low ace))
        List<Integer> values = cards.stream()
            .map(c -> c.getRank().getValue())
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        boolean isNormalStraight = true;                        // <---- check for normal straight (each card 1 less than previous)
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i) - values.get(i + 1) != 1) {
                isNormalStraight = false;
                break;
            }
        }
        if (isNormalStraight) {
            return true;
        }
        if (values.get(0) == 14 &&              // <---- check for wheel straight, in this case Ace (14) is treated as a 1
            values.get(1) == 5 &&
            values.get(2) == 4 &&
            values.get(3) == 3 &&
            values.get(4) == 2) {
                return true;
            }
        return false;
    }

    private int getHighCardOfStraight(List<Card> cards) {           // Get high card value in a straight
        List<Integer> values = cards.stream()
            .map(c -> c.getRank().getValue())
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        if (values.get(0) == 14 &&              // <----- check if its a wheel
            values.get(1) == 5) {
                return 5;
            }
        return values.get(0);               // <---- otherwise highest card is high card
    }

    private Map<Integer, Integer> getRankCounts(List<Card> cards) {     // Creates a map of rank values to their counts (K-K-K-5-5) ((13: 3, 5: 2))
        Map<Integer, Integer> counts = new HashMap<>();
        for (Card card : cards) {
            int value = card.getRank().getValue();
            counts.put(value, counts.getOrDefault(value, 0) + 1);
        }
        return counts;
    }
}
