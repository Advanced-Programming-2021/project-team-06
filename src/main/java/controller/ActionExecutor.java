package controller;

import models.Deck;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class ActionExecutor {


    private static final ArrayList<ActionExecutor> ALL_ACTION_EXECUTORS = new ArrayList<>();
    private Matcher neededInformation;
    private Deck collectedDeck;
    private Card clientsCard;
    private String actionString;
    public ActionExecutor(String name , Card clientsCard , String actionString) {
        this.actionString = actionString;
        this.clientsCard = clientsCard;
        collectedDeck = new Deck(name, clientsCard.getCurrentDeck().getOwner());
        ALL_ACTION_EXECUTORS.add(this);
    }

    public void redo() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ActionJsonParser.getInstance().doActionExecutor(this , actionString);
    }
    public  static ActionExecutor getActionExecutorByName(String name) {
        for (ActionExecutor actionExecutor : ALL_ACTION_EXECUTORS) {
            if (actionExecutor.getCollectedDeck().getName().equals(name))
                return actionExecutor;
        }
        return null;
    }

    public Deck getCollectedDeck() {
        return collectedDeck;
    }

    public void execute(String methodName, Matcher matcher) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        neededInformation = matcher;
        this.getClass().getDeclaredMethod(methodName).invoke(this);
    }

    private void die() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Duel.getCurrentDuel().changeDeck(clientsCard, "GY");
    }

    private void drawCard() {
        int howMany = Integer.parseInt(neededInformation.group("howMany"));
        for (int i = 0; i < howMany; i++) {
            Duel.getCurrentDuel().getOnlinePlayer().getBoard().drawCard();
        }
    }

    public void cancel() {
        String[] eventNames = neededInformation.group("eventName").split("\\.");
        for (String eventName : eventNames) {
            ActionExecutor actionExecutor = ActionExecutor.getActionExecutorByName(eventName + ((Object) clientsCard).toString());
            while (actionExecutor != null) {
                for (Card card : actionExecutor.collectedDeck.mainCards) {
                    if (card instanceof Monster)
                        ((Monster) card).resetAllFields(clientsCard);
                }
                ALL_ACTION_EXECUTORS.remove(actionExecutor);
                actionExecutor = ActionExecutor.getActionExecutorByName(eventName + ((Object) clientsCard).toString());
            }
        }
        ALL_ACTION_EXECUTORS.remove(this);
    }

    private void collectCards() {
        ArrayList<Deck> deckList = ActionJsonParser.getInstance().getDecksByTheirName(neededInformation.group("deckList").split("\\."));
        getCardsFromTheirDeck(deckList, neededInformation.group("class"));
        String attributeList = neededInformation.group("attributeList");
        collectedDeck.mainCards.removeIf(card -> card == null || !card.hasAttributes(attributeList));
    }

    private void kill() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (Card card : collectedDeck.mainCards)
            if (card instanceof Monster)
                Duel.getCurrentDuel().kill(((Monster) card));
    }

    private void sendCardsToDeck() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String destination = neededInformation.group("deckName");
        for (Card card : collectedDeck.getMainCards()) {
            Duel.getCurrentDuel().changeDeck(card, destination);
        }
    }

    private void killOffender() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Duel currentDuel = Duel.getCurrentDuel();
        currentDuel.kill(currentDuel.getOnlinePlayer(), currentDuel.getAttackingMonster());
    }

    private void increaseAttackPower() {
        int amount = Integer.parseInt(neededInformation.group("amount"));
        for (Card card : collectedDeck.mainCards)
            if (card instanceof Monster)
                ((Monster) card).setAdditionalAttackPower(amount , clientsCard);
    }
    private void increaseDefencePower() {
        int amount = Integer.parseInt(neededInformation.group("amount"));
        for (Card card : collectedDeck.mainCards)
            if (card instanceof Monster)
                ((Monster) card).setAdditionalDefencePower(amount , clientsCard);
    }
    private void setAttackPower() {
        int amount = Integer.parseInt(neededInformation.group("amount"));
        for (Card card : collectedDeck.mainCards)
            if (card instanceof Monster){
                Monster theMonster = ((Monster) card);
                theMonster.setAdditionalAttackPower(amount - theMonster.getAttackPower() , clientsCard);
            }
    }

    private void consumeEffect() {
        clientsCard.consumeEffect(collectedDeck.getName().replace(((Object)clientsCard).toString() , ""));
    }
    private void selectCardsByUserChoice() {
        int howMany = Integer.parseInt(neededInformation.group("howMany"));
        collectedDeck = Deck.getSelectionDeckFrom(collectedDeck , howMany);

    }
    private void getCardsFromTheirDeck(ArrayList<Deck> decks, String ofClass) {
        ArrayList<Card> cards = collectedDeck.getMainCards();
        for (Deck deck : decks)
            cards.addAll(deck.getMainCards());
        switch (ofClass) {
            case "":
            case "Any":
                return;
            case "Monster":
                cards.removeIf(card -> !(card instanceof Monster));
                break;
            case "Spell":
                cards.removeIf(card -> !(card instanceof Spell));
                break;
            case "Trap":
                cards.removeIf(card -> !(card instanceof Trap));
                break;
        }

    }

}
