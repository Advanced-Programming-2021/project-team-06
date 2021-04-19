package models;

import java.util.ArrayList;

public class Player {
    public static ArrayList<Player> allPlayers = new ArrayList<>();

    private String username;
    private String password;
    private String nickname;
    private int score;
    private int health;
    private ArrayList<Deck> allDeck = new ArrayList<>();
    private ArrayList<Deck> gameDecks = new ArrayList<>();
    private Deck activeDeck;
    private Deck sideDeck;
    private int roundsWon;


    public Player(String username, String password){
        this.username = username;
        this.password = password;
        this.score = 0;
        allPlayers.add(this);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Deck> getAllDeck() {
        return allDeck;
    }

    public void setAllDeck(ArrayList<Deck> allDeck) {
        this.allDeck = allDeck;
    }
}
