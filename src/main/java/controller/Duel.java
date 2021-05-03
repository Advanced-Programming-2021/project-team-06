package controller;

import models.Player;

public class Duel {
    private Player firstPlayer;
    private Player secondPlayer;
    private int numberOfPhase;

    public Duel(Player firstPlayer, Player secondPlayer, int numberOfPhase) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.numberOfPhase = numberOfPhase;
    }
}
