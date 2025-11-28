package com.github.zipcodewilmington.casino.games.slots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wheel {
    private List<Symbol> reelStrip;
    private Symbol currentSymbol;
    private Random random;
    
    //Creates a wheel with symbols from the given SymbolSet
    public Wheel(SymbolSet symbolSet, int reelLength) {
        this.reelStrip = new ArrayList<>();
        this.random = new Random();
        buildReelStrip(symbolSet, reelLength);
        this.currentSymbol = reelStrip.get(0);
    }

    //Builds the physical reel strip with weighted symbol distribution
    private void buildReelStrip(SymbolSet symbolSet, int reelLength) {
        for (int i = 0; i < reelLength; i++) {
            reelStrip.add(symbolSet.getRandomSymbol());
        }
    }

    //Spins the wheel and returns the landed symbol
    public Symbol spin() {
        int randomPosition = random.nextInt(reelStrip.size());
        currentSymbol = reelStrip.get(randomPosition);
        return currentSymbol;
    }

    //Gets current symbol the wheel is showing
    public Symbol getCurrentSymbol() {
        return currentSymbol;
    }

    //Gets the entire reel strip 
    public List<Symbol> getReelStrip() {
        return new ArrayList<>(reelStrip);
    }

    //Gets the size of reel strip
    public int getReelSize() {
        return reelStrip.size();
    }

    @Override
    public String toString() {
        return "Wheel showing: " + currentSymbol.getIcon() + " (" + currentSymbol.getName() + ")";
    }
}
