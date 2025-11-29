package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.games.Poker.PotManager;
import com.github.zipcodewilmington.casino.games.Poker.PokerPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PotManagerTest {

    private PotManager potManager;
    private PokerPlayer player1;
    private PokerPlayer player2;
    private PokerPlayer player3;

    @BeforeEach
    public void setup() {
        potManager = new PotManager();
        player1 = new PokerPlayer(new CasinoAccount("Player1", "pass"));
        player2 = new PokerPlayer(new CasinoAccount("Player2", "pass"));
        player3 = new PokerPlayer(new CasinoAccount("Player3", "pass"));
    }

    @Test
    public void testInitialPotIsZero() {
        assertEquals(0.0, potManager.getTotalPotSize());
    }

    @Test
    public void testCollectBetsSimpleCase() {

        player1.placeBet(100);
        player2.placeBet(100);
        player3.placeBet(100);

        potManager.collectBets(Arrays.asList(player1, player2, player3));

        assertEquals(300.0, potManager.getTotalPotSize());
        assertEquals(1, potManager.getNumberOfPots());
    }

    @Test
    public void testCollectBetsWithOneFold() {

        player1.placeBet(100);
        player2.placeBet(100);
        player3.fold();

        potManager.collectBets(Arrays.asList(player1, player2, player3));

        assertEquals(200.0, potManager.getTotalPotSize());
    }

    @Test
    public void testSidePotCreation() {

        player1.placeBet(100);
        player2.placeBet(500);
        player3.placeBet(500);

        potManager.collectBets(Arrays.asList(player1, player2, player3));
        assertTrue(potManager.getNumberOfPots() >= 2);
        assertEquals(1100.0, potManager.getTotalPotSize());
    }

    @Test
    public void testAwardPotToSingleWinner() {

        player1.placeBet(100);
        player2.placeBet(100);
        potManager.collectBets(Arrays.asList(player1, player2));

        double player1BalanceBefore = player1.getBalance();
        potManager.awardPot(0, Arrays.asList(player1));
        assertEquals(player1BalanceBefore + 200, player1.getBalance());
    }

    @Test
    public void testAwardPotSplitBetweenWinners() {

        player1.placeBet(100);
        player2.placeBet(100);
        player3.placeBet(100);

        potManager.collectBets(Arrays.asList(player1, player2, player3));
        double p1Before = player1.getBalance();
        double p2Before = player2.getBalance();
        potManager.awardPot(0, Arrays.asList(player1, player2));

        assertEquals(p1Before + 150, player1.getBalance()); // 300 / 2
        assertEquals(p2Before + 150, player2.getBalance());
    }

    @Test
    public void testGetEligiblePlayers() {

        player1.placeBet(100);
        player2.placeBet(100);
        player3.fold();

        potManager.collectBets(Arrays.asList(player1, player2, player3));
        List<PokerPlayer> eligible = potManager.getEligiblePlayers(0);
        assertEquals(2, eligible.size());
        assertTrue(eligible.contains(player1));
        assertTrue(eligible.contains(player2));
    }

    @Test
    public void testReset() {

        player1.placeBet(100);
        player2.placeBet(100);

        potManager.collectBets(Arrays.asList(player1, player2));
        potManager.reset();
        assertEquals(0.0, potManager.getTotalPotSize());
        assertEquals(1, potManager.getNumberOfPots());
    }

    @Test
    public void testToString() {

        player1.placeBet(100);
        player2.placeBet(100);

        potManager.collectBets(Arrays.asList(player1, player2));
        String str = potManager.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("200"));
    }
}