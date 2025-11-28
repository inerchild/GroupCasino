package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.games.Poker.HandEvaluator;
import com.github.zipcodewilmington.casino.games.Poker.HandRank;
import com.github.zipcodewilmington.casino.games.Poker.HandType;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandEvaluatorTest {
    
    private HandEvaluator evaluator;

    @BeforeEach
    public void setup() {
        evaluator = new HandEvaluator();
    }

    @Test
    public void testRoyalFlush() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.SPADES, Rank.QUEEN),
            new Card(Suit.SPADES, Rank.JACK),
            new Card(Suit.SPADES, Rank.TEN)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.ROYAL_FLUSH, result.getHandType());
    }

    @Test
    public void testStraightFlush() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.DIAMONDS, Rank.NINE),
            new Card(Suit.DIAMONDS, Rank.EIGHT),
            new Card(Suit.DIAMONDS, Rank.SEVEN),
            new Card(Suit.DIAMONDS, Rank.SIX),
            new Card(Suit.DIAMONDS, Rank.FIVE)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.STRAIGHT_FLUSH, result.getHandType());
    }

    @Test
    public void testFourOfAKind() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.SPADES, Rank.KING)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.FOUR_OF_A_KIND, result.getHandType());
        assertEquals(Arrays.asList(14), result.getPrimaryValues());
    }

    @Test
    public void testFullHouse() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.HEARTS, Rank.KING),
            new Card(Suit.DIAMONDS, Rank.KING),
            new Card(Suit.CLUBS, Rank.FIVE),
            new Card(Suit.SPADES, Rank.FIVE)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.FULL_HOUSE, result.getHandType());
    }

        @Test
    public void testFlush() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.SPADES, Rank.NINE),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.SPADES, Rank.THREE)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.FLUSH, result.getHandType());
    }

    @Test
    public void testStraight() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.HEARTS, Rank.KING),
            new Card(Suit.DIAMONDS, Rank.QUEEN),
            new Card(Suit.CLUBS, Rank.JACK),
            new Card(Suit.SPADES, Rank.TEN)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.STRAIGHT, result.getHandType());
    }

    @Test
    public void testThreeOfAKind() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.QUEEN),
            new Card(Suit.HEARTS, Rank.QUEEN),
            new Card(Suit.DIAMONDS, Rank.QUEEN),
            new Card(Suit.CLUBS, Rank.NINE),
            new Card(Suit.SPADES, Rank.FIVE)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.THREE_OF_A_KIND, result.getHandType());
    }

    @Test
    public void testTwoPair() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.HEARTS, Rank.KING),
            new Card(Suit.DIAMONDS, Rank.TEN),
            new Card(Suit.CLUBS, Rank.TEN),
            new Card(Suit.SPADES, Rank.FIVE)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.TWO_PAIR, result.getHandType());
    }

    @Test
    public void testPair() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.HEARTS, Rank.ACE),
            new Card(Suit.DIAMONDS, Rank.KING),
            new Card(Suit.CLUBS, Rank.QUEEN),
            new Card(Suit.SPADES, Rank.JACK)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.PAIR, result.getHandType());
    }

    @Test
    public void testHighCard() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.HEARTS, Rank.KING),
            new Card(Suit.DIAMONDS, Rank.TEN),
            new Card(Suit.CLUBS, Rank.SEVEN),
            new Card(Suit.SPADES, Rank.THREE)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.HIGH_CARD, result.getHandType());
    }

    @Test
    public void testWheelStraight() {

        List<Card> cards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.DIAMONDS, Rank.THREE),
            new Card(Suit.CLUBS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE)
        );
        HandRank result = evaluator.evaluateHand(cards);
        assertEquals(HandType.STRAIGHT, result.getHandType());
        assertEquals(Arrays.asList(5), result.getPrimaryValues());
    }

    @Test
    public void testKickerComparison() {

        List<Card> hand1 = Arrays.asList(
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.HEARTS, Rank.KING),
            new Card(Suit.DIAMONDS, Rank.ACE),
            new Card(Suit.CLUBS, Rank.TEN),
            new Card(Suit.SPADES, Rank.FIVE)
        );

        List<Card> hand2 = Arrays.asList(
            new Card(Suit.DIAMONDS, Rank.KING),
            new Card(Suit.CLUBS, Rank.KING),
            new Card(Suit.HEARTS, Rank.QUEEN),
            new Card(Suit.SPADES, Rank.JACK),
            new Card(Suit.DIAMONDS, Rank.NINE)
        );

        HandRank rank1 = evaluator.evaluateHand(hand1);
        HandRank rank2 = evaluator.evaluateHand(hand2);
        
        assertTrue(rank1.compareTo(rank2) > 0);
    }

    @Test
    public void testFlushBeatsStrait() {

        List<Card> flush = Arrays.asList(
            new Card(Suit.HEARTS, Rank.KING),
            new Card(Suit.HEARTS, Rank.JACK),
            new Card(Suit.HEARTS, Rank.NINE),
            new Card(Suit.HEARTS, Rank.SEVEN),
            new Card(Suit.HEARTS, Rank.TWO)
        );

        List<Card> straight = Arrays.asList(
            new Card(Suit.SPADES, Rank.TEN),
            new Card(Suit.HEARTS, Rank.NINE),
            new Card(Suit.DIAMONDS, Rank.EIGHT),
            new Card(Suit.CLUBS, Rank.SEVEN),
            new Card(Suit.SPADES, Rank.SIX)
        );

        HandRank flushRank = evaluator.evaluateHand(flush);
        HandRank straightRank = evaluator.evaluateHand(straight);
        
        assertTrue(flushRank.compareTo(straightRank) > 0);
    }

    @Test
    public void testInvalidInput() {
        
        assertThrows(IllegalArgumentException.class, () -> {
            evaluator.evaluateHand(null);
        });
        
        List<Card> tooFewCards = Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.HEARTS, Rank.KING)
        );
        assertThrows(IllegalArgumentException.class, () -> {
            evaluator.evaluateHand(tooFewCards);
        });
    }
}
