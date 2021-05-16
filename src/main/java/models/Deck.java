package models;

import controller.AI;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;
import view.ConsoleBasedMenus;
import view.Output;

import java.util.ArrayList;

public class Deck {
    public ArrayList<Card> mainCards = new ArrayList<>();
    public ArrayList<Card> sideCards = new ArrayList<>();
    Player owner;
    String name;
    DeckType type;
    Boolean isActive = false;
    Boolean IsValid;

    public Deck(String name, Player owner, boolean hasSideDeck, boolean shouldBeSaved) {
        this.name = name;
        if (shouldBeSaved)
            Database.allDecks.add(this);
        if (!hasSideDeck)
            sideCards = null;
        this.owner = owner;
    }

    public Deck(String name, Player owner) {
        this.name = name;
        sideCards = null;
        this.owner = owner;
    }

    public static Deck getSelectionDeckFrom(Deck collectedDeck, int howMany) {
        Output.getInstance().showMessage("you have to select " + howMany + " cards from cards below:");
        collectedDeck.toString(true);
        Deck selectionDeck = new Deck("selected collected deck" , collectedDeck.owner);
        while (howMany > 0) {
            Output.getInstance().showMessage("you have to select " + howMany + " more card(s)");
            int number = 0;
            try {
                number =Integer.parseInt(ConsoleBasedMenus.scanner.nextLine().replaceAll("\\s+", ""));
            } catch (NumberFormatException numberFormatException) {
                Output.getInstance().showMessage("please just enter a number");
                continue;
            }
            if (number > collectedDeck.mainCards.size()){
                Output.getInstance().showMessage("invalid number!");
                continue;
            }
            howMany--;
            selectionDeck.addCard(collectedDeck.getMainCards().get(number - 1));
        }
        return selectionDeck;
    }

    public void updateOwnerDecks() {
        String deckType = (name.length() > 16) ? name.substring(name.length() - 16) : "";
        if (deckType.equals(".purchased-cards"))
            owner.setAllPlayerCard(this);
        else {
            owner.getAllDeck().add(this);
            if (this.isActive)
                owner.setActiveDeck(this);
        }
    }

    public ArrayList<Card> getMainCards() {
        if (mainCards == null)
            return (mainCards = new ArrayList<>());
        return mainCards;
    }

    public void setMainCards(ArrayList<Card> mainCards) {
        this.mainCards = mainCards;
    }

    public ArrayList<Card> getSideCards() {
        return sideCards;
    }

    public void setSideCards(ArrayList<Card> sideCards) {
        this.sideCards = sideCards;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActivation(Boolean active) {
        isActive = active;
    }

    public void setValid(Boolean valid) {
        IsValid = valid;
    }

    public void setType(DeckType type) {
        this.type = type;
    }

    public void addCard(Card card, boolean shouldBeAddedToMain) {
        if (shouldBeAddedToMain)
            mainCards.add(card);
        else
            sideCards.add(card);
        if(card != null)
            card.setCurrentDeck(this);
    }

    public void addCard(Card card) {
        mainCards.add(card);
        if(card != null)
            card.setCurrentDeck(this);
    }

    public void moveCardTo(Deck destination, Card card, boolean isMainForOrigin , boolean isMainForDestination) {
        removeCard(card, isMainForOrigin);
        destination.addCard(card, isMainForDestination);
    }
    public void moveCardToForGame(Deck destination, Card card, boolean isMainForOrigin , boolean isMainForDestination) {
        removeCardForGame(card, isMainForOrigin);
        destination.addCard(card, isMainForDestination);
    }

    public boolean hasCard(Card card, boolean isMain) {
        if (isMain) {
            for (Card cardInMain : mainCards)
                if (cardInMain.getName().equals(card.getName()))
                    return true;

        } else
            for (Card cardInSide : sideCards)
                if (cardInSide.getName().equals(card.getName())) {
                    return true;
                }
        return false;
    }

    public void removeCard(Card card, boolean shouldBeRemovedFromMain) {
        if (shouldBeRemovedFromMain) {
            for (Card cardInMain : mainCards)
                if (cardInMain.getName().equals(card.getName())) {
                    mainCards.remove(cardInMain);
                    return;
                }
        } else
            for (Card cardInSide : sideCards)
                if (cardInSide.getName().equals(card.getName())) {
                    sideCards.remove(cardInSide);
                    return;
                }
    }
    public void removeCardForGame(Card card, boolean shouldBeRemovedFromMain) {
        if (shouldBeRemovedFromMain)
           mainCards.remove(card);
         else
           sideCards.remove(card);
    }

    public int getSumOfAttackPowers() {
        int sum = 0;
        for (Card card : mainCards) {
            Monster monster = (Monster) card;
            sum += monster.getAttackPower();
        }
        return sum;
    }

    public int getNumberOfCardsInDeck(Card card) {
        int count = 0;
        for (Card cardInDeck : mainCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        for (Card cardInDeck : sideCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        return count;
    }

    public int getNumberOfCardsInMainDeck() {
        return mainCards.size();
    }

    public int getNumberOfCardsInSideDeck() {
        return sideCards.size();
    }
    @Override
    protected Deck clone() throws CloneNotSupportedException {
        Deck deck = new Deck(this.name , this.owner , true , false);
        ArrayList<Card> main = new ArrayList<>()
                , side = new ArrayList<>();
        for (Card card : mainCards) {
            if (card instanceof Monster)
                main.add(((Monster)card).clone());
            else if (card instanceof Spell)
                main.add(((Spell)card).clone());
            else if (card instanceof Trap)
                main.add(((Trap)card).clone());
            if(card != null)
                card.setCurrentDeck(this);
        }
        for (Card card : sideCards) {
            if (card instanceof Monster)
                side.add(((Monster)card).clone());
            else if (card instanceof Spell)
                side.add(((Spell)card).clone());
            else if (card instanceof Trap)
                side.add(((Trap)card).clone());
        }
        deck.setMainCards(main);
        deck.setSideCards(side);
        return deck;
    }

    public String toString(boolean isMain) {
        StringBuilder output = new StringBuilder();
        int number = 1;
        if (isMain)
            for (Card card : mainCards) {
                if (card == null)
                    continue;
                output.append(number).append(". ").append(card.toString());
                number++;
            }
        else
            for (Card card : sideCards) {
                if (card == null)
                    continue;
                output.append(number).append(". ").append(card.toString());
                number++;
            }
        return output.toString();
    }
}
