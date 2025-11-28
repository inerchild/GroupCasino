package com.github.zipcodewilmington.casino.games.slots;

import java.util.ArrayList;
import java.util.List;

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
                if (lastSpin.get(i).getName().equals(lastSpin.get(j).getName())) {
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
        
        for (Symbol symbol : lastSpin) {
            if (!symbol.getName().equals(firstName)) {
                return false;
            }
        }
        return true;
    }

    //Checks if the spin contains a Bomb symbol
    public boolean hasBomb() {
        for (Symbol symbol : lastSpin) {
            if (symbol.getName().equals("Bomb")) {
                return true;
            }
        }
        return false;
    }

    //Checks if the spin contains a SkullOfDoom symbol
    public boolean hasSkullOfDoom() {
        for (Symbol symbol : lastSpin) {
            if (symbol.getName().equals("SkullOfDoom")) {
                return true;
            }
        }
        return false;
    }

    //Calculates the payout for the last spin
    public double calculatePayout() {
        if (lastSpin.isEmpty()) {
            return 0.0;
        }

        //Checks for bombs - instant loss of bet
        if (hasBomb()) {
            return -currentBet;
        }

        //Checks for SkullOfDoom - GAME OVER easter egg
        if (hasSkullOfDoom()) {
            return 0.0; //Wont actually be reached
        }

        //Check for jackpot (all symbols match)
        if (isJackpot()) {
            int multiplier = lastSpin.get(0).getMultiplier();
            return currentBet * multiplier * numberOfWheels;
        }

        //Checks for any matches
        if (isWin()) {
            //Find the highest multiplier among matching symbols
            int highestMultiplier = 0;

            for (int i = 0; i < lastSpin.size() - 1; i++) {
                for (int j = i + 1; j < lastSpin.size(); j++) {
                    if (lastSpin.get(i).getName().equals(lastSpin.get(j).getName())) {
                        int multiplier = lastSpin.get(i).getMultiplier();
                        if (multiplier > highestMultiplier) {
                            highestMultiplier = multiplier;
                        }    
                    }
                }
            }
            return currentBet * highestMultiplier;
        }
        //No win
        return 0.0;
    }

    //Gets the current bet amount
    public double getCurrentBet() {
        return currentBet;
    }

    //Gets the number of wheels on this machine
    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    //Gets all wheels
    public List<Wheel> getWheels() {
        return new ArrayList<>(wheels);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SlotMachine [").append(numberOfWheels).append(" wheels]\n");
        sb.append("Current Bet: $").append(String.format("%.2f", currentBet)).append("\n");

        if (!lastSpin.isEmpty()) {
            sb.append("Last Spin: ");
            for (Symbol s : lastSpin) {
                sb.append(s.getIcon()).append(" ");
            }
        }
    
        return sb.toString();
    }
}


    
