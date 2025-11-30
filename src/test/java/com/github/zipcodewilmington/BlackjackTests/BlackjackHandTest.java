package com.github.zipcodewilmington.BlackjackTests;

import com.github.zipcodewilmington.casino.games.blackjack.BlackjackHand;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackHandTest {

    private BlackjackHand hand;

    @BeforeEach
    void setUp() {
        hand = new BlackjackHand();
    }

    @Test
    void testConstructor_CreatesEmptyHand() {
        assertEquals(0, hand.size(), "New hand should be empty");
        assertEquals(0, hand.getValue(), "New hand should have value 0");
        assertFalse(hand.isBlackjack(), "Empty hand should not be blackjack");
        assertFalse(hand.isBusted(), "Empty hand should not be busted");
    }

    @Test
    void testAddCard_SingleCard() {
        Card card = new Card(Suit.HEARTS, Rank.KING);

        hand.addCard(card);

        assertEquals(1, hand.size(), "Hand should have 1 card");
        assertEquals(10, hand.getValue(), "King should have value 10");
    }

    @Test
    void testAddCard_MultipleCards() {
        Card card1 = new Card(Suit.HEARTS, Rank.SEVEN);
        Card card2 = new Card(Suit.SPADES, Rank.EIGHT);

        hand.addCard(card1);
        hand.addCard(card2);

        assertEquals(2, hand.size(), "Hand should have 2 cards");
        assertEquals(15, hand.getValue(), "7 + 8 should equal 15");
    }

    @Test
    void testGetValue_EmptyHand() {
        int value = hand.getValue();

        assertEquals(0, value, "Empty hand should have value 0");
    }

    @Test
    void testGetValue_NumberCards() {
        hand.addCard(new Card(Suit.HEARTS, Rank.FIVE));
        hand.addCard(new Card(Suit.SPADES, Rank.NINE));

        int value = hand.getValue();

        assertEquals(14, value, "5 + 9 should equal 14");
    }

    @Test
    void testGetValue_FaceCards() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));

        int value = hand.getValue();

        assertEquals(20, value, "King (10) + Queen (10) should equal 20");
    }

    @Test
    void testGetValue_AceAs11() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.SIX));

        int value = hand.getValue();

        assertEquals(17, value, "Ace (11) + 6 should equal 17 (soft 17)");
    }

    @Test
    void testGetValue_AceAs1_WhenBusting() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.NINE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.SEVEN));

        int value = hand.getValue();
 
        assertEquals(17, value, "Ace (1) + 9 + 7 should equal 17");
    }

    @Test
    void testGetValue_MultipleAces() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));

        int value = hand.getValue();

        assertEquals(12, value, "Ace (11) + Ace (1) should equal 12");
    }

    @Test
    void testGetValue_MultipleAcesWithHighCard() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));

        int value = hand.getValue();

        assertEquals(21, value, "Ace (11) + Ace (1) + 9 should equal 21");
    }

    @Test
    void testGetValue_ThreeAces() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));

        int value = hand.getValue();

        assertEquals(13, value, "Ace (11) + Ace (1) + Ace (1) should equal 13");
    }

    @Test
    void testIsBlackjack_AceAndKing() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));

        boolean isBlackjack = hand.isBlackjack();

        assertTrue(isBlackjack, "Ace + King should be blackjack");
        assertEquals(21, hand.getValue(), "Should have value 21");
    }

    @Test
    void testIsBlackjack_AceAndTen() {
        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
        hand.addCard(new Card(Suit.CLUBS, Rank.TEN));

        boolean isBlackjack = hand.isBlackjack();

        assertTrue(isBlackjack, "Ace + Ten should be blackjack");
    }

    @Test
    void testIsBlackjack_AceAndQueen() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.QUEEN));

        boolean isBlackjack = hand.isBlackjack();

        assertTrue(isBlackjack, "Ace + Queen should be blackjack");
    }

    @Test
    void testIsBlackjack_Not21() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.NINE));

        boolean isBlackjack = hand.isBlackjack();

        assertFalse(isBlackjack, "King + 9 (19) should not be blackjack");
    }

    @Test
    void testIsBlackjack_ThreeCardsTotaling21() {
        hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        hand.addCard(new Card(Suit.SPADES, Rank.SEVEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.SEVEN));
        
        boolean isBlackjack = hand.isBlackjack();

        assertFalse(isBlackjack, "21 with 3 cards should not be blackjack");
        assertEquals(21, hand.getValue(), "Should still have value 21");
    }

    @Test
    void testIsBlackjack_EmptyHand() {
        boolean isBlackjack = hand.isBlackjack();
        assertFalse(isBlackjack, "Empty hand should not be blackjack");
    }

    @Test
    void testIsBlackjack_OneCard() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));

        boolean isBlackjack = hand.isBlackjack();
        assertFalse(isBlackjack, "One card should not be blackjack");
    }

    @Test
    void testIsBusted_Under21() {
        hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand.addCard(new Card(Suit.SPADES, Rank.NINE));

        boolean isBusted = hand.isBusted();
        assertFalse(isBusted, "Hand with value 19 should not be busted");
    }

    @Test
    void testIsBusted_Exactly21() {
        hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));

        boolean isBusted = hand.isBusted();

        assertFalse(isBusted, "Hand with value 21 should not be busted");
        assertEquals(21, hand.getValue(), "Hand should have value 21");
    }

    @Test
    void testIsBusted_Over21() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        boolean isBusted = hand.isBusted();

        assertTrue(isBusted, "Hand with value 25 should be busted");
        assertEquals(25, hand.getValue(), "Hand should have value 25");
    }

    @Test
    void testIsBusted_AceConvertsToAvoidBust() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.NINE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        boolean isBusted = hand.isBusted();

        assertFalse(isBusted, "Ace should convert to 1 to avoid bust");
        assertEquals(15, hand.getValue(), "Hand should have value 15");
    }

    @Test
    void testIsBusted_EmptyHand() {

        boolean isBusted = hand.isBusted();

        assertFalse(isBusted, "Empty hand should not be busted");
    }

    @Test
    void testIsSoft_AceCountedAs11() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.SIX));

        boolean isSoft = hand.isSoft();

        assertTrue(isSoft, "Ace + 6 should be soft 17");
        assertEquals(17, hand.getValue(), "Should have value 17");
    }

    @Test
    void testIsSoft_AceCountedAs1() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.NINE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        boolean isSoft = hand.isSoft();

        assertFalse(isSoft, "Ace as 1 should not be soft");
        assertEquals(15, hand.getValue(), "Should have value 15");
    }

    @Test
    void testIsSoft_NoAce() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.SEVEN));

        boolean isSoft = hand.isSoft();
 
        assertFalse(isSoft, "Hand without Ace should not be soft");
    }

    @Test
    void testIsSoft_MultipleAces() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        boolean isSoft = hand.isSoft();

        assertTrue(isSoft, "Multiple Aces with one as 11 should be soft");
        assertEquals(17, hand.getValue(), "Should have value 17");
    }

    @Test
    void testIsSoft_EmptyHand() {
        boolean isSoft = hand.isSoft();

        assertFalse(isSoft, "Empty hand should not be soft");
    }

    @Test
    void testIsSoft_AceAndTenValue() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));

        boolean isSoft = hand.isSoft();

        assertTrue(isSoft, "Blackjack should be soft");
        assertEquals(21, hand.getValue(), "Should have value 21");
    }

    @Test
    void testCanSplit_TwoIdenticalRanks() {
        hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));

        boolean canSplit = hand.canSplit();

        assertTrue(canSplit, "Two 8s should be splittable");
    }

    @Test
    void testCanSplit_TwoAces() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));

        boolean canSplit = hand.canSplit();

        assertTrue(canSplit, "Two Aces should be splittable");
    }

    @Test
    void testCanSplit_TwoFaceCards_DifferentRanks() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        
        boolean canSplit = hand.canSplit();
        
        assertFalse(canSplit, "King and Queen should not be splittable (different ranks)");
    }

    @Test
    void testCanSplit_TenAndJack() {
        hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand.addCard(new Card(Suit.SPADES, Rank.JACK));

        boolean canSplit = hand.canSplit();

        assertFalse(canSplit, "Ten and Jack should not be splittable (different ranks)");
    }

    @Test
    void testCanSplit_ThreeCards() {
        hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        hand.addCard(new Card(Suit.SPADES, Rank.SEVEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.SEVEN));

        boolean canSplit = hand.canSplit();

        assertFalse(canSplit, "Three cards should not be splittable");
    }

    @Test
    void testCanSplit_OneCard() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));

        boolean canSplit = hand.canSplit();

        assertFalse(canSplit, "One card should not be splittable");
    }

    @Test
    void testCanSplit_EmptyHand() {
        boolean canSplit = hand.canSplit();

        assertFalse(canSplit, "Empty hand should not be splittable");
    }

    @Test
    void testCanSplit_DifferentRanks() {
        hand.addCard(new Card(Suit.HEARTS, Rank.FIVE));
        hand.addCard(new Card(Suit.SPADES, Rank.NINE));

        boolean canSplit = hand.canSplit();

        assertFalse(canSplit, "Different ranks should not be splittable");
    }

    @Test
    void testGetCards_EmptyHand() {
        List<Card> cards = hand.getCards();

        assertNotNull(cards, "getCards() should not return null");
        assertTrue(cards.isEmpty(), "Empty hand should return empty list");
    }

    @Test
    void testGetCards_WithCards() {
        Card card1 = new Card(Suit.HEARTS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.ACE);
        hand.addCard(card1);
        hand.addCard(card2);

        List<Card> cards = hand.getCards();

        assertEquals(2, cards.size(), "Should return 2 cards");
        assertTrue(cards.contains(card1), "Should contain first card");
        assertTrue(cards.contains(card2), "Should contain second card");
    }

    @Test
    void testGetCards_ReturnsDefensiveCopy() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));

        List<Card> cards = hand.getCards();
        int originalSize = hand.size();
        cards.clear();

        assertEquals(originalSize, hand.size(), "Modifying returned list should not affect hand");
        assertEquals(2, hand.size(), "Hand should still have 2 cards");
    }

    @Test
    void testSize_EmptyHand() {

        int size = hand.size();

        assertEquals(0, size, "Empty hand should have size 0");
    }

    @Test
    void testSize_WithCards() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.JACK));

        int size = hand.size();

        assertEquals(3, size, "Hand with 3 cards should have size 3");
    }

    @Test
    void testClear_RemovesAllCards() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));

        hand.clear();
  
        assertEquals(0, hand.size(), "Cleared hand should be empty");
        assertEquals(0, hand.getValue(), "Cleared hand should have value 0");
    }

    @Test
    void testClear_EmptyHand() {
 
        hand.clear();

        assertEquals(0, hand.size(), "Clearing empty hand should keep it empty");
    }

    @Test
    void testClear_CanAddAfterClearing() {

        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.clear();

        hand.addCard(new Card(Suit.SPADES, Rank.ACE));

        assertEquals(1, hand.size(), "Should be able to add cards after clearing");
        assertEquals(11, hand.getValue(), "New card should be counted");
    }

    @Test
    void testToString_EmptyHand() {
        String str = hand.toString();
        assertNotNull(str, "toString() should not return null");
        assertTrue(str.contains("Empty"), "Should indicate empty hand");
    }

    @Test
    void testToString_WithCards() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));

        String str = hand.toString();
        assertNotNull(str, "toString() should not return null");
        assertTrue(str.contains("21"), "Should show value 21");
    }

    @Test
    void testToString_ShowsSoftIndicator() {
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.SIX));
  
        String str = hand.toString();
  
        assertTrue(str.toLowerCase().contains("soft"), "Should indicate soft hand");
    }

    @Test
    void testSplit_CreatesNewHand() {
        hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));

        BlackjackHand newHand = hand.split();

        assertNotNull(newHand, "Split should return a new hand");
        assertEquals(1, hand.size(), "Original hand should have 1 card");
        assertEquals(1, newHand.size(), "New hand should have 1 card");
    }

    @Test
    void testSplit_DistributesCards() {
        Card card1 = new Card(Suit.HEARTS, Rank.EIGHT);
        Card card2 = new Card(Suit.SPADES, Rank.EIGHT);
        hand.addCard(card1);
        hand.addCard(card2);

        BlackjackHand newHand = hand.split();

        assertEquals(card1, hand.getCards().get(0), "Original hand should have first card");
        assertEquals(card2, newHand.getCards().get(0), "New hand should have second card");
    }

    @Test
    void testSplit_ThrowsException_WhenCannotSplit() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));

        assertThrows(IllegalStateException.class, () -> hand.split(),
            "Should throw IllegalStateException when trying to split non-pair");
    }

    @Test
    void testSplit_ThrowsException_WhenThreeCards() {
        hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertThrows(IllegalStateException.class, () -> hand.split(),
            "Should throw IllegalStateException when trying to split hand with 3 cards");
    }

    @Test
    void testSplit_ThrowsException_EmptyHand() {
        assertThrows(IllegalStateException.class, () -> hand.split(),
            "Should throw IllegalStateException when trying to split empty hand");
    }

    @Test
    void testCompleteBlackjackScenario() {

        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));

        assertTrue(hand.isBlackjack(), "Should be blackjack");
        assertFalse(hand.isBusted(), "Should not be busted");
        assertTrue(hand.isSoft(), "Should be soft");
        assertFalse(hand.canSplit(), "Should not be splittable");
        assertEquals(21, hand.getValue(), "Should have value 21");
        assertEquals(2, hand.size(), "Should have 2 cards");
    }

    @Test
    void testCompleteBustScenario() {
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertFalse(hand.isBlackjack(), "Should not be blackjack");
        assertTrue(hand.isBusted(), "Should be busted");
        assertFalse(hand.isSoft(), "Should not be soft");
        assertFalse(hand.canSplit(), "Should not be splittable");
        assertEquals(25, hand.getValue(), "Should have value 25");
    }

    @Test
    void testAceFlexibility() {
    hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
    hand.addCard(new Card(Suit.SPADES, Rank.FIVE));
    assertEquals(16, hand.getValue(), "Ace + 5 should be 16 (soft)");
    assertTrue(hand.isSoft(), "Should be soft");

    hand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));

    assertEquals(15, hand.getValue(), "Ace should convert to 1 (1 + 5 + 9 = 15)");
    assertFalse(hand.isSoft(), "Should not be soft anymore");
    assertFalse(hand.isBusted(), "Should not be busted");
}
}






