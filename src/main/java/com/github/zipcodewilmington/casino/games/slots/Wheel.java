package com.github.zipcodewilmington.casino.games.slots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wheel {
    private List<Symbol> reelStrip;
    private Symbol currentSymbol;
    private Random random;

    public Wheel(SymbolSet symbolSet, int reelLength) {
        this.reelStrip = new ArrayList<>();
        this.random = new Random();
        buildReelStrip(symbolSet, reelLength);
        this.currentSymbol = reelStrip.get(0);
    }

    private void buildReelStrip(SymbolSet symbolSet, int reelLength) {
        for (int i = 0; i < reelLength; i++) {
            reelStrip.add(symbolSet.getRandomSymbol());
        }
    }

    public Symbol spin() {
        int randomPosition = random.nextInt(reelStrip.size());
        currentSymbol = reelStrip.get(randomPosition);
        return currentSymbol;
    }

    public Symbol getCurrentSymbol() {
        return currentSymbol;
    }

    public List<Symbol> getReelStrip() {
        return new ArrayList<>(reelStrip);
    }

    public int getReelSize() {
        return reelStrip.size();
    }

    @Override
    public String toString() {
        return "Wheel showing: " + currentSymbol.getIcon() + " (" + currentSymbol.getName() + ")";
    }
}
