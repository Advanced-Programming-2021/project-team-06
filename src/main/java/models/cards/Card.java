package models.cards;


import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import controller.ActionExecutor;
import models.Database;
import models.Deck;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Card implements Cloneable{
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
    protected CardPlacement cardPlacement;
    protected ArrayList<ActionExecutor> effectedCards;
    @SerializedName("Price")
    protected int price;

    public Card(String name) {
        this.name = name;
        setPrice(this.price);
    }

    public CardPlacement getCardPlacement() {
        return cardPlacement;
    }

    public static CardSerializerForDeckDatabase getCardSerializerForDeck() {
        return new CardSerializerForDeckDatabase();
    }

    public static CardDeserializerForDeckDatabase getCardDeserializerForDeck() {
        return new CardDeserializerForDeckDatabase();
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

    public void setPossiblePlays(ArrayList<PlayType> possiblePlays) {
        this.possiblePlays = possiblePlays;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public void setCardPlacement(CardPlacement cardPlacement) {
        this.cardPlacement = cardPlacement;
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

    @Override
    public Card clone() throws CloneNotSupportedException
    {
        Card card = (Card) super.clone();
        card.setName(this.name);
        card.setType(this.type);
        card.setDescription(this.description);
        card.setPrice(this.price);
        return card;
    }
}

class CardSerializerForDeckDatabase implements JsonSerializer<Card> {

    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(card.getName());
    }
}

class CardDeserializerForDeckDatabase implements JsonDeserializer<Card> {

    @Override
    public Card deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Card card;
        String cardName = jsonElement.getAsString();
        card = Database.getInstance().getMonsterByName(cardName);
        if (card != null) {
            return card;
        }
        card = Database.getInstance().getSpellByName(cardName);
        if (card != null) {
            return card;
        }
        card = Database.getInstance().getTrapByName(cardName);
        if (card != null) {
            return card;
        }

        return null;
    }
}