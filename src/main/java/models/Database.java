package models;

import models.cards.Card;

import java.util.ArrayList;

public class Database {

    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static ArrayList<Card> allCards = new ArrayList<>();


    private Database() {
    }

    private static Database instance;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public static Player getPlayerByUsername(String username){
        for(Player player : allPlayers) {
            if (player.getUsername().equals(username))
                return player;
        }
        return null;

    }

    public static Card getCardByName(String name){
        for(Card card : allCards) {
            if (card.getName().equals(name))
                return card;
        }
        return null;

    }


}
