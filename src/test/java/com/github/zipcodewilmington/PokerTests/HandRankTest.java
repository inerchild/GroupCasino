package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.games.Poker.HandRank;
import com.github.zipcodewilmington.casino.games.Poker.HandType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class HandRankTest {
    @Test
    public void testGetters() {

        HandRank rank = new HandRank(
            HandType.PAIR,
            Arrays.asList(13),        
            Arrays.asList(14, 10, 5) 
        );

        assertEquals(HandType.PAIR, rank.getHandType());
        assertEquals(Arrays.asList(13), rank.getPrimaryValues());
        assertEquals(Arrays.asList(14, 10, 5), rank.getKickers());
    }

    @Test
    public void testCompareDifferentHandTypes() {

        HandRank flush = new HandRank(HandType.FLUSH, Arrays.asList(14), Arrays.asList(10, 8, 5, 3));
        HandRank straight = new HandRank(HandType.STRAIGHT, Arrays.asList(10), Collections.emptyList());

        assertTrue(flush.compareTo(straight) > 0);
        assertTrue(straight.compareTo(flush) < 0);
    }

    @Test
    public void testCompareSameHandTypeDifferentPrimaryValues() {

        HandRank pairAces = new HandRank(HandType.PAIR, Arrays.asList(14), Arrays.asList(10, 8, 5));
        HandRank pairKings = new HandRank(HandType.PAIR, Arrays.asList(13), Arrays.asList(10, 8, 5));

        assertTrue(pairAces.compareTo(pairKings) > 0);
        assertTrue(pairKings.compareTo(pairAces) < 0);
    }

    @Test
    public void testCompareSamePrimaryValuesDifferentKickers() {

        HandRank aceKicker = new HandRank(HandType.PAIR, Arrays.asList(13), Arrays.asList(14, 10, 5));
        HandRank queenKicker = new HandRank(HandType.PAIR, Arrays.asList(13), Arrays.asList(12, 10, 5));

        assertTrue(aceKicker.compareTo(queenKicker) > 0);
        assertTrue(queenKicker.compareTo(aceKicker) < 0);
    }

    @Test
    public void testCompareIdenticalHands() {

        HandRank hand1 = new HandRank(HandType.TWO_PAIR, Arrays.asList(13, 10), Arrays.asList(5));
        HandRank hand2 = new HandRank(HandType.TWO_PAIR, Arrays.asList(13, 10), Arrays.asList(5));

        assertEquals(0, hand1.compareTo(hand2));
    }

    @Test
    public void testCompareMultipleKickers() {

        HandRank hand1 = new HandRank(HandType.PAIR, Arrays.asList(10), Arrays.asList(14, 11, 5));
        HandRank hand2 = new HandRank(HandType.PAIR, Arrays.asList(10), Arrays.asList(14, 9, 5));

        assertTrue(hand1.compareTo(hand2) > 0);
    }

    @Test
    public void testEquals() {
        
        HandRank hand1 = new HandRank(HandType.FULL_HOUSE, Arrays.asList(13, 5), Collections.emptyList());
        HandRank hand2 = new HandRank(HandType.FULL_HOUSE, Arrays.asList(13, 5), Collections.emptyList());
        HandRank hand3 = new HandRank(HandType.FULL_HOUSE, Arrays.asList(13, 4), Collections.emptyList());

        assertEquals(hand1, hand2);
        assertNotEquals(hand1, hand3);
        assertNotEquals(hand1, null);
        assertNotEquals(hand1, "not a HandRank");
    }

    @Test
    public void testHashCode() {

        HandRank hand1 = new HandRank(HandType.STRAIGHT, Arrays.asList(10), Collections.emptyList());
        HandRank hand2 = new HandRank(HandType.STRAIGHT, Arrays.asList(10), Collections.emptyList());

        assertEquals(hand1.hashCode(), hand2.hashCode());
    }

    @Test
    public void testGetDescription() {

        HandRank pairKings = new HandRank(HandType.PAIR, Arrays.asList(13), Arrays.asList(14, 10, 5));
        String description = pairKings.getDescription();

        assertTrue(description.contains("Pair"));
        assertTrue(description.contains("King"));
    }

    @Test
    public void testToString() {

        HandRank rank = new HandRank(HandType.THREE_OF_A_KIND, Arrays.asList(12), Arrays.asList(9, 5));
        String str = rank.toString();

        assertNotNull(str);
        assertTrue(str.contains("Three of a Kind"));
    }

    @Test
    public void testEmptyKickers() {

        HandRank straight = new HandRank(HandType.STRAIGHT, Arrays.asList(14), Collections.emptyList());

        assertTrue(straight.getKickers().isEmpty());
    }

    @Test
    public void testTwoPairPrimaryValues() {

        HandRank twoPair = new HandRank(HandType.TWO_PAIR, Arrays.asList(13, 10), Arrays.asList(5));

        assertEquals(2, twoPair.getPrimaryValues().size());
        assertEquals(Arrays.asList(13, 10), twoPair.getPrimaryValues());
    }
}
