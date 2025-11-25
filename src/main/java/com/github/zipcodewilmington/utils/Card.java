package com.github.zipcodewilmington.utils;

public class Card implements Comparable<Card> {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public int compareTo(Card other) {
        // Compare rank first
        int rankComparison = Integer.compare(this.rank.getValue(), other.rank.getValue());
        return rankComparison;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
