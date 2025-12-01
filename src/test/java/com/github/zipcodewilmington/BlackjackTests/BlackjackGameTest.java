package com.github.zipcodewilmington.BlackjackTests;

import com.github.zipcodewilmington.casino.games.blackjack.BlackjackGame;
import com.github.zipcodewilmington.casino.games.blackjack.BlackjackPlayer;
import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackGameTest {

    private BlackjackGame game;
    private CasinoAccount account;
    private BlackjackPlayer player;
    private static final String ACCOUNT_NAME = "TestPlayer";
    private static final String ACCOUNT_PASSWORD = "password123";
    private static final double STARTING_BALANCE = 1000.0;

    @BeforeEach
    void setUp() {
        game = new BlackjackGame();
        account = new CasinoAccount(ACCOUNT_NAME, ACCOUNT_PASSWORD);
        account.creditAccount(STARTING_BALANCE);
        player = new BlackjackPlayer(account);
    }

    @Test
    void testConstructor_CreatesGame() {
        assertNotNull(game);
    }

    @Test
    void testConstructor_InitializesComponents() {
        BlackjackGame testGame = new BlackjackGame();
        assertNotNull(testGame);
    }

    @Test
    void testAdd_AddsPlayer() {
        game.add(player);
        assertDoesNotThrow(() -> game.add(player));
    }

    @Test
    void testAdd_AcceptsPlayerInterface() {
        PlayerInterface playerInterface = player;
        assertDoesNotThrow(() -> game.add(playerInterface));
    }

    @Test
    void testRemove_RemovesPlayer() {
        game.add(player);
        assertDoesNotThrow(() -> game.remove(player));
    }

    @Test
    void testGameImplementsGameInterface() {
        assertTrue(game instanceof com.github.zipcodewilmington.casino.GameInterface);
    }

    @Test
    void testPlayerImplementsPlayerInterface() {
        assertTrue(player instanceof PlayerInterface);
    }

    @Test
    void testGameHasRunMethod() {
        assertDoesNotThrow(() -> {
            game.getClass().getMethod("run");
        });
    }

    @Test
    void testGameHasAddMethod() {
        assertDoesNotThrow(() -> {
            game.getClass().getMethod("add", PlayerInterface.class);
        });
    }

    @Test
    void testGameHasRemoveMethod() {
        assertDoesNotThrow(() -> {
            game.getClass().getMethod("remove", PlayerInterface.class);
        });
    }

    @Test
    void testMultiplePlayersCanBeCreated() {
        CasinoAccount account2 = new CasinoAccount("Player2", "pass");
        account2.creditAccount(500.0);
        BlackjackPlayer player2 = new BlackjackPlayer(account2);
        assertNotNull(player2);
        assertNotSame(player, player2);
    }

    @Test
    void testGameCanBeReused() {
        game.add(player);
        game.remove(player);
        CasinoAccount newAccount = new CasinoAccount("NewPlayer", "pass");
        newAccount.creditAccount(500.0);
        BlackjackPlayer newPlayer = new BlackjackPlayer(newAccount);
        assertDoesNotThrow(() -> game.add(newPlayer));
    }

    @Test
    void testPlayerMaintainsAccountReference() {
        game.add(player);
        assertSame(account, player.getArcadeAccount());
    }

    @Test
    void testPlayerBalanceTracking() {
        game.add(player);
        double initialBalance = player.getArcadeAccount().getAccountBalance();
        player.placeBet(100);
        assertEquals(initialBalance - 100, player.getArcadeAccount().getAccountBalance());
    }

    @Test
    void testPlayerWinIncreasesBalance() {
        game.add(player);
        player.placeBet(100);
        double balanceAfterBet = player.getArcadeAccount().getAccountBalance();
        player.win(100);
        assertEquals(balanceAfterBet + 200, player.getArcadeAccount().getAccountBalance());
    }

    @Test
    void testPlayerLoseKeepsBalanceUnchanged() {
        game.add(player);
        player.placeBet(100);
        double balanceAfterBet = player.getArcadeAccount().getAccountBalance();
        player.lose();
        assertEquals(balanceAfterBet, player.getArcadeAccount().getAccountBalance());
    }

    @Test
    void testPlayerPushReturnsBalance() {
        game.add(player);
        double initialBalance = player.getArcadeAccount().getAccountBalance();
        player.placeBet(100);
        player.push();
        assertEquals(initialBalance, player.getArcadeAccount().getAccountBalance());
    }

    @Test
    void testGameSupportsMultipleRounds() {
        game.add(player);
        player.placeBet(50);
        player.win(50);
        player.resetHand();
        player.placeBet(75);
        player.lose();
        player.resetHand();
        assertEquals(STARTING_BALANCE + 50 - 75, player.getArcadeAccount().getAccountBalance());
    }

    @Test
    void testPlayerCanGoBlackjack() {
        game.add(player);
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.HEARTS, 
            com.github.zipcodewilmington.utils.Rank.ACE));
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.SPADES, 
            com.github.zipcodewilmington.utils.Rank.KING));
        assertTrue(player.getHand().isBlackjack());
        assertEquals(21, player.getHandValue());
    }

    @Test
    void testPlayerCanBust() {
        game.add(player);
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.HEARTS, 
            com.github.zipcodewilmington.utils.Rank.KING));
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.SPADES, 
            com.github.zipcodewilmington.utils.Rank.QUEEN));
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.DIAMONDS, 
            com.github.zipcodewilmington.utils.Rank.FIVE));
        assertTrue(player.getHand().isBusted());
        assertEquals(25, player.getHandValue());
    }

    @Test
    void testPlayerCanSplit() {
        game.add(player);
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.HEARTS, 
            com.github.zipcodewilmington.utils.Rank.EIGHT));
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.SPADES, 
            com.github.zipcodewilmington.utils.Rank.EIGHT));
        assertTrue(player.getHand().canSplit());
    }

    @Test
    void testPlayerCanDoubleDown() {
        game.add(player);
        player.placeBet(200);
        com.github.zipcodewilmington.utils.Deck deck = new com.github.zipcodewilmington.utils.Deck();
        boolean success = player.doubleDown(deck);
        assertTrue(success);
        assertEquals(400, player.getCurrentBet());
    }

    @Test
    void testIntegration_CompleteGameFlow() {
        game.add(player);
        double initialBalance = account.getAccountBalance();
        player.placeBet(100);
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.HEARTS, 
            com.github.zipcodewilmington.utils.Rank.KING));
        player.receiveCard(new com.github.zipcodewilmington.utils.Card(
            com.github.zipcodewilmington.utils.Suit.SPADES, 
            com.github.zipcodewilmington.utils.Rank.NINE));
        assertFalse(player.getHand().isBusted());
        assertEquals(19, player.getHandValue());
        player.win(100);
        assertEquals(initialBalance + 100, account.getAccountBalance());
    }
}