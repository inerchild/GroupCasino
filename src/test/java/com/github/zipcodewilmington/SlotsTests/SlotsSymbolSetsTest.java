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
        assertTrue(s.getMultiplier() >= 0);
    }

    @Test
    public void testDistribution() {
        int[] counts = new int[10]; 

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
                case "Bomb": counts[8]++; break;
                case "SkullOfDoom": counts[9]++; break;

            }
        }

        assertTrue(counts[0] >= 150 && counts[0] <= 250,
            "Cherry should appear ~20% of time, got: " + counts[0]);
        
        assertTrue(counts[7] >= 20 && counts[7] <= 80,
            "Diamond should appear ~5% of time, got: " + counts[7]);

        assertTrue(counts[8] >= 20 && counts[8] <= 60, "Bomb should appear ~4%");
        assertTrue(counts[9] >= 5 && counts[9] <= 40, "SkullOfDoom should appear ~2%");

        System.out.println("Cherry: " + counts[0] + " (expected ~170)");
        System.out.println("Lemon: " + counts[1] + " (expected ~170)");
        System.out.println("Orange: " + counts[2] + " (expected ~150)");
        System.out.println("Grape: " + counts[3] + " (expected ~120)");
        System.out.println("Bell: " + counts[4] + " (expected ~100)");
        System.out.println("Star: " + counts[5] + " (expected ~80)");
        System.out.println("Seven: " + counts[6] + " (expected ~100)");
        System.out.println("Diamond: " + counts[7] + " (expected ~50)");
        System.out.println("💣 Bomb: " + counts[8] + " (expected ~40)");
        System.out.println("☠️ SkullOfDoom: " + counts[9] + " (expected ~20");
    }

    @Test
    public void testGetAllSymbols() {
        assertEquals(10, vegas.getAllSymbols().size());
    }

    @Test 
    public void testBombSymbolExists() {
        SymbolSet vegas = SymbolSet.createVegaSymbolSet();

        boolean foundBomb = false;
        for (int i = 0; i < 1000; i++) {
            Symbol s = vegas.getRandomSymbol();
            if (s.getName().equals("Bomb")) {
                foundBomb = true;
                assertEquals(0, s.getMultiplier(), "Bomb should have 0 multiplier");
                assertEquals("💣", s.getIcon());
                System.out.println("💣 Found Bomb symbol!");
                break;
            }
        }
        assertTrue(foundBomb, "Bomb should appear in spins");
    }

    @Test
    public void testSkullOfDoomSymbolExists() {
        SymbolSet vegas = SymbolSet.createVegaSymbolSet();

        boolean foundSkullOfDoom = false;
        for (int i = 0; i < 2000; i++) {
            Symbol s = vegas.getRandomSymbol();
            if (s.getName().equals("SkullOfDoom")) {
                foundSkullOfDoom = true;
                assertEquals(0, s.getMultiplier(), "SkullOfDoom should have 0 multiplier");
                assertEquals("☠️", s.getIcon());
                System.out.println("☠️ Found SkullOfDoom easter egg after " + i + " spins!");
                break;
            }
        }
        assertTrue(foundSkullOfDoom, "SkullOfDoom should appear in spins (rare but possible)");
    }
    
}