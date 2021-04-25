package models;

import java.util.*;

public class Player {
    static Arraylist<Player> players;

    static {
        players = new ArrayList<>();
    }

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
        players.add(this);
        Database.allPlayers.add(this);
    }

    public static Player getUsernameByPlayer(String username) {
        for (Player playeruser : players) {
            if (username.equals(playeruser.getUsername())) {
                return playeruser;
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

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", score=" + score +
                '}';
    }
}
