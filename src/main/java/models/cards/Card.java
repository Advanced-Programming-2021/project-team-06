package models.cards;


import models.Deck;

import java.util.ArrayList;

public class Card {
    private static Deck ALL_CARDS_TEMPLATES;
    String name;
    private String overriddenName;
    private CardType type;
    private String DESCRIPTION;
    private String overriddenDescription;
    private Deck currentDeck;
    private ArrayList<PlayType> possiblePlays = new ArrayList<>();
    private Deck effectedCards;

    public Card(String name) {
        this.name = name;
        ALL_CARDS_TEMPLATES.addCard(this);
        setType(this.type);
        setDESCRIPTION(this.DESCRIPTION);
        setCurrentDeck(this.currentDeck);
        setEffectedCards(this.effectedCards);
        setPossiblePlays(this.possiblePlays);
    }

    public static void setAllCardsTemplates(Deck allCardsTemplates) {
        ALL_CARDS_TEMPLATES = allCardsTemplates;
    }

    public static Card getCardByName(String cardName) {
        for (Card card : ALL_CARDS_TEMPLATES.getCards()) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        return null;
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

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }
    public void goTo(Deck deck){
        setCurrentDeck(deck);
    }

    public Boolean is(CardType type){
        return type == this.type;
    }
}
