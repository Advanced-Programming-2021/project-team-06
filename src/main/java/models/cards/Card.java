package models.cards;


import com.google.gson.annotations.SerializedName;
import models.Deck;
import models.Player;

import java.util.ArrayList;

public class Card {
    @SerializedName("Name")
    protected String name;
    protected String overriddenName;
    @SerializedName("Card Type")
    protected CardType type;
    @SerializedName("Description")
    protected String description;
    protected String overriddenDescription;
    protected Deck currentDeck;
    protected ArrayList<PlayType> possiblePlays = new ArrayList<>();
    protected Deck effectedCards;
    @SerializedName("Price")
    protected int price;

    public Card(String name) {
        this.name = name;
        setPrice(this.price);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    @Override
    public String toString() {
        return name + ':' + description + '\n';
    }

}
