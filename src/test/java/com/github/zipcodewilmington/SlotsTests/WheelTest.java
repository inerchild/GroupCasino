package com.github.zipcodewilmington.SlotsTests;

import com.github.zipcodewilmington.casino.games.slots.Symbol;
import com.github.zipcodewilmington.casino.games.slots.SymbolSet;
import com.github.zipcodewilmington.casino.games.slots.Wheel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class WheelTest {
    private Wheel wheel;
    private SymbolSet symbolSet;

    @BeforeEach
    public void setUp() {
        symbolSet = SymbolSet.createVegaSymbolSet();
        wheel = new Wheel(symbolSet, 30);
    }

    @Test
    public void testWheelCreation() {
        assertNotNull(wheel);
        assertNotNull(wheel.getCurrentSymbol());
    }

    @Test
    public void testReelStripSize() {
        assertEquals(30, wheel.getReelSize());
    }

    @Test
    public void testSpin() {
        Symbol result = wheel.spin();
        assertNotNull(result);
        assertNotNull(result.getIcon());
        assertNotNull(result.getName());
        assertTrue(result.getMultiplier() >= 0);
    }

    @Test
    public void testMultipleSpins() {
        Symbol first = wheel.spin();
        Symbol second = wheel.spin();
        Symbol third = wheel.spin();

        assertNotNull(first);
        assertNotNull(second);
        assertNotNull(third);
        assertEquals(third, wheel.getCurrentSymbol());
    }

    @Test public void testSpinRandomness() {
        boolean foundDifferentSymbols = false;
        Symbol firstSpin = wheel.spin();

        for (int i = 0; i < 50; i++) {
            Symbol spin = wheel.spin();
            if (!spin.getName().equals(firstSpin.getName())) {
                foundDifferentSymbols = true;
                break;
            }
        }
        assertTrue(foundDifferentSymbols, "Spins should produce different symbols");
    }

    @Test
    public void testGetReelStrip() {
        assertEquals(30, wheel.getReelStrip().size());

        for (Symbol symbol : wheel.getReelStrip()) {
            assertNotNull(symbol);
            assertNotNull(symbol.getName());
            assertNotNull(symbol.getIcon());
        }
    }

    @Test
    public void testToString() {
        String wheelString = wheel.toString();
        assertNotNull(wheelString);
        assertTrue(wheelString.contains("Wheel showing:"));
    }

    @Test
    public void testDifferentReelLengths() {
        Wheel smallWheel = new Wheel(symbolSet, 10);
        Wheel largeWheel = new Wheel(symbolSet, 100);

        assertEquals(10, smallWheel.getReelSize());
        assertEquals(100, largeWheel.getReelSize());
    }
}
