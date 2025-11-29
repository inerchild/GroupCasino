package com.github.zipcodewilmington.SlotsTests;

import com.github.zipcodewilmington.casino.games.slots.SlotMachine;
import com.github.zipcodewilmington.casino.games.slots.Symbol;
import com.github.zipcodewilmington.casino.games.slots.SymbolSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SlotMachineTest {

    private SlotMachine slotMachine;
    private SymbolSet symbolSet;

    @BeforeEach
    public void setUp() {
        symbolSet = SymbolSet.createVegaSymbolSet();
        slotMachine = new SlotMachine(symbolSet);
    }

    @Test
    public void testSlotMachineCreation() {
        assertNotNull(slotMachine);
        assertEquals(3, slotMachine.getNumberOfWheels());
    }

    @Test
    public void testCumstomWheelCount() {
        SlotMachine fiveReelMachine = new SlotMachine(symbolSet, 5, 30);
        assertEquals(5, fiveReelMachine.getNumberOfWheels());
    }

    @Test
    public void testPlaceBet() {
        slotMachine.placeBet(10.0);
        assertEquals(10.0, slotMachine.getCurrentBet());
    }

    @Test
    public void testPLaceBetNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            slotMachine.placeBet(0.0);
        });
    }

    @Test
    public void testSpin() {
        List<Symbol> result = slotMachine.spin();
        assertNotNull(result);
        assertEquals(3, result.size());

        for (Symbol symbol : result) {
            assertNotNull(symbol);
            assertNotNull(symbol.getName());
            assertNotNull(symbol.getIcon());
        }
    }

    @Test
    public void testGetLastSpin() {
        slotMachine.spin();
        List<Symbol> lastSpin = slotMachine.getLastSpin();
        
        assertNotNull(lastSpin);
        assertEquals(3, lastSpin.size());
    }

    @Test
    public void testMultipleSpins() {
        List<Symbol> spin1 = slotMachine.spin();
        List<Symbol> spin2 = slotMachine.spin();

        assertNotNull(spin1);
        assertNotNull(spin2);
        assertEquals(3, spin1.size());
        assertEquals(3, spin2.size());
    }

    @Test
    public void testIsWinDetection() {
        boolean foundWin = false;

        for (int i = 0; i < 100; i++) {
            slotMachine.spin();
            if (slotMachine.isWin()) {
                foundWin = true;
                System.out.prinln("Found a winning spin!");
                break;
            }
        }
        
        assertTrue(foundWin, "Should eventually find a winning combination");
    }

    @Test 
    public void testIsJackpotDetection() {
        boolean foundJackpot = false;

        for (int i = 0; i < 500; i++) {
            slotMachine.spin();
            if (slotMachine.isJackpot()) {
                foundJackpot = true;
                List<Symbol> jackpotSpin = slotMachine.getLastSpin();
                System.out.println("JACKPOT! All " + jackpotSpin.get(0).getName() + "s!");
                break;
            }
        }

        assertTrue(foundJackpot, "Should eventually find a jackpot (may take many spins)");
    }

    @Test
    
}
