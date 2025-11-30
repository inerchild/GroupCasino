package com.github.zipcodewilmington.BlackjackTests;

import com.github.zipcodewilmington.casino.games.blackjack.BlackjackPlayer;
import com.github.zipcodewilmington.casino.games.blackjack.BlackjackHand;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Deck;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackPlayerTest {

    private BlackjackPlayer player;
    private Deck deck;
    private static final String PLAYER_NAME = "TestPlayer";
    private static final int STARTING_BALANCE = 1000;

    @BeforeEach
    void setUp() {
        player = new BlackjackPlayer(PLAYER_NAME, STARTING_BALANCE);
        deck = new Deck();
        deck.shuffle();
    }

    @Test
    void testConstructor_SetsName() {
        assertEquals(PLAYER_NAME, player.getName(), "Player name should be set");
    }

    @Test
    void testConstructor_SetsStartingBalance() {
        assertEquals(STARTING_BALANCE, player.getBalance(), "Starting balance should be set");
    }

    @Test
    void testConstructor_InitializesEmptyHand() {
        assertNotNull(player.getHand(), "Player should have a hand");
        assertEquals(0, player.getHand().size(), "Hand should be empty initially");
    }

    @Test
    void testConstructor_InitializesZeroBet() {
        assertEquals(0, player.getCurrentBet(), "Current bet should be 0 initially");
    }

    @Test
    void testGetName_ReturnsCorrectName() {
        String name = player.getName();
        assertEquals(PLAYER_NAME, name, "Should return player name");
    }

    @Test
    void testGetHand_ReturnsBlackjackHand() {
        BlackjackHand hand = player.getHand();
        assertNotNull(hand, "Should return non-null hand");
        assertTrue(hand instanceof BlackjackHand, "Should return BlackjackHand instance");
    }

    @Test
    void testGetHand_ReturnsSameInstance() {
        BlackjackHand hand1 = player.getHand();
        BlackjackHand hand2 = player.getHand();
        assertSame(hand1, hand2, "Should return same hand instance");
    }

    @Test
    void testGetBalance_InitialBalance() {
        int balance = player.getBalance();
        assertEquals(STARTING_BALANCE, balance, "Should return starting balance");
    }

    @Test
    void testGetBalance_AfterBet() {
        player.placeBet(100);
        int balance = player.getBalance();
        assertEquals(STARTING_BALANCE - 100, balance, "Balance should be reduced by bet amount");
    }

    @Test
    void testGetCurrentBet_Initially() {
        int currentBet = player.getCurrentBet();
        assertEquals(0, currentBet, "Initial bet should be 0");
    }

    @Test
    void testGetCurrentBet_AfterPlacingBet() {
        player.placeBet(250);
        int currentBet = player.getCurrentBet();
        assertEquals(250, currentBet, "Current bet should be 250");
    }

    @Test
    void testGetHandValue_EmptyHand() {
        int value = player.getHandValue();
        assertEquals(0, value, "Empty hand should have value 0");
    }

    @Test
    void testGetHandValue_WithCards() {
        player.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        player.receiveCard(new Card(Suit.SPADES, Rank.SEVEN));
        int value = player.getHandValue();
        assertEquals(17, value, "King + 7 should equal 17");
    }

    @Test
    void testReceiveCard_AddsToHand() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        player.receiveCard(card);
        assertEquals(1, player.getHand().size(), "Hand should have 1 card");
        assertTrue(player.getHand().getCards().contains(card), "Hand should contain the card");
    }

    @Test
    void testReceiveCard_MultipleCards() {
        Card card1 = new Card(Suit.HEARTS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        player.receiveCard(card1);
        player.receiveCard(card2);
        assertEquals(2, player.getHand().size(), "Hand should have 2 cards");
        assertEquals(20, player.getHandValue(), "Should have value 20");
    }

    @Test
    void testHit_DrawsFromDeck() {
        int initialDeckSize = deck.size();
        player.hit(deck);
        assertEquals(initialDeckSize - 1, deck.size(), "Deck should have one fewer card");
        assertEquals(1, player.getHand().size(), "Player should have 1 card");
    }

    @Test
    void testHit_AddsToHand() {
        player.hit(deck);
        assertEquals(1, player.getHand().size(), "Hand should have 1 card after hit");
    }

    @Test
    void testHit_MultipleHits() {
        player.hit(deck);
        player.hit(deck);
        player.hit(deck);
        assertEquals(3, player.getHand().size(), "Hand should have 3 cards");
    }

    @Test
    void testPlaceBet_ValidBet() {
        boolean success = player.placeBet(100);
        assertTrue(success, "Should successfully place valid bet");
        assertEquals(100, player.getCurrentBet(), "Current bet should be 100");
        assertEquals(STARTING_BALANCE - 100, player.getBalance(), "Balance should be reduced");
    }

    @Test
    void testPlaceBet_MaximumBet() {
        boolean success = player.placeBet(STARTING_BALANCE);
        assertTrue(success, "Should allow betting entire balance");
        assertEquals(STARTING_BALANCE, player.getCurrentBet(), "Bet should equal starting balance");
        assertEquals(0, player.getBalance(), "Balance should be 0");
    }

    @Test
    void testPlaceBet_NegativeAmount() {
        boolean success = player.placeBet(-50);
        assertFalse(success, "Should not allow negative bet");
        assertEquals(0, player.getCurrentBet(), "Current bet should remain 0");
        assertEquals(STARTING_BALANCE, player.getBalance(), "Balance should be unchanged");
    }

    @Test
    void testPlaceBet_ZeroAmount() {
        boolean success = player.placeBet(0);
        assertFalse(success, "Should not allow zero bet");
        assertEquals(0, player.getCurrentBet(), "Current bet should remain 0");
    }

    @Test
    void testPlaceBet_ExceedsBalance() {
        boolean success = player.placeBet(STARTING_BALANCE + 100);
        assertFalse(success, "Should not allow bet exceeding balance");
        assertEquals(0, player.getCurrentBet(), "Current bet should remain 0");
        assertEquals(STARTING_BALANCE, player.getBalance(), "Balance should be unchanged");
    }

    @Test
    void testPlaceBet_MultipleBets() {
        player.placeBet(100);
        player.placeBet(50);
        assertEquals(50, player.getCurrentBet(), "Second bet should replace first");
        assertEquals(STARTING_BALANCE - 100 - 50, player.getBalance(), 
            "Both bets should be deducted from balance");
    }

    @Test
    void testDoubleDown_WithSufficientFunds() {
        player.placeBet(100);
        boolean success = player.doubleDown(deck);
        assertTrue(success, "Should successfully double down");
        assertEquals(200, player.getCurrentBet(), "Bet should be doubled");
        assertEquals(STARTING_BALANCE - 200, player.getBalance(), "Balance should reflect doubled bet");
        assertEquals(1, player.getHand().size(), "Should have drawn one card");
    }

    @Test
    void testDoubleDown_WithInsufficientFunds() {
        player.placeBet(600);
        boolean success = player.doubleDown(deck);
        assertFalse(success, "Should not allow double down with insufficient funds");
        assertEquals(600, player.getCurrentBet(), "Bet should remain unchanged");
        assertEquals(STARTING_BALANCE - 600, player.getBalance(), "Balance should be unchanged");
    }

    @Test
    void testDoubleDown_WithExactFunds() {
        player.placeBet(500);
        boolean success = player.doubleDown(deck);
        assertTrue(success, "Should allow double down with exact funds");
        assertEquals(1000, player.getCurrentBet(), "Bet should be doubled to 1000");
        assertEquals(0, player.getBalance(), "Balance should be 0");
    }

    @Test
    void testDoubleDown_DrawsOneCard() {
        player.placeBet(100);
        int initialHandSize = player.getHand().size();
        player.doubleDown(deck);
        assertEquals(initialHandSize + 1, player.getHand().size(), 
            "Should draw exactly one card");
    }

    @Test
    void testWin_RegularWin() {
        player.placeBet(100);
        player.win(100);
        assertEquals(STARTING_BALANCE + 100, player.getBalance(), 
            "Balance should be original + payout");
        assertEquals(0, player.getCurrentBet(), "Current bet should be reset to 0");
    }

    @Test
    void testWin_BlackjackPayout() {
        player.placeBet(100);
        player.win(150);
        assertEquals(STARTING_BALANCE + 150, player.getBalance(), 
            "Balance should be original + blackjack payout");
        assertEquals(0, player.getCurrentBet(), "Current bet should be reset to 0");
    }

    @Test
    void testWin_LargePayout() {
        player.placeBet(500);
        player.win(500);
        assertEquals(STARTING_BALANCE + 500, player.getBalance(), 
            "Should receive large payout correctly");
        assertEquals(0, player.getCurrentBet(), "Bet should be reset");
    }

    @Test
    void testLose_BetAlreadyDeducted() {
        player.placeBet(100);
        int balanceAfterBet = player.getBalance();
        player.lose();
        assertEquals(balanceAfterBet, player.getBalance(), "Balance should remain unchanged");
        assertEquals(0, player.getCurrentBet(), "Current bet should be reset to 0");
    }

    @Test
    void testLose_ResetsCurrentBet() {
        player.placeBet(250);
        player.lose();
        assertEquals(0, player.getCurrentBet(), "Current bet should be reset to 0");
    }

    @Test
    void testPush_ReturnsOriginalBet() {
        player.placeBet(100);
        int balanceAfterBet = player.getBalance();
        player.push();
        assertEquals(balanceAfterBet + 100, player.getBalance(), 
            "Should receive original bet back");
        assertEquals(STARTING_BALANCE, player.getBalance(), 
            "Balance should be back to starting amount");
        assertEquals(0, player.getCurrentBet(), "Current bet should be reset to 0");
    }

    @Test
    void testPush_WithLargeBet() {
        player.placeBet(500);
        player.push();
        assertEquals(STARTING_BALANCE, player.getBalance(), 
            "Should return to starting balance");
        assertEquals(0, player.getCurrentBet(), "Bet should be reset");
    }

    @Test
    void testCanAffordBet_WithSufficientFunds() {
        boolean canAfford = player.canAffordBet(500);
        assertTrue(canAfford, "Should be able to afford bet within balance");
    }

    @Test
    void testCanAffordBet_ExactBalance() {
        boolean canAfford = player.canAffordBet(STARTING_BALANCE);
        assertTrue(canAfford, "Should be able to afford bet equal to balance");
    }

    @Test
    void testCanAffordBet_ExceedingBalance() {
        boolean canAfford = player.canAffordBet(STARTING_BALANCE + 100);
        assertFalse(canAfford, "Should not be able to afford bet exceeding balance");
    }

    @Test
    void testCanAffordBet_AfterPlacingBet() {
        player.placeBet(700);
        boolean canAfford = player.canAffordBet(400);
        assertFalse(canAfford, "Should check against remaining balance (300)");
        assertTrue(player.canAffordBet(300), "Should be able to afford bet of remaining balance");
    }

    @Test
    void testCanAffordBet_ZeroBalance() {
        player.placeBet(STARTING_BALANCE);
        boolean canAfford = player.canAffordBet(1);
        assertFalse(canAfford, "Should not be able to afford bet with zero balance");
    }

    @Test
    void testIsBroke_WithPositiveBalance() {
        boolean isBroke = player.isBroke();
        assertFalse(isBroke, "Player with positive balance should not be broke");
    }

    @Test
    void testIsBroke_WithZeroBalance() {
        player.placeBet(STARTING_BALANCE);
        player.lose();
        boolean isBroke = player.isBroke();
        assertTrue(isBroke, "Player with zero balance should be broke");
        assertEquals(0, player.getBalance(), "Balance should be 0");
    }

    @Test
    void testIsBroke_AfterLosingAllMoney() {
        player.placeBet(500);
        player.lose();
        player.placeBet(500);
        player.lose();
        boolean isBroke = player.isBroke();
        assertTrue(isBroke, "Player who lost all money should be broke");
    }

    @Test
    void testIsBroke_AfterWinning() {
        player.placeBet(100);
        player.win(100);
        boolean isBroke = player.isBroke();
        assertFalse(isBroke, "Player who won should not be broke");
        assertTrue(player.getBalance() > 0, "Should have positive balance");
    }

    @Test
    void testCompleteRound_PlayerWins() {
        int initialBalance = player.getBalance();
        player.placeBet(100);
        player.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        player.receiveCard(new Card(Suit.SPADES, Rank.QUEEN));
        player.win(100);
        assertEquals(initialBalance + 100, player.getBalance(), 
            "Balance should increase by payout");
        assertEquals(20, player.getHandValue(), "Hand should have value 20");
    }

    @Test
    void testCompleteRound_PlayerLoses() {
        int initialBalance = player.getBalance();
        player.placeBet(100);
        player.receiveCard(new Card(Suit.HEARTS, Rank.TEN));
        player.receiveCard(new Card(Suit.SPADES, Rank.FIVE));
        player.lose();
        assertEquals(initialBalance - 100, player.getBalance(), 
            "Balance should decrease by bet");
        assertEquals(15, player.getHandValue(), "Hand should have value 15");
    }

    @Test
    void testCompleteRound_Push() {
        int initialBalance = player.getBalance();
        player.placeBet(100);
        player.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        player.receiveCard(new Card(Suit.SPADES, Rank.NINE));
        player.push();
        assertEquals(initialBalance, player.getBalance(), 
            "Balance should return to initial amount");
    }

    @Test
    void testMultipleRounds_TrackingBalance() {
        int initialBalance = player.getBalance();
        player.placeBet(100);
        player.win(100);
        player.placeBet(50);
        player.lose();
        player.placeBet(75);
        player.push();
        int expectedBalance = initialBalance + 100 - 50;
        assertEquals(expectedBalance, player.getBalance(), 
            "Balance should reflect all round outcomes");
    }

    @Test
    void testDoubleDown_Integration() {
        player.placeBet(200);
        player.receiveCard(new Card(Suit.HEARTS, Rank.FIVE));
        player.receiveCard(new Card(Suit.SPADES, Rank.SIX));
        player.doubleDown(deck);
        player.win(400);
        assertEquals(STARTING_BALANCE + 400, player.getBalance(), 
            "Should win with doubled bet amount");
    }

    @Test
    void testPlayerGoingBroke() {
        BlackjackPlayer poorPlayer = new BlackjackPlayer("Poor", 100);
        poorPlayer.placeBet(100);
        poorPlayer.lose();
        assertTrue(poorPlayer.isBroke(), "Player should be broke");
        assertFalse(poorPlayer.canAffordBet(1), "Should not be able to afford any bet");
        assertEquals(0, poorPlayer.getBalance(), "Balance should be 0");
    }
}






