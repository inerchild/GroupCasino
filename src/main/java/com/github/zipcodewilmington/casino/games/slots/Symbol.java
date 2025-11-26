package com.github.zipcodewilmington.casino.games.slots;

public class Symbol {
    private String icon;
    private int multiplier;
    private String name;

    public Symbol (String icon, int multiplier, String name){
        this.icon = icon;
        this.multiplier = multiplier;
        this.name = name;
    }

    public String getIcon() {
            return icon;
    }

    public int getMultiplier() {
            return multiplier;
    }

    public String getName() {
            return name;
    }

    @Override
    public String toString() {
        return name + " " + icon + " (" + multiplier + "x)";
    }
}
