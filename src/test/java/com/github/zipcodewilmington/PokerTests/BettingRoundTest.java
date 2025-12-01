package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.games.Poker.BettingRound;
import com.github.zipcodewilmington.casino.games.Poker.NPCPlayer;
import com.github.zipcodewilmington.casino.games.Poker.PokerPlayer;
import com.github.zipcodewilmington.casino.games.Poker.PotManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BettingRoundTest {

    private BettingRound bettingRound;
    private PotManager potManager;
    private NPCPlayer npc1;
    private NPCPlayer npc2;

    @BeforeEach
    public void setup() {

        bettingRound = new BettingRound(10.0);
        potManager = new PotManager();
        
        npc1 = new NPCPlayer("NPC1", NPCPlayer.Personality.CONSERVATIVE);
        npc2 = new NPCPlayer("NPC2", NPCPlayer.Personality.AGGRESSIVE);
    }

    @Test
    public void testConstructor() {

        assertNotNull(bettingRound);
        assertEquals(0.0, bettingRound.getCurrentBet());
    }

    @Test
    public void testSetCurrentBet() {

        bettingRound.setCurrentBet(50.0);
        assertEquals(50.0, bettingRound.getCurrentBet());
    }

    @Test
    public void testReset() {

        bettingRound.setCurrentBet(100.0);
        bettingRound.reset();
        assertEquals(0.0, bettingRound.getCurrentBet());
    }

    @Test
    public void testRunBettingRoundWithNPCs() {

        List<PokerPlayer> players = Arrays.asList(npc1, npc2);
        assertDoesNotThrow(() -> {
            bettingRound.runBettingRound(players, potManager);
        });
    }

    @Test
    public void testBettingRoundEndsWhenAllFold() {

        npc1.fold();
        npc2.fold();
        List<PokerPlayer> players = Arrays.asList(npc1, npc2);
        
        assertDoesNotThrow(() -> {
            bettingRound.runBettingRound(players, potManager);
        });
    }

    @Test
    public void testBettingRoundWithAllInPlayer() {

        double balance = npc1.getBalance();
        npc1.placeBet(balance - 10);
        List<PokerPlayer> players = Arrays.asList(npc1, npc2);

        assertDoesNotThrow(() -> {
            bettingRound.runBettingRound(players, potManager);
        });
    }

    @Test
    public void testGetCurrentBet() {

        assertEquals(0.0, bettingRound.getCurrentBet());
        bettingRound.setCurrentBet(100.0);
        assertEquals(100.0, bettingRound.getCurrentBet());
    }

    @Test
    public void testBettingRoundWithMixedPlayers() {

        List<PokerPlayer> players = Arrays.asList(npc1, npc2);
        assertDoesNotThrow(() -> {
            bettingRound.runBettingRound(players, potManager);
        });
    }
}