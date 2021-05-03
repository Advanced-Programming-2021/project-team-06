package models;

import models.cards.Card;
import models.cards.Monster;

import java.util.ArrayList;

public class Deck {
    public ArrayList<Card> mainCards = new ArrayList<>();
    public ArrayList<Card> sideCards = new ArrayList<>();
    Player owner;
    String name;
    DeckType type;
    Boolean isActive;
    Boolean IsValid;

    public Deck(String name, Player owner) {
        this.name = name;
        Database.allDecks.add(this);
        this.owner = owner;
    }

    public ArrayList<Card> getMainCards() {
        return mainCards;
    }

    public void setMainCards(ArrayList<Card> mainCards) {
        this.mainCards = mainCards;
    }

    public ArrayList<Card> getSideCards() {
        return sideCards;
    }

    public void setSideCards(ArrayList<Card> sideCards) {
        this.sideCards = sideCards;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActivation(Boolean active) {
        isActive = active;
    }

    public void setValid(Boolean valid) {
        IsValid = valid;
    }

    public void setType(DeckType type) {
        this.type = type;
    }

    public void addCard(Card card, boolean shouldBeAddedToMain) {
        if (shouldBeAddedToMain)
            mainCards.add(card);
        else
            sideCards.add(card);
    }

    public void removeCard(Card card, boolean shouldBeRemovedFromMain) {
        if (shouldBeRemovedFromMain)
            mainCards.remove(card);
        else
            sideCards.remove(card);
    }

    public int getSumOfAttackPowers() {
        int sum = 0;
        for (Card card : mainCards) {
            Monster monster = (Monster) card;
            sum += monster.getAttackPower();
        }
        return sum;
    }

    public int getNumberOfCardsInDeck(Card card) {
        int count = 0;
        for (Card cardInDeck : mainCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        for (Card cardInDeck : sideCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        return count;
    }

    public int getNumberOfCardsInMainDeck() {
        return mainCards.size();
    }

    public int getNumberOfCardsInSideDeck() {
        return sideCards.size();
    }

    public String toString(boolean isMain) {
        StringBuilder output = new StringBuilder();
        if (isMain)
            for (Card card : mainCards)
                output.append(card.toString());
        else
            for (Card card : sideCards)
                output.append(card.toString());
        return output.toString();
    }
}
