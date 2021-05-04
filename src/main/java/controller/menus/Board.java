package controller.menus;

import models.Deck;
import models.Player;
import models.cards.Card;

public class Board {
    Player player;
    Player opponent;
    private Deck deckZone;
    private Deck hand;
    private Deck graveyardZone;
    private Deck banishedZone;
    private Deck monsterZone;
    private Deck spellZone;
    private Card fieldZone;


    public Board(Player player, Player opponent) {
        deckZone = new Deck("DZ", player);
        hand = new Deck("HZ", player);
        graveyardZone = new Deck("GZ", player);
        banishedZone = new Deck("BZ", player);
        monsterZone = new Deck("MZ", player);
        spellZone = new Deck("SZ", player);
        setPlayer(player);
        setOpponent(opponent);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
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

    public void putCardInMonsterZone(Card card) {
        if (!isMonsterZoneFull()) {
            monsterZone.mainCards.add(card);
        }
    }

    public void putInSpellZone(Card card) {
        if (!isSpellZoneFull()) {
            spellZone.mainCards.add(card);
        }
    }

    public void putInFieldZone(Card card) {
        if (fieldZone != null) {
            putInGraveyard(fieldZone);
        }
        fieldZone = card;
    }

    public void putInGraveyard(Card card) {
        graveyardZone.mainCards.add(card);
    }

    public void putInBanished(Card card) {
        banishedZone.mainCards.add(card);
    }

    public void putInHand(Card card) {
        hand.mainCards.add(card);
    }

    public void putInDeck(Card card) {
        deckZone.mainCards.add(card);
    }

    public void removeFromMonsterZone(Card card) {
        monsterZone.mainCards.remove((Object) card);
    }

    public void removeFromSpellZone(Card card) {
        spellZone.mainCards.remove((Object) card);
    }

    public void removeFromFieldZone(Card card) {
        if (isInFieldZone(card)) {
            fieldZone = null;
        }
    }

    public void removeFromGraveyard(Card card) {
        graveyardZone.removeCard(card, true);
    }

    public void removeFromBanished(Card card) {
        banishedZone.removeCard(card, true);
    }

    public void removeFromHand(Card card) {
        hand.removeCard(card, true);
    }

    public void removeFromDeck(Card card) {
        deckZone.removeCard(card, true);
    }

    public boolean isInMonsterZone(Card card) {
        return monsterZone.mainCards.contains((Object) card);
    }

    public boolean isInSpellZone(Card card) {
        return spellZone.mainCards.contains((Object) card);
    }

    public boolean isInFieldZone(Card card) {
        return fieldZone.getName().equals(card.getName());
    }

    public boolean isInGraveyard(Card card) {
        return graveyardZone.mainCards.contains((Object) card);
    }

    public boolean isInBanished(Card card) {
        return banishedZone.mainCards.contains((Object) card);
    }

    public boolean isInHand(Card card) {
        return hand.mainCards.contains((Object) card);
    }

    public boolean isInDeck(Card card) {
        return deckZone.mainCards.contains((Object) card);
    }

    public boolean isInField(Card card) {
        return isInMonsterZone(card) || isInSpellZone(card) || isInFieldZone(card) || isInGraveyard(card) ||
                isInBanished(card) || isInHand(card) || isInDeck(card);
    }

    public boolean isHandFull() {
        return hand.mainCards.size() >= 6;
    }

    public boolean isMonsterZoneFull() {
        return monsterZone.mainCards.size() >= 5;
    }

    public boolean isSpellZoneFull() {
        return spellZone.mainCards.size() >= 5;
    }

}
