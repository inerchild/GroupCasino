package com.github.zipcodewilmington.casino.games.blackjack;

import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.Deck;

public class BlackjackDealer {
    
    private BlackjackHand hand;

    public BlackjackDealer() {
        this.hand = new BlackjackHand();
    }

    public BlackjackHand getHand() {
        return hand;
    }

    public int getHandValue() {
        return hand.getValue();
    }

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void hit(Deck deck) {
        Card newCard = deck.drawCard();
        hand.addCard(newCard);
    }

    public Card getUpCard() {
        if (hand.size() > 0) {
            return hand.getCards().get(0);
        }
        return null;
    }

    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }

    public boolean shouldDealerCheckForBlackjack() {
        Card upCard = getUpCard();
        if (upCard == null) {
            return false;
        }

        com.github.zipcodewilmington.utils.Rank rank = upCard.getRank();
            return rank == com.github.zipcodewilmington.utils.Rank.ACE ||
                   rank == com.github.zipcodewilmington.utils.Rank.TEN ||
                   rank == com.github.zipcodewilmington.utils.Rank.JACK ||
                   rank == com.github.zipcodewilmington.utils.Rank.QUEEN||
                   rank == com.github.zipcodewilmington.utils.Rank.KING;
    }

    public void playTurn(Deck deck) {
        while (hand.getValue() < 17 || (hand.getValue() == 17 && hand.isSoft())) {
            Card newCard = deck.drawCard();
            hand.addCard(newCard);
        }
    }



}
