package com.github.zipcodewilmington.casino.games.Poker;

    public enum HandType {                                              // represents all possible poker hand types
        HIGH_CARD(1, "High Card"),                    // each type has a rank value
        PAIR(2, "Pair"),                              // ordered lowest (HIGH_CARD) to highest (ROYAL_FLUSH)
        TWO_PAIR(3, "Two Pair"),
        THREE_OF_A_KIND(4, "Three of a Kind"),
        STRAIGHT(5, "Straight"),
        FLUSH(6, "Flush"),
        FULL_HOUSE(7, "Full House"),
        FOUR_OF_A_KIND(8, "Four of a Kind"),
        STRAIGHT_FLUSH(9, "Straight Flush"),
        ROYAL_FLUSH(10, "Royal Flush");

        private final int rank;
        private final String displayName;

        HandType(int rank, String displayName) {                        // Constructor for HandType enum
            this.rank = rank;                                           // rank = numerical rank of this hand
            this.displayName = displayName;                             // displayName = readable name for display
        }

        public int getRank() {                                          // gets numerical rank for this hand type
            return rank;
        }

        public String getDisplayName() {                                // gets readable name for this hand type
            return displayName;
        }
    }