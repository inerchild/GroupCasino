package com.github.zipcodewilmington.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testConstructor_CreatesFullDeck() {
        // Given - deck created in setUp()
        int expectedSize = Suit.values().length * Rank.values().length;
        
        // When
        int actualSize = deck.size();
        
        // Then
        assertEquals(expectedSize, actualSize, "Deck should contain 52 cards (4 suits × 13 ranks)");
    }

    @Test
    public void testConstructor_DeckIsNotEmpty() {
        // Given - deck created in setUp()
        
        // Then
        assertFalse(deck.isEmpty(), "Newly created deck should not be empty");
    }

    @Test
    public void testSize_InitialDeck() {
        // Given - deck created in setUp()
        
        // When
        int size = deck.size();
        
        // Then
        assertEquals(52, size, "Standard deck should have 52 cards");
    }

    @Test
    public void testSize_AfterDrawingCards() {
        // Given
        int initialSize = deck.size();
        
        // When
        deck.drawCard();
        deck.drawCard();
        deck.drawCard();
        
        // Then
        assertEquals(initialSize - 3, deck.size(), "Size should decrease by 3 after drawing 3 cards");
    }

    @Test
    public void testIsEmpty_NewDeck() {
        // Given - deck created in setUp()
        
        // Then
        assertFalse(deck.isEmpty(), "New deck should not be empty");
    }

    @Test
    public void testIsEmpty_AfterDrawingAllCards() {
        // Given
        int deckSize = deck.size();
        
        // When - draw all cards
        for (int i = 0; i < deckSize; i++) {
            deck.drawCard();
        }
        
        // Then
        assertTrue(deck.isEmpty(), "Deck should be empty after drawing all cards");
    }

    @Test
    public void testDrawCard_ReturnsCard() {
        // When
        Card card = deck.drawCard();
        
        // Then
        assertNotNull(card, "Drawing from a non-empty deck should return a card");
    }

    @Test
    public void testDrawCard_ReducesDeckSize() {
        // Given
        int initialSize = deck.size();
        
        // When
        deck.drawCard();
        
        // Then
        assertEquals(initialSize - 1, deck.size(), "Drawing a card should reduce deck size by 1");
    }

    @Test
    public void testDrawCard_FromEmptyDeck() {
        // Given - empty the deck
        while (!deck.isEmpty()) {
            deck.drawCard();
        }
        
        // When
        Card card = deck.drawCard();
        
        // Then
        assertNull(card, "Drawing from an empty deck should return null");
    }

    @Test
    public void testDrawCard_MultipleDraws() {
        // When
        Card firstCard = deck.drawCard();
        Card secondCard = deck.drawCard();
        Card thirdCard = deck.drawCard();
        
        // Then
        assertNotNull(firstCard);
        assertNotNull(secondCard);
        assertNotNull(thirdCard);
        assertEquals(49, deck.size(), "Deck should have 49 cards after drawing 3");
    }

    @Test
    public void testShuffle_DeckSizeRemainsSame() {
        // Given
        int initialSize = deck.size();
        
        // When
        deck.shuffle();
        
        // Then
        assertEquals(initialSize, deck.size(), "Shuffling should not change deck size");
    }

    @Test
    public void testShuffle_ChangesCardOrder() {
        // Given
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
        
        // Draw a few cards from unshuffled deck to get the order
        Card firstCard1 = deck1.drawCard();
        Card secondCard1 = deck1.drawCard();
        Card thirdCard1 = deck1.drawCard();
        
        // When - shuffle the second deck
        deck2.shuffle();
        Card firstCard2 = deck2.drawCard();
        Card secondCard2 = deck2.drawCard();
        Card thirdCard2 = deck2.drawCard();
        
        // Then - at least one of the first three cards should be different
        boolean orderChanged = !firstCard1.toString().equals(firstCard2.toString()) ||
                              !secondCard1.toString().equals(secondCard2.toString()) ||
                              !thirdCard1.toString().equals(thirdCard2.toString());
        
        assertTrue(orderChanged, "Shuffling should change the order of cards (may rarely fail due to randomness)");
    }

    @Test
    public void testShuffle_MultipleTimes() {
        // Given
        int initialSize = deck.size();
        
        // When
        deck.shuffle();
        deck.shuffle();
        deck.shuffle();
        
        // Then
        assertEquals(initialSize, deck.size(), "Multiple shuffles should not change deck size");
        assertFalse(deck.isEmpty(), "Deck should still have cards after multiple shuffles");
    }

    @Test
    public void testShuffle_EmptyDeck() {
        // Given - empty the deck
        while (!deck.isEmpty()) {
            deck.drawCard();
        }
        
        // When
        deck.shuffle();
        
        // Then
        assertTrue(deck.isEmpty(), "Shuffling empty deck should keep it empty");
        assertEquals(0, deck.size(), "Empty deck should have size 0 after shuffling");
    }

    @Test
    public void testDrawEntireDeck() {
        // Given
        int initialSize = deck.size();
        int cardsDrawn = 0;
        
        // When - draw all cards
        while (!deck.isEmpty()) {
            Card card = deck.drawCard();
            assertNotNull(card, "All cards drawn before deck is empty should be non-null");
            cardsDrawn++;
        }
        
        // Then
        assertEquals(initialSize, cardsDrawn, "Should be able to draw all cards from deck");
        assertEquals(0, deck.size(), "Deck size should be 0 after drawing all cards");
    }

    @Test
    public void testDeckContainsAllCards() {
        // Given
        int heartsCount = 0;
        int diamondsCount = 0;
        int clubsCount = 0;
        int spadesCount = 0;
        
        // When - draw all cards and count suits
        while (!deck.isEmpty()) {
            Card card = deck.drawCard();
            switch (card.getSuit()) {
                case HEARTS:
                    heartsCount++;
                    break;
                case DIAMONDS:
                    diamondsCount++;
                    break;
                case CLUBS:
                    clubsCount++;
                    break;
                case SPADES:
                    spadesCount++;
                    break;
            }
        }
        
        // Then
        assertEquals(13, heartsCount, "Deck should have 13 hearts");
        assertEquals(13, diamondsCount, "Deck should have 13 diamonds");
        assertEquals(13, clubsCount, "Deck should have 13 clubs");
        assertEquals(13, spadesCount, "Deck should have 13 spades");
    }
}