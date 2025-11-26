package com.github.zipcodewilmington.casino.games.slots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SymbolSet {
    private List<Symbol> symbols;
    private ArrayList<Integer> distribution;

    public SymbolSet(List<Symbol> symbols, ArrayList<Integer> distribution) {
        this.symbols = symbols;
        this.distribution = distribution;
        
    }

    public static SymbolSet createVegaSymbolSet() {
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(new Symbol("🍒", 3, "Cherry"));
        symbols.add(new Symbol("🍋", 3, "Lemon"));
        symbols.add(new Symbol("🍊", 4, "Orange"));
        symbols.add(new Symbol("🍇", 5, "Grape"));
        symbols.add(new Symbol("🔔", 7, "Bell"));
        symbols.add(new Symbol("⭐", 8, "Seven"));
        symbols.add(new Symbol("7️⃣", 10, "Seven"));
        symbols.add(new Symbol("💎", 20, "Diamond"));

        ArrayList<Integer> distribution = new ArrayList<>();
        distribution.add(20); // Cherry 20%
        distribution.add(20); // Lemon 20%
        distribution.add(15); // Orange 15%
        distribution.add(12); // Grape 12%
        distribution.add(10); // Bell 10%
        distribution.add(8); // Star 8%
        distribution.add(10); // Seven 10%
        distribution.add(5); // Diamond 5%

        return new SymbolSet(symbols, distribution);
    }
}
