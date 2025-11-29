package com.github.zipcodewilmington.casino.games.slots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SymbolSet {
    private List<Symbol> symbols;
    private ArrayList<Integer> distribution;
    private Random random;

    public SymbolSet(List<Symbol> symbols, ArrayList<Integer> distribution) {
        this.symbols = symbols;
        this.distribution = distribution;
        this.random = new Random();
        validateDistribution();
    }
    // gets random symbol based on distribution weights
    public Symbol getRandomSymbol() {
        int randomValue = random.nextInt(100);
        int cumlativeWeight = 0;

        for (int i = 0; i < symbols.size(); i++) {
            cumlativeWeight += distribution.get(i);
            if (randomValue < cumlativeWeight) {
                return symbols.get(i);
            }
        }
        return symbols.get(symbols.size() - 1); // Fallback (should never happen when distribution sums to 100)
    }

    // Gets all symbols in the set (useful for building reel strip)
    public List<Symbol> getAllSymbols() {
        return new ArrayList<>(symbols);
    }

    // validates that distribution add to 100
    private void validateDistribution() {
        int sum = 0;
        for (int weight : distribution) {
            sum += weight;
        }
        if (sum != 100) {
            throw new IllegalArgumentException("Distribution must sum to 100, got: " + sum);
        }
    }

    public static SymbolSet createVegaSymbolSet() {
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(new Symbol("🍒", 3, "Cherry"));
        symbols.add(new Symbol("🍋", 3, "Lemon"));
        symbols.add(new Symbol("🍊", 4, "Orange"));
        symbols.add(new Symbol("🍇", 5, "Grape"));
        symbols.add(new Symbol("🔔", 7, "Bell"));
        symbols.add(new Symbol("⭐", 8, "Star"));
        symbols.add(new Symbol("7️⃣", 10, "Seven"));
        symbols.add(new Symbol("💎", 20, "Diamond"));
        symbols.add(new Symbol("💣", 0, "Bomb"));  // Instant loss
        symbols.add(new Symbol("☠️", 0, "SkullOfDoom")); 

        ArrayList<Integer> distribution = new ArrayList<>();
        distribution.add(17); // Cherry 17%
        distribution.add(17); // Lemon 17%
        distribution.add(15); // Orange 15%
        distribution.add(12); // Grape 12%
        distribution.add(10); // Bell 10%
        distribution.add(8); // Star 8%
        distribution.add(10); // Seven 10%
        distribution.add(5); // Diamond 5%
        distribution.add(4); // Bomb 4%
        distribution.add(2); // DeathSkull 2%

        return new SymbolSet(symbols, distribution);
    }

    public static SymbolSet createPirateSymbolSet() {
    List<Symbol> symbols = new ArrayList<>();
    symbols.add(new Symbol("🪙", 3, "Gold"));
    symbols.add(new Symbol("💰", 3, "Treasure"));
    symbols.add(new Symbol("🗡️", 4, "Sword"));
    symbols.add(new Symbol("🏴‍☠️", 5, "Jolly Roger"));
    symbols.add(new Symbol("🦜", 7, "Parrot"));
    symbols.add(new Symbol("⚓", 8, "Anchor"));
    symbols.add(new Symbol("🏴", 10, "Black Flag"));
    symbols.add(new Symbol("💎", 20, "Jewel"));
    symbols.add(new Symbol("🌊", 0, "Kraken"));
    symbols.add(new Symbol("☠️", 0, "Davy Jones"));

    ArrayList<Integer> distribution = new ArrayList<>();
    distribution.add(17); distribution.add(17); distribution.add(15);
    distribution.add(12); distribution.add(10); distribution.add(8);
    distribution.add(10); distribution.add(5); distribution.add(4);
    distribution.add(2);

    return new SymbolSet(symbols, distribution);
}

public static SymbolSet createSpaceSymbolSet() {
    List<Symbol> symbols = new ArrayList<>();
    symbols.add(new Symbol("🌟", 3, "Star"));
    symbols.add(new Symbol("🌙", 3, "Moon"));
    symbols.add(new Symbol("🪐", 4, "Saturn"));
    symbols.add(new Symbol("🌎", 5, "Earth"));
    symbols.add(new Symbol("🚀", 7, "Rocket"));
    symbols.add(new Symbol("👽", 8, "Alien"));
    symbols.add(new Symbol("🛸", 10, "UFO"));
    symbols.add(new Symbol("💫", 20, "Supernova"));
    symbols.add(new Symbol("☄️", 0, "Asteroid"));
    symbols.add(new Symbol("🕳️", 0, "Black Hole"));

    ArrayList<Integer> distribution = new ArrayList<>();
    distribution.add(17); distribution.add(17); distribution.add(15);
    distribution.add(12); distribution.add(10); distribution.add(8);
    distribution.add(10); distribution.add(5); distribution.add(4);
    distribution.add(2);

    return new SymbolSet(symbols, distribution);
}
}
