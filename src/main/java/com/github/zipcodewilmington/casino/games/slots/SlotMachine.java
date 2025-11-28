package com.github.zipcodewilmington.casino.games.slots;

import java.util.List;

import.java.util.ArrayList;
import.java.util.List;

public class SlotMachine {
    private List<Wheel> wheels;
    private SymbolSet symbolSet;
    private int numberOfWheels;
    private double currentBet;
    private List<Symbol> lastSpin;

    //Creates a slot machine with the specified number of wheels
    public SlotMachine(SymbolSet symbolSet, int numberOfWheels, int reelLength) {
        this.symbolSet = symbolSet;
        this.numberOfWheels = numberOfWheels;
        this.wheels = new ArrayList<>();
        this.lastSpin = new ArrayList<>();
        this.currentBet = 0.0;

        //Creates all the wheels
        for (int i = 0; i < numberOfWheels; i++) {
            wheels.add(new Wheel(symbolSet, reelLength));
        }
    }

    //Convenience constructor for 3-reel slot machine
    public SlotMachine(SymbolSet symbolSet) {
        this(symbolSet, 3, 30);
    }

    //Places bet for next spin
    public void placeBet(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bet must be positive");
        }
        this.currentBet = amount;
    }

    //Spins all wheels and returns results
    public List<Symbol> spin() {
        lastSpin.clear();
        for (Wheel wheel : wheels) {
            lastSpin.add(wheel.spin());
        }
        return new ArrayList<>(lastSpin);
    }

    //Gets results of last spin
    public List<Symbol> getLastSpin() {
        return new ArrayList<>(lastSpin);
    }

    //Checks if last spin was a winning combination
    public boolean isWin() {
        if (lastSpin.isEmpty() || lastSpin.size() < 2) {
            return false;
        }

        //Check for any matching symbols
        for (int i = 0; i < lastSpin.size() - 1; i++) {
            for (int j = i + 1; j < lastSpin.size(); j++) {
                if (lastSpin.get(i).getName().equals(lastSpin.get(j).getname())) {
                    //Not counting Bomb or SkullOfDoom as wins
                    String name = lastSpin.get(i).getName();
                    if (!name.equals("Bomb") && !name.equals("SkullOfDoom")) {
                       return true; 
                    }
                }
            }
        }
        return false;
    }

    //Checks if all symbols match (jackpot)
    public boolean isJackpot() {
        if (lastSpin.isEmpty() || lastSpin.size() < 2) {
            return false;
        }

        String firstName = lastSpin.get(0).getName();
        //Bombs and SkullOfDoom are not jackpots
        if (firstName.equals("Bomb") || firstName.equals("SkullOfDoom")) {
            return false;
        }
        }
        return true;
}

    
}
