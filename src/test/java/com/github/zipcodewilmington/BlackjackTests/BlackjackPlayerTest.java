package com.github.zipcodewilmington.BlackjackTests;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.games.blackjack.BlackjackHand;
import com.github.zipcodewilmington.casino.games.blackjack.BlackjackPlayer;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Deck;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackPlayerTest {

    private BlackjackPlayer player;
    private CasinoAccount account;
    private Deck deck;
    private static final String ACCOUNT_NAME = "TestPlayer";
    private static final String ACCOUNT_PASSWORD = "password123";
    private static final double STARTING_BALANCE = 1000.0;

    @BeforeEach
    void setUp() {
        account = new CasinoAccount(ACCOUNT_NAME, ACCOUNT_PASSWORD);
        account.creditAccount(STARTING_BALANCE);
        player = new BlackjackPlayer(account);
        deck = new Deck();
        deck.shuffle();
    }

    @Test
    void testConstructor_SetsAccount() {
        assertNotNull(player.getArcadeAccount());
        assertEquals(ACCOUNT_NAME, player.getArcadeAccount().getAccountName());
    }

    @Test
    void testConstructor_InitializesEmptyHand() {
        assertNotNull(player.getHand());
        assertEquals(0, player.getHand().size());
    }

    @Test
    void testConstructor_InitializesZeroBet() {
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testGetArcadeAccount_ReturnsCorrectAccount() {
        CasinoAccount returnedAccount = player.getArcadeAccount();
        assertSame(account, returnedAccount);
    }

    @Test
    void testPlay_ReturnsAccount() {
        CasinoAccount returnedAccount = player.play();
        assertSame(account, returnedAccount);
    }

    @Test
    void testGetHand_ReturnsBlackjackHand() {
        BlackjackHand hand = player.getHand();
        assertNotNull(hand);
        assertTrue(hand instanceof BlackjackHand);
    }

    @Test
    void testGetHand_ReturnsSameInstance() {
        BlackjackHand hand1 = player.getHand();
        BlackjackHand hand2 = player.getHand();
        assertSame(hand1, hand2);
    }

    @Test
    void testGetCurrentBet_Initially() {
        int currentBet = player.getCurrentBet();
        assertEquals(0, currentBet);
    }

    @Test
    void testGetCurrentBet_AfterPlacingBet() {
        player.placeBet(250);
        int currentBet = player.getCurrentBet();
        assertEquals(250, currentBet);
    }

    @Test
    void testGetHandValue_EmptyHand() {
        int value = player.getHandValue();
        assertEquals(0, value);
    }

    @Test
    void testGetHandValue_WithCards() {
        player.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        player.receiveCard(new Card(Suit.SPADES, Rank.SEVEN));
        int value = player.getHandValue();
        assertEquals(17, value);
    }

    @Test
    void testReceiveCard_AddsToHand() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        player.receiveCard(card);
        assertEquals(1, player.getHand().size());
        assertTrue(player.getHand().getCards().contains(card));
    }

    @Test
    void testReceiveCard_MultipleCards() {
        Card card1 = new Card(Suit.HEARTS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        player.receiveCard(card1);
        player.receiveCard(card2);
        assertEquals(2, player.getHand().size());
        assertEquals(20, player.getHandValue());
    }

    @Test
    void testHit_DrawsFromDeck() {
        int initialDeckSize = deck.size();
        player.hit(deck);
        assertEquals(initialDeckSize - 1, deck.size());
        assertEquals(1, player.getHand().size());
    }

    @Test
    void testHit_AddsToHand() {
        player.hit(deck);
        assertEquals(1, player.getHand().size());
    }

    @Test
    void testHit_MultipleHits() {
        player.hit(deck);
        player.hit(deck);
        player.hit(deck);
        assertEquals(3, player.getHand().size());
    }

    @Test
    void testPlaceBet_ValidBet() {
        boolean success = player.placeBet(100);
        assertTrue(success);
        assertEquals(100, player.getCurrentBet());
        assertEquals(STARTING_BALANCE - 100, account.getAccountBalance());
    }

    @Test
    void testPlaceBet_MaximumBet() {
        boolean success = player.placeBet((int) STARTING_BALANCE);
        assertTrue(success);
        assertEquals((int) STARTING_BALANCE, player.getCurrentBet());
        assertEquals(0.0, account.getAccountBalance());
    }

    @Test
    void testPlaceBet_NegativeAmount() {
        boolean success = player.placeBet(-50);
        assertFalse(success);
        assertEquals(0, player.getCurrentBet());
        assertEquals(STARTING_BALANCE, account.getAccountBalance());
    }

    @Test
    void testPlaceBet_ZeroAmount() {
        boolean success = player.placeBet(0);
        assertFalse(success);
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testPlaceBet_ExceedsBalance() {
        boolean success = player.placeBet((int) STARTING_BALANCE + 100);
        assertFalse(success);
        assertEquals(0, player.getCurrentBet());
        assertEquals(STARTING_BALANCE, account.getAccountBalance());
    }

    @Test
    void testPlaceBet_MultipleBets() {
        player.placeBet(100);
        player.placeBet(50);
        assertEquals(50, player.getCurrentBet());
        assertEquals(STARTING_BALANCE - 100 - 50, account.getAccountBalance());
    }

    @Test
    void testDoubleDown_WithSufficientFunds() {
        player.placeBet(100);
        boolean success = player.doubleDown(deck);
        assertTrue(success);
        assertEquals(200, player.getCurrentBet());
        assertEquals(STARTING_BALANCE - 200, account.getAccountBalance());
        assertEquals(1, player.getHand().size());
    }

    @Test
    void testDoubleDown_WithInsufficientFunds() {
        player.placeBet(600);
        boolean success = player.doubleDown(deck);
        assertFalse(success);
        assertEquals(600, player.getCurrentBet());
        assertEquals(STARTING_BALANCE - 600, account.getAccountBalance());
    }

    @Test
    void testDoubleDown_WithExactFunds() {
        player.placeBet(500);
        boolean success = player.doubleDown(deck);
        assertTrue(success);
        assertEquals(1000, player.getCurrentBet());
        assertEquals(0.0, account.getAccountBalance());
    }

    @Test
    void testDoubleDown_DrawsOneCard() {
        player.placeBet(100);
        int initialHandSize = player.getHand().size();
        player.doubleDown(deck);
        assertEquals(initialHandSize + 1, player.getHand().size());
    }

    @Test
    void testWin_RegularWin() {
        player.placeBet(100);
        player.win(100);
        assertEquals(STARTING_BALANCE + 100, account.getAccountBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testWin_BlackjackPayout() {
        player.placeBet(100);
        player.win(150);
        assertEquals(STARTING_BALANCE + 150, account.getAccountBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testWin_LargePayout() {
        player.placeBet(500);
        player.win(500);
        assertEquals(STARTING_BALANCE + 500, account.getAccountBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testLose_BetAlreadyDeducted() {
        player.placeBet(100);
        double balanceAfterBet = account.getAccountBalance();
        player.lose();
        assertEquals(balanceAfterBet, account.getAccountBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testLose_ResetsCurrentBet() {
        player.placeBet(250);
        player.lose();
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testPush_ReturnsOriginalBet() {
        player.placeBet(100);
        double balanceAfterBet = account.getAccountBalance();
        player.push();
        assertEquals(balanceAfterBet + 100, account.getAccountBalance());
        assertEquals(STARTING_BALANCE, account.getAccountBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testPush_WithLargeBet() {
        player.placeBet(500);
        player.push();
        assertEquals(STARTING_BALANCE, account.getAccountBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testCanAffordBet_WithSufficientFunds() {
        boolean canAfford = player.canAffordBet(500);
        assertTrue(canAfford);
    }

    @Test
    void testCanAffordBet_ExactBalance() {
        boolean canAfford = player.canAffordBet((int) STARTING_BALANCE);
        assertTrue(canAfford);
    }

    @Test
    void testCanAffordBet_ExceedingBalance() {
        boolean canAfford = player.canAffordBet((int) STARTING_BALANCE + 100);
        assertFalse(canAfford);
    }

    @Test
    void testCanAffordBet_AfterPlacingBet() {
        player.placeBet(700);
        boolean canAfford = player.canAffordBet(400);
        assertFalse(canAfford);
        assertTrue(player.canAffordBet(300));
    }

    @Test
    void testCanAffordBet_ZeroBalance() {
        player.placeBet((int) STARTING_BALANCE);
        boolean canAfford = player.canAffordBet(1);
        assertFalse(canAfford);
    }

    @Test
    void testIsBroke_WithPositiveBalance() {
        boolean isBroke = player.isBroke();
        assertFalse(isBroke);
    }

    @Test
    void testIsBroke_WithZeroBalance() {
        player.placeBet((int) STARTING_BALANCE);
        player.lose();
        boolean isBroke = player.isBroke();
        assertTrue(isBroke);
        assertEquals(0.0, account.getAccountBalance());
    }

    @Test
    void testIsBroke_AfterLosingAllMoney() {
        player.placeBet(500);
        player.lose();
        player.placeBet(500);
        player.lose();
        boolean isBroke = player.isBroke();
        assertTrue(isBroke);
    }

    @Test
    void testIsBroke_AfterWinning() {
        player.placeBet(100);
        player.win(100);
        boolean isBroke = player.isBroke();
        assertFalse(isBroke);
        assertTrue(account.getAccountBalance() > 0);
    }

    @Test
    void testResetHand_CreatesNewHand() {
        player.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        player.receiveCard(new Card(Suit.SPADES, Rank.QUEEN));
        player.placeBet(100);
        player.resetHand();
        assertEquals(0, player.getHand().size());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testResetHand_AllowsNewCards() {
        player.resetHand();
        player.receiveCard(new Card(Suit.HEARTS, Rank.ACE));
        assertEquals(1, player.getHand().size());
        assertEquals(11, player.getHandValue());
    }

    @Test
    void testCompleteRound_PlayerWins() {
        double initialBalance = account.getAccountBalance();
        player.placeBet(100);
        player.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        player.receiveCard(new Card(Suit.SPADES, Rank.QUEEN));
        player.win(100);
        assertEquals(initialBalance + 100, account.getAccountBalance());
        assertEquals(20, player.getHandValue());
    }

    @Test
    void testCompleteRound_PlayerLoses() {
        double initialBalance = account.getAccountBalance();
        player.placeBet(100);
        player.receiveCard(new Card(Suit.HEARTS, Rank.TEN));
        player.receiveCard(new Card(Suit.SPADES, Rank.FIVE));
        player.lose();
        assertEquals(initialBalance - 100, account.getAccountBalance());
        assertEquals(15, player.getHandValue());
    }

    @Test
    void testCompleteRound_Push() {
        double initialBalance = account.getAccountBalance();
        player.placeBet(100);
        player.receiveCard(new Card(Suit.HEARTS, Rank.KING));
        player.receiveCard(new Card(Suit.SPADES, Rank.NINE));
        player.push();
        assertEquals(initialBalance, account.getAccountBalance());
    }

    @Test
    void testMultipleRounds_TrackingBalance() {
        double initialBalance = account.getAccountBalance();
        player.placeBet(100);
        player.win(100);
        player.resetHand();
        player.placeBet(50);
        player.lose();
        player.resetHand();
        player.placeBet(75);
        player.push();
        double expectedBalance = initialBalance + 100 - 50;
        assertEquals(expectedBalance, account.getAccountBalance());
    }

    @Test
    void testDoubleDown_Integration() {
        player.placeBet(200);
        player.receiveCard(new Card(Suit.HEARTS, Rank.FIVE));
        player.receiveCard(new Card(Suit.SPADES, Rank.SIX));
        player.doubleDown(deck);
        player.win(400);
        assertEquals(STARTING_BALANCE + 400, account.getAccountBalance());
    }

    @Test
    void testPlayerGoingBroke() {
        CasinoAccount poorAccount = new CasinoAccount("Poor", "password");
        poorAccount.creditAccount(100.0);
        BlackjackPlayer poorPlayer = new BlackjackPlayer(poorAccount);
        poorPlayer.placeBet(100);
        poorPlayer.lose();
        assertTrue(poorPlayer.isBroke());
        assertFalse(poorPlayer.canAffordBet(1));
        assertEquals(0.0, poorAccount.getAccountBalance());
    }
}