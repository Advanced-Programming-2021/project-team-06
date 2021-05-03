package models;

import com.google.gson.*;
import models.cards.Card;

import java.lang.reflect.Type;
import java.util.*;

public class Player {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int rank;
    private int health;
    private ArrayList<Card> allPlayerCard = new ArrayList<>();
    private ArrayList<Deck> allDeck = new ArrayList<>();
    private ArrayList<Deck> gameDecks = new ArrayList<>();
    private Deck activeDeck;
    private Deck sideDeck;
    private int roundsWon;
    private int money;

    public Player(String username, String nickname, String password) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = 0;
        this.health = 5000;
        this.roundsWon = 0;
        this.money = 0;
        Database.allPlayers.add(this);
    }

    public static Player getUsernameByPlayer(String username) {
        for (Player playerUser : Database.allPlayers) {
            if (username.equals(playerUser.getUsername())) {
                return playerUser;
            }
        }
        return null;
    }

    public static PlayerSerializerForDeckDatabase getPlayerSerializerForDeck() {
        return new PlayerSerializerForDeckDatabase();
    }

    public static PlayerDeserializerForDeckDatabase getPlayerDeserializerForDeck() {
        return new PlayerDeserializerForDeckDatabase();
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
    }

    public void setSideDeck(Deck sideDeck) {
        this.sideDeck = sideDeck;
    }

    public void increaseScore(int score) {
        setScore(this.score + score);
    }

    public void decreaseScore(int score) {
        setScore(this.score - score);
    }

    public ArrayList<Deck> getAllDeck() {
        return allDeck;
    }

    public void setAllDeck(ArrayList<Deck> allDeck) {
        this.allDeck = allDeck;
    }

    public ArrayList<Card> getAllPlayerCard() {
        return allPlayerCard;
    }

    public void addCardToAllPlayerCard(Card card) {
        this.allPlayerCard.add(card);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void deleteDeck(Deck deck) {
        allDeck.remove(deck);
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", score=" + score +
                '}';
    }
}

class PlayerSerializerForDeckDatabase implements JsonSerializer<Player> {
    @Override
    public JsonElement serialize(Player player, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(player.getUsername());
    }
}

class PlayerDeserializerForDeckDatabase implements JsonDeserializer<Player> {
    @Override
    public Player deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Database.getInstance().getPlayerByUsername(jsonElement.getAsString());
    }
}
