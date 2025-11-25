package com.github.zipcodewilmington.utils;

public enum Suit {
    CLUBS(1), DIAMONDS(2), HEARTS(3), SPADES(4);

    private final int power;
    Suit(int power) { this.power = power; }
    public int getPower() { return power; }
}
