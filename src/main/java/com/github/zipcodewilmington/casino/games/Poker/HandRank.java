package com.github.zipcodewilmington.casino.games.Poker;
import java.util.ArrayList;
import java.util.List;

    public class HandRank implements Comparable<HandRank> {                      // *******represents an evaluated poker hand with all info needed to compare
        private final HandType handType;                                                // handtype (pair, flush, straight, etc)
        private final List<Integer> primaryValues;                                      // Primary Values (the cards that make the hand)
        private final List<Integer> kickers;                                            // Kickers (remaining cards used for tie breakers)

        public HandRank(HandType handType, List<Integer> primaryValues, List<Integer> kickers) {    // constructor
            this.handType = handType;
            this.primaryValues = new ArrayList<>(primaryValues);
            this.kickers = new ArrayList<>(kickers);
        }

        public HandType getHandType() {                                             // gets hand type
            return handType;
        }

        public List<Integer> getPrimaryValues() {                                   // gets prirmary values
            return new ArrayList<>(primaryValues);
        }

        public List<Integer> getKickers() {                                         // gets kicker values
            return new ArrayList<>(kickers);
        }

        @Override                                                                       // ********compares this hand to another to determine winner
        public int compareTo(HandRank other) {    // <-------- compare hand types               // first compare hand types (flush beats straight)
            int typeComparison = this.handType.compareTo(other.handType);                       // if same hand type, compare primary values (Pair of aces beats pair of Kings)
            if (typeComparison != 0) {                                                          // if primary values equal, compare kickers one by one
                return typeComparison;         // <----- different hand types, returns winner
            }

        int primaryComparison = compareLists(this.primaryValues, other.primaryValues);  // <-------- compare primary values
        if (primaryComparison != 0) {
            return primaryComparison;                // <------- different primary values, returns winner
            }
        return compareLists(this.kickers, other.kickers);     // <-------- compare kickers, returns better kicker as winner
    }

    private int compareLists(List<Integer> list1, List<Integer> list2) {                  // **********Helper method to compare two lists of integers element by element
        int minLength = Math.min(list1.size(), list2.size());                                       // used to compare primary values and kickers
        for (int i = 0; i < minLength; i++) {                                                       // compares first element of each list, then second, etc.
            int comparison = Integer.compare(list1.get(i), list2.get(i));
            if (comparison != 0) {
                return comparison;  // <----- compare lists, difference found
            }
        }
        return Integer.compare(list1.size(), list2.size());   // <------- edge case (shouldnt happen, covering ass: list with more cards wins)
    }

    public String getDescription() {                    // <----------- returns readable description (pair of kings, etc.)
        StringBuilder sb = new StringBuilder();
        sb.append(handType.getDisplayName());

        if (!primaryValues.isEmpty()) {                 // <------------ add primary value descriptions for clarity
            sb.append(" (");
            for (int i = 0; i < primaryValues.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(getRankName(primaryValues.get(i)));
            }
            sb.append(")");
        }
        return sb.toString();
    }

    private String getRankName(int value) {             // ***********Helper method to convert a card value to its rank name
        switch (value) {
            case 14: return "Ace";
            case 13: return "King";
            case 12: return "Queen";
            case 11: return "Jack";
            default: return String.valueOf(value);
        }
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override                                                       // check if two hands are equal (same type, primary value, kickers)
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (obj == null || getClass() != obj.getClass()) 
            return false;
        HandRank other = (HandRank) obj;
        return this.handType == other.handType &&
               this.primaryValues.equals(other.primaryValues) &&
               this.kickers.equals(other.kickers);
    }

    @Override                                                           // Generates a hash code for this HandRank
    public int hashCode() {                                                 // if two HandRanks are equal, they will have the same hashcode
        int result = handType.hashCode();                                   // hash value is handType, primaryValues, kickers in one number
        result = 31 * result + primaryValues.hashCode();
        result = 31 * result + kickers.hashCode();
        return result;
    }
}

