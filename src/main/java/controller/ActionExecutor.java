package controller;

import models.Deck;
import models.Player;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;
import view.GameInputs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;

public class ActionExecutor {


    private static final ArrayList<ActionExecutor> ALL_ACTION_EXECUTORS = new ArrayList<>();
    private Matcher neededInformation;
    private Deck collectedDeck;
    private Card clientsCard;
    public ActionExecutor(String name) {
        collectedDeck = new Deck(name, null);
        ALL_ACTION_EXECUTORS.add(this);
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

    public void execute(String methodName, Matcher matcher , Card clientsCard) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        neededInformation = matcher;
        this.clientsCard = clientsCard;
        this.getClass().getDeclaredMethod(methodName).invoke(this);
    }

    public void cancel() {
        String[] eventNames = neededInformation.group("eventName").split(",");
        for (String eventName : eventNames) {
        ActionExecutor actionExecutor = ActionExecutor.getActionExecutorByName(eventName + ((Object)clientsCard).toString());
        if (actionExecutor == null) continue;
        for (Card card: actionExecutor.collectedDeck.mainCards) {
            if (card instanceof Monster)
                ((Monster) card).resetAllFields();
        }
        ALL_ACTION_EXECUTORS.remove(actionExecutor);
        }
        ALL_ACTION_EXECUTORS.remove(this);
    }

    private void collectCards() {
        ArrayList<Deck> deckList = ActionJsonParser.getInstance().getDecksByTheirName(neededInformation.group("deckList").split(","));
        getCardsFromTheirDeck(deckList, neededInformation.group("class"));
        Card desiredCard = ActionJsonParser.getInstance().getDesiredCard(
                neededInformation.group("attributeList").split(","),
                neededInformation.group("class"));
        collectedDeck.mainCards.removeIf(card -> card == null || !card.isLike(desiredCard));
    }
    private void kill() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (Card card : collectedDeck.mainCards)
            if (card instanceof Monster)
                Duel.getCurrentDuel().kill(((Monster)card));
    }

    private void killOffender() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
     Duel currentDuel = Duel.getCurrentDuel();
     currentDuel.kill(currentDuel.getOnlinePlayer() , currentDuel.getAttackingMonster());
    }
    private void increaseAttackPower() {
        int amount = Integer.parseInt(neededInformation.group("amount"));
        for (Card card : collectedDeck.mainCards)
            if (card instanceof Monster)
                ((Monster) card).setAdditionalAttackPower(amount);
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
