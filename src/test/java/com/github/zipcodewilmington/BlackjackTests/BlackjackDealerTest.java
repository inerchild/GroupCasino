package com.github.zipcodewilmington.BlackjackTests;

import com.github.zipcodewilmington.casino.games.blackjack.BlackjackDealer;
import com.github.zipcodewilmington.casino.games.blackjack.BlackjackHand;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Deck;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackDealerTest {

    private BlackjackDealer dealer;
    private Deck deck;

    @BeforeEach
    void setUp() {
        dealer = new BlackjackDealer();
        deck = new Deck();
        deck.shuffle();
    }

    @Test
    void testConstructor_CreatesEmptyHand() {
        assertNotNull(dealer.getHand(), "Dealer should have a hand");
        assertEquals(0, dealer.getHand().size(), "New dealer should have empty hand");
    }

    @Test
    void testGetHand_ReturnsBlackjackHand() {
        BlackjackHand hand = dealer.getHand();

        assertNotNull(hand, "getHand() should return a non-null hand");
        assertTrue(hand instanceof BlackjackHand, "Should return a BlackjackHand instance");
    }

    @Test
    void testGetHand_ReturnsSameHandInstance() {
        BlackjackHand hand1 = dealer.getHand();
        BlackjackHand hand2 = dealer.getHand();

        assertSame(hand1, hand2, "Should return the same hand instance");
    }

    @Test
    void testGetHandValue_EmptyHand() {
        int value = dealer.getHandValue();

        assertEquals(0, value, "Empty hand should have value of 0");
    }

    @Test
    void testGetHandValue_WithCards() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SEVEN));

        int value = dealer.getHandValue();

        assertEquals(17, value, "King (10) + Seven (7) should equal 17");
    }

    @Test
    void testGetHandValue_WithAce() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.ACE));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SIX));

        int value = dealer.getHandValue();

        assertEquals(17, value, "Ace (11) + Six (6) should equal 17");
    }

    @Test
    void testReceiveCard_AddsCardToHand() {
        Card card = new Card(Suit.DIAMONDS, Rank.QUEEN);

        dealer.receiveCard(card);

        assertEquals(1, dealer.getHand().size(), "Hand should have 1 card");
        assertTrue(dealer.getHand().getCards().contains(card), "Hand should contain the received card");
    }

    @Test
    void testReceiveCard_MultipleCards() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.FIVE);
 
        dealer.receiveCard(card1);
        dealer.receiveCard(card2);

        assertEquals(2, dealer.getHand().size(), "Hand should have 2 cards");
        assertEquals(15, dealer.getHandValue(), "Hand value should be 15");
    }

    @Test
    void testHit_DrawsCardFromDeck() {
        int initialDeckSize = deck.size();

        dealer.hit(deck);

        assertEquals(initialDeckSize - 1, deck.size(), "Deck should have one fewer card");
        assertEquals(1, dealer.getHand().size(), "Dealer should have 1 card");
    }

    @Test
    void testHit_AddsCardToHand() {

        dealer.hit(deck);

        assertFalse(dealer.getHand().getCards().isEmpty(), "Hand should not be empty after hit");
        assertEquals(1, dealer.getHand().size(), "Hand should have 1 card");
    }

    @Test
    void testHit_MultipleHits() {
        dealer.hit(deck);
        dealer.hit(deck);
        dealer.hit(deck);

        assertEquals(3, dealer.getHand().size(), "Hand should have 3 cards");
    }

    @Test
    void testGetUpCard_EmptyHand_ReturnsNull() {
        Card upCard = dealer.getUpCard();
        
        assertNull(upCard, "Up card should be null for empty hand");
    }

    @Test
    void testGetUpCard_ReturnsFirstCard() {
        Card firstCard = new Card(Suit.HEARTS, Rank.KING);
        Card secondCard = new Card(Suit.SPADES, Rank.ACE);
        dealer.receiveCard(firstCard);
        dealer.receiveCard(secondCard);

        Card upCard = dealer.getUpCard();

        assertEquals(firstCard, upCard, "Up card should be the first card dealt");
        assertEquals(Rank.KING, upCard.getRank(), "Up card should be a King");
    }

    @Test
    void testGetUpCard_WithOneCard() {
        Card card = new Card(Suit.DIAMONDS, Rank.SEVEN);
        dealer.receiveCard(card);

        Card upCard = dealer.getUpCard();
        
        assertEquals(card, upCard, "Up card should be the only card");
    }

    @Test
    void testHasBlackjack_WithBlackjack() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.ACE));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.KING));

        boolean hasBlackjack = dealer.hasBlackjack();
        
        assertTrue(hasBlackjack, "Ace + King should be blackjack");
    }

    @Test
    void testHasBlackjack_WithAceAndTen() {

        dealer.receiveCard(new Card(Suit.DIAMONDS, Rank.ACE));
        dealer.receiveCard(new Card(Suit.CLUBS, Rank.TEN));

        boolean hasBlackjack = dealer.hasBlackjack();

        assertTrue(hasBlackjack, "Ace + Ten should be blackjack");
    }

    @Test
    void testHasBlackjack_Without21() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.NINE));

        boolean hasBlackjack = dealer.hasBlackjack();

        assertFalse(hasBlackjack, "King + Nine (19) should not be blackjack");
    }

    @Test
    void testHasBlackjack_With21ButThreeCards() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.SEVEN));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SEVEN));
        dealer.receiveCard(new Card(Suit.DIAMONDS, Rank.SEVEN));

        boolean hasBlackjack = dealer.hasBlackjack();

        assertFalse(hasBlackjack, "21 with three cards should not be blackjack");
    }

    @Test
    void testHasBlackjack_EmptyHand() {

        boolean hasBlackjack = dealer.hasBlackjack();
        
        assertFalse(hasBlackjack, "Empty hand should not be blackjack");
    }

    @Test
    void testShouldCheckForBlackjack_WithAce() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.ACE));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.FIVE));

        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();

        assertTrue(shouldCheck, "Dealer should check for blackjack when showing Ace");
    }

    @Test
    void testShouldCheckForBlackjack_WithTen() {

        dealer.receiveCard(new Card(Suit.DIAMONDS, Rank.TEN));
        dealer.receiveCard(new Card(Suit.CLUBS, Rank.ACE));

        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();

        assertTrue(shouldCheck, "Dealer should check for blackjack when showing Ten");
    }

    @Test
    void testShouldCheckForBlackjack_WithJack() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.JACK));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.NINE));
        
        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();

        assertTrue(shouldCheck, "Dealer should check for blackjack when showing Jack");
    }

    @Test
    void testShouldCheckForBlackjack_WithQueen() {
        dealer.receiveCard(new Card(Suit.DIAMONDS, Rank.QUEEN));
        dealer.receiveCard(new Card(Suit.CLUBS, Rank.EIGHT));

        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();

        assertTrue(shouldCheck, "Dealer should check for blackjack when showing Queen");
    }

    @Test
    void testShouldCheckForBlackjack_WithKing() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SEVEN));

        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();
        
        assertTrue(shouldCheck, "Dealer should check for blackjack when showing King");
    }

    @Test
    void testShouldCheckForBlackjack_WithLowCard() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.SIX));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.KING));
        
        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();
        
        assertFalse(shouldCheck, "Dealer should not check for blackjack when showing Six");
    }

    @Test
    void testShouldCheckForBlackjack_WithNine() {

        dealer.receiveCard(new Card(Suit.DIAMONDS, Rank.NINE));
        dealer.receiveCard(new Card(Suit.CLUBS, Rank.ACE));

        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();

        assertFalse(shouldCheck, "Dealer should not check for blackjack when showing Nine");
    }

    @Test
    void testShouldCheckForBlackjack_EmptyHand() {

        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();

        assertFalse(shouldCheck, "Should return false for empty hand");
    }

    @Test
    void testPlayTurn_StandsOn17() {

        dealer.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SEVEN));
        
        dealer.playTurn(deck);
        
        assertEquals(2, dealer.getHand().size(), "Dealer should stand on hard 17");
        assertEquals(17, dealer.getHandValue(), "Hand value should still be 17");
    }

    @Test
    void testPlayTurn_HitsOn16() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SIX));
        
        dealer.playTurn(deck);
        
        assertTrue(dealer.getHand().size() > 2, "Dealer should hit on 16");
    }

    @Test
    void testPlayTurn_HitsOnSoft17() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.ACE));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SIX));
        
        dealer.playTurn(deck);
        
        assertTrue(dealer.getHand().size() > 2, "Dealer should hit on soft 17");
    }

    @Test
    void testPlayTurn_StandsOnHard18() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.EIGHT));
        
        dealer.playTurn(deck);
        
        assertEquals(2, dealer.getHand().size(), "Dealer should stand on 18");
        assertEquals(18, dealer.getHandValue(), "Hand value should still be 18");
    }

    @Test
    void testPlayTurn_HitsOn12() {

        dealer.receiveCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.TWO));

        dealer.playTurn(deck);

        assertTrue(dealer.getHand().size() > 2, "Dealer should hit on 12");
    }

    @Test
    void testPlayTurn_CanBust() {

        dealer.receiveCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.SIX));

        dealer.playTurn(deck);
 
        assertTrue(dealer.getHand().size() >= 3, "Dealer should have drawn at least one more card");
    }

    @Test
    void testPlayTurn_EventuallyStopsHitting() {
        dealer.receiveCard(new Card(Suit.HEARTS, Rank.TWO));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.THREE));
        
        dealer.playTurn(deck);
        
        int finalValue = dealer.getHandValue();
        assertTrue(finalValue >= 17 || dealer.getHand().isBusted(), 
            "Dealer should end with 17+ or be busted");
    }

    @Test
    void testFullDealerRound_WithBlackjack() {

        dealer.receiveCard(new Card(Suit.HEARTS, Rank.ACE));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.KING));

        boolean shouldCheck = dealer.shouldDealerCheckForBlackjack();
        boolean hasBlackjack = dealer.hasBlackjack();

        assertTrue(shouldCheck, "Should check for blackjack with Ace showing");
        assertTrue(hasBlackjack, "Should have blackjack");
        assertEquals(21, dealer.getHandValue(), "Should have value of 21");
    }

    @Test
    void testFullDealerRound_PlayToCompletion() {

        dealer.receiveCard(new Card(Suit.HEARTS, Rank.SEVEN));
        dealer.receiveCard(new Card(Suit.SPADES, Rank.EIGHT));
        

        int initialValue = dealer.getHandValue();
        dealer.playTurn(deck);
        int finalValue = dealer.getHandValue();
        
        assertEquals(15, initialValue, "Initial value should be 15");
        assertTrue(finalValue >= 17 || dealer.getHand().isBusted(), 
            "Final value should be 17+ or busted");
    }
}