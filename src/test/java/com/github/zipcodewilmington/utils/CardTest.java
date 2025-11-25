package com.github.zipcodewilmington.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    public void testConstructorAndGetters() {
        // Given
        Suit expectedSuit = Suit.HEARTS;
        Rank expectedRank = Rank.ACE;
        
        // When
        Card card = new Card(expectedSuit, expectedRank);
        
        // Then
        assertEquals(expectedSuit, card.getSuit());
        assertEquals(expectedRank, card.getRank());
    }

    @Test
    public void testCompareTo_LowerRankCard() {
        // Given
        Card aceOfHearts = new Card(Suit.HEARTS, Rank.ACE);
        Card kingOfSpades = new Card(Suit.SPADES, Rank.KING);
        
        // When
        int result = kingOfSpades.compareTo(aceOfHearts);
        
        // Then
        assertTrue(result < 0, "King should be less than Ace");
    }

    @Test
    public void testCompareTo_HigherRankCard() {
        // Given
        Card twoOfClubs = new Card(Suit.CLUBS, Rank.TWO);
        Card tenOfDiamonds = new Card(Suit.DIAMONDS, Rank.TEN);
        
        // When
        int result = tenOfDiamonds.compareTo(twoOfClubs);
        
        // Then
        assertTrue(result > 0, "Ten should be greater than Two");
    }

    @Test
    public void testCompareTo_EqualRankDifferentSuits() {
        // Given
        Card aceOfHearts = new Card(Suit.HEARTS, Rank.ACE);
        Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
        
        // When
        int result = aceOfHearts.compareTo(aceOfSpades);
        
        // Then
        assertEquals(0, result, "Cards with same rank should be equal regardless of suit");
    }

    @Test
    public void testCompareTo_SameCardReference() {
        // Given
        Card card = new Card(Suit.DIAMONDS, Rank.QUEEN);
        
        // When
        int result = card.compareTo(card);
        
        // Then
        assertEquals(0, result, "Card should be equal to itself");
    }

    @Test
    public void testToString() {
        // Given
        Card card = new Card(Suit.HEARTS, Rank.JACK);
        
        // When
        String result = card.toString();
        
        // Then
        assertEquals("JACK of HEARTS", result);
    }

    @Test
    public void testToString_AllRanksAndSuits() {
        // Test a few combinations to ensure toString works correctly
        Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
        assertEquals("ACE of SPADES", aceOfSpades.toString());
        
        Card fiveOfClubs = new Card(Suit.CLUBS, Rank.FIVE);
        assertEquals("FIVE of CLUBS", fiveOfClubs.toString());
        
        Card kingOfDiamonds = new Card(Suit.DIAMONDS, Rank.KING);
        assertEquals("KING of DIAMONDS", kingOfDiamonds.toString());
    }

    @Test
    public void testCardImmutability() {
        // Given
        Suit originalSuit = Suit.HEARTS;
        Rank originalRank = Rank.SEVEN;
        Card card = new Card(originalSuit, originalRank);
        
        // When - try to get references
        Suit retrievedSuit = card.getSuit();
        Rank retrievedRank = card.getRank();
        
        // Then - verify the card's properties haven't changed
        assertEquals(originalSuit, card.getSuit());
        assertEquals(originalRank, card.getRank());
    }
}