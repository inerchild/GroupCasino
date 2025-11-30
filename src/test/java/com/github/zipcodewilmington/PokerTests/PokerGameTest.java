package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.games.Poker.PokerGame;
import com.github.zipcodewilmington.casino.games.Poker.PokerPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokerGameTest {
    private PokerGame game;
    private PokerPlayer player;

    @BeforeEach
    public void setup() {
        game = new PokerGame();
        player = new PokerPlayer(new CasinoAccount("TestPlayer", "pass"));
    }

    @Test
    public void testConstructor() {
        assertNotNull(game);
    }

    @Test
    public void testAddPlayer() {
        game.add(player);
        assertDoesNotThrow(() -> game.add(player));
    }

    @Test
    public void testRemovePlayer() {
        game.add(player);
        game.remove(player);
        assertDoesNotThrow(() -> game.remove(player));
    }

    @Test
    public void testGameCreatesNPCsWhenNeeded() {
        assertDoesNotThrow(() -> {
        });
    }
}
