package com.github.zipcodewilmington.SlotsTests;

import com.github.zipcodewilmington.casino.games.slots.Symbol;
import com.github.zipcodewilmington.casino.games.slots.SymbolSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class SlotsSymbolSetsTest {
    
    private SymbolSet vegas;

    @BeforeEach
    public void setUp() {
        vegas = SymbolSet.createVegaSymbolSet();
    }

    @Test
    public void testCreateVegasSymbolSet() {
        assertNotNull(vegas);
    }

    @Test
    public void testGetRandomSymbol() {
        Symbol s = vegas.getRandomSymbol();
        assertNotNull(s);
        assertNotNull(s.getIcon());
        assertNotNull(s.getName());
        assertTrue(s.getMultiplier() > 0);
    }

    @Test
    public void testDistribution() {
        int[] counts = new int[8]; 

        for (int i = 0; i <1000; i++) {
            Symbol s = vegas.getRandomSymbol();

            switch(s.getName()) {
                case "Cherry": counts[0]++; break;
                case "Lemon": counts[1]++; break;
                case "Orange": counts[2]++; break;
                case "Grape": counts[3]++; break;
                case "Bell": counts[4]++; break;
                case "Star": counts[5]++; break;
                case "Seven": counts[6]++; break;
                case "Diamond": counts[7]++; break;

            }
        }

        assertTrue(counts[0] >= 150 && counts[0] <= 250,
            "Cherry should appear ~20% of time, got: " + counts[0]);
        
        assertTrue(counts[7] >- 20 && counts[7] <= 80,
            "Diamond should appear ~5% of time, got: " + counts[7]);

        System.out.println("Cherry: " + counts[0] + " (expected ~200)");
        System.out.println("Lemon: " + counts[1] + " (expected ~200)");
        System.out.println("Orange: " + counts[2] + " (expected ~150)");
        System.out.println("Grape: " + counts[3] + " (expected ~120)");
        System.out.println("Bell: " + counts[4] + " (expected ~100)");
        System.out.println("Star: " + counts[5] + " (expected ~80)");
        System.out.println("Seven: " + counts[6] + " (expected ~100)");
        System.out.println("Diamon: " + counts[7] + " (expected ~50)");
    }

    @Test
    public void testGetAllSymbols() {
        assertEquals(8, vegas.getAllSymbols().size());
    }

    @Test 
    
}