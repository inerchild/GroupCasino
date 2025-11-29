package com.github.zipcodewilmington.PokerTests;

import com.github.zipcodewilmington.casino.games.Poker.NPCPlayer;
import com.github.zipcodewilmington.casino.games.Poker.PlayerAction;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NPCPlayerTest {
    
    @Test
    public void testConstructorAggressivePersonality() {
        NPCPlayer npc = new NPCPlayer("Bot1", NPCPlayer.Personality.AGGRESSIVE);

        assertEquals("Bot1", npc.getName());
        assertEquals(NPCPlayer.Personality.AGGRESSIVE, npc.getPersonality());
        assertTrue(npc.getBalance() >= 500 && npc.getBalance() <= 1500);

    }

    @Test
    public void testConstructorCOnservativePersonality() {
        NPCPlayer npc = new NPCPlayer("Bot2", NPCPlayer.Personality.CONSERVATIVE);

        assertEquals(NPCPlayer.Personality.CONSERVATIVE, npc.getPersonality());

    }

    @Test
    public void testMakeDecisionWhenNoChips() {
        NPCPlayer npc = new NPCPlayer("BrokeBot", NPCPlayer.Personality.AGGRESSIVE);
        npc.placeBet(npc.getBalance());

        PlayerAction action = npc.makeDecision(100, 200);

        assertEquals(PlayerAction.FOLD, action);
    }

    @Test
    public void testCalculateRaiseAmount() {
        NPCPlayer npc = new NPCPlayer("TestBot", NPCPlayer.Personality.AGGRESSIVE);

        double raiseAmount = npc.calculateRaiseAmount(50, 100);
        assertTrue(raiseAmount > 0);
        assertTrue(raiseAmount <= npc.getBalance());
    }

    @Test
    public void testMakeDecisionReturnsValidAction() {
        NPCPlayer npc = new NPCPlayer("TestBot", NPCPlayer.Personality.AGGRESSIVE);

        PlayerAction action = npc.makeDecision(0, 100);

        assertNotNull(action);
        assertTrue(action == PlayerAction.CHECK ||
                   action == PlayerAction.RAISE ||
                   action == PlayerAction.FOLD ||
                   action == PlayerAction.CALL);
    }

    @Test
    public void testCalculateRaiseAmountDoesntExceedBalance() {
        NPCPlayer npc = new NPCPlayer("TestBot", NPCPlayer.Personality.AGGRESSIVE);
        double balance = npc.getBalance();
        double raiseAmount = npc.calculateRaiseAmount(50, 10000);

        assertTrue(raiseAmount <= balance);
    }

    @Test
    public void testNPCCanREceiveCards() {
        NPCPlayer npc = new NPCPlayer("TestBot", NPCPlayer.Personality.CONSERVATIVE);

        npc.receiveHoleCard(new Card(Suit.SPADES, Rank.ACE));
        npc.receiveHoleCard(new Card(Suit.HEARTS, Rank.KING));

        assertEquals(2, npc.getHand().getHoleCards().size());
    }

    @Test
    public void testToStringIncludesPersonality() {
        NPCPlayer npc = new NPCPlayer("TestBot", NPCPlayer.Personality.AGGRESSIVE);

        String str = npc.toString();
        assertTrue(str.contains("AGGRESSIVE"));
        assertTrue(str.contains("TestBot"));
    }
}
