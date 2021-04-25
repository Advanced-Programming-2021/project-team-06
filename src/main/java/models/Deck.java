package models;

import models.cards.Card;
import models.cards.Monster;

import java.util.ArrayList;

public class Deck {
    static ArrayList<Deck> allDecks;

    static {
        allDecks = new ArrayList<Deck>();
    }

    public ArrayList<Card> cards;
    Player owner;
    String name;
    DeckType type;
    Boolean isActive;
    Boolean IsValid;

    public Deck(String name) {
        setName(name);
        setActivation(false);
        setCards(this.cards);
        setType(this.type);
        setValid(false);
    }

    public static Deck getDeckByName(String name) {
        for (Deck deck : allDecks) {
            if (name.equals(deck.getName())) {
                return deck;
            }
        }
        return null;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
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

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

   public int getSumOfAttackPowers() {
        int sum = 0;
        for (Card card : cards) {
            Monster monster = (Monster)card;
            sum += monster.getATTACK_POWER();
        }
        return sum;
    }
}
