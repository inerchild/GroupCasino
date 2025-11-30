package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.games.Poker.Hand;
import com.github.zipcodewilmington.casino.games.Poker.HandRank;
import com.github.zipcodewilmington.casino.games.Poker.HandType;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
        private Hand hand;

    @BeforeEach
    public void setup() {
        hand = new Hand();
    }

    @Test
    public void testAddHoleCard() {
        Card card1 = new Card(Suit.SPADES, Rank.ACE);
        Card card2 = new Card(Suit.HEARTS, Rank.KING);

        hand.addHoleCard(card1);
        hand.addHoleCard(card2);

        assertEquals(2, hand.getHoleCards().size());
        assertTrue(hand.getHoleCards().contains(card1));
        assertTrue(hand.getHoleCards().contains(card2));
    }

    @Test
    public void testAddTooManyHoleCards() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));

        assertThrows(IllegalStateException.class, () -> {
            hand.addHoleCard(new Card(Suit.DIAMONDS, Rank.QUEEN));
        });
    }

    @Test
    public void testAddCommunityCard() {

        Card card1 = new Card(Suit.SPADES, Rank.TEN);
        Card card2 = new Card(Suit.HEARTS, Rank.NINE);
        Card card3 = new Card(Suit.DIAMONDS, Rank.EIGHT);

        hand.addCommunityCard(card1);
        hand.addCommunityCard(card2);
        hand.addCommunityCard(card3);

        assertEquals(3, hand.getCommunityCards().size());
    }

    @Test
    public void testAddTooManyCommunityCards() {

        hand.addCommunityCard(new Card(Suit.SPADES, Rank.TEN));
        hand.addCommunityCard(new Card(Suit.HEARTS, Rank.NINE));
        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.EIGHT));
        hand.addCommunityCard(new Card(Suit.CLUBS, Rank.SEVEN));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.SIX));

        assertThrows(IllegalStateException.class, () -> {
            hand.addCommunityCard(new Card(Suit.HEARTS, Rank.FIVE));
        });
    }

    @Test
    public void testGetAllCards() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));
        
        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.QUEEN));
        hand.addCommunityCard(new Card(Suit.CLUBS, Rank.JACK));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.TEN));

        assertEquals(5, hand.getAllCards().size());
    }

    @Test
    public void testClear() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));

        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.QUEEN));

        hand.clear();

        assertEquals(0, hand.getHoleCards().size());
        assertEquals(0, hand.getCommunityCards().size());
        assertEquals(0, hand.getAllCards().size());
    }

    @Test
    public void testGetBestHandWithExactly5Cards() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.SPADES, Rank.KING));

        hand.addCommunityCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.JACK));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.TEN));

        HandRank bestHand = hand.getBestHand();
        assertEquals(HandType.ROYAL_FLUSH, bestHand.getHandType());
    }

    @Test
    public void testGetBestHandWith7Cards() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.SPADES, Rank.KING));

        hand.addCommunityCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.JACK));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.TEN));
        hand.addCommunityCard(new Card(Suit.HEARTS, Rank.FIVE));
        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.THREE));

        HandRank bestHand = hand.getBestHand();
        assertEquals(HandType.ROYAL_FLUSH, bestHand.getHandType());
    }

    @Test
    public void testGetBestHandChoosesBestCombination() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.SEVEN));
        hand.addHoleCard(new Card(Suit.SPADES, Rank.SIX));

        hand.addCommunityCard(new Card(Suit.SPADES, Rank.FIVE));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.FOUR));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.THREE));
        hand.addCommunityCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.QUEEN));

        HandRank bestHand = hand.getBestHand();
        assertEquals(HandType.STRAIGHT_FLUSH, bestHand.getHandType());
    }

    @Test
    public void testGetBestHandWithPair() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.KING));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));

        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.ACE));
        hand.addCommunityCard(new Card(Suit.CLUBS, Rank.FOUR));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.EIGHT));
        hand.addCommunityCard(new Card(Suit.HEARTS, Rank.TWO));
        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.SEVEN));

        HandRank bestHand = hand.getBestHand();
        assertEquals(HandType.PAIR, bestHand.getHandType());
    }

    @Test
    public void testGetBestHandWithFullHouse() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.KING));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));

        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.KING));
        hand.addCommunityCard(new Card(Suit.CLUBS, Rank.FIVE));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.FIVE));
        hand.addCommunityCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.QUEEN));

        HandRank bestHand = hand.getBestHand();
        assertEquals(HandType.FULL_HOUSE, bestHand.getHandType());
    }

    @Test
    public void testGetBestHandThrowsWithTooFewCards() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));

        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.QUEEN));
        hand.addCommunityCard(new Card(Suit.CLUBS, Rank.JACK));

        assertThrows(IllegalStateException.class, () -> {
            hand.getBestHand();
        });
    }

    @Test
    public void testGetBestHandWithHighCard() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));

        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.TEN));
        hand.addCommunityCard(new Card(Suit.CLUBS, Rank.SEVEN));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.THREE));

        HandRank bestHand = hand.getBestHand();
        assertEquals(HandType.HIGH_CARD, bestHand.getHandType());
    }

    @Test
    public void testToString() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addHoleCard(new Card(Suit.HEARTS, Rank.KING));

        hand.addCommunityCard(new Card(Suit.DIAMONDS, Rank.QUEEN));

        String str = hand.toString();
        assertNotNull(str);
        assertTrue(str.contains("Hole Cards"));
        assertTrue(str.contains("Community Cards"));
    }

    @Test
    public void testToStringWithEmptyHand() {

        String str = hand.toString();
        assertNotNull(str);
        assertTrue(str.contains("None"));
    }

    @Test
    public void testGetBestHandWith6Cards() {

        hand.addHoleCard(new Card(Suit.SPADES, Rank.ACE));

        hand.addCommunityCard(new Card(Suit.SPADES, Rank.KING));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.JACK));
        hand.addCommunityCard(new Card(Suit.SPADES, Rank.TEN));
        hand.addCommunityCard(new Card(Suit.HEARTS, Rank.TWO));

        HandRank bestHand = hand.getBestHand();
        assertEquals(HandType.ROYAL_FLUSH, bestHand.getHandType());
    }
}
