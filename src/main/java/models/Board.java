package models;

import models.cards.Card;
import models.cards.CardPlacement;
import models.cards.Monster;
import models.cards.MonsterMode;

import models.cards.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Board {
    Player player;
    Player opponent;
    private Deck deckZone;
    private Deck hand;
    private Deck graveyardZone;
    private Deck banishedZone;
    private Deck monsterZone;
    private Deck spellZone;
    private Card fieldZone = null;
    private Card selectedCard = null;
    private boolean isSummonedOrSetCardInTurn = false;
    private boolean isChangePositionInTurn = false;


    public Board(Player player, Player opponent) {
        deckZone = new Deck("DZ", player);
        hand = new Deck("HZ", player);
        graveyardZone = new Deck("GZ", player);
        banishedZone = new Deck("BZ", player);
        monsterZone = new Deck("MZ", player);
        for (int i = 0; i < 5; i++)
            monsterZone.addCard(null, true);
        spellZone = new Deck("SZ", player);
        for (int i = 0; i < 5; i++)
            spellZone.addCard(null, true);

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

    public boolean isSummonedOrSetCardInTurn() {
        return isSummonedOrSetCardInTurn;
    }

    public void setSummonedOrSetCardInTurn(boolean summonedOrSetCardInTurn) {
        isSummonedOrSetCardInTurn = summonedOrSetCardInTurn;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public ArrayList<Card> getMonsterZoneCards() {
        return monsterZone.getMainCards();
    }

    public ArrayList<Card> getSpellZoneCards() {
        return spellZone.getMainCards();
    }

    public ArrayList<Card> getHandZoneCards() {
        return hand.getMainCards();
    }

    public boolean isChangePositionInTurn() {
        return isChangePositionInTurn;
    }

    public void setChangePositionInTurn(boolean changePositionInTurn) {
        isChangePositionInTurn = changePositionInTurn;
    }

    public void putCardInMonsterZone(Card card) {
        monsterZone.mainCards.set(getFirstFreeMonsterZoneIndex(), card);
    }

    public void putInSpellZone(Card card) {
        spellZone.mainCards.set(getFirstFreeSpellZoneIndex(), card);
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
        for (int i = 0; i < 6; i++)
            if (hand.getMainCards().get(i) == null)
                hand.getMainCards().add(i, card);
    }

    public void putInDeck(Card card) {
        deckZone.mainCards.add(card);
    }

    public void removeFromMonsterZone(Card card) {
        monsterZone.mainCards.set(monsterZone.mainCards.indexOf(card), null);
    }

    public void removeFromSpellZone(Card card) {
        spellZone.mainCards.set(spellZone.mainCards.indexOf(card), null);
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
        hand.mainCards.set(hand.mainCards.indexOf(card), null);
    }

    public void removeFromDeck(Card card) {
        deckZone.removeCard(card, true);
    }

    public boolean isInMonsterZone(Card card) {
        return monsterZone.mainCards.contains(card);
    }

    public boolean isInSpellZone(Card card) {
        return spellZone.mainCards.contains(card);
    }

    public boolean isInFieldZone(Card card) {
        return fieldZone.getName().equals(card.getName());
    }

    public boolean isInGraveyard(Card card) {
        return graveyardZone.mainCards.contains(card);
    }

    public boolean isInBanished(Card card) {
        return banishedZone.mainCards.contains(card);
    }

    public boolean isInHand(Card card) {
        return hand.mainCards.contains(card);
    }

    public boolean isInDeck(Card card) {
        return deckZone.mainCards.contains(card);
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

    public Card drawCard() {
        if (deckZone.mainCards.size() > 0) {
            if (isHandFull()) {
                removeFromHand(hand.mainCards.get(new Random().nextInt(hand.mainCards.size())));
            }
            putInHand(deckZone.mainCards.get(deckZone.mainCards.size() - 1));
            removeFromDeck(deckZone.mainCards.get(deckZone.mainCards.size() - 1));
            return hand.mainCards.get(hand.mainCards.size() - 1);
        }
        return null;
    }

    public void shuffleDeck() {
        Collections.shuffle(deckZone.mainCards);
    }

    public int getFirstFreeMonsterZoneIndex() {
        for (int i = 0; i < 5; i++) {
            if (monsterZone.mainCards.get(i) == null) return i;
        }
        return -1;
    }

    public int getFirstFreeSpellZoneIndex() {
        for (int i = 0; i < 5; i++) {
            if (spellZone.mainCards.get(i) == null) return i;
        }
        return -1;
    }

    public String toString(Player turn) {
        StringBuilder handString = new StringBuilder(),
                spellZoneString = new StringBuilder(), monsterZoneString = new StringBuilder();
        handString.append(("\t" + "c").repeat(Math.max(0, hand.getNumberOfCardsInMainDeck())));
        if (turn != player) {
            for (Card card : spellZone.getMainCards()) {
                if (card == null) spellZoneString.append("\tE");
                else if (card.getCardPlacement() == CardPlacement.faceUp) spellZoneString.append("\tO");
                else spellZoneString.append("\tH");
            }
            for (Card card : monsterZone.getMainCards()) {
                if (card == null) {
                    spellZoneString.append("\tE");
                    break;
                } else if (((Monster) card).getMonsterMode() == MonsterMode.attack)
                    monsterZoneString.append("\tO");
                else if (((Monster) card).getMonsterMode() == MonsterMode.defence)
                    monsterZoneString.append("\tD");

                if (card.getCardPlacement() == CardPlacement.faceUp)
                    monsterZoneString.append("O");
                else
                    monsterZoneString.append("H");
            }

            return player.getUsername() + ":" + player.getHealth() + "\n"
                    + handString + "\n"
                    + deckZone.getNumberOfCardsInMainDeck() + "\n"
                    + spellZoneString + "\n"
                    + monsterZoneString + "\n" +
                    graveyardZone.getNumberOfCardsInMainDeck() +
                    "\t\t\t\t\t\t" + ((fieldZone == null) ? "FZ" : "O");
        }


        for (Card card : spellZone.getMainCards()) {
            if (card == null) spellZoneString.insert(0, "\tE");
            else if (card.getCardPlacement() == CardPlacement.faceUp) spellZoneString.insert(0, "\tO");
            else spellZoneString.insert(0, "\tH");
        }
        for (Card card : monsterZone.getMainCards()) {
            if (card == null) {
                spellZoneString.insert(0, "\tE");
                break;
            } else if (((Monster) card).getMonsterMode() == MonsterMode.attack)
                monsterZoneString.insert(0, "\tO");
            else if (((Monster) card).getMonsterMode() == MonsterMode.defence)
                monsterZoneString.insert(0, "\tD");

            if (card.getCardPlacement() == CardPlacement.faceUp)
                monsterZoneString.insert(0, "O");
            else
                monsterZoneString.insert(0, "H");
        }

        return ((fieldZone == null) ? "FZ" : "O") +
                "\t\t\t\t\t\t" + graveyardZone.getNumberOfCardsInMainDeck() +
                monsterZoneString + "\n" +
                spellZoneString + "\n" +
                "\t\t\t\t\t\t" + deckZone.getNumberOfCardsInMainDeck() + "\n"
                + handString + "\n" +
                player.getUsername() + ":" + player.getHealth();

    }
}
