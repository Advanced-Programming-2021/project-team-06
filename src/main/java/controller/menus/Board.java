package controller.menus;

import models.Deck;
import models.Player;
import models.cards.Card;

public class Board {
    private Deck deckZone;
    private Deck hand;
    private Deck graveyardZone;
    private Deck banishedZone;
    private Deck monsterZone;
    private Deck spellZone;
    private Card fieldZone;

    public Board(Player player) {
        deckZone = new Deck("deckZone" + player.getUsername(), player);
        hand = new Deck("hand" + player.getUsername(), player);
        graveyardZone = new Deck("graveyardZone" + player.getUsername(), player);
        banishedZone = new Deck("banishedZone" + player.getUsername(), player);
        monsterZone = new Deck("monsterZone" + player.getUsername(), player);
        spellZone = new Deck("spellZone" + player.getUsername(), player);
    }

    public Card getFieldZone() {
        return fieldZone;
    }

    public void setFieldZone(Card fieldZone) {
        this.fieldZone = fieldZone;
    }

    public Deck getDeckZone() {
        return deckZone;
    }

    public void setDeckZone(Deck deckZone) {
        this.deckZone = deckZone;
    }

    public Deck getBanishedZone() {
        return banishedZone;
    }

    public void setBanishedZone(Deck banishedZone) {
        this.banishedZone = banishedZone;
    }

    public Deck getGraveyardZone() {
        return graveyardZone;
    }

    public void setGraveyardZone(Deck graveyardZone) {
        this.graveyardZone = graveyardZone;
    }

    public Deck getHand() {
        return hand;
    }

    public void setHand(Deck hand) {
        this.hand = hand;
    }

    public Deck getMonsterZone() {
        return monsterZone;
    }

    public void setMonsterZone(Deck monsterZone) {
        this.monsterZone = monsterZone;
    }

    public Deck getSpellZone() {
        return spellZone;
    }

    public void setSpellZone(Deck spellZone) {
        this.spellZone = spellZone;
    }

}
