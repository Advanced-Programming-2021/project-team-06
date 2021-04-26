package models.cards;


import models.Deck;

import java.util.ArrayList;

public class Card {
    String name;
    private String overriddenName;
    private CardType type;
    private String description;
    private String overriddenDescription;
    private Deck currentDeck;
    private ArrayList<PlayType> possiblePlays = new ArrayList<>();
    private Deck effectedCards;

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    public void setEffectedCards(Deck effectedCards) {
        this.effectedCards = effectedCards;
    }

    public void setPossiblePlays(ArrayList<PlayType> possiblePlays) {
        this.possiblePlays = possiblePlays;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void goTo(Deck deck) {
        setCurrentDeck(deck);
    }

    public Boolean isLike(Card card) {

        return true;
    }
}
