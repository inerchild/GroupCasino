package com.github.zipcodewilmington.casino.games.blackjack;

import java.util.ArrayList;
import java.util.List;
import com.github.zipcodewilmington.utils.Card;


public class BlackjackHand {
    
    private List<Card> cards;
    
    public BlackjackHand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    private int getBlackjackValue(Card card){
        switch (card.getRank()) {
            case ACE:
                return 11;
            case KING:
            case QUEEN:
            case JACK:
                return 10;
            default:
                return card.getRank().getValue();
        }
    }

    public int getValue() {
        int total = 0;
        int aceCount = 0;

        for(Card card : cards) {
            total += getBlackjackValue(card);
            if (card.getRank() == com.github.zipcodewilmington.utils.Rank.ACE) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getValue() == 21;
    }

    public boolean isBusted() {
        return getValue() > 21;
    }

    public boolean isSoft() {
    int aceCount = 0;
    int total = 0;

    for (Card card : cards) {
        total += getBlackjackValue(card);
        if (card.getRank() == com.github.zipcodewilmington.utils.Rank.ACE){
            aceCount++;
        }
    }

    while (total > 21 && aceCount > 0) {
        total -= 10;
        aceCount--;
    }
    
    return aceCount > 0 && total <= 21;
}

    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public int size() {
        return cards.size();
    }

    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "[Empty hand]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i));
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] = ").append(getValue());

        if (isSoft()) {
            sb.append(" (soft)");
        }
        return sb.toString();
    }

    public BlackjackHand split() {
        if (!canSplit()) {
            throw new IllegalStateException("Cannot split this hand - must have exactly 2 cards of the same rank");
        }
        BlackjackHand newHand = new BlackjackHand();
        newHand.addCard(cards.remove(1));

        return newHand;
    }

}
