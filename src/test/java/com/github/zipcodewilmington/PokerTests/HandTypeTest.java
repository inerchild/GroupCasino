package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.games.Poker.HandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandTypeTest {
    
        @Test
    public void testHandTypeRanks() {

        assertEquals(1, HandType.HIGH_CARD.getRank());
        assertEquals(2, HandType.PAIR.getRank());
        assertEquals(3, HandType.TWO_PAIR.getRank());
        assertEquals(4, HandType.THREE_OF_A_KIND.getRank());
        assertEquals(5, HandType.STRAIGHT.getRank());
        assertEquals(6, HandType.FLUSH.getRank());
        assertEquals(7, HandType.FULL_HOUSE.getRank());
        assertEquals(8, HandType.FOUR_OF_A_KIND.getRank());
        assertEquals(9, HandType.STRAIGHT_FLUSH.getRank());
        assertEquals(10, HandType.ROYAL_FLUSH.getRank());
    }

    @Test
    public void testDisplayNames() {

        assertEquals("High Card", HandType.HIGH_CARD.getDisplayName());
        assertEquals("Pair", HandType.PAIR.getDisplayName());
        assertEquals("Two Pair", HandType.TWO_PAIR.getDisplayName());
        assertEquals("Three of a Kind", HandType.THREE_OF_A_KIND.getDisplayName());
        assertEquals("Straight", HandType.STRAIGHT.getDisplayName());
        assertEquals("Flush", HandType.FLUSH.getDisplayName());
        assertEquals("Full House", HandType.FULL_HOUSE.getDisplayName());
        assertEquals("Four of a Kind", HandType.FOUR_OF_A_KIND.getDisplayName());
        assertEquals("Straight Flush", HandType.STRAIGHT_FLUSH.getDisplayName());
        assertEquals("Royal Flush", HandType.ROYAL_FLUSH.getDisplayName());
    }

    @Test
    public void testEnumComparison() {

        assertTrue(HandType.ROYAL_FLUSH.compareTo(HandType.HIGH_CARD) > 0);
        assertTrue(HandType.FLUSH.compareTo(HandType.STRAIGHT) > 0);
        assertTrue(HandType.PAIR.compareTo(HandType.HIGH_CARD) > 0);
        assertEquals(0, HandType.FULL_HOUSE.compareTo(HandType.FULL_HOUSE));
    }

    @Test
    public void testAllHandTypesExist() {

        HandType[] allTypes = HandType.values();
        assertEquals(10, allTypes.length);
    }
}
