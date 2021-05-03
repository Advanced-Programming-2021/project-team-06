package controller;

import models.Deck;
import models.Player;
import models.cards.Card;

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
