package models;

import java.util.*;

public class Player {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int rank;
    private int health;
    private ArrayList<Deck> allDeck = new ArrayList<>();
    private ArrayList<Deck> gameDecks = new ArrayList<>();
    private Deck activeDeck;
    private Deck sideDeck;
    private int roundsWon;

    public Player(String username, String nickname, String password) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = 0;
        this.health = 5000;
        this.roundsWon = 0;
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
