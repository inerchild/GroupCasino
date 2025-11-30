package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.games.Poker.PokerPlayer;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokerPlayerTest {
    
    private CasinoAccount account;
    private PokerPlayer player;

    @BeforeEach
    public void setup() {
        account = new CasinoAccount("TestPlayer", "password123");
        player = new PokerPlayer(account);
    }

    @Test
    public void testConstructor() {
        assertEquals("TestPlayer", player.getName());
        assertEquals(1000.0, player.getBalance());
        assertFalse(player.isFolded());
    }

    @Test
    public void testPlaceBet() {
        player.placeBet(100.0);
        assertEquals(900.0, player.getBalance());
        assertEquals(100.0, player.getCurrentBet());
    }

    @Test
    public void testPlaceBetTooMuch() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.placeBet(1500.0);
        });
    }

    @Test
    public void testReceiveWinnings() {
        player.receiveWinnings(500.0);
        assertEquals(1500.0, player.getBalance());
    }

    @Test
    public void testFold() {
        player.fold();
        assertTrue(player.isFolded());
    }

    @Test
    public void testResetForNewHand() {
        player.receiveHoleCard(new Card(Suit.SPADES, Rank.ACE));
        player.placeBet(100.0);
        player.fold();

        player.resetForNewHand();

        assertEquals(0, player.getHand().getHoleCards().size());
        assertEquals(0.0, player.getCurrentBet());
        assertFalse(player.isFolded());
    }

    @Test
    public void testIsAllIn() {
        player.placeBet(1000.0);
        assertTrue(player.isAllIn());
    }

    @Test
    public void testGetAmountToCall() {
    player.placeBet(50.0);
    assertEquals(50.0, player.getAmountToCall(100.0));

    }
}
